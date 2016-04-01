package gmrr.kidzarea;

/**
 * Created by Rifqi Zuliansyah on 01/04/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import gmrr.kidzarea.activity.LoginActivity;

public class Splash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        //thread for splash screen running
        Thread logoTimer = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    Log.d("Exception", "Exception" + e);
                } finally {
                    startActivity(new Intent(Splash.this, LoginActivity.class));
                }
                finish();
            }
        };
        logoTimer.start();
    }
}

