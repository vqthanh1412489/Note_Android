package com.hcmus.yennhi0105.usedadvance.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcmus.yennhi0105.usedadvance.CClass.CCategory;
import com.hcmus.yennhi0105.usedadvance.R;
import com.hcmus.yennhi0105.usedadvance.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 14/03/2017.
 */

public class CategoryAdapter extends BaseAdapter{

    private Context context;
    private int layout;
    private List<CCategory> categoryList;
    private List<CCategory> arrayList = new ArrayList<CCategory>();

    public CategoryAdapter(Context context, int layout, List<CCategory> categoryList) {
        this.context = context;
        this.layout = layout;
        this.categoryList = categoryList;
        arrayList.addAll(categoryList);
    }

    @Override
    public int getCount() {
        return categoryList.size();
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
        TextView txtName;
        ImageView imgIcon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(layout, null);

            holder.txtName = (TextView) v.findViewById(R.id.textViewTenDM);
            holder.imgIcon = (ImageView) v.findViewById(R.id.imageViewHinhDM);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();

        }

        CCategory category = categoryList.get(position);

        holder.txtName.setText(category.getName());
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/VNF-Sofia Regular.ttf");
        holder.txtName.setTypeface(typeface);
        //Picasso.with(context).load(category.getIcon()).into(holder.imgIcon);
        Picasso.with(context)
                .load(category.getIcon())
                .transform(new RoundedTransformation(30, 4)) // Redius imageView nhe!
                .into(holder.imgIcon);
        return v;
    }

    // Tìm kiêm
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault()); // Đưa về chữ thường để search
        categoryList.clear(); // Xóa dữ liệu trong mảng
        if (charText.length() == 0) {
            categoryList.addAll(arrayList); // Nếu chưa nhập gì thì lấy kết quả là nguyên gốc đã sao
        } else {
            for (CCategory category : arrayList) { // Duyệt hết mảng thứ 2
                if (category.getName().toLowerCase(Locale.getDefault()) // Tìm theo thuộc tính nào > ở đây tìm theo Title// Chuyển dữ liệu trong Name thành chữ thường
                        .contains(charText)) {
                    categoryList.add(category); // Add thêm vào mảng để đưa lên Listview
                }
            }
        }
        notifyDataSetChanged();
    }
}


// RecylerView
//public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
//    private Context context;
//    private List<CCategory> categoryList;
//
//    public CategoryAdapter(Context context, List<CCategory> categoryList) {
//        this.context = context;
//        this.categoryList = categoryList;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View itemView = inflater.inflate(R.layout.line_category, null);
//        return new ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.txtName.setText(categoryList.get(position).getName());
//        Picasso.with(context).load(categoryList.get(position).getIcon())
//                .into(holder.imgIcon);
//    }
//
//    @Override
//    public int getItemCount() {
//        return categoryList.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        TextView txtName;
//        ImageView imgIcon;
//        public ViewHolder(View itemView) {
//            super(itemView);
//            txtName = (TextView) itemView.findViewById(R.id.textViewTenDM);
//            imgIcon = (ImageView) itemView.findViewById(R.id.imageViewHinhDM);
//
//        }
//    }
//}
