package id.playable.frompassiontoaction.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import id.playable.frompassiontoaction.Model.MenuModel;
import id.playable.frompassiontoaction.R;

import java.util.ArrayList;

public class MenuAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<MenuModel> data;
    private static LayoutInflater inflater = null;
    private ViewHolder holder;

    public int layout;

    public MenuAdapter(Activity act, ArrayList<MenuModel> menus) {
        activity = act;
        data = menus;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.toArray().length;

    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    private static class ViewHolder {
        public TextView title, time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = null;

        if(convertView==null) {
            vi = inflater.inflate(R.layout.info_item, null);
            holder = new ViewHolder();
            holder.title = vi.findViewById(R.id.title);
            holder.time = vi.findViewById(R.id.time);
            vi.setTag(holder);
        } else {
            vi = convertView;
            holder = (ViewHolder) vi.getTag();
            holder.title = vi.findViewById(R.id.title);
            holder.time = vi.findViewById(R.id.time);
        }

        final MenuModel menu = data.get(position);

        holder.title.setText("BAB "+String.valueOf(menu.getNo())+" | "+menu.getName());
        holder.time.setText(menu.getTime());

        return vi;
    }
}