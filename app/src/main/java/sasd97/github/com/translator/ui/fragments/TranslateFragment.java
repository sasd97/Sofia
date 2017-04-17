package sasd97.github.com.translator.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import sasd97.github.com.translator.R;
import sasd97.github.com.translator.events.OnTranslationChangedListener;
import sasd97.github.com.translator.http.HttpError;
import sasd97.github.com.translator.http.HttpResultListener;
import sasd97.github.com.translator.models.Dictionary.DefinitionDictionaryModel;
import sasd97.github.com.translator.models.Dictionary.DictionaryModel;
import sasd97.github.com.translator.models.Dictionary.TranslationDictionaryModel;
import sasd97.github.com.translator.models.TranslationModel;
import sasd97.github.com.translator.repositories.LanguageRepository;
import sasd97.github.com.translator.services.HistorySqlService;
import sasd97.github.com.translator.ui.adapters.AlternativeTranslationAdapter;
import sasd97.github.com.translator.ui.base.BaseFragment;
import sasd97.github.com.translator.utils.AnimationUtils;
import sasd97.github.com.translator.utils.ShareUtils;
import sasd97.github.com.translator.utils.SpinnerUtils;
import sasd97.github.com.translator.utils.watchers.ClearButtonAppearanceDetector;
import sasd97.github.com.translator.utils.watchers.StopTypingDetector;

import static sasd97.github.com.translator.constants.ViewConstants.ALTERNATIVE_TRANSLATION_VIEW_HEIGHT;
import static sasd97.github.com.translator.http.YandexAPIWrapper.lookup;
import static sasd97.github.com.translator.http.YandexAPIWrapper.translate;

public class TranslateFragment extends BaseFragment
        implements AdapterView.OnItemSelectedListener,
        HttpResultListener,
        StopTypingDetector.TypingListener,
        ClearButtonAppearanceDetector.ClearButtonAppearanceListener {

    //region variables

    private static final String TAG = TranslateFragment.class.getCanonicalName();
    private static final String TRANSLATION_ARG = "TRANSLATION_ARGUMENT";

    private Handler handler;
    private Call<?> activeQuery;

    private String[] targetLanguagesList;
    private TranslationModel currentTranslation;
    private StopTypingDetector stopTypingDetector;
    private OnTranslationChangedListener listener;
    private AlternativeTranslationAdapter alternativeTranslationAdapter;
    private LanguageRepository languageRepository = new LanguageRepository();

    @BindArray(R.array.all_languages) String[] allAvailableLanguagesList;
    @BindString(R.string.all_automatic_language) String automaticLanguageRecognitionString;

    @BindView(R.id.translate_action_favorite) ImageView favoritesActionImageView;
    @BindView(R.id.translate_target_language_spinner) Spinner targetLanguageSpinner;
    @BindView(R.id.translate_destination_language_spinner) Spinner destinationLanguageSpinner;
    @BindView(R.id.translate_input_edittext) EditText translateInputEditText;
    @BindView(R.id.translate_symbol_counter_textview) TextView symbolCounterTextView;
    @BindView(R.id.translate_lanugage_holder_textview) TextView translationLanguageHolderTitleTextView;
    @BindView(R.id.translate_primary_translation_textview) TextView primaryTranslationTextView;
    @BindView(R.id.translate_scrollview) View translateScrollView;
    @BindView(R.id.translate_clear_button) View clearTranslateView;
    @BindView(R.id.translate_alternative_translation_cardview) View alternativeTranslationCardView;
    @BindView(R.id.translate_alternative_translation_recyclerview) RecyclerView alternativeTranslationRecyclerView;

    //endregion

    //region fabric methods

    @Override
    protected int getLayout() {
        return R.layout.fragment_translate;
    }

    public static TranslateFragment newInstance(@NonNull OnTranslationChangedListener listener) {
        TranslateFragment translateFragment = new TranslateFragment();
        translateFragment.setTranslationChangedListener(listener);
        return translateFragment;
    }

    public static TranslateFragment newInstance(TranslationModel translation,
                                                OnTranslationChangedListener listener) {
        TranslateFragment translateFragment = new TranslateFragment();
        translateFragment.setTranslationChangedListener(listener);

        if (translation != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(TRANSLATION_ARG, translation);
            translateFragment.setArguments(bundle);
        }

        return translateFragment;
    }

    //endregion

    //region setters & getters

    public void setTranslationChangedListener(OnTranslationChangedListener listener) {
        this.listener = listener;
    }

    //endregion

    //region initialization

    @Override
    protected void onViewCreated(Bundle state) {
        super.onViewCreated(state);

        handler = new Handler();
        stopTypingDetector = new StopTypingDetector(handler, this);
        translateInputEditText.addTextChangedListener(stopTypingDetector);
        translateInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                symbolCounterTextView.setText(getString(R.string.translate_format_counter, editable.length()));
            }
        });
        translateInputEditText.addTextChangedListener(new ClearButtonAppearanceDetector(this));

        onLanguagesInit();

        ArrayAdapter<CharSequence> fromAdapter = new ArrayAdapter<CharSequence>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, targetLanguagesList);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        targetLanguageSpinner.setAdapter(fromAdapter);
        targetLanguageSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> toAdapter = new ArrayAdapter<CharSequence>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, allAvailableLanguagesList);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationLanguageSpinner.setAdapter(toAdapter);
        destinationLanguageSpinner.setOnItemSelectedListener(this);

        alternativeTranslationAdapter = new AlternativeTranslationAdapter();
        alternativeTranslationRecyclerView.setHasFixedSize(true);
        alternativeTranslationRecyclerView.setNestedScrollingEnabled(false);
        alternativeTranslationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        alternativeTranslationRecyclerView.setAdapter(alternativeTranslationAdapter);

        if (getArguments() != null) onArgsExists(getArguments());
        else onPrefsInit();
    }

    private void onLanguagesInit() {
        targetLanguagesList = new String[allAvailableLanguagesList.length + 1];
        targetLanguagesList[0] = automaticLanguageRecognitionString;
        System.arraycopy(allAvailableLanguagesList, 0, targetLanguagesList, 1, allAvailableLanguagesList.length);
    }

    private void onPrefsInit() {
        if (languageRepository.restoreTargetLanguage())
            SpinnerUtils.setSpinnerSelection(
                    targetLanguageSpinner,
                    languageRepository.obtainTargetIndex(targetLanguagesList)
            );

        if (languageRepository.restoreDestinationLanguage())
            SpinnerUtils.setSpinnerSelection(
                    destinationLanguageSpinner,
                    languageRepository.obtainDestinationIndex(allAvailableLanguagesList)
            );
    }

    private void onArgsExists(Bundle args) {
        currentTranslation = args.getParcelable(TRANSLATION_ARG);
        languageRepository = LanguageRepository.fromTranslation(currentTranslation);

        stopTypingDetector.setDetectorActive(false);

        SpinnerUtils.setSpinnerSelection(
                targetLanguageSpinner,
                languageRepository.obtainTargetIndex(targetLanguagesList)
        );

        SpinnerUtils.setSpinnerSelection(
                destinationLanguageSpinner,
                languageRepository.obtainDestinationIndex(allAvailableLanguagesList)
        );

        translateInputEditText.setText(currentTranslation.getOriginalText());
        handleTranslationResponse(currentTranslation);

        stopTypingDetector.setDetectorActive(true);
    }

    //endregion

    //region views animation

    private void showTranslationViews(boolean isShowingBoth) {
        if (translateScrollView.getVisibility() == View.VISIBLE) {
            if (!isShowingBoth) AnimationUtils.fadeIn(alternativeTranslationCardView);
            return;
        }

        if (isShowingBoth) alternativeTranslationCardView.setVisibility(View.VISIBLE);
        else alternativeTranslationCardView.setVisibility(View.INVISIBLE);

        AnimationUtils.fadeIn(translateScrollView);
    }

    //endregion

    //region onClick

    @OnClick(R.id.translate_swap_languages)
    public void onSwapLanguagesClick(View v) {
        if (!languageRepository.swapLanguages()) return;
        int tempPosition = destinationLanguageSpinner.getSelectedItemPosition() + 1;
        destinationLanguageSpinner.setSelection(targetLanguageSpinner.getSelectedItemPosition() - 1);
        targetLanguageSpinner.setSelection(tempPosition);
    }

    @OnClick(R.id.translate_action_favorite)
    public void onFavoriteClick(View v) {
        currentTranslation.switchFavorite();
        changeFavoriteAction(currentTranslation);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                HistorySqlService.update(currentTranslation);
            }
        });
        t.start();
    }

    @OnClick(R.id.translate_action_copy)
    public void onCopyClick(View v) {
        ShareUtils.copyToClipboard(currentTranslation.getTranslatedText());

        Toast
                .makeText(getContext(), R.string.translate_toast_text_was_copied, Toast.LENGTH_SHORT)
                .show();
    }

    @OnClick(R.id.translate_action_share)
    public void onShareClick(View v) {
        startActivity(ShareUtils.shareToAnotherApp(currentTranslation.getTranslatedText()));
    }

    @OnClick(R.id.translate_clear_button)
    public void onClearClick(View v) {
        translateInputEditText.setText("");
    }

    //endregion

    //region service callbacks

    @Override
    public void onStopTyping() {
        if (TextUtils.isEmpty(translateInputEditText.getText().toString().trim())) return;

        activeQuery = translate(
                translateInputEditText.getText().toString(),
                languageRepository.obtainCortege(),
                this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.translate_target_language_spinner:
                if (!SpinnerUtils.isSelectedByUser(targetLanguageSpinner)) return;
                languageRepository
                        .setTargetFromLongName(targetLanguagesList[i])
                        .saveTargetLanguage();
                break;
            case R.id.translate_destination_language_spinner:
                if (!SpinnerUtils.isSelectedByUser(destinationLanguageSpinner)) return;
                languageRepository
                        .setDestinationFromLongName(allAvailableLanguagesList[i])
                        .saveDestinationLanguage();
                break;
        }

        stopTypingDetector.notifyDataChanged(translateInputEditText.getEditableText());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onShowCloseButton() {
        clearTranslateView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideCloseButton() {
        clearTranslateView.setVisibility(View.GONE);
    }

    @Override
    public boolean isShown() {
        return clearTranslateView.getVisibility() == View.VISIBLE;
    }

    //endregion

    // region http results & handlers

    @Override
    public <T> void onHttpSuccess(T result) {
        if (result == null) return;

        if (result instanceof TranslationModel) {
            handleTranslationResponse((TranslationModel) result);
            return;
        }

        if (result instanceof DictionaryModel) {
            handleDictionaryResponse((DictionaryModel) result);
        }
    }

    @Override
    public void onHttpError(HttpError error) {
        Log.d(TAG, "There was an error while obtain request");
    }

    @Override
    public void onHttpCanceled() {
        Log.d(TAG, "Response was canceled");
    }

    private void handleTranslationResponse(TranslationModel translationModel) {
        translationLanguageHolderTitleTextView.setText(languageRepository.getDestinationLanguage().name());
        primaryTranslationTextView.setText(translationModel.getTranslatedText());

        currentTranslation = HistorySqlService.saveTranslation(translationModel);
        changeFavoriteAction(currentTranslation);
        listener.onTranslationChanged(translationModel);

        if (translateInputEditText.getText().toString().trim().contains(" ")) {
            showTranslationViews(false);
        } else {
            showTranslationViews(true);
            lookup(translationModel.getOriginalText(), translationModel.getLanguage(), this);
        }
    }

    private void handleDictionaryResponse(DictionaryModel dictionaryModel) {
        List<DefinitionDictionaryModel> definitions = dictionaryModel.getDefinition();
        if (definitions == null || definitions.isEmpty()) return;
        Log.d(TAG, definitions + "");
        List<TranslationDictionaryModel> translations = new ArrayList<>();

        for (DefinitionDictionaryModel definition: definitions) {
            translations.addAll(definition.getTranslation());
        }

        alternativeTranslationAdapter.clear();
        alternativeTranslationAdapter.addTranslations(translations);
        recalculateAlternativeTranslationRVHeight();
    }

    //endregion

    //region lifecycle

    @Override
    public void onPause() {
        super.onPause();
        if (activeQuery != null && activeQuery.isExecuted()) activeQuery.cancel();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (activeQuery != null && activeQuery.isExecuted()) activeQuery.cancel();
    }

    //endregion

    //region utils

    private void changeFavoriteAction(TranslationModel translation) {
        if (translation.isFavorite()) favoritesActionImageView.setImageResource(R.drawable.ic_favorite_white_24dp);
        else favoritesActionImageView.setImageResource(R.drawable.ic_favorite_border_white_24dp);
    }

    private void recalculateAlternativeTranslationRVHeight() {
        int count = alternativeTranslationAdapter.getItemCount();

        ViewGroup.LayoutParams params = alternativeTranslationRecyclerView.getLayoutParams();
        params.height = count * ALTERNATIVE_TRANSLATION_VIEW_HEIGHT;
        alternativeTranslationRecyclerView.setLayoutParams(params);
    }

    //endregion
}
