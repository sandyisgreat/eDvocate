package advocate.com.advocateapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.advocate.database.UserTable;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import advocate.com.advocateapp.R;
import advocate.com.advocateapp.Utils.AppUtils;
import advocate.com.advocateapp.Utils.DatabaseManager;

public class LawyerDatesActivity extends AppCompatActivity {
    private ViewPager mPager;
    public static Date selectedDate = AppUtils.removeTime(new Date());
    private TextView dateTv, lawyerNameTv;
    private String lawyerId;
    private ImageView dateSelector;
    private CaldroidFragment dateFragment;
    private boolean doubleBackToExitPressedOnce = false;
    private ImageView leftArrow, rightArrow, homeIv;
    private UserTable lawyerProfile;
    private DatabaseManager databaseManager;
    private String[] weekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_dates2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        databaseManager = DatabaseManager.getInstance(LawyerDatesActivity.this);
        setSupportActionBar(toolbar);
        AppUtils.setStatusBarColor(LawyerDatesActivity.this);
        lawyerId = AppUtils.getFromSharedPrefs(LawyerDatesActivity.this, AppUtils.LAWYER_ID);
        lawyerProfile = databaseManager.getUserByLawyerId(lawyerId).get(0);
        getWidgets();
        setWidgets();
        setUpViewPager();

        clickListeners();


    }

    private void setWidgets() {
        lawyerNameTv.setText(lawyerProfile.getFirstName() + " " + lawyerProfile.getLastName());
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    private void clickListeners() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LawyerDatesActivity.this, NewOrEditEntryActivity.class);
                intent.putExtra("type", "new");
                startActivity(intent);
            }
        });

        dateSelector.setOnClickListener(new View.OnClickListener() {
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

                CaldroidListener listener = new CaldroidListener() {
                    @Override
                    public void onSelectDate(Date date, View view) {

                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                        selectedDate = AppUtils.removeTime(date);
                        setUpViewPager();
                        dateFragment.dismiss();
                    }

                };
                dateFragment.setCaldroidListener(listener);
            }

        });

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            }
        });
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
            }
        });

        homeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate = AppUtils.removeTime(new Date());

                setUpViewPager();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpViewPager();
    }

    private void setUpViewPager() {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        dateTv.setText("( " + weekdays[selectedDate.getDay()] + "  " + formatter.format(selectedDate) + " )");

        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(250);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                if (position == 0) {
                    leftArrow.setVisibility(View.GONE);
                    rightArrow.setVisibility(View.VISIBLE);
                } else if (position == 500) {
                    leftArrow.setVisibility(View.VISIBLE);
                    rightArrow.setVisibility(View.GONE);
                } else {
                    leftArrow.setVisibility(View.VISIBLE);
                    rightArrow.setVisibility(View.VISIBLE);
                }
                Date presentDate = AppUtils.addDays(selectedDate, position - 250);
                dateTv.setText(weekdays[presentDate.getDay()] + "  " + formatter.format(presentDate));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getWidgets() {
        mPager = (ViewPager) findViewById(R.id.mpager);
        dateTv = (TextView) findViewById(R.id.current_date_tv);
        dateSelector = (ImageView) findViewById(R.id.date_selector);
        leftArrow = (ImageView) findViewById(R.id.left_arrow);
        rightArrow = (ImageView) findViewById(R.id.right_arrow);
        lawyerNameTv = (TextView) findViewById(R.id.lawyer_name_tv);
        homeIv = (ImageView) findViewById(R.id.home_bt);

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            Calendar calendar = Calendar.getInstance();
            MyDatesFragment fragment = new MyDatesFragment();
            Bundle args = new Bundle();
            args.putString(AppUtils.LAWYER_ID, lawyerId);
            args.putInt("position", position);

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 500;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lawyer_dates, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {

            return true;
        }
        if (id == R.id.action_profile) {
            Intent intent = new Intent(LawyerDatesActivity.this, SignUpActivity.class);
            intent.putExtra("type", "profile");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
