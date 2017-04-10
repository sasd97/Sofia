package sasd97.github.com.translator.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;

import java.util.List;

import butterknife.BindView;
import sasd97.github.com.translator.R;
import sasd97.github.com.translator.models.TranslationModel;
import sasd97.github.com.translator.services.HistorySqlService;
import sasd97.github.com.translator.ui.base.BaseActivity;
import sasd97.github.com.translator.ui.fragments.FavoritesFragment;
import sasd97.github.com.translator.ui.fragments.HistoryFragment;
import sasd97.github.com.translator.ui.fragments.TranslateFragment;

public class HomeActivity extends BaseActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = HomeActivity.class.getCanonicalName();

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
                commitFragment(TranslateFragment.newInstance());
                return true;
            case R.id.bottom_navigation_favorites:
                commitFragment(FavoritesFragment.newInstance());
                return true;
            case R.id.bottom_navigation_history:
                commitFragment(HistoryFragment.newInstance());
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
