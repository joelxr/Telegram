package org.telegram.ui;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.telegram.android.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.BaseFragment;

public class SongSelectActivity extends BaseFragment {

    public static abstract interface SongSelectActivityDelegate {
        public void didSelectSong(SongSelectActivity activity, String song);
    }

    private SongSelectActivityDelegate delegate;
    private TextView emptyView;
    private ListView listView;

    public void setDelegate(SongSelectActivityDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container) {

        if (fragmentView == null) {
            actionBar.setBackButtonImage(R.drawable.ic_ab_back);
            actionBar.setAllowOverlayTitle(true);
            actionBar.setTitle(LocaleController.getString("SelectSong", R.string.SelectSong));
            actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
                @Override
                public void onItemClick(int id) {
                    switch (id) {
                        default:
                            finishFragment();
                            break;
                    }
                }
            });

            ActionBarMenu menu = actionBar.createMenu();
            menu.addItem(0, R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
                @Override
                public void onSearchExpand() {

                }

                @Override
                public void onSearchCollapse() {

                }

                @Override
                public void onTextChanged(EditText editText) {

                }
            });

            fragmentView = inflater.inflate(R.layout.song_select_layout, container, false);
            emptyView = (TextView)fragmentView.findViewById(R.id.searchEmptyView);
            emptyView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            listView = (ListView)fragmentView.findViewById(R.id.listView);
            listView.setEmptyView(emptyView);

        } else {
            ViewGroup parent = (ViewGroup)fragmentView.getParent();
            if (parent != null) {
                parent.removeView(fragmentView);
            }
        }

        return fragmentView;
    }
}
