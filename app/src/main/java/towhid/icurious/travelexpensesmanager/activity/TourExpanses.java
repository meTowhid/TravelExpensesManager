package towhid.icurious.travelexpensesmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;

import towhid.icurious.travelexpensesmanager.R;
import towhid.icurious.travelexpensesmanager.dataModel.Member;
import towhid.icurious.travelexpensesmanager.database.TourManager;
import towhid.icurious.travelexpensesmanager.utils.Cons;

public class TourExpanses extends AppCompatActivity {
    TableLayout table;
    Button[][] buttonsGrid;
    int tourRow_id;
    private int ROW_NUM = 5;
    private int COL_NUM;
    private ArrayList<Member> membersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_expanses);

        tourRow_id = getIntent().getIntExtra(Cons.TOUR_ID, 0);
        TourManager manager = new TourManager(this);
        membersArrayList = manager.getMembersByTourID(tourRow_id);
        COL_NUM = membersArrayList.size();
        table = (TableLayout) findViewById(R.id.tableLayout);
        buttonsGrid = new Button[ROW_NUM][COL_NUM];


    }

    @Override
    protected void onResume() {
        super.onResume();
        generateView();
    }

    private void generateView() {
        for (int row = 0; row < ROW_NUM; row++) {
            TableRow tableRow = new TableRow(this);
            table.addView(tableRow);
            for (int col = 0; col < COL_NUM; col++) {
                final int FINAL_ROW = row;
                final int FINAL_COL = col;

                Button button = new Button(this);
//                button.setText(row + "," + col);
                button.setText(membersArrayList.get(col).getName());
//                button.setPadding(0, 0, 0, 0);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gridButtonClicked(FINAL_ROW, FINAL_COL);
                    }
                });
                tableRow.addView(button);
                buttonsGrid[row][col] = button;
            }
        }
    }

    private void gridButtonClicked(int finalRow, int finalCol) {
        Toast.makeText(TourExpanses.this, "clicked!" + finalRow + "." + finalCol, Toast.LENGTH_SHORT).show();
        buttonsGrid[finalRow][finalCol].setText("Hello!");
    }

    public void fabClicked(View view) {
        startActivity(new Intent(TourExpanses.this, AddCostActivity.class).putExtra(Cons.TOUR_ID, tourRow_id));
    }
}
