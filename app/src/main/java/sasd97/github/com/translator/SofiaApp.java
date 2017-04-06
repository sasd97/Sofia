package sasd97.github.com.translator;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import sasd97.github.com.translator.database.DatabaseConnector;

/**
 * Created by alexander on 19.03.17.
 */

public class SofiaApp extends Application {

    private static SQLiteDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();

        DatabaseConnector databaseConnector = new DatabaseConnector(this);
        database = databaseConnector.getWritableDatabase();
    }

    public static SQLiteDatabase db() {
        return database;
    }
}
