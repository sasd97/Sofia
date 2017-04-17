package sasd97.github.com.translator.utils;

import android.util.Log;
import android.widget.Spinner;

/**
 * Created by alexander on 17/04/2017.
 */

public class SpinnerUtils {

    private static final int SPINNER_PROGRAMMING_FLAG = -1;
    private static final int SPINNER_NON_PROGRAMMING_FLAG = 0;

    private SpinnerUtils() {}

    public static void setSpinnerSelection(Spinner spinner, int index) {
        spinner.setTag(SPINNER_PROGRAMMING_FLAG);
        spinner.setSelection(index);
    }

    public static boolean isSelectedByUser(Spinner spinner) {
        if (spinner == null) return false;
        if (spinner.getTag() == null) return false;

        int tag = (Integer) spinner.getTag();
        spinner.setTag(SPINNER_NON_PROGRAMMING_FLAG);

        return tag != SPINNER_PROGRAMMING_FLAG;
    }
}
