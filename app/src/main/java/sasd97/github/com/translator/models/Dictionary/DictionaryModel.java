package sasd97.github.com.translator.models.Dictionary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alexander on 13/04/2017.
 */

public class DictionaryModel {

    @SerializedName("def")
    @Expose
    private List<DefinitionDictionaryModel> definition;

    public List<DefinitionDictionaryModel> getDefinition() {
        return definition;
    }

    public void setDefinition(List<DefinitionDictionaryModel> definition) {
        this.definition = definition;
    }

    @Override
    public String toString() {
        return "DictionaryModel{" +
                "definition=" + definition +
                '}';
    }
}
