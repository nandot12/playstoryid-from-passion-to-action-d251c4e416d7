package id.playable.frompassiontoaction.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.loopj.android.http.TextHttpResponseHandler;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;
import id.playable.frompassiontoaction.Adapter.AdapterTestimoni;
import id.playable.frompassiontoaction.R;
import id.playable.frompassiontoaction.components.Helper;
import id.playable.frompassiontoaction.components.PlaystoryServer;
import id.playable.frompassiontoaction.Model.TestimoniModel;

/**
 * Created by Zuhri Utama on 7/23/2015.
 */
public class TestimoniFragment extends Fragment {


    ArrayList<TestimoniModel> datarray = new ArrayList<>();

    // Fragment
    ArrayList<Fragment> testimoniList = new ArrayList<>();
    @BindView(R.id.recycler)
    RecyclerView recycler;
    Unbinder unbinder;
    private CirclePageIndicator mIndicator;
    // private ProgressDialog pDialog;
    private View v;
    private TestimoniModel testimoni;
    private TextView name, title, message;
    private ImageView photo;
    private AQuery aQuery;
    AdapterTestimoni adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        v = inflater.inflate(R.layout.activity_testimoni, container, false);

        aQuery = new AQuery(getActivity());

        getTestimoni(v);

        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    private void getTestimoni(final View v) {
        //    pDialog.show();
        HashMap values = new HashMap();
        PlaystoryServer.get("get_testimoni2", null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Helper.popup(getActivity(), "No Internet Connection...");
                //      pDialog.hide();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject result = new JSONObject(responseString);
                    if (result.getInt("error") == 0) {
                        JSONArray data = result.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            TestimoniModel testimoni = new TestimoniModel(data.getJSONObject(i));
                            datarray.add(testimoni);

                            adapter = new AdapterTestimoni(getActivity(), datarray);
                            recycler.setAdapter(adapter);
                            recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                            Log.d("size testimoni : ", String.valueOf(datarray.size()));
                        }
                    } else
                        Toast.makeText(getActivity(), result.getString("msg"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                //setupViewPager(v);
                //      pDialog.hide();
            }


        });
    }
}
