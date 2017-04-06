package sasd97.github.com.translator.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import static sasd97.github.com.translator.constants.DatabaseConstants.FAVORITES_COUNTER;
import static sasd97.github.com.translator.constants.DatabaseConstants.FAVORITES_ID;
import static sasd97.github.com.translator.constants.DatabaseConstants.FAVORITES_TABLE_TITLE;
import static sasd97.github.com.translator.constants.DatabaseConstants.NAME;
import static sasd97.github.com.translator.constants.DatabaseConstants.VERSION;
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
        String createFavoritesTableQuery = DatabaseQueryBuilder
                .getInstance()
                .enableLog()
                .createTable(FAVORITES_TABLE_TITLE, FAVORITES_ID,
                        new DatabaseTableColumn(FAVORITES_COUNTER, INT))
                .build();

        sqLiteDatabase.execSQL(createFavoritesTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //todo: obtain all useful tables updates
    }
}
