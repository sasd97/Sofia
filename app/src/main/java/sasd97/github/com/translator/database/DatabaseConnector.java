package sasd97.github.com.translator.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import static sasd97.github.com.translator.constants.DatabaseConstants.HISTORY_LANGUAGE;
import static sasd97.github.com.translator.constants.DatabaseConstants.HISTORY_TABLE_TITLE;
import static sasd97.github.com.translator.constants.DatabaseConstants.HISTORY_ID;
import static sasd97.github.com.translator.constants.DatabaseConstants.HISTORY_ORIGINAL_TEXT;
import static sasd97.github.com.translator.constants.DatabaseConstants.HISTORY_TRANSLATED_TEXT;
import static sasd97.github.com.translator.constants.DatabaseConstants.HISTORY_IS_FAVORITE;
import static sasd97.github.com.translator.constants.DatabaseConstants.HISTORY_CREATION_DATE;
import static sasd97.github.com.translator.constants.DatabaseConstants.NAME;
import static sasd97.github.com.translator.constants.DatabaseConstants.VERSION;
import static sasd97.github.com.translator.database.DatabaseQueryBuilder.CHAR;
import static sasd97.github.com.translator.database.DatabaseQueryBuilder.INT;

/**
 * Created by alexander on 06.04.17.
 */

public class DatabaseConnector extends SQLiteOpenHelper {

    public DatabaseConnector(@NonNull Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createHistoryTableQuery = DatabaseQueryBuilder
                .getInstance()
                .enableLog()
                .createTable(HISTORY_TABLE_TITLE, HISTORY_ID,
                        new DatabaseTableColumn.Builder().title(HISTORY_ORIGINAL_TEXT).type(CHAR).build(),
                        new DatabaseTableColumn.Builder().title(HISTORY_TRANSLATED_TEXT).type(CHAR).build(),
                        new DatabaseTableColumn.Builder().title(HISTORY_LANGUAGE).type(CHAR).build(),
                        new DatabaseTableColumn.Builder().title(HISTORY_IS_FAVORITE).type(INT).build(),
                        new DatabaseTableColumn.Builder().title(HISTORY_CREATION_DATE).type(CHAR).build()
                )
                .build();

        sqLiteDatabase.execSQL(createHistoryTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //todo: obtain all useful tables updates
    }
}
