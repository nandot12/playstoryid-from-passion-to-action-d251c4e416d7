package id.playable.frompassiontoaction.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import id.playable.frompassiontoaction.Model.ContentModel;
import id.playable.frompassiontoaction.R;
import id.playable.frompassiontoaction.components.DatabaseHandler;

/**
 * Created by Playable on 10/15/2017.
 */

public class ItemMenu extends RecyclerView.Adapter<ItemMenu.MyHolder> {
    FragmentActivity activity;
    int bab;
    ArrayList<ContentModel> contents = new ArrayList<>();
    DatabaseHandler db;


    public ItemMenu(FragmentActivity activity, int bab) {
        db = new DatabaseHandler(activity);

        this.activity = activity;
        this.bab = bab;

        contents = db.getContents(bab);
    }



    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(activity).inflate(R.layout.item_list,parent,false);


        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        final ContentModel content = contents.get(position);
        String chapterText = "";
        if(content.getChapter()>0)
            chapterText = "Chapter "+content.getChapter();
        else
            chapterText = "Bagian "+content.getBab();

        holder.textchapter.setText(chapterText);
        holder.texttitle.setText(contents.get(position).getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent();
                i.putExtra("bab", content.getId());
                activity.setResult(Activity.RESULT_OK, i);
                activity.finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView textchapter, texttitle ;
        public MyHolder(View itemView) {
            super(itemView);

            textchapter = itemView.findViewById(R.id.textchapter);
            texttitle = itemView.findViewById(R.id.texttitle);
        }
    }


}
