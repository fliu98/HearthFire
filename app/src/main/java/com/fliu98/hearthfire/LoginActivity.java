package com.fliu98.hearthfire;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fliu98.hearthfire.server.ServerUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity implements ServerUtils.OnLoginListener{

    private GoogleSignInClient mSignInClient;
    private static final int REQUEST_CODE_SIGN_IN = 1;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private SignInButton mSignInButton;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSignInButton = findViewById(R.id.sign_in_button);
        mProgressBar = findViewById(R.id.loading_progress_bar);
        toggleLoading(true);

        ServerUtils.initializeRequestQueue(this.getApplicationContext());
        DataCache.initialize(this.getApplicationContext());

        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.google_api_server_client_id))
                        .requestEmail()
                        .build();
        mSignInClient = GoogleSignIn.getClient(this, signInOptions);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSignInClient.silentSignIn()
                .addOnCompleteListener(this, new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        Log.d(TAG, "completed silent sign in.");
                        handleSignInComplete(task);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        prepareSignIn();
                    }
                });
    }

    private void prepareSignIn() {
        toggleLoading(false);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.sign_in_button) {
                    toggleLoading(true);
                    startActivityForResult(mSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
                }
            }
        });
    }

    private void handleSignInComplete(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            final String idToken = account.getIdToken();
            ServerUtils.logIn(idToken, this);
        } catch (ApiException e) {
                Log.d(TAG, "failed sign in complete with exception", e);
                prepareSignIn();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            handleSignInComplete(GoogleSignIn.getSignedInAccountFromIntent(data));
        }
    }

    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginFailure() {
        Toast.makeText(this, getString(R.string.failed_login), Toast.LENGTH_SHORT).show();
        prepareSignIn();
    }

    private void toggleLoading(boolean loading) {
        mSignInButton.setVisibility(loading ? View.GONE : View.VISIBLE);
        mProgressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }
}
