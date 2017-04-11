package sasd97.github.com.translator.events;

import sasd97.github.com.translator.models.TranslationModel;

/**
 * Created by alexander on 11/04/2017.
 */

public interface OnTranslationChangedListener {

    int TRANSLATE_FRAGMENT = 0;
    int FAVORITES_FRAGMENT = 1;
    int HISTORY_FRAGMENT = 2;

    void onFragmentNeedToBeSwitched(int fragment);
    void onTranslationChanged(TranslationModel translation);
}
