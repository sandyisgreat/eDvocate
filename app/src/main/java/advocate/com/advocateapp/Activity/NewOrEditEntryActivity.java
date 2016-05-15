package advocate.com.advocateapp.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.advocate.database.ClientTable;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import advocate.com.advocateapp.R;
import advocate.com.advocateapp.Utils.AppUtils;
import advocate.com.advocateapp.Utils.DatabaseManager;

public class NewOrEditEntryActivity extends AppCompatActivity {
    private EditText clientNameEt, clientMobileEt, caseNumberEt, courtNumberEt, nextDateEt, briefDetailsEt, lastNameEt;
    private Button cancelBt, saveBt, editBt;
    private String clientName, clientMobile, clientLastName, caseNumber, courtNumber, nextDate, briefDetails, lawyerId, type, caseNumberIntent;
    private Date nextDateOriginal;
    private DatabaseManager databaseManager;
    private ImageButton calendarIv;
    private CaldroidFragment dateFragment;
    private TextView titleTv;
    private LinearLayout msgLL, whatsAppLl, smsLL;
    private ClientTable clientTable;
    private String message;
    private ImageView homeIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_or_edit_entry);
        AppUtils.setStatusBarColor(NewOrEditEntryActivity.this);

        databaseManager = DatabaseManager.getInstance(NewOrEditEntryActivity.this);
        lawyerId = AppUtils.getFromSharedPrefs(NewOrEditEntryActivity.this, AppUtils.LAWYER_ID);
        getExtras();
        getWidgets();
        if (type.equalsIgnoreCase("edit")) {
            setWidgets();
        }
        nextDateEt.setEnabled(false);


        clickListener();
    }

    private void setWidgets() {
        clientTable = databaseManager.getClientCaseNumber(caseNumberIntent).get(0);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        clientMobileEt.setText(clientTable.getClientMobile());
        clientNameEt.setText(clientTable.getClientFirstName());
        lastNameEt.setText(clientTable.getClientLastName());
        caseNumberEt.setText(clientTable.getCaseNumber());
        courtNumberEt.setText(clientTable.getCourtNumber());
        briefDetailsEt.setText(clientTable.getBriefDetails());
        nextDateOriginal = clientTable.getNextDate();

        nextDateEt.setText(formatter.format(nextDateOriginal));

        setFiedsDisabled(false);
        titleTv.setText("EDIT ENTRY");
        saveBt.setText("EDIT");
        msgLL.setVisibility(View.VISIBLE);
        message = "Hello " + clientTable.getClientFirstName() + ", your next date is on " + formatter.format(clientTable.getNextDate());
        clientMobile = clientTable.getClientMobile();
    }

    private void setFiedsDisabled(Boolean tf) {
        clientNameEt.setEnabled(tf);
        clientMobileEt.setEnabled(tf);
        caseNumberEt.setEnabled(tf);
        courtNumberEt.setEnabled(tf);
        lastNameEt.setEnabled(tf);
        briefDetailsEt.setEnabled(tf);
        calendarIv.setEnabled(tf);


    }

    private void getExtras() {
        type = getIntent().getStringExtra("type");
        caseNumberIntent = getIntent().getStringExtra("caseNumber");
    }

    private void clickListener() {


        saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equalsIgnoreCase("edit")) {
                    if (saveBt.getText().toString().equalsIgnoreCase("edit")) {
                        setFiedsDisabled(true);
                        msgLL.setVisibility(View.GONE);
                        saveBt.setText("SAVE");
                    } else if (saveBt.getText().toString().equalsIgnoreCase("save")) {
                        saveInDatabase();
                        setFiedsDisabled(false);
                        saveBt.setText("DONE");
                    } else {
                        finish();
                    }
                } else {
                    if (saveBt.getText().toString().equalsIgnoreCase("save")) {
                        saveInDatabase();
                    } else {
                        finish();
                    }
                }


            }
        });
        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        calendarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFragment = new CaldroidFragment();
                Bundle bundle = new Bundle();
                final Calendar cal = Calendar.getInstance();
                bundle.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
                bundle.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
                dateFragment.setArguments(bundle);
                FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                t.replace(R.id.calendar, dateFragment);
                t.commit();
                SimpleDateFormat q = new SimpleDateFormat("MM");
                SimpleDateFormat d = new SimpleDateFormat("yyyy");

                String p = q.format(cal.getTime());
                String temp = d.format(cal.getTime());

                dateFragment = CaldroidFragment.newInstance("", Integer.parseInt(p), Integer.parseInt(temp));
                dateFragment.show(getSupportFragmentManager(), "TAG");
                if (type.equalsIgnoreCase("edit")) {
                    dateFragment.setMinDate(AppUtils.addDays(nextDateOriginal,1));
                } else {
                    dateFragment.setMinDate(new Date());

                }
                CaldroidListener listener = new CaldroidListener() {
                    @Override
                    public void onSelectDate(final Date date, View view) {
                        //Date today = AppUtils.removeTime(new Date());
                        //Date select = AppUtils.removeTime(date);
                        //if (date.before(today)) {
                        //
                        //    final AlertDialog.Builder builder = new AlertDialog.Builder(NewOrEditEntryActivity.this);
                        //    builder.setMessage("if u want to assign a date older than current date ? ").setCancelable(false);
                        //    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        //        @Override
                        //        public void onClick(DialogInterface dialogInterface, int i) {
                        //            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                        //
                        //            nextDateOriginal = AppUtils.removeTime(date);
                        //            String v = (formatter.format(date));
                        //            nextDateEt.setText(v);
                        //
                        //            dateFragment.dismiss();
                        //        }
                        //    });
                        //    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        //        @Override
                        //        public void onClick(DialogInterface dialog, int which) {
                        //            dialog.dismiss();
                        //        }
                        //    });
                        //
                        //    builder.show();
                        //
                        //
                        //} else {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

                            nextDateOriginal = AppUtils.removeTime(date);
                            String v = (formatter.format(date));
                            nextDateEt.setText(v);

                            dateFragment.dismiss();
                        //}


                    }
                };
                dateFragment.setCaldroidListener(listener);
            }

        });

        whatsAppLl.setOnClickListener(new View.OnClickListener()

                                      {
                                          @Override
                                          public void onClick(View view) {
                                              Intent sendIntent = new Intent();
                                              sendIntent.setAction(Intent.ACTION_SEND);
                                              sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                                              sendIntent.setType("text/plain");
                                              sendIntent.setPackage("com.whatsapp");
                                              try {
                                                  startActivity(sendIntent);
                                              } catch (android.content.ActivityNotFoundException ex) {
                                                  Toast.makeText(NewOrEditEntryActivity.this, "Whats App not installed on your device", Toast.LENGTH_SHORT).show();
                                              }
                                          }
                                      }

        );
        smsLL.setOnClickListener(new View.OnClickListener()

                                 {
                                     @Override
                                     public void onClick(View view) {
                                         Intent intentt = new Intent(Intent.ACTION_VIEW);
                                         intentt.setData(Uri.parse("sms:"));
                                         intentt.setType("vnd.android-dir/mms-sms");
                                         intentt.putExtra("address", clientMobile);
                                         intentt.putExtra("sms_body", message);
                                         startActivity(intentt);
                                     }
                                 }

        );

        homeIv.setOnClickListener(new View.OnClickListener()

                                  {
                                      @Override
                                      public void onClick(View v) {
                                          AppUtils.goToLawyerActivity(NewOrEditEntryActivity.this);
                                      }
                                  }

        );

    }

    private void saveInDatabase() {

        clientMobile = clientMobileEt.getText().toString();
        clientName = clientNameEt.getText().toString();
        caseNumber = caseNumberEt.getText().toString();
        courtNumber = courtNumberEt.getText().toString();
        nextDate = nextDateEt.getText().toString();
        clientLastName = lastNameEt.getText().toString();
        briefDetails = briefDetailsEt.getText().toString();
        message = "Hello " + clientName + ", your next date is on " + nextDate;

        if (TextUtils.isEmpty(clientMobile) || TextUtils.isEmpty(clientName) || TextUtils.isEmpty(caseNumber) || TextUtils.isEmpty(courtNumber) || TextUtils.isEmpty(nextDate) || TextUtils.isEmpty(briefDetails)) {
            Toast.makeText(NewOrEditEntryActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        } else {
            msgLL.setVisibility(View.VISIBLE);
            cancelBt.setVisibility(View.GONE);
            saveBt.setText("Done");
            setFiedsDisabled(false);
            if (!type.equalsIgnoreCase("edit")) {
                ClientTable clientTable = new ClientTable();
                clientTable.setUserIdClient(UUID.randomUUID().toString());
                clientTable.setLawyerId(lawyerId);
                clientTable.setBriefDetails(briefDetails);
                clientTable.setCaseNumber(caseNumber);
                clientTable.setClientMobile(clientMobile);
                clientTable.setClientFirstName(clientName);
                clientTable.setClientLastName(clientLastName);
                clientTable.setCourtNumber(courtNumber);
                clientTable.setNextDate(nextDateOriginal);
                databaseManager.insertClient(clientTable);
            } else {

                clientTable.setLawyerId(lawyerId);
                clientTable.setBriefDetails(briefDetails);
                clientTable.setCaseNumber(caseNumber);
                clientTable.setClientMobile(clientMobile);
                clientTable.setClientFirstName(clientName);
                clientTable.setClientLastName(clientLastName);
                clientTable.setCourtNumber(courtNumber);
                clientTable.setNextDate(nextDateOriginal);
                databaseManager.insertClient(clientTable);
            }
            Toast.makeText(NewOrEditEntryActivity.this, "Details are saved successfully", Toast.LENGTH_SHORT).show();


        }
    }

    private void getWidgets() {
        clientMobileEt = (EditText) findViewById(R.id.client_mobile_et);
        clientNameEt = (EditText) findViewById(R.id.client_name_et);
        caseNumberEt = (EditText) findViewById(R.id.case_number_et);
        courtNumberEt = (EditText) findViewById(R.id.court_number);
        nextDateEt = (EditText) findViewById(R.id.date_et);
        briefDetailsEt = (EditText) findViewById(R.id.brief_details_et);
        cancelBt = (Button) findViewById(R.id.cancel_bt);
        saveBt = (Button) findViewById(R.id.save_bt);
        calendarIv = (ImageButton) findViewById(R.id.calendar_ib);
        titleTv = (TextView) findViewById(R.id.title_tv);
        msgLL = (LinearLayout) findViewById(R.id.msg_ll);
        whatsAppLl = (LinearLayout) findViewById(R.id.whats_app_ll);
        smsLL = (LinearLayout) findViewById(R.id.sms_ll);
        homeIv = (ImageView) findViewById(R.id.home_bt);
        lastNameEt = (EditText) findViewById(R.id.client_last_name_et);
    }
}
