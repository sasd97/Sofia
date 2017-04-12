package sasd97.github.com.translator.ui.fragments;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import sasd97.github.com.translator.R;
import sasd97.github.com.translator.events.OnTranslationChangedListener;
import sasd97.github.com.translator.models.TranslationModel;
import sasd97.github.com.translator.services.HistorySqlService;
import sasd97.github.com.translator.ui.adapters.HistoryAdapter;
import sasd97.github.com.translator.ui.base.BaseHistoryFragment;
import sasd97.github.com.translator.utils.SearchDetector;

/**
 * Created by alexander on 10/04/2017.
 */

public class HistoryFragment extends BaseHistoryFragment
        implements HistoryAdapter.OnItemSelectedListener {

    private static final String TAG = HistoryFragment.class.getCanonicalName();

    private SearchDetector searchDetector;
    private HistoryAdapter historyAdapter;
    private ItemTouchHelper itemTouchHelper;
    private OnTranslationChangedListener translationChangedListener;

    @BindView(R.id.search_input_edittext) MaterialEditText searchInputEditText;
    @BindView(R.id.history_recyclerview) RecyclerView historyRecyclerView;

    private ItemTouchHelper.SimpleCallback swipeToDismissListener = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            final int positionToDelete = viewHolder.getAdapterPosition();
            final TranslationModel translationToDelete = translations.get(positionToDelete);
            translations.remove(positionToDelete);
            historyAdapter.notifyItemRemoved(positionToDelete);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    HistorySqlService.delete(translationToDelete);
                }
            });
            t.start();
        }
    };

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

        itemTouchHelper = new ItemTouchHelper(swipeToDismissListener);

        historyAdapter = new HistoryAdapter();
        historyAdapter.setItemSelectedListener(this);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        historyRecyclerView.setHasFixedSize(true);
        historyRecyclerView.setAdapter(historyAdapter);
        itemTouchHelper.attachToRecyclerView(historyRecyclerView);

        searchDetector = new SearchDetector(this);
        searchInputEditText.addTextChangedListener(searchDetector);

        Thread t = new Thread(obtainHistory);
        t.start();
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
        translationChangedListener.onTranslationChanged(translation);
        translationChangedListener.onFragmentNeedToBeSwitched(0);
    }


}
