package sasd97.github.com.translator.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import sasd97.github.com.translator.utils.DateFormatter;

/**
 * Created by alexander on 10/04/2017.
 */

public class TranslationModel implements Parcelable {

    private int id = -1;
    private String language;
    private String originalText;
    private String translatedText;
    private boolean isFavorite;
    private Date creationDate;

    public TranslationModel() {
    }

    protected TranslationModel(Parcel in) {
        id = in.readInt();
        language = in.readString();
        originalText = in.readString();
        translatedText = in.readString();
        isFavorite = in.readByte() != 0;
    }

    public static final Creator<TranslationModel> CREATOR = new Creator<TranslationModel>() {
        @Override
        public TranslationModel createFromParcel(Parcel in) {
            return new TranslationModel(in);
        }

        @Override
        public TranslationModel[] newArray(int size) {
            return new TranslationModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(language);
        parcel.writeString(originalText);
        parcel.writeString(translatedText);
        parcel.writeByte((byte) (isFavorite ? 1 : 0));
    }
}
