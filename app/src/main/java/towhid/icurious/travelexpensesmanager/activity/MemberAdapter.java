package towhid.icurious.travelexpensesmanager.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import towhid.icurious.travelexpensesmanager.R;
import towhid.icurious.travelexpensesmanager.dataModel.Member;

/**
 * Created by nipun on 10/30/2016.
 */

class MemberAdapter extends ArrayAdapter {

    private ArrayList<Member> memberList;
    private Context context;


    public MemberAdapter(Context context, ArrayList<Member> memberList) {
        super(context, R.layout.member_row_layout, memberList);
        this.memberList = memberList;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.member_row_layout, parent, false);
        }

        ((TextView) rowView.findViewById(R.id.name_tv)).setText(memberList.get(position).getName());
        ((TextView) rowView.findViewById(R.id.deposit_tv)).setText("" + memberList.get(position).getDeposit());

        return rowView;
    }


}
