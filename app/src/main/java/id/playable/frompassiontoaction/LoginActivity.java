package id.playable.frompassiontoaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import id.playable.frompassiontoaction.components.Helper;
import id.playable.frompassiontoaction.components.PlaystoryServer;
import id.playable.frompassiontoaction.components.SessionManager;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int PREMIUM_REQUEST = 135;
    private Button login_btn;
    private EditText  emailText, passwordText;
    private ImageView login_register, login_forgot;
    private ProgressDialog pDialog;
    private SessionManager session;
    private Button googleplus ;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        session = new SessionManager(this);

        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Login User");
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleplus = findViewById(R.id.btn_sign_in);

        googleplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this,R.anim.clickimage);
                v.startAnimation(animation);
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

        login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this,R.anim.clickimage);
                v.startAnimation(animation);

                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                if (email.trim().length() > 0 && password.trim().length() > 0)
                    login(email, password);
                else
                    Helper.popup(LoginActivity.this, "Email or Password is Empty");
            }
        });

        emailText = findViewById(R.id.login_email);
        passwordText = findViewById(R.id.login_password);
        login_register = findViewById(R.id.login_register);
        login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this,R.anim.clickimage);
                v.startAnimation(animation);
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
        login_forgot = findViewById(R.id.login_forgot);
        login_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this,R.anim.clickimage);
                v.startAnimation(animation);
                Intent i = new Intent(LoginActivity.this, ResetActivity.class);
                startActivity(i);
            }
        });
    }

    private void login(String email, String password) {
        pDialog.show();

        HashMap values = new HashMap();
        values.put("email",email);
        values.put("password",password);
        final String device_token = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        values.put("device_token", device_token);
        RequestParams params = new RequestParams(values);
        PlaystoryServer.post("login", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Helper.popup(LoginActivity.this, "Failed Login");
                pDialog.hide();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("response : " ,responseString);
                try {
                    JSONObject result = new JSONObject(responseString);
                    if(result.getInt("error")==0) {
                        JSONObject data = result.getJSONObject("data");

                        Log.d("response login :" , data.getString("name"));

                        String name = data.getString("name");
                        String email = data.getString("email");
                        session.createLoginSession(name,email,null);

                        showKodePremium();
                    }else{
                        Helper.popup(LoginActivity.this, result.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pDialog.hide();
            }
        });
    }

    private void showKodePremium() {
        Intent i = new Intent(this, KodePremiumActivity.class);
        startActivityForResult(i, PREMIUM_REQUEST);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else if(requestCode == PREMIUM_REQUEST){
            showMain();
        }
    }

    private void showMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void handleSignInResult(GoogleSignInResult result) {

        Log.d(TAG, "handleSignInResult:" + result.getStatus());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            session.createLoginSession(acct.getDisplayName(), acct.getEmail(),String.valueOf(acct.getPhotoUrl()));

            Log.e(TAG, "display name: " + acct.getDisplayName());

            HashMap values = new HashMap();
            values.put("first_name",acct.getDisplayName());
//            values.put("last_name","");
            values.put("email",acct.getEmail());
            values.put("phone","");

            register(values);


        } else {
            Log.e(TAG, result.getStatus().toString());
           Toast.makeText(LoginActivity.this,"Gagal memproses...",Toast.LENGTH_LONG).show();

        }
    }

    public void register(HashMap values){
        RequestParams params = new RequestParams(values);
        PlaystoryServer.post("register_google", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(LoginActivity.this, "Gagal mendaftar, mohon coba lagi", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("response register",responseString.toString());
                /*
                try {
                    JSONObject result = new JSONObject(responseString);
                    if (result.getInt("error") == 0) {
                        Toast.makeText(LoginActivity.this, "Login berhasil dengan Google +", Toast.LENGTH_LONG).show();
                    }else if(result.getInt("error") == 1){
                        Toast.makeText(LoginActivity.this, "Login gagal dengan Google + ", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                */
                showKodePremium();
            }
        });
    }
}
