package com.botanical_garden_iasi.botanical_garden_app.ui.main.maps;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.botanical_garden_iasi.botanical_garden_app.R;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.sections.Section;
import com.google.android.material.card.MaterialCardView;

import org.apache.commons.text.WordUtils;

class SectionsListSelectorAdapter extends PagedListAdapter<Section, SectionsListSelectorAdapter.ViewHolder> {
    private static final String TAG = "SectionsListSelectorAdapter";

    private OnClickListeners onClickListeners;
    private int selectedPosition;

    SectionsListSelectorAdapter() {
        super(new DiffUtil.ItemCallback<Section>() {
            @Override
            public boolean areItemsTheSame(@NonNull Section oldItem,
                                           @NonNull Section newItem) {
                return oldItem.id == newItem.id;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Section oldItem,
                                              @NonNull Section newItem) {
                return oldItem.equals(newItem);
            }
        });
        selectedPosition = -1;
    }

    void setOnSelectListener(OnClickListeners onClickListeners) {
        this.onClickListeners = onClickListeners;
    }

    @NonNull
    @Override
    public SectionsListSelectorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pill_select_list_item, parent, false);

        return new SectionsListSelectorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionsListSelectorAdapter.ViewHolder holder, int position) {
        Section section = getItem(position);
        if (section != null) {
            Pair<String, Integer> sectionItem = new Pair<>(section.name, section.id);
            holder.bindTo(sectionItem);
            holder.selectedLayout(position == selectedPosition);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public Section getSelectedItem() {
        if (getCurrentList() != null && selectedPosition >= 0) {
            return getCurrentList().get(selectedPosition);
        }
        return null;
    }

    public void clearSelectedItem() {
        setSelectedPosition(-1);
    }

    private void callOnItemSelect() {
        if (onClickListeners != null) {
            onClickListeners.onItemSelect(selectedPosition);
        }
    }

    public void setSelectedPosition(int positionToSelect) {
        if( positionToSelect == selectedPosition) {
            positionToSelect = -1;
        }
        notifyItemChanged(selectedPosition);
        selectedPosition = positionToSelect;
        notifyItemChanged(selectedPosition);
        callOnItemSelect();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final String TAG = "ItemListAdapterViewHolder";

        private Context context;
        private TextView title;
        private MaterialCardView cardView;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            title = itemView.findViewById(R.id.title);
            cardView = itemView.findViewById(R.id.card_view);
            itemView.setOnClickListener(this);
        }

        void bindTo(Pair<String, Integer> entry) {
            title.setText(WordUtils.capitalizeFully(entry.first));
        }


        void selectedLayout(boolean isSelected) {
            if (isSelected) {
                title.setTextColor(context.getColor(R.color.color_white));
                cardView.setCardBackgroundColor(context.getColor(R.color.color_primary));
            } else {
                title.setTextColor(context.getColor(R.color.color_primary_dark));
                cardView.setCardBackgroundColor(context.getColor(R.color.color_white));
            }
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
            setSelectedPosition(getAdapterPosition());
        }
    }

    public interface OnClickListeners {
        void onItemSelect(int selectedPosition);
    }
}