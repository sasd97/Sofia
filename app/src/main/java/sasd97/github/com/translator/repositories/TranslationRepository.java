package sasd97.github.com.translator.repositories;

import android.os.Parcel;
import android.os.Parcelable;

import sasd97.github.com.translator.models.Dictionary.DictionaryModel;
import sasd97.github.com.translator.models.TranslationModel;

/**
 * Created by alexander on 17/04/2017.
 */

public class TranslationRepository implements Parcelable {

    private TranslationModel translation;
    private DictionaryModel dictionary;

    public TranslationRepository() {
    }

    protected TranslationRepository(Parcel in) {
        translation = in.readParcelable(TranslationModel.class.getClassLoader());
        dictionary = in.readParcelable(DictionaryModel.class.getClassLoader());
    }

    public static final Creator<TranslationRepository> CREATOR = new Creator<TranslationRepository>() {
        @Override
        public TranslationRepository createFromParcel(Parcel in) {
            return new TranslationRepository(in);
        }

        @Override
        public TranslationRepository[] newArray(int size) {
            return new TranslationRepository[size];
        }
    };

    public TranslationModel getTranslation() {
        return translation;
    }

    public void setTranslation(TranslationModel translation) {
        this.translation = translation;
        this.dictionary = null;
    }

    public DictionaryModel getDictionary() {
        return dictionary;
    }

    public void setDictionary(DictionaryModel dictionary) {
        this.dictionary = dictionary;
    }

    public void set(TranslationModel translation, DictionaryModel dictionary) {
        this.translation = translation;
        this.dictionary = dictionary;
    }

    public boolean isEmpty() {
        return translation == null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(translation, flags);
        dest.writeParcelable(dictionary, flags);
    }

    @Override
    public String toString() {
        return "TranslationRepository{" +
                "translation=" + translation +
                ", dictionary=" + dictionary +
                '}';
    }
}
