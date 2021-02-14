package com.botanical_garden_iasi.botanical_garden_app.ui.main.sections.section_info_activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.botanical_garden_iasi.botanical_garden_app.R;
import com.botanical_garden_iasi.botanical_garden_app.helpers.AnimationHelper;
import com.botanical_garden_iasi.botanical_garden_app.helpers.AppBarStateChangeListener;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.sections.Section;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.sections.SubSection;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.InfoImageSliderAdapter;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.MainActivity;
import com.botanical_garden_iasi.botanical_garden_app.viewmodels.ViewModelProviderFactory;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.jaeger.library.StatusBarUtil;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class SectionInfoActivity extends DaggerAppCompatActivity {

    private static final String TAG = "SectionInfoActivity";

    @Inject
    ViewModelProviderFactory providerFactory;

    private SectionInfoViewModel sectionInfoViewModel;
    private TextView titleTextView;
    private TextView locationAndSizeTextView;

    private RecyclerView subsectionsListRecyclerView;
    private SubsectionsListAdapter subsectionsListAdapter;
    private List<SubSection> subsectionsList;

    private SliderView topSliderView;
    private SliderView focusSliderView;
    private InfoImageSliderAdapter topSliderViewAdapter;
    private InfoImageSliderAdapter focusSliderViewAdapter;
    private ImageView iconImageView;
    private Dialog imageFocusDialog;
    private Toolbar toolbar;
    private View fakeStatusbarView;
    private AppBarLayout appBarLayout;
    private List<String> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.section_info_activity);

        // Get the necessary ViewModel using Dagger
        sectionInfoViewModel = new ViewModelProvider(this, providerFactory)
                .get(SectionInfoViewModel.class);

        titleTextView = findViewById(R.id.section_info_title);
        locationAndSizeTextView = findViewById(R.id.section_info_location_and_size);
        subsectionsListRecyclerView = findViewById(R.id.subsections_list_recyclerview);
        iconImageView = findViewById(R.id.section_info_top_icon_imageview);
        appBarLayout = findViewById(R.id.app_bar_layout);
        topSliderView = findViewById(R.id.section_info_image_slider);
        fakeStatusbarView = findViewById(R.id.fake_statusbar_view);
        toolbar = findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            locationAndSizeTextView.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }

        StatusBarUtil.setTranslucentForImageView(this, toolbar);

        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        setupRecyclerList();

        setupToolbar();

        processArguments();

        setupImageSliders();

        setupClickListeners();

        subscribeToPlant();
    }

    private void setupImageSliders() {
        images = new ArrayList<>();

        topSliderViewAdapter = new InfoImageSliderAdapter(false, images);
        topSliderView.setSliderAdapter(topSliderViewAdapter);
        topSliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        topSliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

        imageFocusDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        imageFocusDialog.setContentView(R.layout.info_image_focus);
        Objects.requireNonNull(imageFocusDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        focusSliderViewAdapter = new InfoImageSliderAdapter(true, images);
        focusSliderView = imageFocusDialog.findViewById(R.id.info_focus_image_slider);
        focusSliderView.setSliderAdapter(focusSliderViewAdapter);
        focusSliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        focusSliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
    }

    private void setupRecyclerList() {
        subsectionsList = new ArrayList<>();
        subsectionsListAdapter = new SubsectionsListAdapter(subsectionsList);
        subsectionsListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        subsectionsListRecyclerView.setAdapter(subsectionsListAdapter);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void processArguments() {
        int sectionId = 0;
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            sectionId = intent.getIntExtra("sectionId", 0);
        }
        sectionInfoViewModel.setSectionId(sectionId);
    }

    private void setupClickListeners() {
        iconImageView.setOnClickListener(v -> focusImage(0));
        topSliderViewAdapter.setOnItemClickListener(this::focusImage);

        focusSliderViewAdapter.setOnItemClickListener(
                p -> imageFocusDialog.dismiss()
        );

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int i) {
                if (state == State.EXPANDED) {
                    AnimationHelper.setVisibilityAnimated(fakeStatusbarView, View.GONE, 0);
                } else {
                    if (state == State.COLLAPSED) {
                        AnimationHelper.setVisibilityAnimated(fakeStatusbarView, View.VISIBLE, 0);
                    } else {
                        AnimationHelper.setVisibilityAnimated(fakeStatusbarView, View.GONE, 0);
                    }
                }
            }
        });
    }

    private void subscribeToPlant() {
        sectionInfoViewModel.getSectionResource().observe(this, sectionResource -> {
            if (sectionResource != null) {
                switch (sectionResource.getStatus()) {
                    case LOADING:
                        if (sectionResource.getData() != null) {
                            displaySection(sectionResource.getData());
                        } else {
                            displaySection("loading");
                        }
                        break;
                    case SUCCESS:
                        if (sectionResource.getData() != null) {
                            displaySection(sectionResource.getData());
                        } else {
                            displaySection("empty");
                        }
                        break;
                    case ERROR:
                        Toast.makeText(this, "Can't connect to our server!", Toast.LENGTH_SHORT).show();
                        if (sectionResource.getData() == null) {
                            displaySection("error");
                        }
                        break;
                }
            }
        });
    }

    private void displaySection(Section section) {
        titleTextView.setText(WordUtils.capitalizeFully(section.name));
        String locationAndSize = "<B>" + getString(R.string.const_location_and_size) + "</B>"
                + " " + StringUtils.capitalize(section.locationAndSize);
        locationAndSizeTextView.setText(Html.fromHtml(locationAndSize, 0));

        setSubSectionsLists(section);

        setImages(section);
    }

    private void setImages(Section section) {
        images.clear();
        if (section != null && section.images.size() > 0) {
            images.addAll(
                    section.images
                            .stream()
                            .map(image -> image.url)
                            .collect(Collectors.toList()));
        } else {
            images.add("");
        }
        topSliderViewAdapter.notifyDataSetChanged();
        focusSliderViewAdapter.notifyDataSetChanged();

        Glide.with(this)
                .asBitmap()
                .load(images.get(0))
                .error(R.drawable.plant_placeholder)
                .into(iconImageView);
    }

    private void setSubSectionsLists(Section section) {
        if (section != null) {
            subsectionsList.clear();
            subsectionsList.add(new SubSection(0, section.id, getString(R.string.description),
                    section.description, 0, 0));
            subsectionsList.addAll(
                    section.subsections
                            .stream()
                            .sorted((s1, s2) -> s1.name.compareTo(s2.name))
                            .collect(Collectors.toList()));
            subsectionsListAdapter.notifyDataSetChanged();
        } else {
            subsectionsList.clear();
            subsectionsListAdapter.notifyDataSetChanged();
        }
    }

    private void displaySection(String placeholder) {
        titleTextView.setText(placeholder);
        locationAndSizeTextView.setText(placeholder);

        setSubSectionsLists(null);

        setImages(null);
    }

    private void focusImage(int position) {
        focusSliderView.setCurrentPagePosition(position);

        imageFocusDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_map:
                onMapPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onMapPressed() {
        int sectionId = sectionInfoViewModel.getSectionId();
        if (sectionId > 0) {
            startMainActivityOnSection(sectionId);
        }
    }

    private void startMainActivityOnSection(Integer sectionId) {
        Intent intent = new Intent(SectionInfoActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("sectionId", sectionId);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.section_info_menu, menu);

        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.color_black), PorterDuff.Mode.SRC_ATOP);
            }
        }

        return true;
    }
}
