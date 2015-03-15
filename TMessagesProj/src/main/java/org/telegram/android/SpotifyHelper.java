package org.telegram.android;

import android.app.Activity;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

public class SpotifyHelper {

    private static SpotifyHelper instance;

    private final String clientId = "f14ccf1b7c0648cb85350639b299ef57";
    private final String redirectUri = "amix://callback";

    private String accessToken;
    private boolean logged;
    private SpotifyApi api;

    private SpotifyHelper() {}

    public static SpotifyHelper getInstance() {
        if (instance == null) {
            instance = new SpotifyHelper();
        }
        return instance;
    }

    public void doAuthetication (Activity activity, int requestCode ) {
        AuthenticationRequest request = new AuthenticationRequest.Builder(clientId, AuthenticationResponse.Type.TOKEN, redirectUri).setScopes(new String[]{"user-read-private", "playlist-read", "playlist-read-private", "streaming"}).build();
        AuthenticationClient.openLoginActivity(activity, requestCode, request);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public SpotifyApi getApi() { return api; }

    public void setApi(SpotifyApi api) { this.api = api; }
}
