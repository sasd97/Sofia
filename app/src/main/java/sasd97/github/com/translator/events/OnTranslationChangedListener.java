package sasd97.github.com.translator.events;

import sasd97.github.com.translator.models.TranslationModel;

/**
 * Created by alexander on 11/04/2017.
 */

public interface OnTranslationChangedListener {

    void onFragmentNeedToBeSwitched(int fragment);
    void onTranslationChanged(TranslationModel translation);
}
