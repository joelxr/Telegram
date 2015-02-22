package org.telegram.ui;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.spotify.sdk.android.playback.Player;

import org.telegram.R;
import org.telegram.android.LocaleController;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Adapters.SongAdapter;

public class SongSelectActivity extends BaseFragment {

    private static final String CLIENT_ID = "f14ccf1b7c0648cb85350639b299ef57";
    private static final String REDIRECT_URI = "amix://callback";
    private SongSelectActivityDelegate delegate;
    private TextView emptyView;
    private ListView listView;
    private SongAdapter songAdapter;
    private Player mPlayer;

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

            songAdapter = new SongAdapter(getParentActivity());

            fragmentView = inflater.inflate(R.layout.song_select_layout, container, false);

            emptyView = (TextView) fragmentView.findViewById(R.id.searchEmptyView);
            emptyView.setText(LocaleController.getString("NoSongs", R.string.NoSongs));
            emptyView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });

            listView = (ListView) fragmentView.findViewById(R.id.listView);
            listView.setEmptyView(emptyView);
            listView.setAdapter(songAdapter);


        } else {
            ViewGroup parent = (ViewGroup) fragmentView.getParent();
            if (parent != null) {
                parent.removeView(fragmentView);
            }
        }

        return fragmentView;
    }

    public static abstract interface SongSelectActivityDelegate {
        public void didSelectSong(SongSelectActivity activity, String song);
    }
}
