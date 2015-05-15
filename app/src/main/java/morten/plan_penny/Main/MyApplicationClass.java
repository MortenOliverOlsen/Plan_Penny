package morten.plan_penny.Main;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by morten on 5/14/15.
 */
public class MyApplicationClass extends Application {
        @Override
        public void onCreate() {
            super.onCreate();
            // Enable Local Datastore.
            Parse.enableLocalDatastore(this);

            Parse.initialize(this, "jAhTMz631ms7GxHtJANHAAAy9ia8x057LFoo5jNL", "bMXBsZSP5BeKJCOJ4VU6mOCm8DPsPv19TRagJJJD");
            Data.getInstance().loadArrayLists();
        }
}
