package id.playable.frompassiontoaction;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import id.playable.frompassiontoaction.Fragment.ResponsePremium;
import id.playable.frompassiontoaction.Retrofit.ApiService;
import id.playable.frompassiontoaction.Retrofit.ConfigRetrofit;
import id.playable.frompassiontoaction.components.SessionManager;

public class KodePremiumActivity extends AppCompatActivity {

    @BindView(R.id.edtcodepremium)
    EditText edtcodepremium;
    @BindView(R.id.skipkode)
    ImageView skipkode;
    SessionManager sesi;
    @BindView(R.id.checkmasuk)
    ImageView checkmasuk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kode_premium);
        ButterKnife.bind(this);



        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_iklan);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView imagekeluar = dialog.findViewById(R.id.iklankeluar);
        ImageView imageweb = dialog.findViewById(R.id.iklanwebsite);

        imagekeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        imageweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://kautsarmanagement.id"));
                startActivity(intent);
            }
        });

        dialog.show();
        sesi = new SessionManager(this);

    }

    @OnClick({R.id.skipkode,R.id.checkmasuk,R.id.getkodepromo})
    public void onViewClicked(View v) {

        switch (v.getId()){
            case R.id.skipkode :
                finish();
                break;
            case R.id.checkmasuk :
                checkAction();
                break;
            case R.id.getkodepromo :
                startActivity(new Intent(getApplicationContext(),DeskPembelianActivity.class));
        }
    }

    private void checkAction() {


        if (!edtcodepremium.getText().toString().isEmpty()) {

            ApiService api = ConfigRetrofit.getInstanceRetrofit();

            Call<ResponsePremium> call = api.data_code(edtcodepremium.getText().toString());
            call.enqueue(new Callback<ResponsePremium>() {
                @Override
                public void onResponse(Call<ResponsePremium> call, Response<ResponsePremium> response) {
                    Log.d("response code", response.message());

                    //List<DataPremium> data = response.body().getData() ;
                    boolean result = response.body().isResult();

                    String message = response.body().getMsg();
                    if (result) {
                        sesi.saveCode();
                        Toast.makeText(KodePremiumActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponsePremium> call, Throwable t) {
                    Log.d("error code premium", t.getLocalizedMessage());
                }
            });
        }
        else{
            Toast.makeText(KodePremiumActivity.this,"Silakan masukkan Kode Premium",Toast.LENGTH_SHORT).show();
        }

    }

}
