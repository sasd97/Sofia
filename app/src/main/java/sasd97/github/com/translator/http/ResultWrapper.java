package sasd97.github.com.translator.http;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

import static sasd97.github.com.translator.http.HttpService.getRestClient;

/**
 * Created by alexander on 17/04/2017.
 */

public class ResultWrapper<R> implements Callback<R> {

    private static final String TAG = ResultWrapper.class.getCanonicalName();
    private static final String CALL_INFO_FORMAT = "Call(%1$s) to %2$s";
    private static final String ERROR_INFO_FORMAT = "Error was acquired while call url\nurl(%1$s): %2$s\nerror: %3$s";

    private HttpResultListener listener;

    public ResultWrapper(@NonNull HttpResultListener listener) {
        this.listener = listener;
    }

    @Override
    public void onResponse(Call<R> call, Response<R> response) {
        String log = String.format(CALL_INFO_FORMAT, call.request().method(), call.request().url().toString());
        Log.i(TAG, log);

        if (response.isSuccessful() && response.body() != null) {
            if (!isExecutionBroken()) listener.onHttpSuccess(response.body());
            else onBrokenExecution(response.body());
            return;
        }

        if (response.errorBody() != null) {
            Converter<ResponseBody, HttpError> errorConverter =
                    getRestClient().responseBodyConverter(HttpError.class, new Annotation[0]);
            HttpError error;

            try {
                error = errorConverter.convert(response.errorBody());
            } catch (IOException io) {
                io.printStackTrace();
                error = HttpError.UNKNOWN;
            }

            Log.e(TAG, String.format(ERROR_INFO_FORMAT,
                    call.request().method(),
                    call.request().url().toString(),
                    error.toString()));
            listener.onHttpError(error);
        }
    }

    @Override
    public void onFailure(Call<R> call, Throwable t) {
        String error = String.format(ERROR_INFO_FORMAT,
                call.request().method(),
                call.request().url().toString(),
                t.getMessage()
                );
        Log.e(TAG, error);
        t.printStackTrace();
        listener.onHttpError(HttpError.UNKNOWN);
    }

    protected boolean isExecutionBroken() {
        return false;
    }

    protected void onBrokenExecution(R result) {
    }
}
