package com.fossdevs.svce;

import android.database.Cursor;

/**
 * @Author Nishchal Gautam <me@nishgtm.com> on 4/21/15.
 * @Licence GPL
 * @desc interface for handling onPostExecute
 */
public interface ListUserInterface {
    void finishProgress(Cursor c);
}
