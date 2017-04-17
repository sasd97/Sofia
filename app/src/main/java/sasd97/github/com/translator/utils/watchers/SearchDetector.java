package sasd97.github.com.translator.utils.watchers;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by alexander on 12/04/2017.
 */

public class SearchDetector implements TextWatcher {

    public interface OnSearchListener {

        void onSearchReady(String query);
    }

    private OnSearchListener searchListener;

    public SearchDetector(@NonNull OnSearchListener searchListener) {
        this.searchListener = searchListener;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        searchListener.onSearchReady(editable.toString());
    }
}
