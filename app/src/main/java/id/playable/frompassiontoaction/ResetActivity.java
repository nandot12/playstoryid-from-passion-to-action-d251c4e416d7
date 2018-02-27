package id.playable.frompassiontoaction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
public class ResetActivity extends Activity implements View.OnClickListener {

    private ImageView reset;
    private EditText emailET;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reset);

        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Register User");
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);

        emailET = findViewById(R.id.reset_email);
        reset = findViewById(R.id.reset_btn);

        reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = null;
        if(v==reset) {
            String email = emailET.getText().toString();

            if(!email.isEmpty()) {
                    HashMap values = new HashMap();
                    values.put("email", email);
                    reset(values);
            }else
                Toast.makeText(ResetActivity.this, "Please input your valid email", Toast.LENGTH_SHORT).show();
        }
    }

    public void reset(HashMap values){
        RequestParams params = new RequestParams(values);
        PlaystoryServer.post("reset", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ResetActivity.this, "Failed Reset Account", Toast.LENGTH_LONG).show();
                pDialog.hide();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject result = new JSONObject(responseString);
                    if (result.getInt("error") == 0) {
                        Toast.makeText(ResetActivity.this, "Reset Succesful, please check your email", Toast.LENGTH_LONG).show();
                        finish();
                    }else
                        Toast.makeText(ResetActivity.this, result.getString("msg"), Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pDialog.hide();
            }
        });
    }
}
