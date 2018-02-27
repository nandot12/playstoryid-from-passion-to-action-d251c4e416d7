package id.playable.frompassiontoaction.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import id.playable.frompassiontoaction.R;

/**
 * Created by Zuhri Utama on 7/23/2015.
 */
public class PustakaFragment extends Fragment {

    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        v = inflater.inflate(R.layout.fragment_pustaka, container, false);
        return v;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        return;
    }
}
