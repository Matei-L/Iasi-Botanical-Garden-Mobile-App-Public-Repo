package com.botanical_garden_iasi.botanical_garden_app.ui.main.plants.plant_info_activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.botanical_garden_iasi.botanical_garden_app.R;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.plants.PlantDescription;
import com.google.android.material.card.MaterialCardView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class DescriptionsListAdapter extends RecyclerView.Adapter<DescriptionsListAdapter.ViewHolder> {
    private static final String TAG = "DescriptionsListAdapter";

    private List<PlantDescription> items;
    private OnClickListeners onClickListeners;
    private boolean hidden;

    public DescriptionsListAdapter(boolean hidden, List<PlantDescription> items) {
        this.items = items;
        this.hidden = hidden;
    }

    @NonNull
    @Override
    public DescriptionsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.descriptions_list_item, parent, false);
        return new ViewHolder(view, onClickListeners);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindTo(items.get(position), hidden);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void setOnClickListeners(OnClickListeners onClickListeners) {
        this.onClickListeners = onClickListeners;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "DescriptionItemViewHold";

        TextView title;
        TextView description;
        TextView moreVert;

        ConstraintLayout bottomShadow;
        MaterialCardView cardView;
        int cardViewMaxHeight;
        AppCompatToggleButton expandToggle;
        ConstraintLayout cardViewContentConstraintLayout;
        OnClickListeners onClickListeners;
        boolean expandable;
        boolean hidden;


        ViewHolder(@NonNull View itemView, OnClickListeners onClickListeners) {
            super(itemView);

            title = itemView.findViewById(R.id.description_item_title);
            description = itemView.findViewById(R.id.description_item_description);

            cardView = itemView.findViewById(R.id.description_item_cardview);
            bottomShadow = itemView.findViewById(R.id.list_item_bottom_shadow);
            expandToggle = itemView.findViewById(R.id.description_item_expand_toggle);
            cardViewContentConstraintLayout = itemView.findViewById(R.id.description_item_card_view_content_constraint_layout);
            moreVert = itemView.findViewById(R.id.description_item_more_vert);
            this.onClickListeners = onClickListeners;
            cardViewMaxHeight = (int) (200 * Resources.getSystem().getDisplayMetrics().density);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                description.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            }

            expandable = true;
            hidden = false;
            setupOnItemClickListener(itemView);
        }

        void bindTo(PlantDescription plantDescription, boolean hidden) {
            this.hidden = hidden;

            title.setText(StringUtils.capitalize(plantDescription.title));
            description.setText(StringUtils.capitalize(plantDescription.description));

            setupExpandable();
            setupMoreVertMenu();

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

        private void setupMoreVertMenu() {
            moreVert.setOnClickListener(v -> {
                Context wrapper = new ContextThemeWrapper(moreVert.getContext(), R.style.PopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, moreVert);
                popup.setGravity(Gravity.END);
                popup.inflate(R.menu.plant_info_description_item_menu);
                MenuItem menuItem;
                if (hidden) {
                    menuItem = popup.getMenu().findItem(R.id.action_hide);
                } else {
                    menuItem = popup.getMenu().findItem(R.id.action_show);
                }
                if (menuItem != null) {
                    menuItem.setVisible(false);
                }
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.action_hide:
                            if (onClickListeners != null) {
                                int position = getAdapterPosition();
                                if (position != RecyclerView.NO_POSITION) {
                                    onClickListeners.onHideClick(position);
                                }
                            }
                            return true;
                        case R.id.action_show:
                            if (onClickListeners != null) {
                                int position = getAdapterPosition();
                                if (position != RecyclerView.NO_POSITION) {
                                    onClickListeners.onShowClick(position);
                                }
                            }
                            return true;
                    }
                    return true;
                });
                popup.show();
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

        default void onHideClick(int position) {

        }

        default void onShowClick(int position) {

        }
    }
}
