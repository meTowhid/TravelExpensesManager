package towhid.icurious.travelexpensesmanager.activity;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import towhid.icurious.travelexpensesmanager.R;
import towhid.icurious.travelexpensesmanager.dataModel.ExpField;
import towhid.icurious.travelexpensesmanager.dataModel.ExpRowItem;
import towhid.icurious.travelexpensesmanager.dataModel.Expense;
import towhid.icurious.travelexpensesmanager.dataModel.Member;
import towhid.icurious.travelexpensesmanager.database.TourManager;

public class TourExpanses extends AppCompatActivity {
    TableLayout table;
    TextView[][] txtGrid;
    TextView tv_title;
    EditText et_title;
    EditText et_amount;
    private int ROW_NUM;
    private int COL_NUM;
    private double membersCost[];
    private int tourRow_id, counter;
    private ArrayList<Member> membersList;
    private ArrayList<ExpRowItem> expRowItemsList;
    private TourManager manager;
    private TableRow.LayoutParams llp;
    private DecimalFormat nf;
    private CoordinatorLayout coordinatorLayout;
    private TextView tourTitle;
    private TextView tourDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_expanses);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.tourExpansesCoordinator);
        table = (TableLayout) findViewById(R.id.tableLayout);
        tourRow_id = getIntent().getIntExtra("tourRowID", 0);
        manager = new TourManager(this);
        nf = new DecimalFormat("#.#");

        tourTitle = (TextView) findViewById(R.id.tourTitle);
        tourDescription = (TextView) findViewById(R.id.tourDescription);

        tourTitle.setText(manager.getTour(tourRow_id).getTitle());
        String des = manager.getTour(tourRow_id).getDescription();
        if (des.isEmpty()) tourDescription.setVisibility(View.GONE);
        else tourDescription.setText(des);

        generateView();
    }

    private void generateView() {
        counter = 0;
        table.removeAllViews();
        membersList = manager.getMembersByTourID(tourRow_id);
        expRowItemsList = manager.getExpRowItemsByTourID(tourRow_id);

        if (expRowItemsList.isEmpty()) {
            findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
            return;
        } else findViewById(R.id.emptyView).setVisibility(View.GONE);


        COL_NUM = membersList.size();
        ROW_NUM = expRowItemsList.size() > 0 ? expRowItemsList.size() / COL_NUM : 0;

        txtGrid = new TextView[ROW_NUM + 1][COL_NUM + 1];
        membersCost = new double[COL_NUM];

        llp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        llp.setMargins(3, 3, 3, 3);

        for (int row = 0; row <= ROW_NUM; row++) {
            TableRow tableRow = new TableRow(this);
            table.addView(tableRow);
            createTableCol(row, tableRow);
        }

        refreshNames();
    }

    private void refreshNames() {
        if (expRowItemsList.size() == 0) return;
        counter = 0;
        for (Member m : membersList) {
            m.setTotalExpenses(membersCost[counter]);
            manager.updateMember(m);
            ((TextView) ((TableRow) table.getChildAt(0)).getChildAt(counter + 1)).setText(m.getName() + "\n"
                    + Math.round(m.getTotalExpenses()));
            counter++;
        }
    }

    private void createTableCol(int row, TableRow tableRow) {
        for (int col = 0; col <= COL_NUM; col++) {
            final int FINAL_ROW = row;
            final int FINAL_COL = col;

            TextView txt = new TextView(this);

            if (row == 0) { // first row of the chart
                if (col == 0) { // null view (first item)
                    txt.setText("Sum");
                } else {
                    txt.setText(membersList.get(col - 1).getName()); // title field or name of the members...
                }
                txt.setTypeface(null, Typeface.BOLD);
//                txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                txt.setBackgroundResource(R.color.colorName);
            } else { // cost title and amounts row here
                if (col == 0) {
                    txt.setTypeface(null, Typeface.BOLD);
                    txt.setText(expRowItemsList.get(counter).getExpField().getTitle()); // exp field names with total cost
                    txt.setBackgroundResource(R.color.colorTitle); // item colors
                } else {
                    double newCost = expRowItemsList.get(counter).getExpField().getAmount();
                    membersCost[col - 1] += newCost;
                    txt.setText(nf.format(newCost));
                    txt.setBackgroundResource(R.color.colorAmount); // item colors
                    counter++;
                }
            }

            txt.setGravity(Gravity.CENTER);
            txt.setPadding(10, 20, 10, 20);
            txt.setLayoutParams(llp);
            txt.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    gridTextViewClicked(FINAL_ROW, FINAL_COL);
                    return true;
                }
            });
            tableRow.addView(txt);
            txtGrid[row][col] = txt;
        }
    }

    private void gridTextViewClicked(final int row, final int col) {
        final int fRow = row;
        final int fCol = col;

        if (row == 0) {
            if (col == 0) { // summary clicked
                AlertDialog.Builder builder = new AlertDialog.Builder(TourExpanses.this)
                        .setTitle("Summary")
                        .setMessage(manager.getTour(tourRow_id).toString())
                        .setPositiveButton("ok", null);

                builder.create().show();

            } else { // member name clicked

                final Dialog d = new Dialog(this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.dialog_update_info);
                d.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = d.getWindow();
                lp.copyFrom(window.getAttributes());
                //This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);


                et_title = (EditText) d.findViewById(R.id.et_costTitle);
                et_amount = (EditText) d.findViewById(R.id.et_costAmount);

                et_title.setText(membersList.get(col - 1).getName());

                double depositAmount = membersList.get(col - 1).getDeposit();
                if (depositAmount != 0.0) et_amount.setText(nf.format(depositAmount));


                d.findViewById(R.id.btn_addCost).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = et_title.getText().toString().trim();
                        String deposit = et_amount.getText().toString().trim();
                        double dou = deposit.isEmpty() ? 0 : Double.parseDouble(deposit);

                        if (name.isEmpty()) {
                            Toast.makeText(TourExpanses.this, "Name can't be empty!", Toast.LENGTH_SHORT).show();
                        } else {
                            Member m = membersList.get(fCol - 1);
                            m.setName(name);
                            m.setDeposit(dou);
                            manager.updateMember(m);
                            refreshNames();
                            Snackbar.make(coordinatorLayout, "Member info updated", Snackbar.LENGTH_SHORT).show();
                            d.dismiss();
                        }
                    }
                });
            }
        } else {
            if (col != 0) { // clicked on amount or price
                final Dialog d = new Dialog(this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.dialog_update_amount);
                d.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = d.getWindow();
                lp.copyFrom(window.getAttributes());
                //This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);


                tv_title = (TextView) d.findViewById(R.id.tv_costTitle);
                et_amount = (EditText) d.findViewById(R.id.et_costAmount);

                tv_title.setText("old amount: " + txtGrid[row][col].getText());

                d.findViewById(R.id.btn_addCost).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String amount = et_amount.getText().toString().trim();

                        if (amount.isEmpty()) {
                            Toast.makeText(TourExpanses.this, "Amount can't be empty!", Toast.LENGTH_SHORT).show();
                        } else {
                            txtGrid[fRow][fCol].setText(amount);
                            int expFieldID = expRowItemsList.get((row - 1) * (membersList.size()) + col - 1).getExpField().getId();
                            manager.updateMembersCost(expFieldID, Double.parseDouble(amount));
                            generateView();
                            Snackbar.make(coordinatorLayout, "Amount changed!", Snackbar.LENGTH_SHORT).show();
                            d.dismiss();
                        }
                    }
                });
            } /*else {
                final ExpField expField = expRowItemsList.get((row - 1) * membersList.size()).getExpField();
//                Toast.makeText(TourExpanses.this, "" + expField.getId(), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(TourExpanses.this)
                        .setMessage("Remove '" + expField.getTitle() + "' from list?")
                        .setNegativeButton("cancel", null)
                        .setPositiveButton("remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                manager.clearCostRow(expField.getId());
                                generateView();
                                Snackbar.make(coordinatorLayout, "Entry removed successfully!", Snackbar.LENGTH_SHORT).show();
                            }
                        });

                builder.create().show();
            }*/
        }
    }

    public void newCost() {

        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog_add_cost);
        d.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = d.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        et_title = (EditText) d.findViewById(R.id.et_costTitle);
        et_amount = (EditText) d.findViewById(R.id.et_costAmount);
        final CheckBox cb = (CheckBox) d.findViewById(R.id.checkBox);
        final ListView lv = (ListView) d.findViewById(R.id.membersList);
        ArrayList<String> nameList = new ArrayList<>();
        final ArrayList<Boolean> selectedNameList = new ArrayList<>();

        for (Member m : membersList) {
            nameList.add(m.getName());
            selectedNameList.add(false);
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(TourExpanses.this, R.layout.row_member, nameList);
        lv.setAdapter(adapter);

        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setItemsCanFocus(false);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                boolean checked = ((CheckedTextView) view).isChecked();
                ((CheckedTextView) view).setChecked(checked);
                selectedNameList.set(i, checked);
            }
        });


        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) lv.setVisibility(View.GONE);
                else lv.setVisibility(View.VISIBLE);
            }
        });


        d.findViewById(R.id.btn_addCost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKey(TourExpanses.this);
                String t = et_title.getText().toString().trim();
                String amount = et_amount.getText().toString().trim();

                if (t.isEmpty() || amount.isEmpty()) {
                    Toast.makeText(TourExpanses.this, "Fields can't be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    int selectedMemberCount = 0;
                    String title = t.substring(0, 1).toUpperCase() + t.substring(1);

                    if (cb.isChecked()) {
                        for (int i = 0; i < selectedNameList.size(); i++) {
                            selectedNameList.set(i, true);
                        }
                    }

                    for (boolean b : selectedNameList) {
                        if (b) selectedMemberCount++;
                    }

                    double singleMembersAmount = Double.parseDouble(amount) / selectedMemberCount;

                    for (int i = 0; i < membersList.size(); i++) {
                        int expFieldRowID;
                        if (selectedNameList.get(i))
                            expFieldRowID = manager.addToExpField(new ExpField(title, singleMembersAmount));
                        else
                            expFieldRowID = manager.addToExpField(new ExpField(title, 0));

                        Expense e = new Expense(tourRow_id, membersList.get(i).getId(), expFieldRowID);
                        manager.addExpenseToTour(e);
                    }
                    generateView();
                    Snackbar.make(coordinatorLayout, title + " added", Snackbar.LENGTH_SHORT).show();
                    d.dismiss();
                }
            }
        });
    }

    public void hideSoftKey(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_expenses, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_newCost:
                newCost();
                return true;
            case R.id.action_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(TourExpanses.this)
                        .setView(R.layout.about_layout)
                        .setPositiveButton("ok", null);
                builder.create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

