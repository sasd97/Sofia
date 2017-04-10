package sasd97.github.com.translator.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import sasd97.github.com.translator.R;
import sasd97.github.com.translator.http.HttpError;
import sasd97.github.com.translator.http.HttpResultListener;
import sasd97.github.com.translator.models.SupportedLanguageModel;
import sasd97.github.com.translator.models.TranslationModel;
import sasd97.github.com.translator.models.YandexTranslationModel;
import sasd97.github.com.translator.services.HistorySqlService;
import sasd97.github.com.translator.ui.base.BaseFragment;
import sasd97.github.com.translator.utils.StopTypingDetector;

import static sasd97.github.com.translator.http.YandexAPIWrapper.translate;

public class TranslateFragment extends BaseFragment
        implements AdapterView.OnItemSelectedListener,
        HttpResultListener,
        StopTypingDetector.TypingListener {

    private String TAG = TranslateFragment.class.getCanonicalName();

    private Call<?> query;
    private Handler handler;

    private String[] fromLanguagesList;
    private SupportedLanguageModel targetLanguage;
    private SupportedLanguageModel destinationLanguage;
    private TranslationModel currentTranslation;
    private StopTypingDetector stopTypingDetector;

    @BindView(R.id.favorite_action) ImageView favoritesImageView;
    @BindView(R.id.target_lang_sprinner) Spinner fromLanguageSpinner;
    @BindView(R.id.destination_lang_spinner) Spinner toLanguageSpinner;
    @BindView(R.id.translate_edittext) MaterialEditText translateEditText;
    @BindView(R.id.main_translation_title_holder) TextView mainTranslationTitleTextView;
    @BindView(R.id.main_translation_textview) TextView mainTranslationTextView;
    @BindView(R.id.translations_list) View translationsListView;
    @BindView(R.id.alternative_variants_holder) View alternativeVariantsCardView;

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

    @OnClick(R.id.favorite_action)
    public void onFavoriteClick(View v) {
        currentTranslation.switchFavorite();
        changeFavoriteAction(currentTranslation);
        HistorySqlService.update(currentTranslation);
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
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void onStopTyping() {
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
