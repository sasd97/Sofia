package sasd97.github.com.translator.services;

/**
 * Created by alexander on 10/04/2017.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import sasd97.github.com.translator.database.DatabaseQueryBuilder;
import sasd97.github.com.translator.database.DatabaseWhereCondition;
import sasd97.github.com.translator.models.TranslationModel;
import sasd97.github.com.translator.utils.DateFormatter;

import static sasd97.github.com.translator.constants.DatabaseConstants.HISTORY_CREATION_DATE;
import static sasd97.github.com.translator.constants.DatabaseConstants.HISTORY_ID;
import static sasd97.github.com.translator.constants.DatabaseConstants.HISTORY_IS_FAVORITE;
import static sasd97.github.com.translator.constants.DatabaseConstants.HISTORY_LANGUAGE;
import static sasd97.github.com.translator.constants.DatabaseConstants.HISTORY_ORIGINAL_TEXT;
import static sasd97.github.com.translator.constants.DatabaseConstants.HISTORY_TABLE_TITLE;

import static sasd97.github.com.translator.SofiaApp.db;
import static sasd97.github.com.translator.database.DatabaseQueryBuilder.EQUAL;
import static sasd97.github.com.translator.constants.DatabaseConstants.HISTORY_TRANSLATED_TEXT;
import static sasd97.github.com.translator.database.DatabaseQueryBuilder.LIKE;
import static sasd97.github.com.translator.database.DatabaseQueryBuilder.TRUE;

public class HistorySqlService {

    private static final String TAG = HistorySqlService.class.getCanonicalName();

    private HistorySqlService() {}

    private static DatabaseQueryBuilder databaseQueryBuilder
            = DatabaseQueryBuilder.getInstance().enableLog();

    public static int update(TranslationModel translationModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(HISTORY_IS_FAVORITE, translationModel.isFavorite());

        Log.d(TAG, translationModel.toString());
        return db().update(HISTORY_TABLE_TITLE, contentValues, "hid = ?",
                new String[] { String.valueOf(translationModel.getId()) });
    }

    public static int delete(TranslationModel translationModel) {
        return db().delete(HISTORY_TABLE_TITLE, "hid = ?",
                new String[] { String.valueOf(translationModel.getId()) });
    }

    public static int delete(int id) {
        return db().delete(HISTORY_TABLE_TITLE, "hid = ?",
                new String[] { String.valueOf(id) });
    }

    public static TranslationModel find(TranslationModel translationModel) {
        databaseQueryBuilder.flush();

        String selectAllQuery = databaseQueryBuilder
                .selectAllFrom(HISTORY_TABLE_TITLE)
                .where(new DatabaseWhereCondition<>(HISTORY_LANGUAGE, LIKE, translationModel.getLanguage()),
                        new DatabaseWhereCondition<>(HISTORY_ORIGINAL_TEXT, LIKE),
                        new DatabaseWhereCondition<>(HISTORY_TRANSLATED_TEXT, LIKE))
                .build();

        Cursor cursor = db().rawQuery(selectAllQuery,
                new String[] { translationModel.getOriginalText(), translationModel.getTranslatedText() });

        if (cursor.getCount() > 1) return null;
        if (!cursor.moveToFirst()) return null;

        int idTextIndex = cursor.getColumnIndex(HISTORY_ID);
        int originalTextIndex = cursor.getColumnIndex(HISTORY_ORIGINAL_TEXT);
        int translatedTextIndex = cursor.getColumnIndex(HISTORY_TRANSLATED_TEXT);
        int languageIndex = cursor.getColumnIndex(HISTORY_LANGUAGE);
        int favoritesIndex = cursor.getColumnIndex(HISTORY_IS_FAVORITE);
        int creationDateIndex = cursor.getColumnIndex(HISTORY_CREATION_DATE);

        TranslationModel translation = new TranslationModel();
        translation.setId(cursor.getInt(idTextIndex));
        translation.setOriginalText(cursor.getString(originalTextIndex).replaceAll("\'", "'"));
        translation.setTranslatedText(cursor.getString(translatedTextIndex).replaceAll("\'", "'"));
        translation.setLanguage(cursor.getString(languageIndex));
        translation.setFavorite(cursor.getInt(favoritesIndex));
        translation.setCreationDate(cursor.getString(creationDateIndex));

        cursor.close();
        return translation;
    }

    public static TranslationModel saveTranslation(TranslationModel translation) {
        TranslationModel translationModel = find(translation);
        if (translationModel != null) return translationModel;

        ContentValues contentValues = new ContentValues();
        contentValues.put(HISTORY_ORIGINAL_TEXT, translation.getOriginalText().replaceAll("'", "\'"));
        contentValues.put(HISTORY_TRANSLATED_TEXT, translation.getTranslatedText().replaceAll("'", "\'"));
        contentValues.put(HISTORY_LANGUAGE, translation.getLanguage());
        contentValues.put(HISTORY_IS_FAVORITE, translation.isFavorite());
        contentValues.put(HISTORY_CREATION_DATE, DateFormatter.formatDate(translation.getCreationDate()));

        long id = db().insert(HISTORY_TABLE_TITLE, null, contentValues);
        translation.setId((int) id);
        return translation;
    }

    public static List<TranslationModel> getAllTranslations() {
        List<TranslationModel> translationModels = new ArrayList<>();
        databaseQueryBuilder.flush();

        String selectAllQuery = databaseQueryBuilder.selectAllFrom(HISTORY_TABLE_TITLE).build();
        Cursor cursor = db().rawQuery(selectAllQuery, null);

        int idTextIndex = cursor.getColumnIndex(HISTORY_ID);
        int originalTextIndex = cursor.getColumnIndex(HISTORY_ORIGINAL_TEXT);
        int translatedTextIndex = cursor.getColumnIndex(HISTORY_TRANSLATED_TEXT);
        int languageIndex = cursor.getColumnIndex(HISTORY_LANGUAGE);
        int favoritesIndex = cursor.getColumnIndex(HISTORY_IS_FAVORITE);
        int creationDateIndex = cursor.getColumnIndex(HISTORY_CREATION_DATE);

        if (!cursor.moveToLast()) return translationModels;

        do {
            TranslationModel translationModel = new TranslationModel();
            translationModel.setId(cursor.getInt(idTextIndex));
            translationModel.setOriginalText(cursor.getString(originalTextIndex).replaceAll("\'", "'"));
            translationModel.setTranslatedText(cursor.getString(translatedTextIndex).replaceAll("\'", "'"));
            translationModel.setLanguage(cursor.getString(languageIndex));
            translationModel.setFavorite(cursor.getInt(favoritesIndex));
            translationModel.setCreationDate(cursor.getString(creationDateIndex));
            translationModels.add(translationModel);
        } while (cursor.moveToPrevious());

        cursor.close();

        return translationModels;
    }

    public static List<TranslationModel> findAllFavorites() {
        List<TranslationModel> translationModels = new ArrayList<>();
        databaseQueryBuilder.flush();

        String whereFavorites = databaseQueryBuilder
                .where(new DatabaseWhereCondition<>(HISTORY_IS_FAVORITE, EQUAL, TRUE))
                .build();

        databaseQueryBuilder.flush();

        String selectAllQuery = databaseQueryBuilder.selectAllFrom(HISTORY_TABLE_TITLE, whereFavorites).build();
        Cursor cursor = db().rawQuery(selectAllQuery, null);

        int idTextIndex = cursor.getColumnIndex(HISTORY_ID);
        int originalTextIndex = cursor.getColumnIndex(HISTORY_ORIGINAL_TEXT);
        int translatedTextIndex = cursor.getColumnIndex(HISTORY_TRANSLATED_TEXT);
        int languageIndex = cursor.getColumnIndex(HISTORY_LANGUAGE);
        int favoritesIndex = cursor.getColumnIndex(HISTORY_IS_FAVORITE);
        int creationDateIndex = cursor.getColumnIndex(HISTORY_CREATION_DATE);

        if (!cursor.moveToLast()) return translationModels;

        do {
            TranslationModel translationModel = new TranslationModel();
            translationModel.setId(cursor.getInt(idTextIndex));
            translationModel.setOriginalText(cursor.getString(originalTextIndex).replaceAll("\'", "'"));
            translationModel.setTranslatedText(cursor.getString(translatedTextIndex).replaceAll("\'", "'"));
            translationModel.setLanguage(cursor.getString(languageIndex));
            translationModel.setFavorite(cursor.getInt(favoritesIndex));
            translationModel.setCreationDate(cursor.getString(creationDateIndex));
            translationModels.add(translationModel);
        } while (cursor.moveToPrevious());

        cursor.close();

        return translationModels;
    }
}
