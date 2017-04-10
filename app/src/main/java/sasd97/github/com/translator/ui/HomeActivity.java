package sasd97.github.com.translator.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;

import java.util.List;

import butterknife.BindView;
import sasd97.github.com.translator.R;
import sasd97.github.com.translator.events.OnTranslationChangedListener;
import sasd97.github.com.translator.models.TranslationModel;
import sasd97.github.com.translator.services.HistorySqlService;
import sasd97.github.com.translator.ui.base.BaseActivity;
import sasd97.github.com.translator.ui.fragments.FavoritesFragment;
import sasd97.github.com.translator.ui.fragments.HistoryFragment;
import sasd97.github.com.translator.ui.fragments.TranslateFragment;

public class HomeActivity extends BaseActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        OnTranslationChangedListener {

    private static final String TAG = HomeActivity.class.getCanonicalName();

    @BindView(R.id.navigation) BottomNavigationView bottomNavigationView;

    private TranslationModel currentTranslation;

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
                .add(R.id.fragmentContainer, TranslateFragment.newInstance(this))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottom_navigation_translate:
                commitFragment(TranslateFragment.newInstance(currentTranslation, this));
                return true;
            case R.id.bottom_navigation_favorites:
                commitFragment(FavoritesFragment.newInstance(this));
                return true;
            case R.id.bottom_navigation_history:
                commitFragment(HistoryFragment.newInstance(this));
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

    @Override
    public void onFragmentNeedToBeSwitched(int fragment) {
        switch (fragment) {
            case 0:
                commitFragment(TranslateFragment.newInstance(currentTranslation, this));
                bottomNavigationView.setSelectedItemId(R.id.bottom_navigation_translate);
                break;
        }
    }

    @Override
    public void onTranslationChanged(TranslationModel translation) {
        this.currentTranslation = translation;
    }
}
