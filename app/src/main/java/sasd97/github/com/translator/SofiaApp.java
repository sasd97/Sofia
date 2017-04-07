package sasd97.github.com.translator;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import sasd97.github.com.translator.database.DatabaseConnector;
import sasd97.github.com.translator.http.HttpService;
import sasd97.github.com.translator.utils.Prefs;

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

        HttpService.init();
        Prefs.init(this);
    }

    public static SQLiteDatabase db() {
        return database;
    }
}
