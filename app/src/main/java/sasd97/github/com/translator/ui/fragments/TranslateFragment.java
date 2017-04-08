package sasd97.github.com.translator.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

import static sasd97.github.com.translator.http.YandexAPIWrapper.translate;

public class TranslateFragment extends BaseFragment
        implements AdapterView.OnItemSelectedListener,
        HttpResultListener<YandexTranslationModel> {

    private String TAG = TranslateFragment.class.getCanonicalName();

    private SupportedLanguageModel targetLanguage;
    private SupportedLanguageModel destinationLanguage;

    @BindView(R.id.translate_edittext) EditText translateEditText;
    @BindView(R.id.target_lang_sprinner) Spinner fromLanguageSpinner;
    @BindView(R.id.destination_lang_spinner) Spinner toLanguageSpinner;

    @BindString(R.string.automatic_lanugage) String automatic;
    @BindArray(R.array.languages) String[] array;
    String[] fromLanguagesList;

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

    @OnClick(R.id.button)
    public void onClick(View view) {
        translate(translateEditText.getText().toString(),
                targetLanguage.getCortege(destinationLanguage),
                this);
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
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void onHttpSuccess(YandexTranslationModel result) {
        Toast.makeText(getContext(), result.getText().get(0), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHttpError(HttpError error) {

    }

    @Override
    public void onHttpCanceled() {

    }
}
