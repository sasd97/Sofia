package sasd97.github.com.translator.database;

import android.support.annotation.NonNull;

/**
 * Created by alexander on 10/04/2017.
 */

public class DatabaseWhereCondition<T, K> {

    private T left;
    private K right;
    private String condition;

    public DatabaseWhereCondition(@NonNull T left,
                                  @NonNull K right,
                                  @NonNull String condition) {
        this.left = left;
        this.right = right;
        this.condition = condition;
    }

    public T getLeft() {
        return left;
    }

    public void setLeft(T left) {
        this.left = left;
    }

    public K getRight() {
        return right;
    }

    public void setRight(K right) {
        this.right = right;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "DatabaseWhereCondition{" +
                "left=" + left +
                ", right=" + right +
                ", condition='" + condition + '\'' +
                '}';
    }
}
