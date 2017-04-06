package sasd97.github.com.translator.database;

import android.support.annotation.NonNull;

/**
 * Created by alexander on 06.04.17.
 */

public class DatabaseTableColumn {

    private String title;
    private String type;

    private boolean isOptional = false;


    public DatabaseTableColumn(@NonNull String title,
                               @NonNull String type) {
        this.title = title;
        this.type = type;
    }

    public DatabaseTableColumn(@NonNull String title,
                               @NonNull String type,
                               boolean isOptional) {
        this.title = title;
        this.type = type;
        this.isOptional = isOptional;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean optional) {
        isOptional = optional;
    }

    @Override
    public String toString() {
        return "DatabaseTableColumn{" +
                "title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", isOptional=" + isOptional +
                '}';
    }
}
