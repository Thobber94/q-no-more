package net.bergby.qnomore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

/**
 * Created by thomas on 04-Mar-17.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
{
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private ProgressDialog mProgressDialog;

    static String nav_user = null;
    static String nav_email;
    static String nav_userImage;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        editor =  preferences.edit();

        // Clears the shared-preferences on system load.
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mGoogleApiClient.clearDefaultAccountAndReconnect();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    private void updateUI(boolean signedIn)
    {
        if (signedIn)
        {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.tvLogin).setVisibility(View.GONE);
        }
        else
        {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.tvLogin).setVisibility(View.VISIBLE);
            Log.i("Info", "Logged out");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from the intent
        if (requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result)
    {
        String profileIdKey = "net.bergby.qnomore.googleId";
        if (result.isSuccess())
        {
            // Signed in successfully
            GoogleSignInAccount userAccount = result.getSignInAccount();
            if (userAccount != null)
            {
                findViewById(R.id.sign_in_button).setVisibility(View.GONE);
                findViewById(R.id.tvLogin).setVisibility(View.GONE);

                nav_email = userAccount.getEmail();
                nav_user = userAccount.getDisplayName();
                nav_userImage = String.valueOf(userAccount.getPhotoUrl());
                editor.putString(profileIdKey, userAccount.getId()).apply();
                onUserAuthenticated(true);
            }
            else
            {
                Log.d("User", "Useraccount is null");
            }
            updateUI(true);
        }
        else
        {
            System.out.println("Not authenticated. Boo");
            System.out.println(result.getStatus().getStatusCode());
            updateUI(false);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone())
        {
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }
        else
            {

            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }


    private void showProgressDialog()
    {
        if (mProgressDialog == null)
        {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog()
    {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Log.e("Network", "No network found");
    }

    private void onUserAuthenticated(boolean user)
    {
        if (user)
        {
            if (mProgressDialog != null)
            {
                mProgressDialog.dismiss();
            }
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(nav_user, nav_user);
            intent.putExtra(nav_email, nav_email);
            intent.putExtra(nav_userImage, nav_userImage);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }

}
