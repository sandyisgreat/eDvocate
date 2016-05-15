package advocate.com.advocateapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import java.util.Timer;
import java.util.TimerTask;

import advocate.com.advocateapp.R;
import advocate.com.advocateapp.Utils.AppUtils;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        AppUtils.setStatusBarColor(SplashScreenActivity.this);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(AppUtils.getFromSharedPrefs(SplashScreenActivity.this, AppUtils.LAWYER_ID))) {
                    Intent intent = new Intent(SplashScreenActivity.this, SignUpActivity.class);
                    intent.putExtra("type", "register");
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, LawyerDatesActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);

    }
}
