package com.botanical_garden_iasi.botanical_garden_app.ui.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.botanical_garden_iasi.botanical_garden_app.R;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.settings.SettingsItem;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class SettingsListAdapter extends RecyclerView.Adapter<SettingsListAdapter.ViewHolder> {

    private List<? extends SettingsItem> settingsItems;
    private OnClickListeners onClickListeners;

    public SettingsListAdapter(List<? extends SettingsItem> settingsItems) {
        this.settingsItems = settingsItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_item, parent, false);
        return new ViewHolder(view, onClickListeners);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindTo(settingsItems.get(position), getItemCount() == position + 1);
    }

    @Override
    public int getItemCount() {
        return settingsItems.size();
    }


    public void setOnClickListeners(OnClickListeners onClickListeners) {
        this.onClickListeners = onClickListeners;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        Switch mySwitch;
        View bottom_line;

        OnClickListeners onClickListeners;

        ViewHolder(@NonNull View itemView, OnClickListeners onClickListeners) {
            super(itemView);
            name = itemView.findViewById(R.id.setting_title);
            mySwitch = itemView.findViewById(R.id.setting_switch);
            bottom_line = itemView.findViewById(R.id.bottom_line);
            this.onClickListeners = onClickListeners;
            setupOnItemClickListeners(itemView);
        }

        private void setupOnItemClickListeners(View itemView) {
            setuponSwitchClickListener();
        }

        void bindTo(SettingsItem settingsItem, boolean isLastItem) {
            name.setText(StringUtils.capitalize(settingsItem.getName()));
            mySwitch.setChecked(settingsItem.isChecked());
            if (isLastItem) {
                bottom_line.setVisibility(View.INVISIBLE);
            } else {
                bottom_line.setVisibility(View.VISIBLE);
            }

        }

        private void setuponSwitchClickListener() {
            mySwitch.setOnClickListener(buttonView -> {
                if (onClickListeners != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onClickListeners.onSwitchClick(position, mySwitch.isChecked());
                    }
                }
            });
        }

    }

    public interface OnClickListeners {
        void onSwitchClick(int position, boolean isChecked);
    }
}
