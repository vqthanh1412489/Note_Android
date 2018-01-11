package com.hcmus.yennhi0105.usedadvance.Dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hcmus.yennhi0105.usedadvance.R;

import java.util.ArrayList;
import java.util.Arrays;

public class DialogCityActivity extends Activity {

    ListView lvCity;
    ArrayList<String> arrayCity;
    ArrayAdapter adapterCity = null;

    Button btnYes;
    TextView txtResult;

    int REQUEST_CODE_DISTRICT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_city);

        AddControl();

        lvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DialogCityActivity.this, DialogDistrictActivity.class);
                intent.putExtra("index", position); // Truyền STT item của City qua bên kia để xác định District của nó
                startActivityForResult(intent, REQUEST_CODE_DISTRICT);

                txtResult.setText(arrayCity.get(position));
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("zone", txtResult.getText().toString().trim());
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_DISTRICT && resultCode == RESULT_OK && data != null) {
            String _district = data.getStringExtra("district");
            txtResult.append(" - " + _district);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void AddControl() {

        txtResult = (TextView) findViewById(R.id.textViewResultCity);
        btnYes = (Button) findViewById(R.id.buttonYesCity);

        lvCity = (ListView) findViewById(R.id.lvCity);
        String[] city = getResources().getStringArray(R.array.City);
        arrayCity = new ArrayList<String>(Arrays.asList(city));
        adapterCity = new ArrayAdapter(DialogCityActivity.this, android.R.layout.simple_list_item_1, arrayCity);
        lvCity.setAdapter(adapterCity);
    }
}
