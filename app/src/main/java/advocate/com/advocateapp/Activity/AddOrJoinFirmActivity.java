package advocate.com.advocateapp.Activity;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.advocate.database.FirmTable;
import com.advocate.database.UserTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import advocate.com.advocateapp.R;
import advocate.com.advocateapp.Utils.AppUtils;
import advocate.com.advocateapp.Utils.DatabaseManager;

public class AddOrJoinFirmActivity extends AppCompatActivity {
    private ListView memberLv;
    private List<UserTable> lawyerList = new ArrayList<>();
    private AutoCompleteTextView lawyersACTV, firmsACTV;
    private List<FirmTable> firmList = new ArrayList<>();
    private Button addBt, joinBt, createBt;
    private EditText createFirmEt;
    private DatabaseManager databaseManager;
    private Activity context = AddOrJoinFirmActivity.this;
    private List<FirmTable> firmSelectedForJoin;
    private UserTable lawyer;
    private List<FirmTable> firmAssociatedWithUser = new ArrayList<>();
    private String lawyerId;
    private ListView firmLV;
    private TextView firmNameTv, numberTv;
    private FirmListAdapter firmListAdapter;
    private List<String> firmNames = new ArrayList<>();
    private ImageView homeIv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_join_firm);
        lawyerId = AppUtils.getFromSharedPrefs(context, AppUtils.LAWYER_ID);
        databaseManager = DatabaseManager.getInstance(context);
        lawyer = databaseManager.getUserByLawyerId(lawyerId).get(0);

        getWidgets();
        getList();
        setWidgets();
        setList();
        clickListeners();
    }

    //private void getFirmsList() {
    //    //String firmNames = lawyer.getFirmNames();
    //    //if (!TextUtils.isEmpty(firmNames)) {
    //    //    String[] firmName = firmNames.split(", ");
    //    //    for (int k = 0; k < firmName.length; k++) {
    //    //        firmAssociatedWithUser.add(databaseManager.getFirmByName(firmName[k]).get(0));
    //    //    }
    //    //
    //    //}
    //
    //    firmSelectedForJoin = databaseManager.getFirmsByLawyerId(lawyerId);
    //
    //}

    private void setWidgets() {
        if (firmSelectedForJoin != null) {
            for (int i = 0; i < firmSelectedForJoin.size(); i++) {
                firmNames.add(firmSelectedForJoin.get(i).getFirmName());
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, firmNames);
        firmsACTV.setAdapter(adapter);
    }

    private void setList() {
        firmListAdapter = new FirmListAdapter();
        firmLV.setAdapter(firmListAdapter);

    }

    private void getList() {

        firmSelectedForJoin = databaseManager.getFirmsByLawyerId(lawyerId);
    }

    private void clickListeners() {
        createBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideSoftKeyboard(AddOrJoinFirmActivity.this);

                String firmName = createFirmEt.getText().toString();
                if (!TextUtils.isEmpty(firmName)) {
                    List<FirmTable> firms = databaseManager.getFirmByName(firmName);
                    if (firms != null) {
                        if (firms.size() == 0) {
                            FirmTable firm = new FirmTable();
                            firm.setUserIdFirm(UUID.randomUUID().toString());
                            firm.setLawyerIds(lawyerId);
                            firm.setFirmName(firmName);
                            databaseManager.insertFirm(firm);

                            String firmIds = lawyer.getFirmNames();
                            if (TextUtils.isEmpty(firmIds)) {
                                firmIds = firmName;
                            } else {
                                firmIds = new StringBuilder().append(firmIds).append(",").append(firmName).toString();
                            }
                            lawyer.setFirmNames(firmIds);
                            databaseManager.insertUser(lawyer);
                            lawyer = databaseManager.getUserByLawyerId(lawyer.getLawyerId()).get(0);
                            getList();
                            firmListAdapter.notifyDataSetChanged();
                            createFirmEt.setText("");
                            Toast.makeText(context, "Firm created successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "This firm already exist", Toast.LENGTH_SHORT).show();
                        }
                    }


                } else {
                    Toast.makeText(context, "Please enter a firm name to create a firm", Toast.LENGTH_SHORT).show();

                }
            }
        });

        firmsACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppUtils.hideSoftKeyboard(AddOrJoinFirmActivity.this);

                String firmName = firmsACTV.getText().toString();
                List<FirmTable> firmSelectedForJoin = databaseManager.getFirmByName(firmName);
                if (firmSelectedForJoin != null) {
                    if (firmSelectedForJoin.size() == 0) {
                        Toast.makeText(context, "This firm does not exist", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        joinBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideSoftKeyboard(AddOrJoinFirmActivity.this);
                if (!firmsACTV.getText().toString().isEmpty()) {
                    if (firmSelectedForJoin != null) {
                        if (firmSelectedForJoin.size() != 0) {

                            String lawyerIds = firmSelectedForJoin.get(0).getLawyerIds();
                            if (TextUtils.isEmpty(lawyerIds)) {
                                lawyerIds = lawyerId;
                            } else {
                                lawyerIds = new StringBuilder().append(lawyerIds).append(",").append(lawyerId).toString();
                            }
                            firmSelectedForJoin.get(0).setLawyerIds(lawyerIds);
                            databaseManager.insertFirm(firmSelectedForJoin.get(0));

                            String firmIds = lawyer.getFirmNames();
                            if (TextUtils.isEmpty(firmIds)) {
                                firmIds = firmSelectedForJoin.get(0).getFirmName();
                            } else {
                                firmIds = new StringBuilder().append(firmIds).append(",").append(firmSelectedForJoin.get(0).getFirmName()).toString();
                            }
                            lawyer.setFirmNames(firmIds);
                            databaseManager.insertUser(lawyer);
                            lawyer = databaseManager.getUserByLawyerId(lawyer.getLawyerId()).get(0);
                            getList();
                            firmListAdapter.notifyDataSetChanged();
                            firmsACTV.setText("");
                            Toast.makeText(context, "You are now a member of " + firmSelectedForJoin.get(0).getFirmName(), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(context, "Please select a firm to join", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                    }
                } else {
                    Toast.makeText(context, "Please select a firm to join", Toast.LENGTH_SHORT).show();

                }
            }
        });

        homeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.goToLawyerActivity(AddOrJoinFirmActivity.this);
            }
        });
    }

    private void getWidgets() {
        joinBt = (Button) findViewById(R.id.join_bt);
        createBt = (Button) findViewById(R.id.create_bt);
        lawyersACTV = (AutoCompleteTextView) findViewById(R.id.lawyer_actv);
        firmsACTV = (AutoCompleteTextView) findViewById(R.id.firm_actv);
        createFirmEt = (EditText) findViewById(R.id.create_firm_et);
        firmLV = (ListView) findViewById(R.id.firm_lv);
        homeIv = (ImageView) findViewById(R.id.home_bt);

    }

    private class FirmListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return firmSelectedForJoin.size();
        }

        @Override
        public Object getItem(int position) {
            return firmSelectedForJoin.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.firm_item_view, parent, false);
            }
            addBt = (Button) itemView.findViewById(R.id.add_bt);
            firmNameTv = (TextView) itemView.findViewById(R.id.firm_name_tv);
            numberTv = (TextView) itemView.findViewById(R.id.number_tv);

            numberTv.setText(position + 1 + "");
            firmNameTv.setText(firmSelectedForJoin.get(position).getFirmName());

            addBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddTeammatesActivity.class);
                    intent.putExtra("firmName", firmSelectedForJoin.get(position).getFirmName());
                    startActivity(intent);
                }
            });
            return itemView;
        }
    }
}
