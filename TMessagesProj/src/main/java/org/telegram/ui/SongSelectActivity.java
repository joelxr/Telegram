package org.telegram.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.playback.Config;
import com.spotify.sdk.android.playback.Player;

import org.telegram.R;
import org.telegram.android.LocaleController;
import org.telegram.android.SpotifyHelper;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.LoaderImageView;
import org.telegram.ui.Listeners.SpotifySearchTextListener;

import java.util.List;

import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Track;

public class SongSelectActivity extends BaseFragment {

    public static abstract interface SongSelectActivityDelegate {
        public void didSelectSong(SongSelectActivity activity, String song);
    }

    private Player mPlayer;
    private SongSelectActivityDelegate delegate;
    private TextView emptyView;
    private ListView listView;
    private SpotifyListAdapter adapter;
    private List searchResult;

    public final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List result = (List) msg.obj;
            adapter.addAll(result);
        }
    };

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
            menu.addItem(0, R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new SpotifySearchTextListener(this));
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
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    play();
                }
            });

            adapter = new SpotifyListAdapter(getParentActivity());
            listView.setAdapter(adapter);


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

    public List getSearchResult() { return searchResult; }

    public void setSearchResult(List searchResult) {
        this.searchResult = searchResult;
    }
}

class SpotifyListAdapter extends ArrayAdapter<Track> {

    public SpotifyListAdapter(Context context) {
        super(context, R.layout.song_list_row);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.song_list_row, parent, false);
        TextView firstLineTextView = (TextView) rowView.findViewById(R.id.firstLine);
        TextView secondLineTextView = (TextView) rowView.findViewById(R.id.secondLine);
        LoaderImageView imageView = (LoaderImageView) rowView.findViewById(R.id.icon);
        Track t = this.getItem(position);
        imageView.setImageDrawable(t.album.images.get(0).url);
        firstLineTextView.setText(t.name);
        StringBuilder artists = new StringBuilder();

        for (int i = 0; i < t.artists.size(); i++) {
            ArtistSimple a = t.artists.get(i);
            artists.append(a.name) ;

            if (i == t.artists.size()-2) {
                artists.append(" and ");
            } else if (i != t.artists.size()-1) {
                artists.append(", ");
            }
        }

        secondLineTextView.setText(artists);

        return rowView;
    }
}
