package advocate.com.advocateapp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.advocate.database.UserTable;

import java.util.List;
import java.util.UUID;

import advocate.com.advocateapp.R;
import advocate.com.advocateapp.Utils.AppUtils;
import advocate.com.advocateapp.Utils.DatabaseManager;

public class SignUpActivity extends AppCompatActivity {
    private EditText nameEt, emailEt, mobileNumberEt, lawyerIdEt, lastName;
    private Button signUpBt, addJoinFirmBt, editBt;
    private Activity activity = SignUpActivity.this;
    private DatabaseManager databaseManager;
    private CheckBox indiCheckBox, firmCheckBox;
    private String type;
    private UserTable user;
    private TextView title_tv;
    private ImageView homeIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseManager = DatabaseManager.getInstance(activity);
        AppUtils.setStatusBarColor(SignUpActivity.this);
        getExtras();

        setContentView(R.layout.activity_sign_up);
        getWidgets();
        if (type.equalsIgnoreCase("profile")) {
            setWidgets();
            signUpBt.setVisibility(View.GONE);
        }else{
            editBt.setVisibility(View.GONE);
        }
        clickListeners();

    }

    private void setWidgets() {
        user = databaseManager.getUserByLawyerId(AppUtils.getFromSharedPrefs(activity, AppUtils.LAWYER_ID)).get(0);
        nameEt.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        mobileNumberEt.setText(user.getMobileNumber());
        lawyerIdEt.setText(user.getLawyerId());
        emailEt.setText(user.getEmailId());

        setFiedsDisabled(false);

    }

    private void getExtras() {
        type = getIntent().getStringExtra("type");
    }

    private void clickListeners() {
        signUpBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    makeListForLawyerProfiles();
                    saveInDatabase();


            }
        });

        indiCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indiCheckBox.setChecked(true);
                firmCheckBox.setChecked(false);
            }
        });

        firmCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firmCheckBox.setChecked(true);
                indiCheckBox.setChecked(false);
            }
        });
        addJoinFirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, AddOrJoinFirmActivity.class);
                startActivity(intent);
            }
        });
        homeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equalsIgnoreCase("profile")) {
                    AppUtils.goToLawyerActivity(SignUpActivity.this);
                }
            }
        });
        editBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editBt.getText().toString().equalsIgnoreCase("edit")) {
                    setFiedsDisabled(true);
                    editBt.setText("SAVE");
                } else {
                    saveInDatabase();

                    setFiedsDisabled(false);
                    editBt.setText("EDIT");
                }
            }
        });

    }

    private void makeListForLawyerProfiles() {

        for (int i = 0; i < 3; i++) {
            UserTable userTable = new UserTable();
            userTable.setUserIdLawyer(UUID.randomUUID().toString());
            userTable.setLawyerId(String.valueOf(Math.pow(10, i)));
            userTable.setCategory("individual");
            userTable.setMobileNumber("123456789" + i);
            userTable.setFirstName("Name");
            userTable.setLastName("lastname");
            databaseManager.insertUser(userTable);
        }
    }

    private void setFiedsDisabled(boolean tf) {
        nameEt.setEnabled(tf);
        mobileNumberEt.setEnabled(tf);
        emailEt.setEnabled(tf);
        lawyerIdEt.setEnabled(false);
        lastName.setEnabled(tf);

        if (user.getCategory().equalsIgnoreCase("individual")) {
            indiCheckBox.setChecked(true);
        } else {
            firmCheckBox.setChecked(true);
        }
        indiCheckBox.setEnabled(tf);
        firmCheckBox.setEnabled(tf);
    }

    private void saveInDatabase() {
        String name = nameEt.getText().toString();
        String lastname = lastName.getText().toString();

        String mobile = mobileNumberEt.getText().toString();
        String email = emailEt.getText().toString();
        String lawyerId = lawyerIdEt.getText().toString();


        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(mobile) || TextUtils.isEmpty(lawyerId) || TextUtils.isEmpty(email) || TextUtils.isEmpty(lastname)) {
            Toast.makeText(activity, "Please fill all the fields", Toast.LENGTH_LONG).show();

        } else if (mobile.toString().length() == 10) {
            if (AppUtils.isUserIdValid(email)) {
                if (!TextUtils.isEmpty(name)) {
                    if (!TextUtils.isEmpty(lawyerId)) {
                        if (indiCheckBox.isChecked() || firmCheckBox.isChecked()) {

                            List<UserTable> isUserExist = databaseManager.getUserByLawyerId(lawyerId);
                            if (isUserExist != null) {
                                if (isUserExist.size() == 0) {


                                    UserTable user = new UserTable();
                                    user.setUserIdLawyer(UUID.randomUUID().toString());


                                    if (indiCheckBox.isChecked()) {
                                        user.setCategory("individual");
                                    } else {
                                        user.setCategory("firm");

                                    }

                                    user.setUserIdLawyer(UUID.randomUUID().toString());
                                    user.setEmailId(email);
                                    user.setLawyerId(lawyerId);
                                    user.setFirstName(name);
                                    user.setMobileNumber(mobile);
                                    user.setLastName(lastname);
                                    databaseManager.insertUser(user);
                                    AppUtils.saveInSharedPrefs(activity, AppUtils.LAWYER_ID, lawyerId);

                                    Intent intent = new Intent(activity, LawyerDatesActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    if (type.equalsIgnoreCase("profile")) {
                                        if (indiCheckBox.isChecked()) {
                                            user.setCategory("individual");
                                        } else {
                                            user.setCategory("firm");

                                        }

                                        user.setUserIdLawyer(UUID.randomUUID().toString());
                                        user.setEmailId(email);
                                        user.setLawyerId(lawyerId);
                                        user.setFirstName(name);
                                        user.setLastName(lastname);
                                        user.setMobileNumber(mobile);
                                        databaseManager.insertUser(user);
                                        Toast.makeText(activity, "Details updated successfully.", Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(activity, "This user already exist.", Toast.LENGTH_LONG).show();

                                    }


                                }
                            }

                        } else {
                            Toast.makeText(activity, "Please choose one category", Toast.LENGTH_LONG).show();

                        }
                    } else {
                        Toast.makeText(activity, "Please enter a lawyer id", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(activity, "Please enter a your name", Toast.LENGTH_LONG).show();

                }
            } else {
                emailEt.setText("");
                Toast.makeText(activity, "Please enter a valid email Id", Toast.LENGTH_LONG).show();
            }
        } else {

        }
    }


    private void getWidgets() {
        nameEt = (EditText) findViewById(R.id.name_et);
        mobileNumberEt = (EditText) findViewById(R.id.mobile_et);
        emailEt = (EditText) findViewById(R.id.email_et);
        lawyerIdEt = (EditText) findViewById(R.id.lawyer_id_et);
        signUpBt = (Button) findViewById(R.id.register_bt);
        title_tv = (TextView) findViewById(R.id.title_tv);
        addJoinFirmBt = (Button) findViewById(R.id.add_join_firm_bt);
        homeIv = (ImageView) findViewById(R.id.home_bt);
        editBt = (Button) findViewById(R.id.save_bt);
        lastName = (EditText) findViewById(R.id.last_name_et);
        indiCheckBox = (CheckBox) findViewById(R.id.indi_checkbox);
        firmCheckBox = (CheckBox) findViewById(R.id.firm_checkbox);
        if (type.equalsIgnoreCase("profile")) {
            addJoinFirmBt.setVisibility(View.VISIBLE);
            signUpBt.setText("EDIT PROFILE");
            title_tv.setText("MY PROFILE");
        }

    }

}