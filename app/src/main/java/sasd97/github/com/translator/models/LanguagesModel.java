package sasd97.github.com.translator.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alexander on 07.04.17.
 */

public class LanguagesModel {

    @SerializedName("dirs")
    @Expose
    private List<String> dirs;

    public List<String> getDirs() {
        return dirs;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (String dir: dirs) {
            builder.append(dir).append("\n");
        }

        return builder.toString();
    }
}
