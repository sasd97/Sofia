package sasd97.github.com.translator.ui.base;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import sasd97.github.com.translator.models.TranslationModel;
import sasd97.github.com.translator.services.HistorySqlService;
import sasd97.github.com.translator.utils.SearchDetector;

/**
 * Created by alexander on 12/04/2017.
 */

public abstract class BaseHistoryFragment extends BaseFragment implements SearchDetector.OnSearchListener {

    private static final int MODE_OBTAIN = 0;
    private static final int MODE_FILTER = 1;

    protected String searchQuery;
    protected WeakHandler weakHandler;
    protected List<TranslationModel> translations;

    protected final Runnable filterHistory = new Runnable() {
        @Override
        public void run() {
            List<TranslationModel> filteredTranslations = new ArrayList<>();
            String query = searchQuery.toLowerCase();

            for (TranslationModel translation: translations) {
                if (translation.getOriginalText().toLowerCase().contains(query) ||
                        translation.getTranslatedText().toLowerCase().contains(query)) {
                    filteredTranslations.add(translation);
                }
            }

            Message message = new Message();
            message.what = MODE_FILTER;
            message.obj = filteredTranslations;
            weakHandler.sendMessage(message);
        }
    };

    protected final Runnable obtainHistory = new Runnable() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = MODE_OBTAIN;
            message.obj = HistorySqlService.getAllTranslations();
            weakHandler.sendMessage(message);
        }
    };

    protected final Runnable obtainFavorites = new Runnable() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = MODE_OBTAIN;
            message.obj = HistorySqlService.findAllFavorites();
            weakHandler.sendMessage(message);
        }
    };

    @Override
    protected void onViewCreate() {
        super.onViewCreate();
        weakHandler = new WeakHandler(this);
    }

    private static class WeakHandler extends Handler {

        private WeakReference<BaseHistoryFragment> fragmentWeakReference;

        WeakHandler(@NonNull BaseHistoryFragment fragment) {
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!(msg.obj instanceof List)) return;
            BaseHistoryFragment fragment = fragmentWeakReference.get();

            if (fragment != null) {

                switch (msg.what) {
                    case MODE_FILTER:
                        fragment.onFilter((List<TranslationModel>) msg.obj);
                        break;
                    case MODE_OBTAIN:
                        List<TranslationModel> translations = (List<TranslationModel>) msg.obj;
                        fragment.setTranslations(translations);
                        fragment.onObtain(translations);
                        break;
                    default:
                        return;
                }
            }
        }
    }

    public void setTranslations(List<TranslationModel> translations) {
        this.translations = translations;
    }

    public abstract void onObtain(List<TranslationModel> translations);
    public abstract void onFilter(List<TranslationModel> translations);

    @Override
    public void onSearchReady(String query) {
        searchQuery = query;
        Thread t = new Thread(filterHistory);
        t.start();
    }
}
