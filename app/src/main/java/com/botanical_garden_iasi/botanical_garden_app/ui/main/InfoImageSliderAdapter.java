package com.botanical_garden_iasi.botanical_garden_app.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.botanical_garden_iasi.botanical_garden_app.R;
import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class InfoImageSliderAdapter extends SliderViewAdapter<InfoImageSliderAdapter.ViewHolder> {
    private static final String TAG = "InfoImageSliderAdapter";

    private List<String> urls;
    private boolean focus;
    private OnItemClickListener onItemClickListener;

    public InfoImageSliderAdapter(boolean focus, List<String> urls) {
        this.urls = urls;
        this.focus = focus;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate;
        if (focus) {
            inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_focus_slider_item, parent, false);
        } else {
            inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_top_slider_item, parent, false);
        }
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Glide.with(viewHolder.itemView)
                .asBitmap()
                .load(urls.get(position))
                .error(R.drawable.plant_placeholder)
                .into(viewHolder.imageViewBackground);

        setupOnItemClick(viewHolder, position);
    }

    private void setupOnItemClick(ViewHolder viewHolder, int position) {
        viewHolder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
    }


    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    class ViewHolder extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        TextView textViewDescription;

        ViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.info_iv_auto_image_slider);
            textViewDescription = itemView.findViewById(R.id.info_tv_auto_image_slider);
            this.itemView = itemView;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}