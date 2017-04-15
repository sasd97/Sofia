package sasd97.github.com.translator.utils;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by alexander on 15/04/2017.
 */

public class ClearButtonAppearanceDetector implements TextWatcher {

    public interface ClearButtonAppearanceListener {
        void onShowCloseButton();
        void onHideCloseButton();
        boolean isShown();
    }

    private ClearButtonAppearanceListener listener;

    public ClearButtonAppearanceDetector(@NonNull ClearButtonAppearanceListener listener) {
        this.listener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() <= 0) {
            if (listener.isShown()) listener.onHideCloseButton();
            return;
        }

        if (!listener.isShown()) listener.onShowCloseButton();
    }
}
