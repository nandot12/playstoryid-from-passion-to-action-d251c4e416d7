package id.playable.frompassiontoaction.components;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import id.playable.frompassiontoaction.BookmarkActivity;
import id.playable.frompassiontoaction.Model.BookmarkModel;
import id.playable.frompassiontoaction.Model.ContentModel;
import id.playable.frompassiontoaction.R;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    private final DatabaseHandler db;
    private final ArrayList<ContentModel> playlist;
    private Activity activity;
    private ArrayList<BookmarkModel> data;
    private static LayoutInflater inflater = null;
    //public ImageLoader imageLoader;
    private ViewHolder holder;

    public int layout;

    public ListAdapter(BookmarkActivity bookmarkActivity, ArrayList<BookmarkModel> bookmarks, DatabaseHandler database) {
        activity = bookmarkActivity;
        data = bookmarks;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db = database;

        playlist = db.getContents(0);
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

    public String[] getSelectedIds(){
        ArrayList values = new ArrayList<>();
        for(BookmarkModel item : data){
            if(item.isChecked())
                values.add(String.valueOf(item.getId()));
        }

        String[] selectedIds = new String[values.size()];
        values.toArray(selectedIds);
        return selectedIds;
    }

    public void clearSelections() {
        for(BookmarkModel item : data){
            if(item.isChecked())
                item.setChecked(false);
        }
    }

    public static class ViewHolder {
        public TextView bab, time;
        public RelativeLayout bab_item;
        public ImageView edit, delete;
        public int layout = -1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if (convertView == null) {
            vi = inflater.inflate(R.layout.bookmark_item, null);
            holder = new ViewHolder();
            holder.bab_item = vi.findViewById(R.id.bab_item);
            holder.bab = vi.findViewById(R.id.bab);
            holder.time = vi.findViewById(R.id.time);
            holder.edit = vi.findViewById(R.id.item_edit);
            holder.delete = vi.findViewById(R.id.item_delete);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        final BookmarkModel bookmark = data.get(position);

        if(bookmark.isChecked()) {
            holder.bab_item.setBackgroundResource(R.drawable.selected_gray);
        }else{
            holder.bab_item.setBackgroundResource(R.color.transparant);
        }
        int timeRemaining = bookmark.getTime();

        ContentModel content = playlist.get(bookmark.getBab()-1);
        String title = "Bab "+content.getBab();
        if(content.getChapter()>0)
        title += " Chapter "+content.getChapter();
        title += " : " + content.getTitle();

        holder.bab.setText(String.valueOf(title)+" ("+bookmark.getFormatedTime()+")");
        holder.time.setText(bookmark.getMessage());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBookmark(position, bookmark);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteBookmark(bookmark);
                data.remove(position);
                notifyDataSetChanged();
            }
        });
        return vi;
    }



    public void editBookmark(final int pos, final BookmarkModel bookmark){
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(activity);
        View promptsView = li.inflate(R.layout.dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        ContentModel content = playlist.get(bookmark.getBab()-1);
        String info_bab = "Bab "+content.getBab();
        if(content.getChapter()>0)
            info_bab += " Chapter "+content.getChapter();

        final TextView title = promptsView.findViewById(R.id.bookmark_title);
        title.setText(info_bab +" ("+bookmark.getFormatedTime()+")");
        final EditText userInput = promptsView
                .findViewById(R.id.bookmark_message);
        userInput.setText(bookmark.getMessage());

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String msg = userInput.getText().toString();
                                bookmark.setMessage(msg);
                                db.updateBookmark(bookmark);
                                data.remove(pos);
                                data.add(pos, bookmark);
                                notifyDataSetChanged();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}