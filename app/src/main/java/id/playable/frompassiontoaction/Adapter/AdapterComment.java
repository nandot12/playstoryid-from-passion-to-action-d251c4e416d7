package id.playable.frompassiontoaction.Adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import id.playable.frompassiontoaction.Model.CommentModel;
import id.playable.frompassiontoaction.R;

/**
 * Created by ima5 on 9/30/17.
 */

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.ViewHolder> {
    FragmentActivity activity;
    List<CommentModel> data;

    public AdapterComment(FragmentActivity activity, List<CommentModel> data) {
        this.activity = activity;
        this.data = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.comment_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Log.d("name :" , data.get(position).toString());
        holder.commentemail.setText(data.get(position).getCommentName());
        holder.commentmessage.setText(data.get(position).getCommentText());
        holder.commenttime.setText(data.get(position).getCommentTanggal());

        Glide.with(activity).load(data.get(position).getCommentImage()).error(R.drawable.logonavigasi).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder {

        TextView commentemail;

        TextView commenttime;

        TextView commentmessage;
        ImageView imageView ;

        ViewHolder(View view) {
            super(view);
            commentemail = itemView.findViewById(R.id.commentemail);
            commentmessage = itemView.findViewById(R.id.commentmessage);
            commenttime = itemView.findViewById(R.id.commenttime);
            imageView = itemView.findViewById(R.id.commentimage);
        }
    }
}
