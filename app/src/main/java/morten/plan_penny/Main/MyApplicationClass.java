package morten.plan_penny.Main;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.parse.Parse;

/**
 * Created by morten on 5/14/15.
 */
public class MyApplicationClass extends Application {
        @Override
        public void onCreate() {
            super.onCreate();

            Data data = Data.getInstance();

            Boolean pref_offlineSync = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("offline", true);

            Parse.enableLocalDatastore(this);


            Parse.initialize(this, "jAhTMz631ms7GxHtJANHAAAy9ia8x057LFoo5jNL", "bMXBsZSP5BeKJCOJ4VU6mOCm8DPsPv19TRagJJJD");

            data.setMode(pref_offlineSync);

        }
}
