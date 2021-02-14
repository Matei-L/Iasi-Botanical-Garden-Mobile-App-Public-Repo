package com.botanical_garden_iasi.botanical_garden_app.network;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.paging.PagedList;

import com.botanical_garden_iasi.botanical_garden_app.network.apis.ApiResponse;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.BaseModel;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;

public abstract class BoundaryCallback<ResponseType, ItemType extends BaseModel> extends PagedList.BoundaryCallback<ItemType> {

    private static final String TAG = "BoundaryCallback";
    private boolean isLoading = false;
    private int lastPage = -1;
    private int pageSize;

    public BoundaryCallback(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public void onZeroItemsLoaded() {
        if (isLoading) return;
        Log.i(TAG, "onZeroItemsLoaded: called.");
        isLoading = true;
        getExecutor().execute(() -> {
            Call<List<ResponseType>> call = createCall(0, pageSize);
            processPage(call, 0, pageSize);
            lastPage += 1;
            isLoading = false;
        });
    }


    @Override
    public void onItemAtFrontLoaded(@NonNull ItemType itemAtFront) {
        super.onItemAtFrontLoaded(itemAtFront);
    }

    @Override
    public void onItemAtEndLoaded(@NonNull ItemType itemAtEnd) {
        if (isLoading) return;
        Log.i(TAG, "onItemAtEndLoaded: called." + itemAtEnd);
        isLoading = true;
        getExecutor().execute(() -> {
            int targetPage = getPage(itemAtEnd, pageSize);
            Log.i(TAG, "onItemAtEndLoaded: target pages: " + lastPage + "<=" + targetPage);
            while (lastPage <= targetPage) {
                List<ItemType> items = loadPageRawFromDb((lastPage + 1) * pageSize, pageSize);
                if (shouldFetch(items) || (lastPage + 1) >= targetPage) {
                    Call<List<ResponseType>> call = createCall((lastPage + 1) * pageSize, pageSize);
                    processPage(call, (lastPage + 1) * pageSize, pageSize);
                }
                lastPage += 1;
            }
            isLoading = false;
        });
    }


    @WorkerThread
    private void processPage(Call<List<ResponseType>> call, int offset, int limit) {
        try {
            if (call != null) {
                ApiResponse<List<ResponseType>> unprocessedApiResponse = new ApiResponse<>(call.execute());
                if (unprocessedApiResponse.isSuccessful()) {
                    List<ResponseType> responses = unprocessedApiResponse.body;
                    saveResponses(responses, offset, limit);
                    Log.i(TAG, "processPage: fetched " + unprocessedApiResponse.body.size() + " items.");
                } else {
                    if (unprocessedApiResponse.isEmptyResponse()) {
                        Log.i(TAG, "processPage: fetching plants failed, empty response");
                    } else {
                        Log.e(TAG, "processPage: fetching plants failed, error response");
                    }
                }
            } else {
                Log.i(TAG, "processPage: call was null. Nothing fetched or saved.");
            }
        } catch (IOException e) {
            Log.e(TAG, "processPage: fetching plants failed, IOException: "
                    + e.getMessage());
        }
    }

    @MainThread
    protected abstract Executor getExecutor();

    @WorkerThread
    protected abstract Call<List<ResponseType>> createCall(int offset, int limit);

    @WorkerThread
    protected abstract void saveResponses(List<ResponseType> responses, int offset, int limit);

    @WorkerThread
    protected abstract boolean shouldFetch(List<ItemType> items);

    @WorkerThread
    protected abstract List<ItemType> loadPageRawFromDb(int offset, int limit);

    @WorkerThread
    protected abstract int getPage(ItemType itemType, int limit);
}
