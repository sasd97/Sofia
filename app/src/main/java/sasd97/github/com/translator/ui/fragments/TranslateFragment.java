package sasd97.github.com.translator.ui.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import sasd97.github.com.translator.R;
import sasd97.github.com.translator.events.OnTranslationChangedListener;
import sasd97.github.com.translator.http.HttpError;
import sasd97.github.com.translator.http.HttpResultListener;
import sasd97.github.com.translator.models.SupportedLanguageModel;
import sasd97.github.com.translator.models.TranslationModel;
import sasd97.github.com.translator.services.HistorySqlService;
import sasd97.github.com.translator.ui.base.BaseFragment;
import sasd97.github.com.translator.utils.StopTypingDetector;

import static sasd97.github.com.translator.http.YandexAPIWrapper.translate;

public class TranslateFragment extends BaseFragment
        implements AdapterView.OnItemSelectedListener,
        HttpResultListener,
        StopTypingDetector.TypingListener,
        TextToSpeech.OnInitListener {

    //region variables

    private static final String TAG = TranslateFragment.class.getCanonicalName();
    private static final String TRANSLATION_ARG = "TRANSLATION_ARGUMENT";

    private Handler handler;
    private Call<?> activeQuery;

    private String[] targetLanguagesList;
    private TranslationModel currentTranslation;
    private StopTypingDetector stopTypingDetector;
    private SupportedLanguageModel targetLanguage;
    private SupportedLanguageModel destinationLanguage;

    private OnTranslationChangedListener listener;

    @BindArray(R.array.all_languages) String[] allAvailableLanguagesList;
    @BindString(R.string.all_automatic_language) String automaticLangugageRecognitionString;

    @BindView(R.id.translate_action_favorite) ImageView favoritesActionImageView;
    @BindView(R.id.translate_target_language_spinner) Spinner targetLanguageSpinner;
    @BindView(R.id.translate_destination_language_spinner) Spinner destinationLanguageSpinner;
    @BindView(R.id.translate_input_edittext) MaterialEditText translateInputEditText;
    @BindView(R.id.translate_lanugage_holder_textview) TextView translationLanguageHolderTitleTextView;
    @BindView(R.id.translate_primary_translation_textview) TextView primaryTranslationTextView;
    @BindView(R.id.translate_scrollview) View translateScrollView;
    @BindView(R.id.translate_alternative_translation_cardview) View alternativeTranslationCardView;

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

    //todo: stops here!!!

    public void setTranslationChangedListener(OnTranslationChangedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onViewCreated(Bundle state) {
        super.onViewCreated(state);
        handler = new Handler();

        stopTypingDetector = new StopTypingDetector(handler, this);
        translateInputEditText.addTextChangedListener(stopTypingDetector);

        targetLanguagesList = new String[allAvailableLanguagesList.length + 1];
        targetLanguagesList[0] = automaticLangugageRecognitionString;
        System.arraycopy(allAvailableLanguagesList, 0, targetLanguagesList, 1, allAvailableLanguagesList.length);

        ArrayAdapter<CharSequence> fromAdapter = new ArrayAdapter<CharSequence>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, targetLanguagesList);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        targetLanguageSpinner.setAdapter(fromAdapter);
        targetLanguageSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> toAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.all_languages, android.R.layout.simple_spinner_item);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationLanguageSpinner.setAdapter(toAdapter);
        destinationLanguageSpinner.setOnItemSelectedListener(this);

        if (getArguments() != null) onArgsExists(getArguments());
    }

    private void onArgsExists(Bundle args) {
        TranslationModel translation = args.getParcelable(TRANSLATION_ARG);
        String[] langs = translation.getLanguage().split("-");

        targetLanguage = SupportedLanguageModel.fromString(langs[0]);
        destinationLanguage = SupportedLanguageModel.fromString(langs[1]);

        currentTranslation = translation;
        translateInputEditText.setText(translation.getOriginalText());
        handleTranslationResponse(translation);
    }

    private void showTranslationViews(boolean isShowingBoth) {
        if (translateScrollView.getVisibility() == View.VISIBLE) {
            if (!isShowingBoth) hideView(alternativeTranslationCardView);
            return;
        }

        if (isShowingBoth) alternativeTranslationCardView.setVisibility(View.VISIBLE);
        else alternativeTranslationCardView.setVisibility(View.INVISIBLE);

        showView(translateScrollView);
    }

    private void playTranslationAnimations(final View v,
                                           float alphaStart, float alphaFinish,
                                           float startY, float finishY,
                                           long duration, final int finalVisibility) {
        //todo: maybe it may to re-use
        AnimationSet set = new AnimationSet(true);
        set.setInterpolator(new AccelerateDecelerateInterpolator());

        Animation alpha = new AlphaAnimation(alphaStart, alphaFinish);
        alpha.setDuration(duration);

        Animation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, startY, Animation.RELATIVE_TO_SELF, finishY
        );
        translate.setDuration(duration);

        set.addAnimation(alpha);
        set.addAnimation(translate);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(finalVisibility);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        v.startAnimation(set);
    }

    private void hideView(final View view) {
        playTranslationAnimations(view, 1.0f, 0.0f, 0.0f, 0.0f, 500L, View.INVISIBLE);
    }

    private void showView(final View view) {
        playTranslationAnimations(view, 0.0f, 1.0f, 1.0f, 0.0f, 500L, View.VISIBLE);
    }

    @Override
    public void onInit(int i) {
    }

    @OnClick(R.id.translate_swap_languages)
    public void onSwapLanguagesClick(View v) {
        if (targetLanguage == SupportedLanguageModel.AUTOMATIC) return;

        SupportedLanguageModel t = targetLanguage;
        targetLanguage = destinationLanguage;
        destinationLanguage = t;

        int tempPosition = destinationLanguageSpinner.getSelectedItemPosition() + 1;
        destinationLanguageSpinner.setSelection(targetLanguageSpinner.getSelectedItemPosition() - 1);
        targetLanguageSpinner.setSelection(tempPosition);
    }

    @OnClick(R.id.translate_action_favorite)
    public void onFavoriteClick(View v) {
        currentTranslation.switchFavorite();
        changeFavoriteAction(currentTranslation);
        HistorySqlService.update(currentTranslation);
    }

    @OnClick(R.id.translate_action_copy)
    public void onCopyClick(View v) {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("sofia.translator", currentTranslation.getTranslatedText());
        clipboard.setPrimaryClip(clip);

        Toast
                .makeText(getContext(), R.string.translate_toast_text_was_copied, Toast.LENGTH_SHORT)
                .show();
    }

    @OnClick(R.id.translate_action_share)
    public void onShareClick(View v) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, currentTranslation.getTranslatedText());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.translate_action_share_via)));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.translate_target_language_spinner:
                targetLanguage = SupportedLanguageModel.fromLongString(targetLanguagesList[i]);
                break;
            case R.id.translate_destination_language_spinner:
                destinationLanguage = SupportedLanguageModel.fromLongString(allAvailableLanguagesList[i]);
                break;
        }

        stopTypingDetector.notifyDataChanged(translateInputEditText.getEditableText());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onStopTyping() {
        if (translateInputEditText.getText().toString().trim().equals("")) return;

        activeQuery = translate(translateInputEditText.getText().toString(),
                targetLanguage.getCortege(destinationLanguage),
                this);
    }

    private void changeFavoriteAction(TranslationModel translation) {
        if (translation.isFavorite())
            favoritesActionImageView.setImageResource(R.drawable.ic_favorite_white_24dp);
        else favoritesActionImageView.setImageResource(R.drawable.ic_favorite_border_white_24dp);
    }

    private void handleTranslationResponse(TranslationModel translationModel) {
        translationLanguageHolderTitleTextView.setText(destinationLanguage.name());
        primaryTranslationTextView.setText(translationModel.getTranslatedText());

        currentTranslation = HistorySqlService.saveTranslation(translationModel);
        changeFavoriteAction(currentTranslation);
        listener.onTranslationChanged(translationModel);

        if (translateInputEditText.getText().toString().trim().contains(" "))
            showTranslationViews(false);
        else showTranslationViews(true);
    }

    private void handleDictionaryResponse() {

    }

    @Override
    public <T> void onHttpSuccess(T result) {
        if (result == null) return;

        if (result instanceof TranslationModel) {
            handleTranslationResponse((TranslationModel) result);
            return;
        }
    }

    @Override
    public void onHttpError(HttpError error) {

    }

    @Override
    public void onHttpCanceled() {
    }

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
}
