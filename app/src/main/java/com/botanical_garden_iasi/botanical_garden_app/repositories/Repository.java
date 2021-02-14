package com.botanical_garden_iasi.botanical_garden_app.repositories;

import androidx.paging.PagedList;

import com.botanical_garden_iasi.botanical_garden_app.AppExecutors;
import com.botanical_garden_iasi.botanical_garden_app.helpers.Constants;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.BaseModel;

import java.util.List;

public abstract class Repository {
    protected AppExecutors executors;

    protected boolean shouldFetch(BaseModel model) {
        if (model != null) {
            return System.currentTimeMillis() - model.modifiedAt > Constants.getCacheTTLMilis();
        }
        return true;
    }

    protected boolean shouldFetch(PagedList<? extends BaseModel> models) {
        if (models != null && models.size() > 0) {
            boolean fetch = false;
            for (BaseModel model : models) {
                fetch = fetch || shouldFetch(model);
            }
            return fetch;
        }
        return true;
    }

    protected boolean shouldFetch(List<? extends BaseModel> models) {
        if (models != null && models.size() > 0) {
            boolean fetch = false;
            for (BaseModel model : models) {
                fetch = fetch || shouldFetch(model);
            }
            return fetch;
        }
        return true;
    }
}
