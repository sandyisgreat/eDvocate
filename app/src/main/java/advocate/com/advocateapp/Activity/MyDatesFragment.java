package advocate.com.advocateapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.advocate.database.ClientTable;

import java.util.ArrayList;
import java.util.List;

import advocate.com.advocateapp.R;
import advocate.com.advocateapp.Utils.AppUtils;
import advocate.com.advocateapp.Utils.DatabaseManager;

public class MyDatesFragment extends Fragment {

    private View rootView;
    private ListView myDatesLv;
    private List<ClientTable> todaysList = new ArrayList<>();
    private String lawyerId;
    private DatabaseManager databaseManager;
    private TextView clientNameTv, caseNumberTv, messageBoxTv, numberTv,courtNumberTv;
    private int position;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_dates, container, false);
        databaseManager = DatabaseManager.getInstance(getActivity());
        getExtras();
        getWidgets(rootView);
        getList();
        setUpList();

        return rootView;
    }

    private void getList() {

        todaysList = databaseManager.getClientByLawyerIdAndDate(lawyerId, AppUtils.addDays(LawyerDatesActivity.selectedDate, position-250));
        if (todaysList != null) {
            if (todaysList.size() == 0) {
                messageBoxTv.setVisibility(View.VISIBLE);
            } else {
                messageBoxTv.setVisibility(View.GONE);

            }
        }
    }

    private void getExtras() {
        lawyerId = getArguments().getString(AppUtils.LAWYER_ID);
        position = getArguments().getInt("position");
    }

    private void setUpList() {
        MyListAdapter myListAdapter = new MyListAdapter();
        myDatesLv.setAdapter(myListAdapter);
    }

    private void getWidgets(View rootView) {
        myDatesLv = (ListView) rootView.findViewById(R.id.dates_lv);
        messageBoxTv = (TextView) rootView.findViewById(R.id.message_box);
    }


    private class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return todaysList.size();
        }

        @Override
        public Object getItem(int position) {
            return todaysList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.date_item_view, parent, false);
            }
            clientNameTv = (TextView) itemView.findViewById(R.id.client_name_tv);
            caseNumberTv = (TextView) itemView.findViewById(R.id.case_number_tv);
            numberTv = (TextView) itemView.findViewById(R.id.number_tv);
            courtNumberTv=(TextView)itemView.findViewById(R.id.court_number_tv);
            numberTv.setText(position + 1 + "");
            clientNameTv.setText( todaysList.get(position).getClientLastName()+" "+todaysList.get(position).getClientFirstName());
            courtNumberTv.setText("Court No. : "+todaysList.get(position).getCourtNumber());
            caseNumberTv.setText("Case No. : " + todaysList.get(position).getCaseNumber());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), NewOrEditEntryActivity.class);
                    intent.putExtra("type", "edit");
                    intent.putExtra("caseNumber", todaysList.get(position).getCaseNumber());
                    startActivity(intent);
                }
            });

            return itemView;
        }
    }
}
