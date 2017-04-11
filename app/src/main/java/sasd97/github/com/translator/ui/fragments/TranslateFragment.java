package sasd97.github.com.translator.ui.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
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

    private static final String TAG = TranslateFragment.class.getCanonicalName();
    private static final String TRANSLATION_ARG = "TRANSLATION_ARGUMENT";

    private Call<?> query;
    private Handler handler;

    private String[] fromLanguagesList;
    private SupportedLanguageModel targetLanguage;
    private SupportedLanguageModel destinationLanguage;
    private TranslationModel currentTranslation;
    private StopTypingDetector stopTypingDetector;

    @BindView(R.id.translate_action_favorite) ImageView favoritesImageView;
    @BindView(R.id.translate_target_language_spinner) Spinner fromLanguageSpinner;
    @BindView(R.id.translate_destination_language_spinner) Spinner toLanguageSpinner;
    @BindView(R.id.translate_input_edittext) MaterialEditText translateEditText;
    @BindView(R.id.translate_lanugage_holder_textview) TextView mainTranslationTitleTextView;
    @BindView(R.id.translate_primary_translation_textview) TextView mainTranslationTextView;
    @BindView(R.id.translate_scrollview) View translationsListView;
    @BindView(R.id.translate_alternative_translation_cardview) View alternativeVariantsCardView;

    @BindString(R.string.all_automatic_language) String automatic;
    @BindArray(R.array.all_languages) String[] array;

    private OnTranslationChangedListener listener;

    @Override
    protected int getLayout() {
        return R.layout.fragment_translate;
    }

    @Override
    protected boolean isButterKnifeEnabled() {
        return true;
    }

    public static TranslateFragment newInstance(OnTranslationChangedListener listener) {
        TranslateFragment translateFragment = new TranslateFragment();
        translateFragment.setTranslationChangedListener(listener);
        return translateFragment;
    }

    public static TranslateFragment newInstance(TranslationModel translation, OnTranslationChangedListener listener) {
        TranslateFragment translateFragment = new TranslateFragment();
        translateFragment.setTranslationChangedListener(listener);
        if (translation == null) return translateFragment;
        Bundle bundle = new Bundle();
        bundle.putParcelable(TRANSLATION_ARG, translation);
        translateFragment.setArguments(bundle);
        return translateFragment;
    }

    public void setTranslationChangedListener(OnTranslationChangedListener listener) {
        this.listener = listener;
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
                R.array.all_languages, android.R.layout.simple_spinner_item);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toLanguageSpinner.setAdapter(toAdapter);
        toLanguageSpinner.setOnItemSelectedListener(this);

        if (getArguments() != null) onArgsExists(getArguments());
    }

    private void onArgsExists(Bundle args) {
        TranslationModel translation = args.getParcelable(TRANSLATION_ARG);
        String[] langs = translation.getLanguage().split("-");

        targetLanguage = SupportedLanguageModel.fromString(langs[0]);
        destinationLanguage = SupportedLanguageModel.fromString(langs[1]);

        currentTranslation = translation;
        translateEditText.setText(translation.getOriginalText());
        handleTranslationResponse(translation);
    }

    private void showTranslationViews(boolean isShowingBoth) {
        if (translationsListView.getVisibility() == View.VISIBLE) {
            if (!isShowingBoth) hideView(alternativeVariantsCardView);
            return;
        }

        if (isShowingBoth) alternativeVariantsCardView.setVisibility(View.VISIBLE);
        else alternativeVariantsCardView.setVisibility(View.INVISIBLE);

        showView(translationsListView);
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

        int tempPosition = toLanguageSpinner.getSelectedItemPosition() + 1;
        toLanguageSpinner.setSelection(fromLanguageSpinner.getSelectedItemPosition() - 1);
        fromLanguageSpinner.setSelection(tempPosition);
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
                targetLanguage = SupportedLanguageModel.fromLongString(fromLanguagesList[i]);
                break;
            case R.id.translate_destination_language_spinner:
                destinationLanguage = SupportedLanguageModel.fromLongString(array[i]);
                break;
        }

        stopTypingDetector.notifyMadeNewOnceDelay(translateEditText.getEditableText());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void onStopTyping() {
        if (translateEditText.getText().toString().trim().equals("")) return;

        query = translate(translateEditText.getText().toString(),
                targetLanguage.getCortege(destinationLanguage),
                this);
    }

    private void changeFavoriteAction(TranslationModel translation) {
        if (translation.isFavorite()) favoritesImageView.setImageResource(R.drawable.ic_favorite_white_24dp);
        else favoritesImageView.setImageResource(R.drawable.ic_favorite_border_white_24dp);
    }

    private void handleTranslationResponse(TranslationModel translationModel) {
        mainTranslationTitleTextView.setText(destinationLanguage.name());
        mainTranslationTextView.setText(translationModel.getTranslatedText());

        currentTranslation = HistorySqlService.saveTranslation(translationModel);
        changeFavoriteAction(currentTranslation);
        listener.onTranslationChanged(translationModel);

        if (translateEditText.getText().toString().trim().contains(" ")) showTranslationViews(false);
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
        if (query != null && query.isExecuted()) query.cancel();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (query != null && query.isExecuted()) query.cancel();
    }
}
