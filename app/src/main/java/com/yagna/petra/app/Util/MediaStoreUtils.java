package com.yagna.petra.app.Util;

/**
 * Created by Welcome on 06-01-2017.
 */

import android.content.Context;
import android.content.Intent;

public final class MediaStoreUtils {
    private MediaStoreUtils() {
    }

    public static Intent getPickImageIntent(final Context context) {
        final Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        return Intent.createChooser(intent, "Select picture");
    }
}