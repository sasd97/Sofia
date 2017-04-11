package sasd97.github.com.translator.database;

import android.support.annotation.NonNull;

/**
 * Created by alexander on 06.04.17.
 */

public class DatabaseTableColumn {

    private String title;
    private String type;

    private boolean isOptional;
    private boolean isUnique;

    private DatabaseTableColumn(Builder builder) {
        title = builder.title;
        type = builder.type;
        isOptional = builder.isOptional;
        isUnique = builder.isUnique;
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

    public boolean isUnique() {
        return isUnique;
    }

    public void setUnique(boolean unique) {
        isUnique = unique;
    }

    public static class Builder {

        private String title;
        private String type;
        private boolean isOptional = false;
        private boolean isUnique = false;

        public Builder title(@NonNull String title) {
            this.title = title;
            return this;
        }

        public Builder type(@NonNull String type) {
            this.type = type;
            return this;
        }

        public Builder isOptional(boolean isOptional) {
            this.isOptional = isOptional;
            return this;
        }

        public Builder isUnique(boolean isUnique) {
            this.isUnique = isUnique;
            return this;
        }

        public DatabaseTableColumn build() {
            return new DatabaseTableColumn(this);
        }
    }

    @Override
    public String toString() {
        return "DatabaseTableColumn{" +
                "title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", isOptional=" + isOptional +
                ", isUnique=" + isUnique +
                '}';
    }
}
