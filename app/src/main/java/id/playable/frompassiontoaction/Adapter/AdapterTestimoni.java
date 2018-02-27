package id.playable.frompassiontoaction.Adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import id.playable.frompassiontoaction.R;
import id.playable.frompassiontoaction.Model.TestimoniModel;


/**
 * Created by nandoseptianhusni on 10/28/17.
 */

public class AdapterTestimoni extends RecyclerView.Adapter<AdapterTestimoni.Myholder> {

    FragmentActivity c ;
    ArrayList<TestimoniModel> data ;

    public AdapterTestimoni(FragmentActivity c, ArrayList<TestimoniModel> data) {
        this.c = c;
        this.data = data;
    }

    @Override
    public AdapterTestimoni.Myholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(c).inflate(R.layout.recyclerview_testimoni,parent,false);

        return new Myholder(v);
    }

    @Override
    public void onBindViewHolder(AdapterTestimoni.Myholder holder, int position) {
        holder.content.setText("\""+data.get(position).getMessage()+ ".\"");

        holder.people.setText(data.get(position).getName() + "." + data.get(position).getTitle());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        TextView content ,people ;
        public Myholder(View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.testimoniContent);
            people = itemView.findViewById(R.id.testemonipeople);
        }
    }
}
