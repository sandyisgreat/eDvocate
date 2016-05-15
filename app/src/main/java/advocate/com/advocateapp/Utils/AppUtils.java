package advocate.com.advocateapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import advocate.com.advocateapp.Activity.LawyerDatesActivity;
import advocate.com.advocateapp.R;

/**
 * Created by aditya on 2/9/15.
 */
public class AppUtils {
    private static final String APP_NAME ="AdvocateApp" ;
    public static String LAWYER_ID="lawyerId";
    public static String USER="user";

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        } else if (str.trim().equalsIgnoreCase("null")) {
            return true;
        } else {
            return false;
        }
    }


    public static void saveInSharedPrefs(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(AppUtils.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getFromSharedPrefs(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(AppUtils.APP_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, "");
    }

    public static void removeFromSharedPrefs(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(AppUtils.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        editor.commit();
    }


    public static void setStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(activity.getResources().getColor(R.color.statusBarClor));
        }
    }


    //public static String connectToServer(String requestJson, String path) {
    //    StringBuilder result = new StringBuilder("");
    //    try{
    //        URL url = new URL(path);
    //        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
    //        connection.setRequestProperty("Content-Type", "application/json");
    //        connection.setRequestProperty("User-Agent", "");
    //        connection.setRequestMethod("POST");
    //        connection.setDoInput(true);
    //        connection.connect();
    //
    //        byte[] outputInBytes = requestJson.getBytes("UTF-8");
    //        OutputStream os = connection.getOutputStream();
    //        os.write(outputInBytes);
    //        os.close();
    //
    //        InputStream inputStream = connection.getInputStream();
    //        BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
    //        String line = "";
    //        while ((line = rd.readLine()) != null) {
    //            result.append(line);
    //        }
    //
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //    }
    //    return result.toString();
    //}
    //

    public static void hideKeyboardOnTouch(final Activity activity, View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }
            });
        }

        // If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                hideKeyboardOnTouch(activity, innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void clearApplicationData(Context context) {
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    public static Date getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat text = new SimpleDateFormat("dd/MM/yyyy");
        String stext = text.format(cal.getTime());
        SimpleDateFormat checkinsdf = new SimpleDateFormat("dd/MM/yyyy");
        Date checkindate = null;
        try {
            checkindate = checkinsdf.parse(stext);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date currentDate = checkindate;
        return currentDate;
    }

    public static boolean isUserIdValid(String userId) {
           return Patterns.EMAIL_ADDRESS.matcher(userId).matches() || Patterns.PHONE.matcher(userId).matches();
       }

    public static Date removeTime(Date date) {
           Calendar cal = Calendar.getInstance();
           cal.setTime(date);
           cal.set(Calendar.HOUR_OF_DAY, 0);
           cal.set(Calendar.MINUTE, 0);
           cal.set(Calendar.SECOND, 0);
           cal.set(Calendar.MILLISECOND, 0);

           return cal.getTime();
       }

    public static Date addDays(Date date, int days)
       {
           Calendar cal = Calendar.getInstance();
           cal.setTime(date);
           cal.add(Calendar.DATE, days); //minus number would decrement the days

           return cal.getTime();
       }

    public static void goToLawyerActivity(Activity activity){
        Intent intent=new Intent(activity, LawyerDatesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }
}
