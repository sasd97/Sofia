package sasd97.github.com.translator.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import sasd97.github.com.translator.R;
import sasd97.github.com.translator.http.HttpError;
import sasd97.github.com.translator.http.HttpResultListener;
import sasd97.github.com.translator.models.SupportedLanguageModel;
import sasd97.github.com.translator.models.YandexTranslationModel;
import sasd97.github.com.translator.ui.base.BaseFragment;
import sasd97.github.com.translator.utils.StopTypingDetector;

import static sasd97.github.com.translator.http.YandexAPIWrapper.translate;

public class TranslateFragment extends BaseFragment
        implements AdapterView.OnItemSelectedListener,
        HttpResultListener<YandexTranslationModel>,
        StopTypingDetector.TypingListener {

    private String TAG = TranslateFragment.class.getCanonicalName();

    private Handler handler;

    private String[] fromLanguagesList;
    private SupportedLanguageModel targetLanguage;
    private SupportedLanguageModel destinationLanguage;
    private StopTypingDetector stopTypingDetector;

    @BindView(R.id.target_lang_sprinner) Spinner fromLanguageSpinner;
    @BindView(R.id.destination_lang_spinner) Spinner toLanguageSpinner;
    @BindView(R.id.translate_edittext) MaterialEditText translateEditText;
    @BindView(R.id.main_translation_title_holder) TextView mainTranslationTitleTextView;
    @BindView(R.id.main_translation_textview) TextView mainTranslationTextView;

    @BindString(R.string.automatic_language) String automatic;
    @BindArray(R.array.languages) String[] array;

    @Override
    protected int getLayout() {
        return R.layout.fragment_translate;
    }

    @Override
    protected boolean isButterKnifeEnabled() {
        return true;
    }

    public static TranslateFragment newInstance() {
        return new TranslateFragment();
    }

    @Override
    protected void onViewCreated(Bundle state) {
        super.onViewCreated(state);
        handler = new Handler();

        stopTypingDetector = new StopTypingDetector(handler, this);
        translateEditText.addTextChangedListener(stopTypingDetector);

        fromLanguagesList = new String[array.length + 1];
        fromLanguagesList[0] = automatic;
        System.arraycopy(array, 0, fromLanguagesList, 1, array.length);

        ArrayAdapter<CharSequence> fromAdapter = new ArrayAdapter<CharSequence>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, fromLanguagesList);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromLanguageSpinner.setAdapter(fromAdapter);
        fromLanguageSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> toAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.languages, android.R.layout.simple_spinner_item);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toLanguageSpinner.setAdapter(toAdapter);
        toLanguageSpinner.setOnItemSelectedListener(this);
    }

    @OnClick(R.id.swap_frame_layout)
    public void onSwapLanguagesClick(View v) {
        if (targetLanguage == SupportedLanguageModel.AUTOMATIC) return;

        SupportedLanguageModel t = targetLanguage;
        targetLanguage = destinationLanguage;
        destinationLanguage = t;

        int tempPosition = toLanguageSpinner.getSelectedItemPosition() + 1;
        toLanguageSpinner.setSelection(fromLanguageSpinner.getSelectedItemPosition() - 1);
        fromLanguageSpinner.setSelection(tempPosition);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.target_lang_sprinner:
                targetLanguage = SupportedLanguageModel.fromLongString(fromLanguagesList[i]);
                break;
            case R.id.destination_lang_spinner:
                destinationLanguage = SupportedLanguageModel.fromLongString(array[i]);
                break;
        }

        stopTypingDetector.notifyMadeNewOnceDelay(translateEditText.getEditableText());
    }

    @Override
    public void onStopTyping() {
        translate(translateEditText.getText().toString(),
                targetLanguage.getCortege(destinationLanguage),
                this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void onHttpSuccess(YandexTranslationModel result) {
        if (result == null) return;

        mainTranslationTitleTextView.setText(destinationLanguage.name());
        mainTranslationTextView.setText(result.getText().get(0));
    }

    @Override
    public void onHttpError(HttpError error) {

    }

    @Override
    public void onHttpCanceled() {

    }
}
