package com.botanical_garden_iasi.botanical_garden_app.ui.main.sections.section_info_activity;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.botanical_garden_iasi.botanical_garden_app.R;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.sections.SubSection;
import com.google.android.material.card.MaterialCardView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class SubsectionsListAdapter extends RecyclerView.Adapter<SubsectionsListAdapter.ViewHolder> {
    private static final String TAG = "SubsectionsListAdapter";

    private List<SubSection> items;
    private OnClickListeners onClickListeners;

    public SubsectionsListAdapter(List<SubSection> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public SubsectionsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.descriptions_list_item, parent, false);
        return new ViewHolder(view, onClickListeners);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindTo(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void setOnClickListeners(OnClickListeners onClickListeners) {
        this.onClickListeners = onClickListeners;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "SubsectionItemViewHold";

        TextView title;
        TextView description;
        TextView moreVert;

        ConstraintLayout bottomShadow;
        MaterialCardView cardView;
        int cardViewMaxHeight;
        ToggleButton expandToggle;
        ConstraintLayout cardViewContentConstraintLayout;
        OnClickListeners onClickListeners;
        boolean expandable;


        ViewHolder(@NonNull View itemView, OnClickListeners onClickListeners) {
            super(itemView);

            title = itemView.findViewById(R.id.description_item_title);
            description = itemView.findViewById(R.id.description_item_description);
            moreVert = itemView.findViewById(R.id.description_item_more_vert);
            cardView = itemView.findViewById(R.id.description_item_cardview);
            bottomShadow = itemView.findViewById(R.id.list_item_bottom_shadow);
            expandToggle = itemView.findViewById(R.id.description_item_expand_toggle);
            cardViewContentConstraintLayout = itemView.findViewById(R.id.description_item_card_view_content_constraint_layout);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                description.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            }

            cardViewMaxHeight = (int) (200 * Resources.getSystem().getDisplayMetrics().density);
            expandable = true;
            moreVert.setVisibility(View.INVISIBLE);


            this.onClickListeners = onClickListeners;
            setupOnItemClickListener(itemView);
        }

        void bindTo(SubSection subSection) {

            title.setText(StringUtils.capitalize(subSection.name));
            description.setText(StringUtils.capitalize(subSection.description));

            setupExpandable();

        }

        private void setupOnItemClickListener(@NonNull View itemView) {
            itemView.setOnClickListener(v -> {
                if (onClickListeners != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onClickListeners.onItemClick(position);
                    }
                }
                expandToggle();
            });
        }

        private void setupExpandable() {
            ViewTreeObserver vto = cardView.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    cardView.getViewTreeObserver().removeOnPreDrawListener(this);
                    int cardViewConstraintLayoutHeight = cardViewContentConstraintLayout.getMeasuredHeight();

                    if (cardViewConstraintLayoutHeight > cardViewMaxHeight) {
                        bottomShadow.setBackgroundColor(
                                ContextCompat.getColor(
                                        bottomShadow.getContext(),
                                        R.color.color_white_transparent
                                )
                        );
                        expandToggle.setVisibility(View.VISIBLE);
                        expandable = true;
                        setExpandedToggle(false);
                    } else {
                        bottomShadow.setBackgroundColor(Color.TRANSPARENT);
                        expandToggle.setVisibility(View.GONE);
                        expandable = false;
                        setExpandedToggle(true);
                    }
                    return true;
                }
            });
        }

        private void expandToggle() {
            if (expandable) {
                boolean expanded = expandToggle.isChecked();
                expanded = !expanded;
                setExpandedToggle(expanded);
            }
        }

        private void setExpandedToggle(boolean expanded) {
            if (expanded) {
                ViewGroup.LayoutParams params = cardView.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                cardView.setLayoutParams(params);
                bottomShadow.setBackgroundColor(Color.TRANSPARENT);
            } else {
                ViewGroup.LayoutParams params = cardView.getLayoutParams();
                params.height = cardViewMaxHeight;
                cardView.setLayoutParams(params);
                bottomShadow.setBackgroundColor(ContextCompat.getColor(bottomShadow.getContext(), R.color.color_white_transparent));
            }
            expandToggle.setChecked(expanded);
        }
    }

    public interface OnClickListeners {
        default void onItemClick(int position) {

        }
    }
}
