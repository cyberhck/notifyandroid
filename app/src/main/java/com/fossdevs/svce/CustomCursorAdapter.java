package com.fossdevs.svce;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * @Author Nishchal Gautam <me@nishgtm.com> on 5/5/15.
 * @Licence GPL
 * @desc My custom adapter for displaying files and text seperately.
 */
public class CustomCursorAdapter extends CursorAdapter {
    private LayoutInflater mInflater;
    public CustomCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        String type=cursor.getString(cursor.getColumnIndex("type"));
        if(type.equals("text")){
            return mInflater.inflate(R.layout.list_display_notices, parent, false);
        }else{
            return mInflater.inflate(R.layout.list_file_download, parent, false);
        }
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String type=cursor.getString(cursor.getColumnIndex("type"));
        if(type.equals("text")){
            TextView textView=(TextView) view.findViewById(R.id.note_bubble);
            textView.setText(cursor.getString(cursor.getColumnIndex("message")));
        }else{
            TextView textView=(TextView) view.findViewById(R.id.download_filename);
            String filename=cursor.getString(cursor.getColumnIndex("message"));
            String[] bits = filename.split("/");
            filename=bits[bits.length-1];
            textView.setText(filename);
        }


    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
