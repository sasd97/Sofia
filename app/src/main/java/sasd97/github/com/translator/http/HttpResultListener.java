package sasd97.github.com.translator.http;

/**
 * Created by alexander on 07.04.17.
 */

public interface HttpResultListener {
    <T> void onHttpSuccess(T result);
    void onHttpError(HttpError error);
}
