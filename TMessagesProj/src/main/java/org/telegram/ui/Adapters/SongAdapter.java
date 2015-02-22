package org.telegram.ui.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.telegram.R;
import org.telegram.ui.Cells.TextCell;

public class SongAdapter extends BaseSectionsAdapter {

    private Context mContext;

    public SongAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getSectionCount() {
        return 0;
    }

    @Override
    public int getCountForSection(int section) {
        return 0;
    }

    @Override
    public boolean isRowEnabled(int section, int row) {
        return false;
    }

    @Override
    public int getItemViewType(int section, int position) {
        return 0;
    }

    @Override
    public Object getItem(int section, int position) {
        return null;
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new TextCell(mContext);
        }

        TextCell actionCell = (TextCell) convertView;
        actionCell.setTextAndIcon("", R.drawable.play1);
        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        return null;
    }
}
