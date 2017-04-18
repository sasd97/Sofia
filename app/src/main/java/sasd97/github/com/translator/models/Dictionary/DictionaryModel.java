package sasd97.github.com.translator.models.Dictionary;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alexander on 13/04/2017.
 */

public class DictionaryModel implements Parcelable {

    @SerializedName("def")
    @Expose
    private List<DefinitionDictionaryModel> definition;

    public DictionaryModel() {
    }

    protected DictionaryModel(Parcel in) {
        definition = in.createTypedArrayList(DefinitionDictionaryModel.CREATOR);
    }

    public static final Creator<DictionaryModel> CREATOR = new Creator<DictionaryModel>() {
        @Override
        public DictionaryModel createFromParcel(Parcel in) {
            return new DictionaryModel(in);
        }

        @Override
        public DictionaryModel[] newArray(int size) {
            return new DictionaryModel[size];
        }
    };

    public List<DefinitionDictionaryModel> getDefinition() {
        return definition;
    }

    public void setDefinition(List<DefinitionDictionaryModel> definition) {
        this.definition = definition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(definition);
    }

    @Override
    public String toString() {
        return "DictionaryModel{" +
                "definition=" + definition +
                '}';
    }
}
