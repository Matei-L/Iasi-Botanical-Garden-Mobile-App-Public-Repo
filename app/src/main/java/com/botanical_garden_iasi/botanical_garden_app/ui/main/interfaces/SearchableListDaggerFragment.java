package com.botanical_garden_iasi.botanical_garden_app.ui.main.interfaces;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.botanical_garden_iasi.botanical_garden_app.R;
import com.botanical_garden_iasi.botanical_garden_app.helpers.ConnectionCheckAsync;

import java.util.List;

import dagger.android.support.DaggerFragment;

public abstract class SearchableListDaggerFragment extends DaggerFragment {

    private boolean disableTopPadding;

    protected SwipeRefreshLayout swipeRefreshLayout;
    private NestedScrollView nestedScrollView;
    private View refreshButton;

    private View fakeStatusbar;
    private View topActionbarPadding;


    protected SearchableListDaggerFragment() {
        this.disableTopPadding = false;
    }

    protected SearchableListDaggerFragment(boolean disableTopPadding) {
        this.disableTopPadding = disableTopPadding;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh);
        nestedScrollView = view.findViewById(R.id.nested_scroll_view);
        refreshButton = view.findViewById(R.id.refresh_button);
        topActionbarPadding = view.findViewById(R.id.top_actionbar_padding);
        fakeStatusbar = view.findViewById(R.id.fake_statusbar_view);

        setupViews();
        setupOnClickListeners();
        disableTopPadding(disableTopPadding);
    }

    protected void setupViews() {
        swipeRefreshLayout.setColorSchemeResources(
                R.color.color_secondary,
                R.color.color_secondary_dark,
                R.color.color_primary);
    }

    private void disableTopPadding(boolean disable) {
        if (disable) {
            topActionbarPadding.setVisibility(View.GONE);
            fakeStatusbar.setVisibility(View.GONE);
        } else {
            topActionbarPadding.setVisibility(View.VISIBLE);
            fakeStatusbar.setVisibility(View.VISIBLE);
        }
    }

    protected void setupOnClickListeners() {

        swipeRefreshLayout.setOnRefreshListener(this::refreshPagedListRequest);

        refreshButton.setOnClickListener(v -> {
            nestedScrollView.scrollTo(0, 0);
            swipeRefreshLayout.setRefreshing(true);
            refreshPagedListRequest();
        });
    }

    private void refreshPagedListRequest() {
        new ConnectionCheckAsync(serverOn -> {
            if (!serverOn) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(),
                        "Can't connect to our server!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            refreshPagedList();
        });
    }

    protected abstract void refreshPagedList();

    public abstract List<Pair<String, Integer>> getSearchOptions();

    public abstract void Filter(String query, int option);

    public abstract void ClearFilter(int option);
}
