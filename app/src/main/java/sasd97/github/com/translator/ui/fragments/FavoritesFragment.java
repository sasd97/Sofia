package sasd97.github.com.translator.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import java.util.List;

import butterknife.BindView;
import sasd97.github.com.translator.R;
import sasd97.github.com.translator.events.OnTranslationChangedListener;
import sasd97.github.com.translator.models.TranslationModel;
import sasd97.github.com.translator.ui.adapters.HistoryAdapter;
import sasd97.github.com.translator.ui.base.BaseHistoryFragment;
import sasd97.github.com.translator.utils.watchers.SearchDetector;

/**
 * Created by alexander on 10/04/2017.
 */

public class FavoritesFragment extends BaseHistoryFragment
        implements HistoryAdapter.HistoryInteractionListener, HistoryAdapter.OnItemSelectedListener {

    private static final String TAG = HistoryFragment.class.getCanonicalName();

    private SearchDetector searchDetector;
    private HistoryAdapter historyAdapter;
    private OnTranslationChangedListener translationChangedListener;

    @BindView(R.id.search_input_edittext)
    EditText searchInputEditText;
    @BindView(R.id.favorites_recyclerview)
    RecyclerView favoritesRecyclerView;

    @Override
    protected int getLayout() {
        return R.layout.fragment_favorites;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            setTranslationChangedListener((OnTranslationChangedListener) getActivity());
        } catch (ClassCastException classCastException) {
            classCastException.printStackTrace();
        }
    }

    @Override
    protected void onViewCreated(Bundle state) {
        super.onViewCreated(state);

        historyAdapter = new HistoryAdapter();
        historyAdapter.setItemSelectedListener(this);
        historyAdapter.setHistoryInteractionListener(this);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        favoritesRecyclerView.setHasFixedSize(true);
        favoritesRecyclerView.setAdapter(historyAdapter);

        searchDetector = new SearchDetector(this);
        searchInputEditText.addTextChangedListener(searchDetector);

        Thread t = new Thread(obtainFavorites);
        t.start();
    }

    public void setTranslationChangedListener(OnTranslationChangedListener translationChangedListener) {
        this.translationChangedListener = translationChangedListener;
    }

    @Override
    public void onObtain(List<TranslationModel> translations) {
        historyAdapter.addHistories(translations);
    }

    @Override
    public void onFilter(List<TranslationModel> translations) {
        historyAdapter.filterHistories(translations, searchQuery);
    }

    @Override
    public void onSelect(TranslationModel translation, int position) {
        translationChangedListener.onTranslationChanged(translation, null);
        translationChangedListener.onFragmentNeedToBeSwitched(OnTranslationChangedListener.TRANSLATE_FRAGMENT);
    }

    @Override
    public void onRemoveFavorite(int position, TranslationModel translation) {
        translations.remove(translation);
        historyAdapter.removeHistory(position);
    }
}
