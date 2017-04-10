package sasd97.github.com.translator.constants;

/**
 * Created by alexander on 06.04.17.
 */

public interface DatabaseConstants {

    //region Basic Connection Constants

    String NAME = "sofia.yandex.mobile.db";
    int VERSION = 1;

    //endregion

    //region Tables

    String HISTORY_TABLE_TITLE = "history";

    //endregion

    //region History Table

    String HISTORY_ID = "hid";
    String HISTORY_LANGUAGE = "language";
    String HISTORY_ORIGINAL_TEXT = "original_text";
    String HISTORY_TRANSLATED_TEXT = "translated_text";
    String HISTORY_IS_FAVORITE = "favorite";
    String HISTORY_CREATION_DATE = "creation_date";

    //endregion
}
