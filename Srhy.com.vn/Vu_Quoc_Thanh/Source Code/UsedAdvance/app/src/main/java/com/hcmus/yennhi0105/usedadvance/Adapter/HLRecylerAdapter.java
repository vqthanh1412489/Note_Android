package com.hcmus.yennhi0105.usedadvance.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcmus.yennhi0105.usedadvance.CClass.CHighLightProduct;
import com.hcmus.yennhi0105.usedadvance.R;
import com.hcmus.yennhi0105.usedadvance.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 25/03/2017.
 */

// Dùng khi muốn chuyển thành ListView

//public class HLRecylerAdapter extends BaseAdapter{
//    private Context context;
//    private int layout;
//    private List<CHighLightProduct> lightProductList;
//
//    @Override
//    public int getCount() {
//        return lightProductList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    private class ViewHolder {
//        ImageView imgIcon;
//        TextView txtName, txtPrice;
//    }
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        ViewHolder holder = new ViewHolder();
//        View v = convertView;
//        if (v == null) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            v = inflater.inflate(layout, null);
//            holder.txtName = (TextView) v.findViewById(R.id.textViewTenHL);
//            holder.txtPrice= (TextView) v.findViewById(R.id.textViewGiaHL);
//            holder.imgIcon = (ImageView) v.findViewById(R.id.imageViewHinhHL);
//        }
//        return null;
//    }
//}

    // Dùng khi muốn chuyển thành RecylerView
public class HLRecylerAdapter extends RecyclerView.Adapter<HLRecylerAdapter.ViewHolder>{

    private Context context;
    private List<CHighLightProduct> arrayListHL = new ArrayList<CHighLightProduct>();
    public HLRecylerAdapter(Context context, List<CHighLightProduct> arrayHL) {
        this.context = context;
        this.arrayListHL = arrayHL;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.line_recylerview, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtName.setText(arrayListHL.get(position).getName());
        Typeface typefaceName = Typeface.createFromAsset(context.getAssets(), "fonts/UVF HeraBig-Black.ttf");
        holder.txtName.setTypeface(typefaceName);

        DecimalFormat decimalFormat = new DecimalFormat(",###");
        holder.txtPrice.setText(decimalFormat.format(arrayListHL.get(position).getPrice()) + " VND");
        Typeface typefacePrice = Typeface.createFromAsset(context.getAssets(), "fonts/Lemon-Regular.ttf");
        holder.txtPrice.setTypeface(typefaceName);
        String linkVideo = "https://img.youtube.com/vi/" + arrayListHL.get(position).getLinkVideo() + "/hqdefault.jpg";
        //Picasso.with(context).load(linkVideo).into(holder.imgIcon);
        Picasso.with(context)
                .load(linkVideo)
                .transform(new RoundedTransformation(30, 4)) // Redius imageView nhe!
                .into(holder.imgIcon);
    }

    @Override
    public int getItemCount() {
        return arrayListHL.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView txtName, txtPrice;

        public ViewHolder(View itemView) {
            super(itemView);

            imgIcon = (ImageView) itemView.findViewById(R.id.imageViewIconUser);
            txtPrice = (TextView) itemView.findViewById(R.id.textViewGiaUser);
            txtName = (TextView) itemView.findViewById(R.id.textViewTenUser);
        }
    }

}
