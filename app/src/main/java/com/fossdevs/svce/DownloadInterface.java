package com.fossdevs.svce;

import android.database.Cursor;

/**
 * @Author Nishchal Gautam <me@nishgtm.com> on 5/7/15.
 * @Licence GPL
 * @desc ${text}
 */
public interface DownloadInterface {
    void onProg(int progress);
    void onFinish();
}
