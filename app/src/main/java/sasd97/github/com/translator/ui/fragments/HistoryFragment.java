package sasd97.github.com.translator.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

public class HistoryFragment extends BaseFragment implements HistoryAdapter.OnItemSelectedListener {

    private static final String TAG = HistoryFragment.class.getCanonicalName();

    private HistoryAdapter historyAdapter;
    private OnTranslationChangedListener translationChangedListener;

    @BindView(R.id.history_recyclerview) RecyclerView historyRecyclerView;

    public static HistoryFragment newInstance(OnTranslationChangedListener translationChangedListener) {
        HistoryFragment historyFragment = new HistoryFragment();
        historyFragment.setTranslationChangedListener(translationChangedListener);
        return historyFragment;
    }

    public void setTranslationChangedListener(OnTranslationChangedListener translationChangedListener) {
        this.translationChangedListener = translationChangedListener;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_history;
    }

    @Override
    protected void onViewCreated(Bundle state) {
        super.onViewCreated(state);

        historyAdapter = new HistoryAdapter();
        historyAdapter.setItemSelectedListener(this);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        historyRecyclerView.setAdapter(historyAdapter);

        List<TranslationModel> trans = HistorySqlService.getAllTranslations();
        historyAdapter.addHistories(trans);
    }

    @Override
    public void onSelect(TranslationModel translation, int position) {
        translationChangedListener.onTranslationChanged(translation);
        translationChangedListener.onFragmentNeedToBeSwitched(0);
    }
}
