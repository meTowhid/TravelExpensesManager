package towhid.icurious.travelexpensesmanager.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import towhid.icurious.travelexpensesmanager.R;
import towhid.icurious.travelexpensesmanager.dataModel.ExpField;
import towhid.icurious.travelexpensesmanager.dataModel.Expense;
import towhid.icurious.travelexpensesmanager.dataModel.Member;
import towhid.icurious.travelexpensesmanager.database.TourManager;
import towhid.icurious.travelexpensesmanager.utils.Cons;


public class AddCostActivity extends AppCompatActivity {
    EditText et_title;
    EditText et_amount;
    TourManager manager;
    int tourRow_id;
    private ArrayList<Member> memberArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cost);
        et_title = (EditText) findViewById(R.id.et_costTitle);
        et_amount = (EditText) findViewById(R.id.et_costAmount);

        tourRow_id = getIntent().getIntExtra(Cons.TOUR_ID, 1);
        manager = new TourManager(this);
        memberArrayList = manager.getMembersByTourID(tourRow_id);
    }

    public void addCost(View view) {
        String title = et_title.getText().toString().trim();
        String amount = et_amount.getText().toString().trim();

        if (title.isEmpty() || amount.isEmpty()) {
            Toast.makeText(AddCostActivity.this, "Fields can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        double singleMembersAmount = Double.parseDouble(amount) / memberArrayList.size();
        int expFieldRowID = manager.addToExpField(new ExpField(title, singleMembersAmount));
        for (Member m : memberArrayList) {
            Expense e = new Expense(tourRow_id, m.getId(), expFieldRowID);
            manager.addExpenseToTour(e);
        }
        finish();
    }
}
