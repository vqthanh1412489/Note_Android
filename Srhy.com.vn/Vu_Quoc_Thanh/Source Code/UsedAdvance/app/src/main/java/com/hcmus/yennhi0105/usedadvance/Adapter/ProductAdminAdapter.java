package com.hcmus.yennhi0105.usedadvance.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
 * Created by Administrator on 21/03/2017.
 */

public class ProductAdminAdapter extends BaseAdapter {
    CProduct cProduct = new CProduct();

    private Context context;
    private int layout;
    private List<CProduct> productList;
    private List<CProduct> arrayList = new ArrayList<CProduct>();


    public ProductAdminAdapter(Context context, int layout, List<CProduct> productList) {
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(layout, null);

            holder.txtName = (TextView) v.findViewById(R.id.textViewTenSPAdmin);
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/UVF Chikita.ttf");
            holder.txtName.setTypeface(typeface);
            holder.txtPrice = (TextView) v.findViewById(R.id.textViewGiaSPAdmin);

            holder.imgIcon = (ImageView) v.findViewById(R.id.imageViewIconSPAdmin);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        CProduct product = productList.get(position);

        holder.txtName.setText(product.getTitle());
        Typeface typefaceName = Typeface.createFromAsset(context.getAssets(), "fonts/UVF Chikita.ttf");
        holder.txtName.setTypeface(typefaceName);

        DecimalFormat decimalFormat = new DecimalFormat(",###");
        holder.txtPrice.setText(decimalFormat.format(product.getPrice()) + " VND");
        Typeface typefacePrice = Typeface.createFromAsset(context.getAssets(), "fonts/Lemon-Regular.ttf");
        holder.txtPrice.setTypeface(typefacePrice);

        Picasso.with(context)
                .load(product.getArrayIcon().get(0))
                .transform(new RoundedTransformation(20, 4)) // Redius imageView nhe!
                .into(holder.imgIcon);
//        // Bắt sự kiện khi click vào Icon menu trên lv
//        holder.imgMemu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final PopupMenu popupMenu = new PopupMenu(context, holder.imgMemu); // Nó sẽ hiển thị ngay cái này
//                popupMenu.getMenuInflater().inflate(R.menu.menu_popup_listpro_admin, popupMenu.getMenu());
//
//                // Bắt sụ kiên khi click vào Popup_menu
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//
//                        switch (item.getItemId()) {
//                            case R.id.menu_admin_popup_delete:
//                                ShowDialogDeletePro();
//                                break;
//                            case R.id.menu_admin_popup_private:
//                                ShowDialogPirvatePro();
//                                break;
//                            case R.id.menu_admin_popup_public:
//                                ShowDialogPublicPro();
//                                break;
//                        }
//                        return false;
//                    }
//                });
//                popupMenu.show();
//            }
//        });
//
//        // Bắt sự kiện khi click bào Lv
//        ListProductAdminActivity.lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                cProduct = ListProductAdminActivity.arrayProduct.get(position);
//                //Log.d("vqtt", "ID:" + cProduct.getID());
//                index = position;
//            }
//        });

        if (product.getStatusSell() == 1) {
            v.setBackground(context.getResources().getDrawable(R.drawable.ct_private));
            notifyDataSetChanged();

        } else {
            v.setBackground(context.getResources().getDrawable(R.drawable.ct_public));
            notifyDataSetChanged();
        }
        return v;
    }

    // Ham loc Filter
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
}
