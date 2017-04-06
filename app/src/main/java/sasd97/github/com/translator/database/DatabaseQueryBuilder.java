package sasd97.github.com.translator.database;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by alexander on 06.04.17.
 */

public final class DatabaseQueryBuilder {

    private static final String TAG = DatabaseQueryBuilder.class.getCanonicalName();

    //region Keywords

    private static final String CREATE = " CREATE ";
    private static final String TABLE = " TABLE ";
    private static final String OPEN_ROUND_BRACKET = "( ";
    private static final String CLOSE_ROUND_BRACKET = " )";
    private static final String AUTOINCREMENT = " AUTOINCREMENT ";
    private static final String COMA = ", ";
    private static final String SELECT = " SELECT ";
    private static final String ALL = " * ";
    private static final String INSERT = " INSERT ";
    private static final String VALUES = " VALUES ";
    private static final String SPACE = " ";
    private static final String END_EXECUTION = ";";
    private static final String FROM = " FROM ";

    //endregion

    //region Data Types

    public static final String NULL = " NULL ";
    public static final String INT = " INTEGER ";
    public static final String CHAR = " VARCHAR ";
    public static final String REAL = " REAL ";
    public static final String NUMERIC = " NUMERIC ";
    public static final String BLOB = " BLOB ";

    //endregion

    //region Special Expressions

    private static String IF_EXISTS = " IF EXISTS ";
    private static String IF_NOT_EXISTS = " IF NOT EXISTS ";
    private static String PRIMARY_KEY = " PRIMARY KEY ";
    private static String NOT_NULL = " NOT NULL ";

    //endregion

    //region Logic



    //endregion

    private StringBuilder builder = new StringBuilder();
    private boolean isLogged = false;

    private DatabaseQueryBuilder() {}

    public static DatabaseQueryBuilder getInstance() {
        return new DatabaseQueryBuilder();
    }

    public void flush() {
        builder.delete(0, builder.length());
    }

    private void flushLastComa() {
        builder.deleteCharAt(builder.length() - 2);
    }

    public DatabaseQueryBuilder createTable(@NonNull String tableName,
                            @NonNull String idColumnTitle,
                            @NonNull DatabaseTableColumn... columns) {
        builder
                .append(CREATE)
                .append(TABLE)
                .append(IF_NOT_EXISTS)
                .append(tableName)
                .append(OPEN_ROUND_BRACKET)
                .append(idColumnTitle)
                .append(INT)
                .append(NOT_NULL)
                .append(PRIMARY_KEY)
                .append(AUTOINCREMENT)
                .append(COMA);

        for (DatabaseTableColumn column: columns) {
            builder
                    .append(column.getTitle())
                    .append(SPACE)
                    .append(column.getType());

            if (!column.isOptional()) builder.append(NOT_NULL);

            builder.append(COMA);
        }

        flushLastComa();

        builder
                .append(CLOSE_ROUND_BRACKET)
                .append(END_EXECUTION);

        return this;
    }

    public DatabaseQueryBuilder selectAllFrom(@NonNull String tableName) {
        builder
                .append(SELECT)
                .append(ALL)
                .append(FROM)
                .append(tableName)
                .append(END_EXECUTION);

        return this;
    }

    public DatabaseQueryBuilder enableLog() {
        isLogged = true;
        return this;
    }

    public String build() {
        String query = builder.toString();
        if (isLogged) Log.d(TAG, query);
        return query;
    }

    @Override
    public String toString() {
        //todo: internal representation of query
        return super.toString();
    }
}
