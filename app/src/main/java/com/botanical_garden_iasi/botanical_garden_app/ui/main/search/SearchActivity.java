package com.botanical_garden_iasi.botanical_garden_app.ui.main.search;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.botanical_garden_iasi.botanical_garden_app.R;
import com.botanical_garden_iasi.botanical_garden_app.helpers.Constants;
import com.botanical_garden_iasi.botanical_garden_app.ui.BaseActivity;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.sections.sections_list_fragment.SectionsListFragment;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.interfaces.SearchableListDaggerFragment;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.plants.plants_list_fragment.PlantsListFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class SearchActivity extends BaseActivity {

    private TextView searchByTexView;
    private SearchView searchView;

    private SearchPageAdapter searchPageAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private SearchByAdapter searchByAdapter;
    private RecyclerView searchByListRecyclerView;
    private List<Pair<String, Integer>> searchByList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        searchByTexView = findViewById(R.id.search_by_textView);
        searchByListRecyclerView = findViewById(R.id.search_by_list_recycler_view);

        setupTabLayout();
        setupSearchByList();
    }

    private void setupSearchByList() {
        searchByList = getCurrentListFragment().getSearchOptions();
        searchByAdapter = new SearchByAdapter(searchByList);
        searchByListRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        searchByListRecyclerView.setAdapter(searchByAdapter);

        searchByAdapter.setOnSelectListener(selectedPosition -> {
            processQuery("");
        });
    }

    private void setupTabLayout() {
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                List<Pair<String, Integer>> newList =
                        searchPageAdapter
                                .getItem(position)
                                .getSearchOptions();

                replaceSearchByList(newList);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void replaceSearchByList(List<Pair<String, Integer>> newList) {
        searchByList.clear();
        searchByList.addAll(newList);
        searchByAdapter.notifyDataSetChanged();
        searchByAdapter.clearSelectedItem();
    }

    private void setupViewPager() {
        searchPageAdapter = new SearchPageAdapter(
                getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        );
        searchPageAdapter.addFragment(new PlantsListFragment(true), getString(R.string.plants_title));
        searchPageAdapter.addFragment(new SectionsListFragment(true), getString(R.string.sections_title));
        viewPager.setAdapter(searchPageAdapter);
    }

    private String getIntentQuery() {
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            return intent.getStringExtra(SearchManager.QUERY);
        }
        return "";
    }

    private void processQuery(String query) {
        if (query.equals(Constants.SEARCH_HOME_KEYWORD)) {
            return;
        }
        query = query.trim().toLowerCase();
        SearchableListDaggerFragment currentListFragment =
                getCurrentListFragment();
        if (query.equals("")) {
            tabLayout.setVisibility(View.VISIBLE);
            searchByTexView.setVisibility(View.VISIBLE);
            searchByListRecyclerView.setVisibility(View.VISIBLE);

            int option = searchByAdapter.getSelectedItem().second;

            currentListFragment.ClearFilter(option);
        } else {
            tabLayout.setVisibility(View.GONE);
            searchByTexView.setVisibility(View.GONE);
            searchByListRecyclerView.setVisibility(View.GONE);

            int option = searchByAdapter.getSelectedItem().second;

            currentListFragment.Filter(query, option);
        }
    }

    private SearchableListDaggerFragment getCurrentListFragment() {
        return searchPageAdapter.getItem(viewPager.getCurrentItem());

    }

    @Override
    protected int getContentView() {
        return R.layout.search_activity;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toolbar_menu, menu);

        setupSearchView(menu);

        return true;
    }

    private void setupSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(getBaseContext(), SearchActivity.class)));

        searchView.onActionViewExpanded();

        processQuery(getIntentQuery());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                processQuery(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                processQuery(query);
                return true;
            }
        });
    }

}
