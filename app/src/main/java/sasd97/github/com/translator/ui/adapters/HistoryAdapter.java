package sasd97.github.com.translator.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import sasd97.github.com.translator.R;
import sasd97.github.com.translator.models.TranslationModel;
import sasd97.github.com.translator.services.HistorySqlService;
import sasd97.github.com.translator.ui.base.BaseViewHolder;

/**
 * Created by alexander on 10/04/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private static final String TAG = HistoryAdapter.class.getCanonicalName();

    public interface OnItemSelectedListener {
        void onSelect(TranslationModel translation, int position);
    }

    public interface HistoryInteractionListener {
        void onRemoveFavorite(int position, TranslationModel translation);
    }

    private final int yandexTextColor = Color.parseColor("#44FFCC00");

    private String searchQuery;
    private List<TranslationModel> translations;

    private OnItemSelectedListener itemSelectedListener;
    private HistoryInteractionListener historyInteractionListener;

    public HistoryAdapter() {
        translations = new ArrayList<>();
    }

    public HistoryAdapter(@NonNull List<TranslationModel> translations) {
        this.translations = new ArrayList<>(translations);
    }

    public void setItemSelectedListener(OnItemSelectedListener itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
    }

    public void setHistoryInteractionListener(HistoryInteractionListener historyInteractionListener) {
        this.historyInteractionListener = historyInteractionListener;
    }

    public class HistoryViewHolder extends BaseViewHolder implements View.OnClickListener {

        @BindView(R.id.item_history_action_favorite) ImageView favoriteImageView;
        @BindView(R.id.item_history_original_textview) TextView originalTextView;
        @BindView(R.id.item_history_translated_text) TextView translatedTextView;

        public HistoryViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupViews() {
            favoriteImageView.setOnClickListener(this);
        }

        @OnClick(R.id.item_history_relative_layout)
        public void onCardClick(View v) {
            if (itemSelectedListener == null) return;
            itemSelectedListener.onSelect(translations.get(getAdapterPosition()), getAdapterPosition());
        }

        @Override
        public void onClick(View view) {
            TranslationModel translation = translations.get(getAdapterPosition());
            translation.switchFavorite();
            changeFavoriteIcon(translation);

            HistorySqlService.update(translation);

            if (historyInteractionListener == null) return;
            if (!translation.isFavorite())
                historyInteractionListener.onRemoveFavorite(getAdapterPosition(), translation);
        }

        private Spannable highlightFoundedText(String searchQuery, String highlightedText, int background) {
            Spannable spannable = Spannable.Factory.getInstance().newSpannable(highlightedText);
            if (TextUtils.isEmpty(searchQuery)) return spannable;

            String comparable = highlightedText.toLowerCase();
            String searched = searchQuery.toLowerCase();

            if (!comparable.contains(searched)) return spannable;

            int startIndex = comparable.indexOf(searched);
            int finishIndex = startIndex + searched.length();
            spannable.setSpan(new BackgroundColorSpan(background), startIndex, finishIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            return spannable;
        }

        private void changeFavoriteIcon(TranslationModel translation) {
            if (translation.isFavorite()) favoriteImageView.setImageResource(R.drawable.ic_favorite_black_24dp);
            else favoriteImageView.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        TranslationModel translation = translations.get(position);

        holder.originalTextView.setText(holder.highlightFoundedText(searchQuery, translation.getOriginalText(), yandexTextColor));
        holder.translatedTextView.setText(holder.highlightFoundedText(searchQuery, translation.getTranslatedText(), yandexTextColor));

        if (translation.isFavorite()) holder.favoriteImageView.setImageResource(R.drawable.ic_favorite_black_24dp);
        else holder.favoriteImageView.setImageResource(R.drawable.ic_favorite_border_black_24dp);
    }

    public TranslationModel get(int position) {
        return translations.get(position);
    }

    public void filterHistories(List<TranslationModel> translations, String searchQuery) {
        this.searchQuery = searchQuery;
        this.translations = new ArrayList<>(translations);
        notifyDataSetChanged();
    }

    public void addHistories(List<TranslationModel> translations) {
        int size = this.translations.size();
        this.translations.addAll(translations);
        notifyItemRangeInserted(size, this.translations.size());
    }

    public void removeHistory(int position) {
        translations.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAllHistories() {
        int oldLength = translations.size();
        translations.clear();
        notifyItemRangeRemoved(0, oldLength);
    }

    @Override
    public int getItemCount() {
        return translations.size();
    }
}
