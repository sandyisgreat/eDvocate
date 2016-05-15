package advocate.com.advocateapp.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.advocate.database.FirmTable;
import com.advocate.database.UserTable;

import java.util.ArrayList;
import java.util.List;

import advocate.com.advocateapp.R;
import advocate.com.advocateapp.Utils.AppUtils;
import advocate.com.advocateapp.Utils.DatabaseManager;

public class AddTeammatesActivity extends AppCompatActivity {
    private String firmName;
    private AutoCompleteTextView lawyersACTV;
    private Button addBt;
    private ListView membersLv;
    private List<UserTable> lawyersList = new ArrayList<>();
    private List<UserTable> allLawyesrList = new ArrayList<>();
    private TextView lawyerNameTv, lawyerIdTv, numberTv, firmNameTv;
    private DatabaseManager databaseManager;
    private Activity context = AddTeammatesActivity.this;
    private String selectedLawyerId;
    private FirmTable firmSelectedForJoin;
    private LawyerListAdapter lawyerListAdapter;
    private List<String> lawyerNames=new ArrayList<>();
    private ImageView homeIv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teammates);
        databaseManager = DatabaseManager.getInstance(AddTeammatesActivity.this);
        getExtras();
        getWidgets();
        getList();

        setWidgets();
        setList();
        clickListeners();
    }

    private void setList() {
         lawyerListAdapter = new LawyerListAdapter();
        membersLv.setAdapter(lawyerListAdapter);


    }

    private void getList() {
        //String lawyerIds = getLawyerIds();
        //if (!TextUtils.isEmpty(lawyerIds)) {
        //    String[] categoryId = lawyerIds.split(", ");
        //    for (int k = 0; k < categoryId.length; k++) {
        //        lawyersAssociatedWithFirm.add(databaseManager.getUserByLawyerId(categoryId[k]).get(0));
        //    }
        //
        //}

        lawyersList = databaseManager.getLawyerByFirmId(firmName);

    }

    private void clickListeners() {
        lawyersACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String lawyerId = lawyersACTV.getText().toString();
                List<FirmTable> firms = databaseManager.getLawyersByFirmIdAndLawyerId(firmName, lawyerId);
                if (firms != null) {
                    if (firms.size() == 0) {
                        selectedLawyerId = lawyerId;
                    } else {
                        Toast.makeText(context, "This Lawyer is already a member", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });

        addBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideSoftKeyboard(AddTeammatesActivity.this);
                if (!TextUtils.isEmpty(selectedLawyerId)) {
                    UserTable lawyer = databaseManager.getUserByLawyerId(selectedLawyerId).get(0);
                    String firmIds = lawyer.getFirmNames();
                    if (TextUtils.isEmpty(firmIds)) {
                        firmIds = firmName;
                    } else {
                        firmIds = new StringBuilder().append(firmIds).append(",").append(firmName).toString();
                    }
                    lawyer.setFirmNames(firmIds);
                    databaseManager.insertUser(lawyer);

                    String lawyerIds = getLawyerIds();
                    if (TextUtils.isEmpty(lawyerIds)) {
                        lawyerIds = selectedLawyerId;
                    } else {
                        lawyerIds = new StringBuilder().append(lawyerIds).append(",").append(selectedLawyerId).toString();
                    }
                    firmSelectedForJoin.setLawyerIds(lawyerIds);
                    databaseManager.insertFirm(firmSelectedForJoin);

                    getList();

                } else {
                    Toast.makeText(context, "Please enter a lawyer Id ", Toast.LENGTH_SHORT).show();

                }
            }
        });


        homeIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppUtils.goToLawyerActivity(AddTeammatesActivity.this);
                    }
                });
    }

    private String getLawyerIds() {
        firmSelectedForJoin = databaseManager.getFirmByName(firmName).get(0);
        String lawyerIds = firmSelectedForJoin.getLawyerIds();
        return lawyerIds;
    }

    private void setWidgets() {
        allLawyesrList=databaseManager.getAllLawyers();
        firmNameTv.setText(firmName);

        if (allLawyesrList != null) {
            for (int i = 0; i < allLawyesrList.size(); i++) {
                lawyerNames.add(allLawyesrList.get(i).getLawyerId());
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, lawyerNames);
        lawyersACTV.setAdapter(adapter);
    }

    private void getWidgets() {
        lawyersACTV = (AutoCompleteTextView) findViewById(R.id.lawyer_actv);
        membersLv = (ListView) findViewById(R.id.members_lv);
        addBt = (Button) findViewById(R.id.add_bt);
        firmNameTv = (TextView) findViewById(R.id.firm_name_tv);
        homeIv=(ImageView)findViewById(R.id.home_bt);
    }

    private void getExtras() {
        firmName = getIntent().getStringExtra("firmName");

    }

    private class LawyerListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return lawyersList.size();
        }

        @Override
        public Object getItem(int position) {
            return lawyersList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.members_lv, parent, false);
            }
            lawyerIdTv = (TextView) itemView.findViewById(R.id.lawyer_id_tv);
            lawyerNameTv = (TextView) itemView.findViewById(R.id.lawyer_name_tv);
            numberTv = (TextView) itemView.findViewById(R.id.number_tv);

            numberTv.setText(position + 1 + "");
            lawyerNameTv.setText(lawyersList.get(position).getLastName()+" "+ lawyersList.get(position).getFirstName());
            lawyerIdTv.setText(lawyersList.get(position).getLawyerId());

            return itemView;
        }
    }
}
