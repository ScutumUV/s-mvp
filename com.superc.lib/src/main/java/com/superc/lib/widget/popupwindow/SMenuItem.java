package com.superc.lib.widget.popupwindow;

import android.support.annotation.IdRes;

/**
 * Created by owner on 2017/8/21.
 */
public class SMenuItem<T> {

    private int itemId;

    private String title;

    private int icon;

    private boolean isChecked;

    private boolean isSelected;

    private T t;

    public SMenuItem(String title) {
        setTitle(title);
    }

    public SMenuItem(String title, @IdRes int icon) {
        setTitle(title);
        setIcon(icon);
    }

    public SMenuItem(String title, @IdRes int icon, boolean isChecked) {
        setTitle(title);
        setIcon(icon);
        setChecked(isChecked);
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "SMenuItem{" +
                "itemId=" + itemId +
                ", title='" + title + '\'' +
                ", icon=" + icon +
                ", isChecked=" + isChecked +
                ", isSelected=" + isSelected +
                ", t=" + t +
                '}';
    }
}
