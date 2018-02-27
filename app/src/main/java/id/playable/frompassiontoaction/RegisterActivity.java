package id.playable.frompassiontoaction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import id.playable.frompassiontoaction.components.PlaystoryServer;

/**
 * Created by Zuhri Utama on 9/17/2015.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {

    private Button register;
    private EditText firstET, lastET, emailET, phoneET, passwordET, repasswordET;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Register User");
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);

        firstET = findViewById(R.id.register_first);
        lastET = findViewById(R.id.register_last);
        emailET = findViewById(R.id.register_email);
        phoneET = findViewById(R.id.register_phone);
        passwordET = findViewById(R.id.register_password);
        repasswordET = findViewById(R.id.register_repassword);
        register = findViewById(R.id.register_btn);

        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = null;
        if(v==register) {
            String first = firstET.getText().toString();
            String last = lastET.getText().toString();
            String email = emailET.getText().toString();
            String phone = phoneET.getText().toString();
            String password = passwordET.getText().toString();
            String repassword = repasswordET.getText().toString();

            if(!first.isEmpty() && !last.isEmpty() && !email.isEmpty() && !password.isEmpty() && !repassword.isEmpty()) {
                if(!password.equals(repassword))
                    Toast.makeText(RegisterActivity.this, "Password tidak sama", Toast.LENGTH_SHORT).show();
                else {
                    pDialog.show();

                    HashMap values = new HashMap();
                    values.put("first_name", first);
                    values.put("last_name", last);
                    values.put("email", email);
                    values.put("phone", phone);
                    values.put("password", password);

                    register(values);
                }
            }else
                Toast.makeText(RegisterActivity.this, "Mohon mengisi semua field", Toast.LENGTH_SHORT).show();
        }
    }

    public void register(HashMap values){
        RequestParams params = new RequestParams(values);
        PlaystoryServer.post("register", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(RegisterActivity.this, "Gagal mendaftar, mohon coba lagi", Toast.LENGTH_LONG).show();
                pDialog.hide();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject result = new JSONObject(responseString);
                    if (result.getInt("error") == 0) {
                        Toast.makeText(RegisterActivity.this, "Pendaftaran berhasil, cek email anda untuk aktivasi", Toast.LENGTH_LONG).show();
                        finish();
                    }else
                        Toast.makeText(RegisterActivity.this, result.getString("msg"), Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pDialog.hide();
            }
        });
    }
}
