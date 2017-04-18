package sasd97.github.com.translator.models.Dictionary;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alexander on 13/04/2017.
 */

public class TextDictionaryModel implements Parcelable {

    @SerializedName("text")
    @Expose
    private String text;

    public TextDictionaryModel() {

    }

    protected TextDictionaryModel(Parcel in) {
        text = in.readString();
    }

    public static final Creator<TextDictionaryModel> CREATOR = new Creator<TextDictionaryModel>() {
        @Override
        public TextDictionaryModel createFromParcel(Parcel in) {
            return new TextDictionaryModel(in);
        }

        @Override
        public TextDictionaryModel[] newArray(int size) {
            return new TextDictionaryModel[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
    }

    @Override
    public String toString() {
        return "TextDictionaryModel{" +
                "text='" + text + '\'' +
                '}';
    }
}
