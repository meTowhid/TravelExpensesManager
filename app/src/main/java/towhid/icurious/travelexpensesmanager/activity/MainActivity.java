package towhid.icurious.travelexpensesmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import towhid.icurious.travelexpensesmanager.R;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.tourList);
        String[] tourNames = {"Bandarban", "Cox's bazar", "Sylet", "Sundarban", "Saint Martin", "Kuakata", "Bogura"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tourNames);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(MainActivity.this, "" + i, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, TourExpanses.class));
            }
        });
    }
}
