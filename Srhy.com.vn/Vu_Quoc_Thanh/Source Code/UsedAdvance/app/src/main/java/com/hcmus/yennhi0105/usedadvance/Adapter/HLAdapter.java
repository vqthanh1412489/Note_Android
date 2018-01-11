package com.hcmus.yennhi0105.usedadvance.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcmus.yennhi0105.usedadvance.CClass.CHighLightProduct;
import com.hcmus.yennhi0105.usedadvance.R;
import com.hcmus.yennhi0105.usedadvance.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 23/03/2017.
 */

public class HLAdapter extends BaseAdapter{
    private Context context;
    private int layout;
    private List<CHighLightProduct> lightProductList;
    private List<CHighLightProduct> arrayList = new ArrayList<CHighLightProduct>(); // Array dụ phòng chứa nguyên mảng lightProductList


    public HLAdapter(Context context, int layout, List<CHighLightProduct> lightProductList) {
        this.context = context;
        this.layout = layout;
        this.lightProductList = lightProductList;
        arrayList.addAll(lightProductList);
    }

    @Override
    public int getCount() {
        return lightProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        ImageView imgIcon;
        TextView txtName, txtPrice, txtDate, txtCompany;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(layout, null);

            holder.imgIcon = (ImageView) v.findViewById(R.id.imageViewHinhHL);
            holder.txtName = (TextView) v.findViewById(R.id.textViewTenHL);
            holder.txtPrice = (TextView) v.findViewById(R.id.textViewGiaHL);
            holder.txtDate = (TextView) v.findViewById(R.id.textViewNgayDangHL);
            holder.txtCompany = (TextView) v.findViewById(R.id.textViewTenCTHL);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        CHighLightProduct hl = lightProductList.get(position);

        holder.txtName.setText(hl.getName());
        holder.txtPrice.setText("Price: "+ hl.getPrice());
        holder.txtDate.setText("Date: " + hl.getDataPost());
        holder.txtDate.setSelected(true);
        holder.txtCompany.setText("Company: " + hl.getNameCompany());
        holder.txtCompany.setSelected(true);
        String linkVideo = "https://img.youtube.com/vi/" + hl.getLinkVideo() + "/hqdefault.jpg"; // Tách ảnh thumnaib 1 vifdeo YT

        //Picasso.with(context).load(linkVideo).into(holder.imgIcon);
        Picasso.with(context)
                .load(linkVideo)
                .transform(new RoundedTransformation(40, 4)) // Redius imageView nhe!
                .into(holder.imgIcon);
        return v;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault()); // Đưa về chữ thường để search
        lightProductList.clear(); // Xóa dữ liệu trong mảng chinh
        if (charText.length() == 0) {
            lightProductList.addAll(arrayList); // Nếu chưa nhập gì thì lấy kết quả là nguyên gốc đã sao
        } else {
            for (CHighLightProduct hl : arrayList) { // Duyệt hết mảng thứ 2
                if (hl.getName().toLowerCase(Locale.getDefault()) // Tìm theo thuộc tính nào > ở đây tìm theo Title// Chuyển dữ liệu trong Name thành chữ thường
                        .contains(charText)) {
                    lightProductList.add(hl); // Add thêm vào mảng để đưa lên Listview
                }
            }
        }
        notifyDataSetChanged();
    }
}
