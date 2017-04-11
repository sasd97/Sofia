package sasd97.github.com.translator.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import sasd97.github.com.translator.R;
import sasd97.github.com.translator.events.OnTranslationChangedListener;
import sasd97.github.com.translator.models.TranslationModel;
import sasd97.github.com.translator.services.HistorySqlService;
import sasd97.github.com.translator.ui.adapters.HistoryAdapter;
import sasd97.github.com.translator.ui.base.BaseFragment;

/**
 * Created by alexander on 10/04/2017.
 */

public class FavoritesFragment extends BaseFragment
        implements HistoryAdapter.HistoryInteractionListener, HistoryAdapter.OnItemSelectedListener {

    private static final String TAG = HistoryFragment.class.getCanonicalName();

    private HistoryAdapter historyAdapter;
    private OnTranslationChangedListener translationChangedListener;

    @BindView(R.id.favorites_recyclerview) RecyclerView favoritesRecyclerView;

    public static FavoritesFragment newInstance(OnTranslationChangedListener translationChangedListener) {
        FavoritesFragment favoritesFragment = new FavoritesFragment();
        favoritesFragment.setTranslationChangedListener(translationChangedListener);
        return favoritesFragment;
    }

    public void setTranslationChangedListener(OnTranslationChangedListener translationChangedListener) {
        this.translationChangedListener = translationChangedListener;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_favorites;
    }

    @Override
    protected void onViewCreated(Bundle state) {
        super.onViewCreated(state);

        historyAdapter = new HistoryAdapter();
        historyAdapter.setItemSelectedListener(this);
        historyAdapter.setHistoryInteractionListener(this);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        favoritesRecyclerView.setAdapter(historyAdapter);

        List<TranslationModel> trans = HistorySqlService.findAllFavorites();
        historyAdapter.addHistories(trans);
    }

    @Override
    public void onSelect(TranslationModel translation, int position) {
        translationChangedListener.onTranslationChanged(translation);
        translationChangedListener.onFragmentNeedToBeSwitched(0);
    }

    @Override
    public void onRemoveFavorite(int position) {
        historyAdapter.removeHistory(position);
    }

    @Override
    public void onDelete(int position) {}
}
