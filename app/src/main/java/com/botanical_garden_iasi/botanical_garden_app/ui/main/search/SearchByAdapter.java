package com.botanical_garden_iasi.botanical_garden_app.ui.main.search;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.botanical_garden_iasi.botanical_garden_app.R;
import com.google.android.material.card.MaterialCardView;

import org.apache.commons.text.WordUtils;

import java.util.List;

public class SearchByAdapter extends RecyclerView.Adapter<SearchByAdapter.ViewHolder> {
    private static final String TAG = "SearchByAdapter";
    private List<Pair<String, Integer>> items;
    private OnClickListeners onClickListeners;
    private int selectedPosition;

    public SearchByAdapter(List<Pair<String, Integer>> items) {
        this.items = items;
        selectedPosition = 0;
    }

    @NonNull
    @Override
    public SearchByAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pill_select_list_item, parent, false);
        return new SearchByAdapter.ViewHolder(view);
    }

    public void setOnSelectListener(OnClickListeners onClickListeners) {
        this.onClickListeners = onClickListeners;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchByAdapter.ViewHolder holder, int position) {
        holder.bindTo(items.get(position));
        holder.selectedLayout(position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Pair<String, Integer> getSelectedItem() {
        return items.get(selectedPosition);
    }

    public void clearSelectedItem() {
        selectedPosition = 0;
        callOnItemSelect();
    }

    private void setSelectedPosition(int positionToSelect) {
        notifyItemChanged(selectedPosition);
        selectedPosition = positionToSelect;
        notifyItemChanged(selectedPosition);
        callOnItemSelect();
    }

    private void callOnItemSelect() {
        if (onClickListeners != null) {
            onClickListeners.onItemSelect(selectedPosition);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final String TAG = "SearchByAdapterViewHolder";

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
