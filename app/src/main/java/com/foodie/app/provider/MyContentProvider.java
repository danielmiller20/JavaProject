package com.foodie.app.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.foodie.app.DebugHelper.DebugHelper;
import com.foodie.app.database.DBManagerFactory;
import com.foodie.app.database.IDBManager;

/**
 * Created by David on 14/13/2016.
 */


public class MyContentProvider extends ContentProvider {

    IDBManager manager = DBManagerFactory.getManager();
    final String TAG = "foodie";

    public MyContentProvider() {
        manager = DBManagerFactory.getManager();
        Log.d(TAG, "ContentProvided ctor ");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "insert " + uri.toString());
        String listName = uri.getLastPathSegment();
        long id = ContentUris.parseId(uri);
        try {
            switch (listName) {
                case "user":
                    if (manager.removeUser(id))
                        return 1;
                case "Business":
                    if (manager.removeBusiness(id))
                        return 1;
                case "activity":
                    if (manager.removeActivity(id))
                        return 1;
                case "cpuser":
                    if (manager.removeCPUser(id))
                        return 1;
            }
        } catch (Exception ex) {
            return 0;
        }
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return DBManagerFactory.getDBtype();
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        DebugHelper.Log("My content povider operation: insert");
        if (manager == null) {
            DebugHelper.Log("manager null");
            manager = DBManagerFactory.getManager();
        }
        String listName = uri.getLastPathSegment();
        long id = -1;
        try {
            switch (listName) {
                case "user":
                    id = manager.addUser(values);
                    return ContentUris.withAppendedId(uri, id);


                case "Business":
                    id = manager.addBusiness(values);
                    return ContentUris.withAppendedId(uri, id);

                case "activity":
                    id = manager.addActivity(values);
                    return ContentUris.withAppendedId(uri, id);

                case "cpuser":
                    id = manager.addCPUser(values);
                    return ContentUris.withAppendedId(uri, id);

            }
        } catch (Exception ex) {
            DebugHelper.Log("My content povider operation: insert, error: " + ex.getCause() + ", " + ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "ContentProvided created ");
        //so we move mContext initialization here
        //mContext = getContext();
        //   dbHelper = new DatabaseHelper(mContext);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {


        DebugHelper.Log("My content povider operation: query");

        if (projection != null)
            for (int i = 0; i < projection.length; i++) {
                DebugHelper.Log("My content povider operation query: projection = " + projection[i] + " and selectionArgs =  " + selectionArgs[i]);
            }
        else
            DebugHelper.Log("My content povider operation query: selectionArgs is null");


        String listName = uri.getLastPathSegment();
        long id = -1;
        try {
            switch (listName) {
                case "user":
                    return manager.getUser(selectionArgs, projection);


                case "Business":
                    return manager.getBusiness(selectionArgs, projection);


                case "activity":
                    return manager.getActivity(selectionArgs, projection);


                case "cpuser":
                    return manager.getCPUser(selectionArgs, projection);

            }
        } catch (Exception ex) {
            return null;
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(TAG, "insert " + uri.toString());
        String listName = uri.getLastPathSegment();
        int id = (int) ContentUris.parseId(uri);
        try {
            switch (listName) {
                case "user":
                    if (manager.updateUser(id, values))
                        return 1;


                case "Business":
                    if (manager.updateBusiness(id, values))
                        return 1;

                case "activity":
                    if (manager.updateActivity(id, values))
                        return 1;

                case "cpuser":
                    if (manager.updateCPUser(id, values))
                        return 1;

            }
        } catch (Exception ex) {
            return 0;
        }
        return 0;
    }
}
