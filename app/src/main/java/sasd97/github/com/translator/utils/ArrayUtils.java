package sasd97.github.com.translator.utils;

/**
 * Created by alexander on 11/04/2017.
 */

public class ArrayUtils {

    private ArrayUtils() {}

    public static int indexOfCaseInsensitive(String[] array, String x){
        for (int i = 0; i < array.length; i++) {
            if (x.equalsIgnoreCase(array[i])){
                return i;
            }
        }
        return -1;
    }
}
