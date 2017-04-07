package sasd97.github.com.translator.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import butterknife.BindView;
import sasd97.github.com.translator.R;
import sasd97.github.com.translator.ui.base.BaseActivity;
import sasd97.github.com.translator.ui.fragments.TranslateFragment;

public class HomeActivity extends BaseActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.navigation) BottomNavigationView bottomNavigationView;

    public HomeActivity() {
        super(R.layout.activity_home);
    }

    @Override
    protected boolean isButterKnifeEnabled() {
        return true;
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, TranslateFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottom_navigation_translate:
                return true;
            case R.id.bottom_navigation_favorites:
                return true;
            case R.id.bottom_navigation_history:
                return true;
        }
        return false;
    }

    private void commitFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
}
