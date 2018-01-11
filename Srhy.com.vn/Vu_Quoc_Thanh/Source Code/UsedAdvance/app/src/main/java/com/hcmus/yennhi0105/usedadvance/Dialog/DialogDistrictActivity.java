package com.hcmus.yennhi0105.usedadvance.Dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hcmus.yennhi0105.usedadvance.R;

import java.util.ArrayList;
import java.util.Arrays;

public class DialogDistrictActivity extends Activity {

    ListView lvDistrict;
    ArrayList<String> arrayDistrict;
    ArrayAdapter adapterDistrict = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_district);


        AddControl();
        final int index = getIntent().getIntExtra("index", -1); // Nhận index từ phía City

        if (index != -1) {
            switch (index) {
                case 0:
                    SelectDistrict(R.array.HaNoi);
                    break;
                case 1:
                    SelectDistrict(R.array.ThanhPhoHCM);
                    break;
            }
        }


        lvDistrict.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Chuyển tên quận về bên City
                Intent intent = new Intent();
                intent.putExtra("district", arrayDistrict.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    private void SelectDistrict(int resourceArrayString) { // Truyền vô danh sách Huyện của mỗi Tỉnh từ Resource
        arrayDistrict.clear(); // Xóa mảng khỏi trùng nhé!
        arrayDistrict = new ArrayList<String>(Arrays.asList(getResources().getStringArray(resourceArrayString)));

        // Khởi tạo ở trong cho mới. Không cần cập nhật
        adapterDistrict = new ArrayAdapter(DialogDistrictActivity.this, android.R.layout.simple_list_item_1, arrayDistrict);
        lvDistrict.setAdapter(adapterDistrict);
    }

    private void AddControl() {

        lvDistrict = (ListView) findViewById(R.id.lvDistrict);
        arrayDistrict = new ArrayList<String>();
    }
}
