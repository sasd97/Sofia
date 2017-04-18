package sasd97.github.com.translator.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import butterknife.BindView;
import sasd97.github.com.translator.R;
import sasd97.github.com.translator.events.OnTranslationChangedListener;
import sasd97.github.com.translator.models.Dictionary.DictionaryModel;
import sasd97.github.com.translator.models.TranslationModel;
import sasd97.github.com.translator.repositories.TranslationRepository;
import sasd97.github.com.translator.ui.base.BaseActivity;
import sasd97.github.com.translator.ui.fragments.FavoritesFragment;
import sasd97.github.com.translator.ui.fragments.HistoryFragment;
import sasd97.github.com.translator.ui.fragments.TranslateFragment;

public class HomeActivity extends BaseActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        OnTranslationChangedListener {

    private static final String TAG = HomeActivity.class.getCanonicalName();

    private TranslationRepository activeTranslationRepository;
    @BindView(R.id.home_bottom_navigation) BottomNavigationView bottomNavigationView;

    public HomeActivity() {
        super(R.layout.activity_home);
    }

    @Override
    protected boolean isToolbarEnabled() {
        return true;
    }

    @Override
    protected int getToolbarId() {
        return R.id.home_toolbar;
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        activeTranslationRepository = new TranslationRepository();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.home_fragment_container, new TranslateFragment())
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottom_navigation_translate:
                commitFragment(TranslateFragment.getInstance(activeTranslationRepository));
                return true;
            case R.id.bottom_navigation_favorites:
                commitFragment(new FavoritesFragment());
                return true;
            case R.id.bottom_navigation_history:
                commitFragment(new HistoryFragment());
                return true;
        }
        return false;
    }

    private void commitFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_fragment_container, fragment)
                .commit();
    }

    @Override
    public void onTranslationChanged(TranslationModel translation, DictionaryModel dictionary) {
        activeTranslationRepository.set(translation, dictionary);
    }

    @Override
    public void onFragmentNeedToBeSwitched(int fragment) {
        switch (fragment) {
            case TRANSLATE_FRAGMENT:
                commitFragment(TranslateFragment.getInstance(activeTranslationRepository));
                bottomNavigationView.setSelectedItemId(R.id.bottom_navigation_translate);
                break;
            case FAVORITES_FRAGMENT:
                commitFragment(new FavoritesFragment());
                bottomNavigationView.setSelectedItemId(R.id.bottom_navigation_favorites);
                break;
            case HISTORY_FRAGMENT:
                commitFragment(new HistoryFragment());
                bottomNavigationView.setSelectedItemId(R.id.bottom_navigation_history);
                break;
        }
    }
}
