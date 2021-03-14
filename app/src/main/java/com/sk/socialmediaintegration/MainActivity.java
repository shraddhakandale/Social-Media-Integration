package com.sk.socialmediaintegration;

// importing libraries

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.jar.JarException;

public class MainActivity extends AppCompatActivity {

    private TextView nametext,emailtext;
    private ImageView imgv;

    // Google
    GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 100;
    private  SignInButton signInButton;

    // Facebook
    private LoginButton facebooksignin;
    private CallbackManager c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nametext = findViewById(R.id.nametextView);
        emailtext = findViewById(R.id.emailtextView);
        imgv = findViewById(R.id.imageView2);


        // Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

         signInButton = findViewById(R.id.sign_in_button);
         signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

         // Facebook
         facebooksignin = findViewById(R.id.facebooksignin);
        facebooksignin.setLoginBehavior( LoginBehavior.WEB_ONLY );

        c = CallbackManager.Factory.create();
        facebooksignin.setPermissions(Arrays.asList("email","public_profile"));
        facebooksignin.registerCallback(c, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("SocialMediaIntegration","Login successfull");
                Toast.makeText(MainActivity.this, "Logged in successfully!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Log.d("SocialMediaIntegration","Login canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("SocialMediaIntegration","Login Error");
            }
        });

    }

    // Google
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        c.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Google
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    // Facebook
    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken == null){
                LoginManager.getInstance().logOut();
                nametext.setText("");
                emailtext.setText("");
                imgv.setImageResource(0);
                Toast.makeText(MainActivity.this,"Logged out successfully!!", Toast.LENGTH_LONG).show();
                signInButton.setVisibility(View.VISIBLE);
                nametext.setVisibility(View.INVISIBLE);
                emailtext.setVisibility(View.INVISIBLE);
            }
            else {
                loaduserprofile(currentAccessToken);
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tokenTracker.stopTracking();
    }

    private void loaduserprofile(AccessToken newAccessToken){
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    signInButton.setVisibility(View.INVISIBLE);
                    String fname = object.getString("first_name");
                    String lname = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    String pic = object.getJSONObject("picture").getJSONObject("data").getString("url");

                    nametext.setText(fname+" "+lname);
                    nametext.setVisibility(View.VISIBLE);
                    emailtext.setText(email);
                    emailtext.setVisibility(View.VISIBLE);

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();

                    Glide.with(MainActivity.this).load(pic).into(imgv);


                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","first_name,last_name,email,id,picture.width(150).height(150)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    // Google
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                Toast.makeText(this,"user email : "+personEmail, Toast.LENGTH_SHORT).show();
            }
            startActivity(new Intent(MainActivity.this,UserActivity2.class));
        } catch (ApiException e) {
            Log.d( "Login failed " , e.toString());
        }
    }
}









