package com.fliu98.hearthfire.server;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fliu98.hearthfire.model.Deck;
import com.fliu98.hearthfire.model.DeckInfo;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * For communications with the server.
 */

public class ServerUtils {

    private static RequestQueue sQueue;
    private static final String LOG_TAG = ServerUtils.class.getSimpleName();
    private static int sStatusCode = 200;
    private static String sUserId;
    private static String sSessionKey;

    public static void initializeRequestQueue(Context context) {
        Log.d(LOG_TAG, "Initializing RequestQueue.");
        sQueue = Volley.newRequestQueue(context);
    }

    public static void logIn(final String idToken, final OnLoginListener loginListener) {
        StringRequest postRequest = new StringRequest(Request.Method.POST,
                JsonKeys.URL_BASE + "/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (sStatusCode == HttpURLConnection.HTTP_OK) {
                            Log.d(LOG_TAG, "Id Token verification successful.");
                            String[] responses = response.split("-");
                            sUserId = responses[0];
                            sSessionKey = responses[1];
                            loginListener.onLoginSuccess();
                        } else {
                            loginListener.onLoginFailure();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_TAG + "error on post",
                                error.toString());
                        loginListener.onLoginFailure();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(JsonKeys.ID_TOKEN, idToken);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                sStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
        sQueue.add(postRequest);
    }

    public static void uploadDeck(DeckInfo deckInfo, final OnDeckSaveListener saveListener) {
        try {

            JSONObject postParams = new JSONObject();
            postParams.put(JsonKeys.USER_ID, sUserId);
            postParams.put(JsonKeys.SESSION_KEY, sSessionKey);
            postParams.put(JsonKeys.DECK_NAME, deckInfo.name);
            postParams.put(JsonKeys.DECK_HERO_CLASS, deckInfo.heroClass);
            postParams.put(JsonKeys.DECK_DESCRIPTION, deckInfo.description);
            postParams.put(JsonKeys.DECK_LIST, new Gson()
                    .toJson(Deck.deckListToArray(deckInfo.deckList)));

            if (deckInfo.deckId != null) {
                // Update existing deck
                postParams.put(JsonKeys.DECK_ID, deckInfo.deckId);
            }


            JsonObjectRequest jsonPost = new JsonObjectRequest(Request.Method.POST,
                    JsonKeys.URL_BASE + "/save-deck", postParams,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            if (sStatusCode == HttpURLConnection.HTTP_OK) {
                                Log.d(LOG_TAG, "Successfully saved deck");
                                saveListener.onSaveSuccess();
                            } else {
                                saveListener.onSaveFailure();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(LOG_TAG, "Deck save failed with error: " + error);
                            saveListener.onSaveFailure();
                        }
                    })
            {
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    sStatusCode = response.statusCode;
                    return super.parseNetworkResponse(response);
                }
            };
            sQueue.add(jsonPost);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "cannot upload deck: ", e);
        }
    }

    public static void getDecks(final OnDeckGetListener listener) {
        try {
            JSONObject postParams = new JSONObject();
            postParams.put(JsonKeys.USER_ID, sUserId);
            postParams.put(JsonKeys.SESSION_KEY, sSessionKey);

            StringRequest stringGet = new StringRequest(Request.Method.GET,
                    JsonKeys.URL_BASE + "/get-decks?userId=" + sUserId
                            + "&sessionKey=" + sSessionKey,
                    new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            if (sStatusCode == HttpURLConnection.HTTP_OK
                        || sStatusCode == HttpURLConnection.HTTP_NOT_MODIFIED) {
                                Gson gson = new Gson();
                                Deck[] decks = gson.fromJson(response, Deck[].class);
                                listener.onDecksReceived(new ArrayList<> (Arrays.asList(decks)));
                            }
                        }
                    },
                    new Response.ErrorListener(){

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(LOG_TAG, "Failed to get decks with error: " + error);
                        }
                    })
            {
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    sStatusCode = response.statusCode;
                    return super.parseNetworkResponse(response);
                }
            };
            sQueue.add(stringGet);
        } catch (Exception e) {
            Log.e(LOG_TAG, "cannot get decks: ", e);
        }
    }

    public interface OnLoginListener {
        void onLoginSuccess();
        void onLoginFailure();
    }

    public interface OnDeckSaveListener {
        void onSaveSuccess();
        void onSaveFailure();
    }

    public interface OnDeckGetListener {
        void onDecksReceived(ArrayList<Deck> decks);
    }
}
