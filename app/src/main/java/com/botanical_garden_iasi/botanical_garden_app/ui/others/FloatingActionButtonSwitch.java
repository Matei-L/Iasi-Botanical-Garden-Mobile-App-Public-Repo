package com.botanical_garden_iasi.botanical_garden_app.ui.others;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.getbase.floatingactionbutton.FloatingActionButton;

public class FloatingActionButtonSwitch extends FloatingActionButton implements View.OnClickListener {

    private boolean isActive = false;
    private int activeDrawableId = 0;
    private int inactiveDrawableId = 0;
    private String activeTitle = null;
    private String inactiveTitle = null;
    private OnSwitchListener onSwitchListener = null;

    public FloatingActionButtonSwitch(@NonNull Context context) {
        super(context);
        init();
    }

    public FloatingActionButtonSwitch(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatingActionButtonSwitch(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = !active;
        callOnClick();
    }

    public void init(int activeDrawableId, String activeTitle, int inactiveDrawableId, String inactiveTitle) {
        this.activeDrawableId = activeDrawableId;
        this.inactiveDrawableId = inactiveDrawableId;
        this.activeTitle = activeTitle;
        this.inactiveTitle = inactiveTitle;
        onSwitch();
    }

    private void init() {
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        isActive = !isActive;
        onSwitch();
        if (onSwitchListener != null) {
            onSwitchListener.onSwitch(v, isActive);
        }
    }

    private void onSwitch() {
        if (isActive) {
            if (activeDrawableId != 0) {
                setImageResource(activeDrawableId);
            }
            if (activeTitle != null) {
                setTitle(activeTitle);
            }

        } else {
            if (inactiveDrawableId != 0) {
                setImageResource(inactiveDrawableId);
            }
            if (inactiveTitle != null) {
                setTitle(inactiveTitle);
            }
        }
    }


    public void setOnSwitchListener(@Nullable OnSwitchListener l) {
        onSwitchListener = l;
    }

    public interface OnSwitchListener {
        void onSwitch(View v, boolean isActive);
    }
}
