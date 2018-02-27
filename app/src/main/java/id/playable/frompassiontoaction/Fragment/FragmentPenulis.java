package id.playable.frompassiontoaction.Fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import id.playable.frompassiontoaction.R;
import id.playable.frompassiontoaction.components.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPenulis extends Fragment {


    @BindView(R.id.ig)
    ImageView ig;
    @BindView(R.id.fb)
    ImageView fb;
    @BindView(R.id.blog)
    ImageView blog;

    Unbinder unbinder;

    public FragmentPenulis() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_penulis, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @OnClick({R.id.ig, R.id.fb, R.id.blog})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ig:
                openUrl(Constants.INSTAGRAM_URL);
                break;
            case R.id.fb:
                openUrl(Constants.FACEBOOK_URL);
                break;
            case R.id.blog:
               openUrl(Constants.WEB);
                break;
           // case R.id.tw:
            //    openUrl(Constants.TW);
               // break;
        }
    }
}
