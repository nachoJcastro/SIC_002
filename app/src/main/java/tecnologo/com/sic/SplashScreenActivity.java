package tecnologo.com.sic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import tecnologo.com.sic.Utilidades.Sesion;

/**
 * Created by Gaston on 14/11/2014.
 */
public class SplashScreenActivity extends Activity implements Runnable {

    private static String TAG = SplashScreenActivity.class.getName();
    private static long SLEEP_TIME = 5;
    private Thread mThread;

    static SplashScreenActivity splash;
    Sesion sesion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash_screen);
        sesion = new Sesion(this);
        splash=this;
        // Start timer and launch main activity
        //IntentLauncher launcher = new IntentLauncher();
      //  launcher.start();

    //}
        mThread = new Thread(this);

        mThread.start();
    }

    @Override
    public void run()
    {
        try
        {
            Thread.sleep(3000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
           // if (SplashScreenActivity.splash.sesion.isLoggedIn()){
                startActivity(new Intent(SplashScreenActivity.this, Main.class));
            /* }
            else
            {
                startActivity(new Intent(SplashScreenActivity.this, Login.class));

            }
            */

            finish();
        }
    }

/*
    private class IntentLauncher extends Thread {
        @Override

        public void run() {
            try {
                // Sleeping
                Thread.sleep(SLEEP_TIME*3000);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            // Start main activity
            Intent intent = new Intent(SplashScreenActivity.this, Main.class);
            SplashScreenActivity.this.startActivity(intent);
            SplashScreenActivity.this.finish();
        }
    }
}*/


}