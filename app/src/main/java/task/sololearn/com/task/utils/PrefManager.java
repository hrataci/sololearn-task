package task.sololearn.com.task.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static task.sololearn.com.task.utils.Constants.Preference.*;

public class PrefManager {

    private SharedPreferences mPreference;
    private SharedPreferences.Editor mEditor;
    private static  PrefManager ourInstance ;
    public static PrefManager getInstance() {
        if (ourInstance == null) {
            throw new RuntimeException("PrefManager is not initialized");
        }
        return ourInstance;
    }

    public static synchronized void init(Context context) {
        ourInstance = new PrefManager(context);
    }

    private PrefManager(Context context) {
        mPreference = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
        mEditor = mPreference.edit();
    }


    public void setIsBackgrounded(boolean isBackgrounded) {
        mEditor.putBoolean(IS_BACKGROUNDED, isBackgrounded);
        mEditor.commit();
    }

    public boolean isBackgrounded() {
        return mPreference.getBoolean(IS_BACKGROUNDED, false);
    }

}
