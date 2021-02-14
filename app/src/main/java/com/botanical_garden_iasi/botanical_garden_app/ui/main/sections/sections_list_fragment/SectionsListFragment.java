package com.botanical_garden_iasi.botanical_garden_app.ui.main.sections.sections_list_fragment;

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
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.sections.Section;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.ItemListAdapter;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.interfaces.SearchableListDaggerFragment;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.sections.section_info_activity.SectionInfoActivity;
import com.botanical_garden_iasi.botanical_garden_app.viewmodels.ViewModelProviderFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

public class SectionsListFragment extends SearchableListDaggerFragment {
    private static final String TAG = "SectionsListFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    private SectionsListViewModel sectionsListViewModel;
    private RecyclerView sectionsListRecyclerView;
    private ItemListAdapter<Section> sectionsListAdapter;
    private PagedList<Section> sections;

    public SectionsListFragment() {
        super();
    }

    public SectionsListFragment(boolean disableTopPadding) {
        super(disableTopPadding);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sectionsListViewModel = new ViewModelProvider(this, providerFactory)
                .get(SectionsListViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.vertical_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sectionsListRecyclerView = view.findViewById(R.id.listRecyclerView);

        super.onViewCreated(view, savedInstanceState);

        subscribeToViewModels();
    }

    @Override
    protected void setupViews() {
        super.setupViews();
        sectionsListAdapter = new ItemListAdapter<>();
        sectionsListRecyclerView.setAdapter(sectionsListAdapter);
        sectionsListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected void setupOnClickListeners() {
        super.setupOnClickListeners();

        sectionsListAdapter.setOnClickListeners(position -> {
            Section section = sections.get(position);
            if (section != null) {
                openSectionInfoActivity(section.id);
            }
        });
    }

    protected void refreshPagedList() {
        sectionsListViewModel.getSections().removeObservers(this);
        AtomicBoolean firstPageLoaded = new AtomicBoolean(false);
        sectionsListViewModel.getSections(true).observe(this, pagedList -> {
            processPagedList(pagedList);
            if (!firstPageLoaded.get()) {
                firstPageLoaded.set(true);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void processPagedList(PagedList<Section> pagedList) {
        if (pagedList != null) {
            sections = pagedList;
            sectionsListAdapter.submitList(sections);
        }
    }

    private void openSectionInfoActivity(int sectionId) {
        Intent intent = new Intent(getActivity(), SectionInfoActivity.class);
        intent.putExtra("sectionId", sectionId);
        startActivity(intent);
        onPause();
    }

    private void subscribeToViewModels() {
        sectionsListViewModel.getSections(false).observe(getViewLifecycleOwner(), this::processPagedList);
    }

    @Override
    public List<Pair<String, Integer>> getSearchOptions() {
        List<Pair<String, Integer>> options = new ArrayList<>();
        options.add(new Pair<>("Nume", Section.FilterOptions.NAME.ordinal()));
        return options;
    }

    @Override
    public void Filter(String query, int option) {
        if (sectionsListViewModel != null && (option != sectionsListViewModel.getCurrentOption() ||
                !query.equals(sectionsListViewModel.getCurrentKeyword()))) {
            sectionsListViewModel.getSections().removeObservers(getViewLifecycleOwner());
            sectionsListViewModel.setFilters(query, option);
            sectionsListViewModel.getSections(false).observe(getViewLifecycleOwner(), this::processPagedList);

        }
        Log.i(TAG, "Filter: query: <<" + query + ">> option: <<" + option + ">>");
    }

    @Override
    public void ClearFilter(int option) {
        if (sectionsListViewModel != null && (option != sectionsListViewModel.getCurrentOption() ||
                !"".equals(sectionsListViewModel.getCurrentKeyword()))) {
            sectionsListViewModel.getSections().removeObservers(getViewLifecycleOwner());
            sectionsListViewModel.setFilters("", option);
            sectionsListViewModel.getSections(false).observe(getViewLifecycleOwner(), this::processPagedList);

        }
    }
}
