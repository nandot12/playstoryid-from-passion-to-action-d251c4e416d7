package id.playable.frompassiontoaction.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import id.playable.frompassiontoaction.Adapter.AdapterComment;
import id.playable.frompassiontoaction.Model.LikeModel;
import id.playable.frompassiontoaction.Model.CommentModel;
import id.playable.frompassiontoaction.Model.ResponseComment;
import id.playable.frompassiontoaction.Model.ResponseInsert;
import id.playable.frompassiontoaction.Model.ResponseLike;
import id.playable.frompassiontoaction.R;
import id.playable.frompassiontoaction.Retrofit.ApiService;
import id.playable.frompassiontoaction.Retrofit.ConfigRetrofit;
import id.playable.frompassiontoaction.Model.BannerModel;
import id.playable.frompassiontoaction.components.Constants;
import id.playable.frompassiontoaction.components.PlaystoryServer;
import id.playable.frompassiontoaction.components.SessionManager;

/**
 * Created by Zuhri Utama on 7/23/2015.
 */
public class InfoFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    long lastClickTime = 0;

    AdapterComment adapter;


    Unbinder unbinder;
    @BindView(R.id.comment)
    EditText comment;

    ImageView imgegmail;


    private List<CommentModel> data;
    private List<LikeModel> datacount;
    @BindView(R.id.imglike)
    ImageView imglike;
    @BindView(R.id.textlike)
    TextView textlike;
    //@BindView(R.id.imgdislike)
    //ImageView imgdislike;
    //@BindView(R.id.textdislike)
    //TextView textdislike;
    private View v;

    SessionManager sesi;
    private SliderLayout mDemoSlider;
    private ArrayList<BannerModel> banners = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        v = inflater.inflate(R.layout.fragment_info, container, false);

        sesi = new SessionManager(getActivity());
        recycler = v.findViewById(R.id.recycler);

        imgegmail = v.findViewById(R.id.vvv);

        Glide.with(getActivity()).load(sesi.getImage()).error(R.drawable.logonavigasi).into(imgegmail);

        Log.d("photo url :", String.valueOf(sesi.getImage()));





        data = new ArrayList<>();

        datacount = new ArrayList<>();

        getComment();

        getLike();

        //getBanner();

        mDemoSlider = v.findViewById(R.id.slider);

        setupSlider();
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    private void getLike() {
        ApiService api = ConfigRetrofit.getInstanceRetrofit();

        Call<ResponseLike> call = api.action_like();
        call.enqueue(new Callback<ResponseLike>() {
            @Override
            public void onResponse(Call<ResponseLike> call, Response<ResponseLike> response) {

                Log.d(Constants.LOG_TAG, response.body().getData().toString());
                if (response.isSuccessful()) {

                    datacount = response.body().getData();

                    UI(datacount);
                }
            }

            @Override
            public void onFailure(Call<ResponseLike> call, Throwable t) {

            }
        });


        try {


        } catch (NullPointerException e) {
            Log.d("error adapter ", e.toString());

        }
    }

    private void UI(List<LikeModel> data) {
        //textdislike.setText(data.get(0).getDislikeCount());

        textlike.setText(data.get(0).getLikeCount());

    }

    private void getComment() {

        data.clear();

        ApiService api = ConfigRetrofit.getInstanceRetrofit();

        Call<ResponseComment> call = api.action_getcomment("3");
        call.enqueue(new Callback<ResponseComment>() {
            @Override
            public void onResponse(Call<ResponseComment> call, Response<ResponseComment> response) {

                Log.d(Constants.LOG_TAG, response.body().getData().toString());
                if (response.isSuccessful()) {

                    data = response.body().getData();
                    adapter = new AdapterComment(getActivity(), data);
                    recycler.setAdapter(adapter);

//                   Log.d("response data :" , data.get(0).toString());

                    Log.d("size adapter :", String.valueOf(adapter.getItemCount()));
                    LinearLayoutManager layout = new LinearLayoutManager(getActivity());
                    recycler.setLayoutManager(layout);
                }
            }

            @Override
            public void onFailure(Call<ResponseComment> call, Throwable t) {

            }
        });


        try {


        } catch (NullPointerException e) {
            Log.d("error adapter ", e.toString());

        }

    }

    public void setupSlider() {
        mDemoSlider.removeAllSliders();

        DefaultSliderView defaultView = new DefaultSliderView(getActivity());
        // initialize a SliderLayout
        defaultView
                .description("Slide")
                .image(R.drawable.frompassiontoaction)
                .setScaleType(BaseSliderView.ScaleType.CenterInside)
                .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.BASE_URL));
                        startActivity(browserIntent);
                    }
                });
        mDemoSlider.addSlider(defaultView);

        if (banners != null && banners.size() > 0) {
            for (final BannerModel banner : banners) {
                DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
                // initialize a SliderLayout
                textSliderView
                        .description("Slide")
                        .image(banner.getImage())
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {
                                if (banner.getLink() != null && !banner.getLink().equals("")) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(banner.getLink()));
                                    startActivity(browserIntent);
                                }
                            }
                        });
                mDemoSlider.addSlider(textSliderView);
            }
        }

        mDemoSlider.setCustomIndicator((PagerIndicator) v.findViewById(R.id.custom_indicator));
        mDemoSlider.setDuration(4000);
    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }
//
//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        menu.findItem(R.id.action_bookmark).setVisible(false);
//        menu.findItem(R.id.action_openbookmark).setVisible(false);
//        menu.findItem(R.id.action_fb).setVisible(true);
//        menu.findItem(R.id.action_ig).setVisible(true);
//        menu.findItem(R.id.action_web).setVisible(true);
//        menu.findItem(R.id.action_logout).setVisible(false);
//        return;
//    }

    @Override
    public void onClick(View v) {

    }

    public void getBanner() {
        if (banners != null && banners.size() > 0)
            return;

        PlaystoryServer.get("get_banners", null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //Log.e("playstory", responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject result = new JSONObject(responseString);
                    if (result.getInt("error") == 0) {
                        JSONArray data = result.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            BannerModel banner = new BannerModel(data.getJSONObject(i));
                            banners.add(banner);
                        }
                        setupSlider();
                    } else
                        Toast.makeText(getActivity(), result.getString("msg"), Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.comment, R.id.sendcomment, R.id.imglike})
    public void onViewClicked(View view) {
        LikeModel d = new LikeModel();
        switch (view.getId()) {

            /*case R.id.imgdislike:


                if (sesi.getStatus() == 2) {
                    sesi.saveStatus(1);
                    //  status = 1 ;
                    imglike.setEnabled(true);
                    //imgdislike.setEnabled(false);
                    String like = String.valueOf(Integer.parseInt(textlike.getText().toString()) - 1);
                    //String dislike = String.valueOf(Integer.parseInt(textdislike.getText().toString()) + 1);
                    //d.setDislikeCount(dislike);
                    d.setLikeCount(like);

                    // textdislike.setText(dislike);
                    //textlike.setText(like);
                    datacount.clear();
                    datacount.add(d);

                    UI(datacount);
                } else {
                    sesi.saveStatus(1);
                    //imgdislike.setEnabled(false);
                    imglike.setEnabled(true);

                    String like = String.valueOf(Integer.parseInt(textlike.getText().toString()));
                    //String dislike = String.valueOf(Integer.parseInt(textdislike.getText().toString()) + 1);

                    //d.setDislikeCount(dislike);
                    d.setLikeCount(like);

                    datacount.clear();
                    datacount.add(d);

                    UI(datacount);
                    //datacount.add(d);
                    //  textdislike.setText(dislike);

                    //  d.notifyAll();

                    updateCount(like, "");
                }


                break;*/

            case R.id.imglike:


                if (sesi.getStatus() == 1) {

                    sesi.saveStatus(2);
                    //imgdislike.setEnabled(true);
                    imglike.setEnabled(false);
                    String like = String.valueOf(Integer.parseInt(textlike.getText().toString()) + 1);
                    //String dislike = String.valueOf(Integer.parseInt(textdislike.getText().toString()) - 1);
                    // textlike.setText(like);
                    // textdislike.setText(dislike);
                    //d.setDislikeCount(dislike);
                    d.setLikeCount(like);
                    //updateCount(like, dislike);
                    datacount.clear();
                    datacount.add(d);
                    UI(datacount);

                } else {
                    sesi.saveStatus(2);
                    imglike.setEnabled(false);
                    //imgdislike.setEnabled(true);

                    String like = String.valueOf(Integer.parseInt(textlike.getText().toString()) + 1);
                    //String dislike = String.valueOf(Integer.parseInt(textdislike.getText().toString()));
                    //textlike.setText(like);
                    //d.setDislikeCount(dislike);
                    d.setLikeCount(like);
                    //updateCount(like, dislike);
                    datacount.clear();
                    datacount.add(d);
                    // textdislike.setText(dislike);
                    //updateCount(like, dislike);
                    UI(datacount);
                }

                break;
            case R.id.comment:
                //  sendcomment.setVisibility(View.VISIBLE);


                break;
            case R.id.sendcomment:

                if (!comment.getText().toString().isEmpty()) {


                    String komentar = comment.getText().toString();
                    String sesiname = sesi.getName();

                    // Long timeNow = SystemClock.elapsedRealtime();
                    //  Long timeElapsed = SystemClock.elapsedRealtime() - lastClickTime;
                    //  lastClickTime = timeNow;

                    // Date currentTime = Calendar.getInstance().getTime();

                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => " + c.getTime());

                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(c.getTime());


                    comment.setText("");
                    //String time =String.valueOf(currentTime);

                    CommentModel comment = new CommentModel();
                    comment.setCommentName(sesiname);
                    comment.setCommentText(komentar);
                    comment.setCommentTanggal(formattedDate);
                    comment.setCommentImage(sesi.getImage());

                    data.add(comment);


                    adapter.notifyDataSetChanged();

                    ApiService api = ConfigRetrofit.getInstanceRetrofit();

                    Call<ResponseInsert> call = api.action_insertcomment(sesiname, komentar, formattedDate, sesi.getImage(),"2");

                    call.enqueue(new Callback<ResponseInsert>() {
                        @Override
                        public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                            Log.d(Constants.LOG_TAG, response.message());

                            if (response.isSuccessful()) {

                                boolean result = response.body().isResult();

                                if (result) {
                                    Log.d("success simpan comment", "true");
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseInsert> call, Throwable t) {

                            Log.d(Constants.LOG_TAG, t.getLocalizedMessage());

                        }
                    });
                }
                else{
                    Toast.makeText(getActivity(),"Please insert your comment",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void updateCount(String countlike, String countdisk) {


        ApiService api = ConfigRetrofit.getInstanceRetrofit();

        Call<ResponseInsert> call = api.update_like(countlike, countdisk);

        call.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                Log.d("response insert like :", response.message());

                if (response.isSuccessful()) {

                    boolean result = response.body().isResult();

                    if (result) {
                        Log.d("success simpan like", "true");
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseInsert> call, Throwable t) {

                Log.d(Constants.LOG_TAG, t.getLocalizedMessage());

            }
        });
    }

}
