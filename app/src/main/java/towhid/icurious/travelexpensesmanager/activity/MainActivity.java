package towhid.icurious.travelexpensesmanager.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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

public class MainActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    private ListView lv;
    private ArrayAdapter<String> adapter;
    private TourManager manager;
    private ArrayList<Tour> tours;
    private ArrayList<String> names;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.mainCoordinator);
        lv = (ListView) findViewById(R.id.tourList);
        manager = new TourManager(this);
        names = new ArrayList<>();

        refreshList();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(MainActivity.this, TourExpanses.class).putExtra("tourRowID", tours.get(i).getId()));
            }
        });
        if (tours.isEmpty()) findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
        else findViewById(R.id.emptyView).setVisibility(View.GONE);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long l) {
                final int i = index;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete tour!")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                adapter.remove(names.get(i));
                                manager.deleteTour(tours.get(i).getId());
                                refreshList();
                                Snackbar.make(coordinatorLayout, "Tour deleted!", Snackbar.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("no", null);

                builder.create().show();
                return true;
            }
        });
    }


    private void refreshList() {
        tours = manager.getTourList();
        names.clear();
        for (Tour t : tours) names.add(t.getTitle());

        adapter = new ArrayAdapter<>(this, R.layout.row_tour_list, R.id.row_tourName, names);
        lv.setAdapter(adapter);
    }

    public void fabClicked(View view) {
        startActivityForResult(new Intent(MainActivity.this, NewTourActivity.class), 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setView(R.layout.about_layout)
                        .setPositiveButton("ok", null);
                builder.create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                int tourRowID = data.getIntExtra("tourRowID", 1);
                adapter.add(manager.getTour(tourRowID).getTitle());
                Snackbar.make(coordinatorLayout, "New tour created!", Snackbar.LENGTH_SHORT).show();
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(coordinatorLayout, "Press again to exit", Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
