package org.telegram.ui.Listeners;

import android.os.Message;
import android.widget.EditText;

import org.telegram.android.SpotifyHelper;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.SongSelectActivity;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by joel on 14/03/15.
 */
public class SpotifySearchTextListener implements ActionBarMenuItem.ActionBarMenuItemSearchListener {

    private SongSelectActivity parent;

    public SpotifySearchTextListener (SongSelectActivity parent) {
        this.parent = parent;
    }

    @Override
    public void onSearchExpand() {}

    @Override
    public void onSearchCollapse() {}

    @Override
    public void onTextChanged(EditText editText) {
        String text = editText.getText().toString();
        searchText(text);
    }

    private void searchText(String text) {
        if (text.length() > 5 ) {

            SpotifyService service = SpotifyHelper.getInstance().getApi().getService();


            service.searchTracks(text, new Callback<TracksPager>() {
                @Override
                public void success(TracksPager tracksPager, Response response) {
                    Pager<Track> result = tracksPager.tracks;
                    parent.setSearchResult(result.items);
                    Message msg = parent.handler.obtainMessage();
                    msg.obj = result.items;
                    parent.handler.sendMessage(msg);

                }

                @Override
                public void failure(RetrofitError retrofitError) {

                }
            });
        }
    }

}
