package sasd97.github.com.translator.models;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import sasd97.github.com.translator.utils.DateFormatter;

/**
 * Created by alexander on 10/04/2017.
 */

public class TranslationModel {

    private int id = -1;
    private String language;
    private String originalText;
    private String translatedText;
    private boolean isFavorite;
    private Date creationDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void setFavorite(int favorite) {
        isFavorite = (favorite == 1);
    }

    public void switchFavorite() {
        this.isFavorite = !this.isFavorite;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void defaultCreationDate() {
        Calendar calendar = Calendar.getInstance();
        this.creationDate = calendar.getTime();
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = DateFormatter.fromString(creationDate);
    }

    @Override
    public String toString() {
        return "TranslationModel{" +
                "id='" + id + '\'' +
                "language='" + language + '\'' +
                ", originalText='" + originalText + '\'' +
                ", translatedText='" + translatedText + '\'' +
                ", isFavorite=" + isFavorite +
                ", creationDate=" + creationDate +
                '}';
    }
}
