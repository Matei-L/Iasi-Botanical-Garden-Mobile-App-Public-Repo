package com.botanical_garden_iasi.botanical_garden_app.ui.main.plants.plants_list_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.botanical_garden_iasi.botanical_garden_app.R;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.plants.Plant;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.interfaces.SearchableListDaggerFragment;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.ItemListAdapter;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.plants.plant_info_activity.PlantInfoActivity;
import com.botanical_garden_iasi.botanical_garden_app.viewmodels.ViewModelProviderFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

public class PlantsListFragment extends SearchableListDaggerFragment {
    private static final String TAG = "PlantsListFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    private PlantsListViewModel plantsListViewModel;
    private RecyclerView plantsListRecyclerView;
    private ItemListAdapter<Plant> plantsListAdapter;
    private PagedList<Plant> plants;

    public PlantsListFragment() {
        super();
    }

    public PlantsListFragment(boolean disableTopPadding) {
        super(disableTopPadding);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plantsListViewModel = new ViewModelProvider(this, providerFactory)
                .get(PlantsListViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.vertical_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        plantsListRecyclerView = view.findViewById(R.id.listRecyclerView);

        super.onViewCreated(view, savedInstanceState);

        subscribeToViewModels();
    }

    @Override
    protected void setupViews() {
        super.setupViews();
        plantsListAdapter = new ItemListAdapter<>();
        plantsListRecyclerView.setAdapter(plantsListAdapter);
        plantsListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected void setupOnClickListeners() {
        super.setupOnClickListeners();

        plantsListAdapter.setOnClickListeners(position -> {
            Plant plant = plants.get(position);
            if (plant != null) {
                openPlantInfoActivity(plant.id);
            }
        });
    }

    protected void refreshPagedList() {
        plantsListViewModel.getPlants().removeObservers(getViewLifecycleOwner());
        AtomicBoolean firstPageLoaded = new AtomicBoolean(false);
        plantsListViewModel.getPlants(true).observe(getViewLifecycleOwner(), pagedList -> {
            processPagedList(pagedList);
            if (!firstPageLoaded.get()) {
                firstPageLoaded.set(true);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void processPagedList(PagedList<Plant> pagedList) {
        if (pagedList != null) {
            plants = pagedList;
            plantsListAdapter.submitList(plants);
        }
    }

    private void openPlantInfoActivity(int plantId) {
        Intent intent = new Intent(getActivity(), PlantInfoActivity.class);
        intent.putExtra("plantId", plantId);
        startActivity(intent);
        onPause();
    }

    private void subscribeToViewModels() {
        plantsListViewModel.getPlants(false).observe(getViewLifecycleOwner(), this::processPagedList);
    }

    @Override
    public List<Pair<String, Integer>> getSearchOptions() {
        List<Pair<String, Integer>> options = new ArrayList<>();
        options.add(new Pair<>("Toate", Plant.FilterOptions.ALL.ordinal()));
        options.add(new Pair<>("Favorite", Plant.FilterOptions.FAVORITES.ordinal()));
        options.add(new Pair<>("Gen", Plant.FilterOptions.TYPE.ordinal()));
        options.add(new Pair<>("Specie", Plant.FilterOptions.SPECIES.ordinal()));
        options.add(new Pair<>("Nume popular", Plant.FilterOptions.POPULAR_NAME.ordinal()));
        options.add(new Pair<>("Arie naturală", Plant.FilterOptions.NATURAL_AREA.ordinal()));
        options.add(new Pair<>("Origine", Plant.FilterOptions.ORIGIN.ordinal()));
        options.add(new Pair<>("Cu informații despre...", Plant.FilterOptions.OTHER_INFO_TITLE.ordinal()));
        return options;
    }

    @Override
    public void Filter(String query, int option) {
        if (option != plantsListViewModel.getCurrentOption() ||
                !query.equals(plantsListViewModel.getCurrentKeyword())) {
            plantsListViewModel.getPlants().removeObservers(getViewLifecycleOwner());
            plantsListViewModel.setFilters(query, option);
            plantsListViewModel.getPlants(false).observe(getViewLifecycleOwner(), this::processPagedList);

        }
        Log.i(TAG, "Filter: query: <<" + query + ">> option: <<" + option + ">>");
    }

    @Override
    public void ClearFilter(int option) {
        if (Plant.FilterOptions.values()[option] != Plant.FilterOptions.FAVORITES) {
            option = 0;
        }
        if (option != plantsListViewModel.getCurrentOption() ||
                !"".equals(plantsListViewModel.getCurrentKeyword())) {
            plantsListViewModel.getPlants().removeObservers(getViewLifecycleOwner());
            plantsListViewModel.setFilters("", option);
            plantsListViewModel.getPlants(false).observe(getViewLifecycleOwner(), this::processPagedList);

        }

        Log.i(TAG, "ClearFilter: done.");
    }
}
