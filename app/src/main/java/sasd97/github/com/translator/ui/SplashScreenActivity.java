package sasd97.github.com.translator.ui;

import android.content.Intent;

import sasd97.github.com.translator.ui.base.BaseActivity;

/**
 * Created by alexander on 11/04/2017.
 */

public class SplashScreenActivity extends BaseActivity {

    @Override
    protected boolean dropLifecycleExecution() {
        return true;
    }

    @Override
    protected void onBrokenExecution() {
        super.onBrokenExecution();

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
