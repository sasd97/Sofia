package sasd97.github.com.translator.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;

import sasd97.github.com.translator.R;

/**
 * Created by alexander on 17/04/2017.
 */

public class ShareUtils {

    private static ClipboardManager clipboardManager;
    private static String shareViaText;

    private static final String CLIPBOARD_TITLE = "sofia.translate";
    private static final String SHARE_INTENT_MEDIA_TYPE = "text/plain";

    private ShareUtils() {}

    public static void init(Context context) {
        shareViaText = context.getResources().getString(R.string.translate_action_share_via);
        clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public static void copyToClipboard(CharSequence text) {
        ClipData clip = ClipData.newPlainText(CLIPBOARD_TITLE, text);
        clipboardManager.setPrimaryClip(clip);
    }

    public static Intent shareToAnotherApp(CharSequence text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType(SHARE_INTENT_MEDIA_TYPE);
        return Intent.createChooser(sendIntent, shareViaText);
    }
}
