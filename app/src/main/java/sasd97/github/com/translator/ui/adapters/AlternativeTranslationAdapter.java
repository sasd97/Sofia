package sasd97.github.com.translator.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import sasd97.github.com.translator.R;
import sasd97.github.com.translator.models.Dictionary.TextDictionaryModel;
import sasd97.github.com.translator.models.Dictionary.TranslationDictionaryModel;
import sasd97.github.com.translator.models.TranslationModel;
import sasd97.github.com.translator.ui.base.BaseViewHolder;

/**
 * Created by alexander on 13/04/2017.
 */

public class AlternativeTranslationAdapter extends RecyclerView.Adapter<AlternativeTranslationAdapter.AlternativeTranslationViewHolder> {

    private List<TranslationDictionaryModel> translations;

    public AlternativeTranslationAdapter() {
        this.translations = new ArrayList<>();
    }

    public AlternativeTranslationAdapter(@NonNull List<TranslationDictionaryModel> translations) {
        this.translations = translations;
    }

    public class AlternativeTranslationViewHolder extends BaseViewHolder {

        @BindView(R.id.item_alternative_translation_translation_text) TextView alternativeTranslationTextView;
        @BindView(R.id.item_alternative_translation_part_of_speech) TextView alternativeTranslationPartOfSpeech;

        public AlternativeTranslationViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupViews() {

        }
    }

    @Override
    public AlternativeTranslationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.item_alternative_translation, parent, false);
        return new AlternativeTranslationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AlternativeTranslationViewHolder holder, int position) {
        TranslationDictionaryModel translation = translations.get(position);
        String partOfSpeech = String.format("(%1$s)", translation.getPartOfSpeech());
        StringBuilder translationText = new StringBuilder(translation.getText());

        if (translation.getSynonyms() != null) {
            for (TextDictionaryModel synonym : translation.getSynonyms()) {
                translationText.append(", ").append(synonym.getText());
            }
        }

        holder.alternativeTranslationTextView.setText(translationText.toString());
        holder.alternativeTranslationPartOfSpeech.setText(partOfSpeech);
    }

    public void addTranslations(@NonNull Collection<TranslationDictionaryModel> translations) {
        int oldLength = getItemCount();
        this.translations.addAll(translations);
        notifyItemRangeInserted(oldLength, getItemCount());
    }

    public void clear() {
        int length = getItemCount();
        this.translations.clear();
        notifyItemRangeRemoved(0, length);
    }

    @Override
    public int getItemCount() {
        return translations.size();
    }
}
