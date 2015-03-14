package org.telegram.ui;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.playback.Config;
import com.spotify.sdk.android.playback.Player;

import org.telegram.android.LocaleController;
import org.telegram.R;
import org.telegram.android.SpotifyHelper;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.BaseFragment;

public class SongSelectActivity extends BaseFragment {

    public static abstract interface SongSelectActivityDelegate {
        public void didSelectSong(SongSelectActivity activity, String song);
    }

    private Player mPlayer;
    private SongSelectActivityDelegate delegate;
    private TextView emptyView;
    private ListView listView;
    private Button playButton;

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

            playButton = (Button) fragmentView.findViewById(R.id.play_button);
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    play();
                }
            });


        } else {
            ViewGroup parent = (ViewGroup)fragmentView.getParent();
            if (parent != null) {
                parent.removeView(fragmentView);
            }
        }

        return fragmentView;
    }

    private void play() {
        String clientId = SpotifyHelper.getInstance().getClientId();
        String accessToken = SpotifyHelper.getInstance().getAccessToken();

        if (!"".equals(accessToken)) {
            Config playerConfig = new Config(getParentActivity(), accessToken, clientId);

            mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                @Override
                public void onInitialized(Player player) {
                    mPlayer.addConnectionStateCallback((LaunchActivity) getParentActivity());
                    mPlayer.addPlayerNotificationCallback((LaunchActivity) getParentActivity());
                }

                @Override
                public void onError(Throwable throwable) { }
            });

            mPlayer.play("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");
        }
    }


}
