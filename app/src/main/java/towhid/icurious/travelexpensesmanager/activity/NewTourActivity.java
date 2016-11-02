package towhid.icurious.travelexpensesmanager.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import towhid.icurious.travelexpensesmanager.R;
import towhid.icurious.travelexpensesmanager.dataModel.Member;
import towhid.icurious.travelexpensesmanager.dataModel.Tour;
import towhid.icurious.travelexpensesmanager.database.TourManager;
import towhid.icurious.travelexpensesmanager.utils.Cons;

public class NewTourActivity extends AppCompatActivity {
    private ListView membersListView;
    private EditText et_title;
    private EditText et_description;
    private EditText et_budget;
    private EditText et_memberName;
    private TextView tv_goingDate;
    private TextView tv_returnDate;

    private ArrayList<Member> memberArrayList;
    private MemberAdapter adapter;
    private TourManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tour);

        et_title = (EditText) findViewById(R.id.et_title);
        et_description = (EditText) findViewById(R.id.et_description);

        tv_goingDate = (TextView) findViewById(R.id.tv_goingDate);
        tv_returnDate = (TextView) findViewById(R.id.tv_return);

        et_budget = (EditText) findViewById(R.id.et_budget);
        et_memberName = (EditText) findViewById(R.id.memberName);

        membersListView = (ListView) findViewById(R.id.membersList);
        memberArrayList = new ArrayList<>();
        adapter = new MemberAdapter(NewTourActivity.this, memberArrayList);
        membersListView.setAdapter(adapter);

        manager = new TourManager(this);

        final Calendar myCalendar = java.util.Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(java.util.Calendar.YEAR, year);
                myCalendar.set(java.util.Calendar.MONTH, monthOfYear);
                myCalendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);

                long dateLong = myCalendar.get(Calendar.YEAR) * 10000 + (myCalendar.get(Calendar.MONTH) + 1) * 100 + myCalendar.get(Calendar.DATE);
                tv_goingDate.setText("" + dateLong);
            }
        };


        tv_goingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        NewTourActivity.this,
                        date,
                        myCalendar.get(java.util.Calendar.YEAR), myCalendar.get(java.util.Calendar.MONTH),
                        myCalendar.get(java.util.Calendar.DAY_OF_MONTH)).show();
            }
        });

        final DatePickerDialog.OnDateSetListener retDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(java.util.Calendar.YEAR, year);
                myCalendar.set(java.util.Calendar.MONTH, monthOfYear);
                myCalendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);

                long dateLong = myCalendar.get(Calendar.YEAR) * 10000 + (myCalendar.get(Calendar.MONTH) + 1) * 100 + myCalendar.get(Calendar.DATE);
                tv_returnDate.setText("" + dateLong);
            }
        };


        tv_returnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        NewTourActivity.this,
                        retDate,
                        myCalendar.get(java.util.Calendar.YEAR), myCalendar.get(java.util.Calendar.MONTH),
                        myCalendar.get(java.util.Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_tour_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_saveData:
                saveData();
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

    private void saveData() {
        String title = et_title.getText().toString().trim();
        String desc = et_description.getText().toString().trim();
        String budget = et_budget.getText().toString().trim();
        String goingDate = tv_goingDate.getText().toString().trim(); // TODO this should be taken from date variable
        String returnDate = tv_returnDate.getText().toString().trim(); // TODO this should be taken from date variable

        if (title.isEmpty()) {
            Toast.makeText(NewTourActivity.this, "Title can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (memberArrayList.size() == 0) {
            Toast.makeText(NewTourActivity.this, "At least one member must be added!", Toast.LENGTH_SHORT).show();
            return;
        }

        budget = budget.isEmpty() ? "0" : budget;
        Tour tour = new Tour(title, desc, goingDate, returnDate, Double.valueOf(budget));

        int tourRow_id = manager.createTour(tour); // inserting Tour To Database

        for (Member m : memberArrayList) {// add members to database
            m.setTour_id(tourRow_id);
            manager.addMemberToTour(m);
        }
        startActivity(new Intent(NewTourActivity.this, TourExpanses.class).putExtra(Cons.TOUR_ID, tourRow_id));
        finish();


    }

    public void addMember(View view) {
        String name = et_memberName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(NewTourActivity.this, "Enter a name", Toast.LENGTH_SHORT).show();
            return;
        }
        adapter.add(new Member(name));
        et_memberName.getText().clear();
    }

}
