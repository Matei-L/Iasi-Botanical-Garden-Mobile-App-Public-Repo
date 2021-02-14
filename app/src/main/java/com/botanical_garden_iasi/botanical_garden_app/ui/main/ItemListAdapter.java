package com.botanical_garden_iasi.botanical_garden_app.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.botanical_garden_iasi.botanical_garden_app.R;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.ItemModel;
import com.bumptech.glide.Glide;

import org.apache.commons.text.WordUtils;

public class ItemListAdapter<T extends ItemModel> extends PagedListAdapter<T, ItemListAdapter.ViewHolder> {
    private static final String TAG = "ItemListAdapter";

    private OnClickListeners onClickListeners;
    private boolean horizontal;

    public ItemListAdapter() {
        super(new DiffUtil.ItemCallback<T>() {
            @Override
            public boolean areItemsTheSame(@NonNull T oldItem,
                                           @NonNull T newItem) {
                return oldItem.id == newItem.id;
            }

            @Override
            public boolean areContentsTheSame(@NonNull T oldItem,
                                              @NonNull T newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.horizontal = false;
    }

    public ItemListAdapter(boolean horizontal) {
        this();
        this.horizontal = horizontal;
    }

    public void setOnClickListeners(OnClickListeners onClickListeners) {
        this.onClickListeners = onClickListeners;
    }

    @NonNull
    @Override
    public ItemListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (horizontal) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_list_item, parent, false);
        }
        return new ItemListAdapter.ViewHolder(view, onClickListeners);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListAdapter.ViewHolder holder, int position) {
        ItemModel item = getItem(position);
        if (item != null) {
            holder.bindTo(item);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ItemListAdapterViewHolder";

        private TextView title;
        private TextView subtitle;
        private ImageView Icon;


        ViewHolder(@NonNull View itemView, OnClickListeners onClickListeners) {
            super(itemView);
            title = itemView.findViewById(R.id.list_item_title);
            subtitle = itemView.findViewById(R.id.list_item_subtitle);
            Icon = itemView.findViewById(R.id.list_item_icon);
            setupOnItemClickListener(itemView, onClickListeners);
        }

        void bindTo(ItemModel item) {
            subtitle.setText(WordUtils.capitalizeFully(item.getSubTitle()));
            title.setText(WordUtils.capitalizeFully(item.getTitle()));
            Glide.with(Icon.getContext())
                    .asBitmap()
                    .load(item.getIconUrl())
                    .error(R.drawable.plant_placeholder)
                    .into(Icon);
        }

        private void setupOnItemClickListener(@NonNull View itemView,
                                              OnClickListeners onClickListeners) {
            itemView.setOnClickListener(v -> {
                if (onClickListeners != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onClickListeners.onItemClick(position);
                    }
                }
            });
        }
    }

    public interface OnClickListeners {
        void onItemClick(int position);
    }
}
