package com.hcmus.yennhi0105.usedadvance.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcmus.yennhi0105.usedadvance.CClass.CNews;
import com.hcmus.yennhi0105.usedadvance.R;
import com.hcmus.yennhi0105.usedadvance.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 27/03/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    private Context context;
    private List<CNews> newsList = new ArrayList<CNews>();

    public NewsAdapter(Context context, List<CNews> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.line_news, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txt.setText(newsList.get(position).getTitle());
        Typeface typefaceName = Typeface.createFromAsset(context.getAssets(), "fonts/VNF-Sofia Regular.ttf");
        holder.txt.setTypeface(typefaceName);
        holder.txt.setSelected(true);

        Picasso.with(context)
                .load(newsList.get(position).getPicture())
                .transform(new RoundedTransformation(20, 2)) // Redius imageView nhe!
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt;
        ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.textViewTenNews);
            img = (ImageView) itemView.findViewById(R.id.imageViewHinhNews);

        }
    }

}
