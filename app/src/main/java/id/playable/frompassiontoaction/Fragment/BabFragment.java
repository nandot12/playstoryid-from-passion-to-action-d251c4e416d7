package id.playable.frompassiontoaction.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.playable.frompassiontoaction.Adapter.ItemMenu;
import id.playable.frompassiontoaction.R;

/**
 * Created by zuhriutama on 12/5/17.
 */

public class BabFragment extends Fragment {

    @BindView(R.id.recyler)
    RecyclerView recyler;
    Unbinder unbinder;
    int bab;

    public BabFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bab, container, false);
        unbinder = ButterKnife.bind(this, view);

        Bundle data = getArguments();
        bab = data.getInt("pos");

        ItemMenu item = new ItemMenu(getActivity(),bab);
        recyler.setAdapter(item);
        GridLayoutManager grib = new GridLayoutManager(getActivity(),2);
        grib.setSmoothScrollbarEnabled(true);
        recyler.setHasFixedSize(true);

        recyler.setLayoutManager(grib);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

