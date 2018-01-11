package com.hcmus.yennhi0105.usedadvance.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcmus.yennhi0105.usedadvance.CClass.CProduct;
import com.hcmus.yennhi0105.usedadvance.R;
import com.hcmus.yennhi0105.usedadvance.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 14/03/2017.
 */

public class ProductAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<CProduct> productList;
    private List<CProduct> arrayList = new ArrayList<CProduct>();

    public ProductAdapter(Context context, int layout, List<CProduct> productList) {
        this.context = context;
        this.layout = layout;
        this.productList = productList;
        arrayList.addAll(productList);
    }

    @Override
    public int getCount() {
        return productList.size();
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
        TextView txtName, txtPrice;
        ImageView imgIcon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(layout, null);

            holder.txtName = (TextView) v.findViewById(R.id.textViewTenPro);
            holder.txtPrice = (TextView) v.findViewById(R.id.textViewGiaPro);
            holder.imgIcon = (ImageView) v.findViewById(R.id.imageViewHinhPro);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        CProduct product = productList.get(position);

        holder.txtName.setText(product.getTitle());
        Typeface typefaceName = Typeface.createFromAsset(context.getAssets(), "fonts/UVF HeraBig-Black.ttf");
        holder.txtName.setTypeface(typefaceName);

        DecimalFormat decimalFormat = new DecimalFormat(",###");
        holder.txtPrice.setText(decimalFormat.format(product.getPrice()) + " VND");
        Typeface typefacePrice = Typeface.createFromAsset(context.getAssets(), "fonts/Lemon-Regular.ttf");
        holder.txtPrice.setTypeface(typefacePrice);

        Picasso.with(context)
                .load(product.getArrayIcon().get(0))
                .transform(new RoundedTransformation(20, 4)) // Redius imageView nhe!
                .into(holder.imgIcon);
        return v;
    }

    //Ham loc theo ten
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault()); // Đưa về chữ thường để search
        productList.clear(); // Xóa dữ liệu trong mảng
        if (charText.length() == 0) {
            productList.addAll(arrayList); // Nếu chưa nhập gì thì lấy kết quả là nguyên gốc đã sao
        } else {
            for (CProduct product : arrayList) { // Duyệt hết mảng thứ 2
                if (product.getTitle().toLowerCase(Locale.getDefault()) // Tìm theo thuộc tính nào > ở đây tìm theo Title// Chuyển dữ liệu trong Name thành chữ thường
                        .contains(charText)) {
                    productList.add(product); // Add thêm vào mảng để đưa lên Listview
                }
            }
        }
        notifyDataSetChanged();
    }

    // Ham loc theo tỉnh thành
    public void filterCity(String charText) {
        charText = charText.toLowerCase(Locale.getDefault()); // Đưa về chữ thường để search
        productList.clear(); // Xóa dữ liệu trong mảng
        if (charText.length() == 0) {
            productList.addAll(arrayList); // Nếu chưa nhập gì thì lấy kết quả là nguyên gốc đã sao
        } else {
            for (CProduct product : arrayList) { // Duyệt hết mảng thứ 2
                if (product.getZone().toLowerCase(Locale.getDefault()) // Tìm theo thuộc tính nào > ở đây tìm theo Title// Chuyển dữ liệu trong Name thành chữ thường
                        .contains(charText)) {
                    productList.add(product); // Add thêm vào mảng để đưa lên Listview
                }
            }
        }
        notifyDataSetChanged();
    }
}
