package id.playable.frompassiontoaction.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.playable.frompassiontoaction.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTestimoni extends Fragment {


    public FragmentTestimoni() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_testimoni, container, false);
    }

}
