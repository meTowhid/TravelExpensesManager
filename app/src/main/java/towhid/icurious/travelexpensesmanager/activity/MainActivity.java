package towhid.icurious.travelexpensesmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import towhid.icurious.travelexpensesmanager.R;
import towhid.icurious.travelexpensesmanager.dataModel.Tour;
import towhid.icurious.travelexpensesmanager.database.TourManager;
import towhid.icurious.travelexpensesmanager.utils.Cons;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    private ArrayAdapter<String> adapter;
    private TourManager manager;
    private ArrayList<Tour> tours;
    private ArrayList<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.tourList);
        manager = new TourManager(this);
        names = new ArrayList<>();

        refreshList();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(MainActivity.this, TourExpanses.class).putExtra(Cons.TOUR_ID, tours.get(i).getId()));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        tours = manager.getTourList();
        names.clear();
        for (Tour t : tours) names.add(t.getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_createTour:
                startActivity(new Intent(MainActivity.this, NewTourActivity.class));
                return true;
           /* case R.id.action_exit:
                // Exit option clicked.
                return true;
            case R.id.action_settings:
                // Settings option clicked.
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
