package towhid.icurious.travelexpensesmanager.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import towhid.icurious.travelexpensesmanager.R;

public class TourExpanses extends AppCompatActivity {
    TableLayout table;
    Button[][] buttonsGrid;
    private int ROW_NUM = 15;
    private int COL_NUM = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_expanses);
        table = (TableLayout) findViewById(R.id.tableLayout);
        buttonsGrid = new Button[ROW_NUM][COL_NUM];

        for (int row = 0; row < ROW_NUM; row++) {
            TableRow tableRow = new TableRow(this);
            table.addView(tableRow);
            for (int col = 0; col < COL_NUM; col++) {
                final int FINAL_ROW = row;
                final int FINAL_COL = col;

                Button button = new Button(this);
                button.setText(row + "," + col);
                button.setPadding(0, 0, 0, 0);
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
}
