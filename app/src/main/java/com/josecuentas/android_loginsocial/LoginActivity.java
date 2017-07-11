package com.josecuentas.android_loginsocial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONObject;
import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private LinearLayout llaLoginGoogle;
    private LoginButton mLoginButton;

    private CallbackManager callbackManager;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);
        injectView();
        setup();
        events();
    }

    private void injectView() {
        mLoginButton = (LoginButton) findViewById(R.id.butLoginFacebook);
        llaLoginGoogle = (LinearLayout) findViewById(R.id.llaLoginGoogle);
    }

    private void events() {
        llaLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email"));
            }
        });
    }

    private void setup() {
        setupFacebook();
    }

    private void setupFacebook() {
        mLoginButton.setReadPermissions(Arrays.asList("email"));
        callbackManager = new CallbackManager.Factory().create();

        //login register callback
        LoginManager
                .getInstance()
                .registerCallback(
                        callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override public void onSuccess(LoginResult loginResult) {
                                Log.d(TAG, "onSuccess: ");
                                getInfoProfile();
                            }

                            @Override public void onCancel() {
                                Log.d(TAG, "onCancel: ");
                            }

                            @Override public void onError(FacebookException error) {
                                Log.d(TAG, "onError: ");
                            }
                        }
        );
    }

    private void getInfoProfile() {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override public void onCompleted(JSONObject object, GraphResponse response) {
                JSONObject json = response.getJSONObject();
                if(json == null) return;
                Log.d(TAG, "onCompleted: json: " + json.toString());
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,last_name,link,email");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}