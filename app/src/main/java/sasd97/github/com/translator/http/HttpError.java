package sasd97.github.com.translator.http;

/**
 * Created by alexander on 07.04.17.
 */

public class HttpError {

    private int code;
    private String message;

    public HttpError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "HttpError{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
