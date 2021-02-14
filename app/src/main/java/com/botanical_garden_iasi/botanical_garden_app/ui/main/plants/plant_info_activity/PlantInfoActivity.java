package com.botanical_garden_iasi.botanical_garden_app.ui.main.plants.plant_info_activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.botanical_garden_iasi.botanical_garden_app.R;
import com.botanical_garden_iasi.botanical_garden_app.helpers.AnimationHelper;
import com.botanical_garden_iasi.botanical_garden_app.helpers.AppBarStateChangeListener;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.plants.Plant;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.plants.PlantDescription;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.InfoImageSliderAdapter;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.ItemListAdapter;
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

public class PlantInfoActivity extends DaggerAppCompatActivity {
    private static final String TAG = "PlantInfoActivity";

    @Inject
    ViewModelProviderFactory providerFactory;

    private PlantInfoViewModel plantInfoViewModel;
    private TextView popularNameTextView;
    private TextView scientificNameTextView;
    private TextView originTextView;
    private TextView naturalAreaTextView;
    private TextView similarPlantsTextView;

    private RecyclerView descriptionsListRecyclerView;
    private RecyclerView hiddenDescriptionsListRecyclerView;
    private RecyclerView similarPlantsListRecyclerView;
    private DescriptionsListAdapter descriptionsListAdapter;
    private DescriptionsListAdapter hiddenDescriptionsListAdapter;
    private ItemListAdapter<Plant> similarPlantsListAdapter;
    private List<PlantDescription> descriptionsList;
    private List<PlantDescription> hiddenDescriptionsList;
    private PagedList<Plant> similarPlantsPagedList;

    private SliderView topSliderView;
    private SliderView focusSliderView;
    private InfoImageSliderAdapter topSliderViewAdapter;
    private InfoImageSliderAdapter focusSliderViewAdapter;
    private ImageView iconImageView;
    private Dialog imageFocusDialog;
    private Toolbar toolbar;
    private MenuItem favoriteMenuItem;
    private TextView showMoreButton;
    private View fakeStatusbarView;
    private AppBarLayout appBarLayout;
    private List<String> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_info_activity);

        // Get the necessary ViewModel using Dagger
        plantInfoViewModel = new ViewModelProvider(this, providerFactory)
                .get(PlantInfoViewModel.class);

        popularNameTextView = findViewById(R.id.plant_info_popular_name);
        scientificNameTextView = findViewById(R.id.plant_info_scientific_name);
        originTextView = findViewById(R.id.plant_info_origin);
        similarPlantsTextView = findViewById(R.id.similar_plants_title);
        naturalAreaTextView = findViewById(R.id.plant_info_natural_area);
        descriptionsListRecyclerView = findViewById(R.id.descriptions_list_recyclerview);
        hiddenDescriptionsListRecyclerView = findViewById(R.id.hidden_descriptions_list_recyclerview);
        similarPlantsListRecyclerView = findViewById(R.id.similar_plants_list_recyclerview);
        iconImageView = findViewById(R.id.plant_info_top_icon_imageview);
        appBarLayout = findViewById(R.id.app_bar_layout);
        fakeStatusbarView = findViewById(R.id.fake_statusbar_view);
        toolbar = findViewById(R.id.toolbar);
        topSliderView = findViewById(R.id.plant_info_image_slider);
        showMoreButton = findViewById(R.id.plant_info_show_more_button);

        StatusBarUtil.setTranslucentForImageView(this, toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            originTextView.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            naturalAreaTextView.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }

        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        setupRecyclerLists();

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

    private void setupRecyclerLists() {
        descriptionsList = new ArrayList<>();
        hiddenDescriptionsList = new ArrayList<>();
        similarPlantsPagedList = null;
        descriptionsListAdapter = new DescriptionsListAdapter(false, descriptionsList);
        hiddenDescriptionsListAdapter = new DescriptionsListAdapter(true, hiddenDescriptionsList);
        similarPlantsListAdapter = new ItemListAdapter<>(true);
        descriptionsListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        hiddenDescriptionsListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        similarPlantsListRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        descriptionsListRecyclerView.setAdapter(descriptionsListAdapter);
        hiddenDescriptionsListRecyclerView.setAdapter(hiddenDescriptionsListAdapter);
        similarPlantsListRecyclerView.setAdapter(similarPlantsListAdapter);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void processArguments() {
        PlantInfoActivityArgs args;
        int plantId = 0;
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            args = PlantInfoActivityArgs.fromBundle(intent.getExtras());
            plantId = args.getPlantId();
        }
        plantInfoViewModel.setPlantId(plantId);
    }

    private void setupClickListeners() {
        iconImageView.setOnClickListener(v -> focusImage(0));
        topSliderViewAdapter.setOnItemClickListener(this::focusImage);
        showMoreButton.setOnClickListener(v -> {
            if (showMoreButton.getText().equals(getString(R.string.show_more))) {
                showMoreButton.setText(R.string.show_less);
                hiddenDescriptionsListRecyclerView.setVisibility(View.VISIBLE);
                return;
            }
            if (showMoreButton.getText().equals(getString(R.string.show_less))) {
                showMoreButton.setText(R.string.show_more);
                hiddenDescriptionsListRecyclerView.setVisibility(View.GONE);
            }
        });
        focusSliderViewAdapter.setOnItemClickListener(
                p -> imageFocusDialog.dismiss()
        );
        descriptionsListAdapter.setOnClickListeners(new DescriptionsListAdapter.OnClickListeners() {
            @Override
            public void onHideClick(int position) {
                plantInfoViewModel.setOtherPlantInfoHidden(descriptionsList.get(position).settings, true);
            }
        });
        hiddenDescriptionsListAdapter.setOnClickListeners(new DescriptionsListAdapter.OnClickListeners() {
            @Override
            public void onShowClick(int position) {
                plantInfoViewModel.setOtherPlantInfoHidden(hiddenDescriptionsList.get(position).settings, false);
            }
        });
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
        similarPlantsListAdapter.setOnClickListeners(position -> {
            Plant plant = similarPlantsPagedList.get(position);
            if (plant != null) {
                navigateToPlantInfoFor(plant.id);
            }
        });
    }

    private void navigateToPlantInfoFor(int plantId) {
        Intent intent = new Intent(PlantInfoActivity.this, PlantInfoActivity.class);
        intent.putExtra("plantId", plantId);
        startActivity(intent);
        onPause();
    }

    private void subscribeToPlant() {
        plantInfoViewModel.getPlantResource().observe(this, plantResource -> {
            if (plantResource != null) {
                switch (plantResource.getStatus()) {
                    case LOADING:
                        if (plantResource.getData() != null) {
                            displayPlant(plantResource.getData());
                            subscribeToSimilarPlants();
                        } else {
                            displayPlant("loading");
                        }
                        break;
                    case SUCCESS:
                        if (plantResource.getData() != null) {
                            displayPlant(plantResource.getData());
                            subscribeToSimilarPlants();
                        } else {
                            displayPlant("empty");
                        }
                        break;
                    case ERROR:
                        Toast.makeText(this, "Can't connect to our server!", Toast.LENGTH_SHORT).show();
                        if (plantResource.getData() == null) {
                            displayPlant("error");
                        }
                        break;
                }
            }
        });
    }

    private void subscribeToSimilarPlants() {
        plantInfoViewModel.getSimilarPlantsPagedList().observe(this, similarPlantsPagedList -> {
            this.similarPlantsPagedList = similarPlantsPagedList;
            similarPlantsListAdapter.submitList(this.similarPlantsPagedList);
            if (this.similarPlantsPagedList != null && this.similarPlantsPagedList.size() > 0) {
                similarPlantsTextView.setVisibility(View.VISIBLE);
            } else {
                similarPlantsTextView.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void displayPlant(Plant plant) {
        Log.d(TAG, "displayPlant: " + plant.sectionId);
        popularNameTextView.setText(WordUtils.capitalizeFully(plant.popularName));
        scientificNameTextView.setText(WordUtils.capitalizeFully(plant.scientificName));
        String originText = "<B>" + getResources().getString(R.string.const_origin) + "</B>"
                + " " + StringUtils.capitalize(plant.origin);
        originTextView.setText(Html.fromHtml(originText, 0));
        String naturalAreaText = "<B>" + getResources().getString(R.string.const_natural_area) + "</B>"
                + " " + StringUtils.capitalize(plant.naturalArea);
        naturalAreaTextView.setText(Html.fromHtml(naturalAreaText, 0));

        setFavoriteMenuItem(plant.favorite);

        setDescriptionLists(plant);

        setImages(plant);
    }

    private void setImages(Plant plant) {
        images.clear();
        if (plant != null && plant.images.size() > 0) {
            images.addAll(
                    plant.images
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

    private void setDescriptionLists(Plant plant) {
        if (plant != null) {
            descriptionsList.clear();
            descriptionsList.addAll(
                    plant.descriptions
                            .stream()
                            .filter(d -> d.settings.show)
                            .sorted((d1, d2) -> d1.title.compareTo(d2.title))
                            .collect(Collectors.toList()));
            descriptionsListAdapter.notifyDataSetChanged();

            hiddenDescriptionsList.clear();
            hiddenDescriptionsList.addAll(plant.descriptions
                    .stream()
                    .filter(d -> !d.settings.show)
                    .sorted((d1, d2) -> d1.title.compareTo(d2.title))
                    .collect(Collectors.toList()));
            hiddenDescriptionsListAdapter.notifyDataSetChanged();
        } else {
            descriptionsList.clear();
            descriptionsListAdapter.notifyDataSetChanged();
            hiddenDescriptionsList.clear();
            hiddenDescriptionsListAdapter.notifyDataSetChanged();
        }

        if (hiddenDescriptionsList.size() > 0) {
            showMoreButton.setVisibility(View.VISIBLE);
        } else {
            showMoreButton.setVisibility(View.GONE);
            hiddenDescriptionsListRecyclerView.setVisibility(View.GONE);
            showMoreButton.setText(R.string.show_more);
        }
    }

    private void displayPlant(String placeholder) {
        popularNameTextView.setText(placeholder);
        scientificNameTextView.setText(placeholder);
        originTextView.setText(placeholder);
        naturalAreaTextView.setText(placeholder);

        setDescriptionLists(null);

        setImages(null);
    }

    private void focusImage(int position) {
        focusSliderView.setCurrentPagePosition(position);

        imageFocusDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.plant_info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_favorite:
                onFavoritePressed();
                return true;
            case R.id.action_map:
                onMapPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onMapPressed() {
        plantInfoViewModel.getPlantResource().observe(this, plantResource -> {
            if (plantResource != null) {
                switch (plantResource.getStatus()) {
                    case ERROR:
                        Toast.makeText(this, "Can't connect to our server!", Toast.LENGTH_SHORT).show();
                    case SUCCESS:
                    case LOADING:
                        if (plantResource.getData() != null) {
                            Integer sectionId = plantResource.getData().sectionId;
                            if(sectionId != null){
                                startMainActivityOnSection(sectionId);
                            }
                            else{
                                Toast.makeText(this, "Această plantă nu a fost asignată încă unei secții.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                }
            }
        });
    }

    private void startMainActivityOnSection(Integer sectionId) {
        Intent intent = new Intent(PlantInfoActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("sectionId", sectionId);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        favoriteMenuItem = menu.findItem(R.id.action_favorite);
        subscribeToPlant();
        return true;
    }

    private void onFavoritePressed() {
        boolean favorite = favoriteMenuItem.isChecked();
        favorite = !favorite;
        plantInfoViewModel.setFavorite(favorite);
        setFavoriteMenuItem(favorite);
    }


    private void setFavoriteMenuItem(boolean favorite) {
        if (favoriteMenuItem != null && favorite != favoriteMenuItem.isChecked()) {
            if (favorite) {
                favoriteMenuItem.setChecked(true);
                favoriteMenuItem.setIcon(ContextCompat
                        .getDrawable(getApplicationContext(), R.drawable.ic_favorite));
            } else {
                favoriteMenuItem.setChecked(false);
                favoriteMenuItem.setIcon(ContextCompat
                        .getDrawable(getApplicationContext(), R.drawable.ic_favorite_border));
            }
        }
    }
}
