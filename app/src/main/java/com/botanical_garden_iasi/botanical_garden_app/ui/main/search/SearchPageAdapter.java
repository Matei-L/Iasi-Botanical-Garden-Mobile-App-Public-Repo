package com.botanical_garden_iasi.botanical_garden_app.ui.main.search;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.botanical_garden_iasi.botanical_garden_app.ui.main.interfaces.SearchableListDaggerFragment;

import java.util.ArrayList;
import java.util.List;

public class SearchPageAdapter extends FragmentPagerAdapter {
    private final List<SearchableListDaggerFragment> fragments = new ArrayList<>();
    private final List<String> titles = new ArrayList<>();

    public SearchPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void addFragment(SearchableListDaggerFragment fragment, String title) {
        fragments.add(fragment);
        titles.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @NonNull
    @Override
    public SearchableListDaggerFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
