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
    private static final String UNIQUE = " UNIQUE ";
    private static final String WHERE = " WHERE ";
    private static final String DROP = " DROP ";

    //endregion

    //region Data Types

    public static final String NULL = " NULL ";
    public static final String INT = " INTEGER ";
    public static final String CHAR = " TEXT ";
    public static final String REAL = " REAL ";
    public static final String NUMERIC = " NUMERIC ";
    public static final String BLOB = " BLOB ";

    //endregion

    //region Special Expressions

    public static String IF_EXISTS = " IF EXISTS ";
    public static String IF_NOT_EXISTS = " IF NOT EXISTS ";
    public static String PRIMARY_KEY = " PRIMARY KEY ";
    public static String NOT_NULL = " NOT NULL ";

    //endregion

    //region Logic

    public static String AND = " AND ";
    public static String IS = " IS ";
    public static String OR = " OR ";
    public static String NOT = " NOT ";
    public static String LIKE = " LIKE ";
    public static String IN = " IN ";
    public static String BIGGER = " > ";
    public static String BIGGER_OR_EQUAL = " >= ";
    public static String EQUAL = " = ";
    public static String SMALLER = " < ";
    public static String SMALLER_OR_EQUAL = " <= ";

    //endregion

    //region Alias

    public static final int TRUE = 1;
    public static final int FALSE = 0;
    public static final String UNKNOWN = " ? ";

    //endregion

    private boolean isLogged = false;
    private StringBuilder builder = new StringBuilder();

    private DatabaseQueryBuilder() {
    }

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

        for (DatabaseTableColumn column : columns) {
            builder
                    .append(column.getTitle())
                    .append(SPACE)
                    .append(column.getType());

            if (column.isUnique()) builder.append(UNIQUE);
            if (!column.isOptional()) builder.append(NOT_NULL);

            builder.append(COMA);
        }

        flushLastComa();

        builder
                .append(CLOSE_ROUND_BRACKET);

        return this;
    }

    public DatabaseQueryBuilder selectAllFrom(@NonNull String tableName) {
        builder
                .append(SELECT)
                .append(ALL)
                .append(FROM)
                .append(tableName);

        return this;
    }

    public DatabaseQueryBuilder selectAllFrom(@NonNull String tableName, @NonNull String whereCondition) {
        builder
                .append(SELECT)
                .append(ALL)
                .append(FROM)
                .append(tableName)
                .append(whereCondition);

        return this;
    }

    public DatabaseQueryBuilder where(@NonNull DatabaseWhereCondition<?, ?>... conditions) {
        builder
                .append(WHERE);

        for (DatabaseWhereCondition<?, ?> condition : conditions) {
            builder
                    .append(condition.getLeft())
                    .append(condition.getCondition());

            if (condition.getRight() == null) {
                builder
                        .append(UNKNOWN);
            } else {
                if (condition.getCondition().equals(LIKE)) {
                    builder.append('\'').append(condition.getRight()).append('\'');
                } else {
                    builder.append(condition.getRight());
                }
            }

            builder
                    .append(AND);
        }

        int length = builder.length();
        builder.delete(length - 5, length);

        return this;
    }

    public DatabaseQueryBuilder dropDatabase(@NonNull String tableName) {
        builder
                .append(DROP)
                .append(TABLE)
                .append(IF_EXISTS)
                .append(tableName);

        return this;
    }

    public DatabaseQueryBuilder enableLog() {
        isLogged = true;
        return this;
    }

    public String build() {
        builder.append(END_EXECUTION);
        String query = builder.toString();
        if (isLogged) Log.d(TAG, query);
        return query;
    }

    @Override
    public String toString() {
        return String.format("DatabaseQueryBuilder{\nisLoggingEnabled = %1$b\nquery = %2$s\n}", isLogged, builder.toString());
    }
}
