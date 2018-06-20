package com.example.lenovo.recipeinfo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {
    public static final int CODE_INSERT=100;
    public static final int CODE_INSERT_ID=101;
    private static final UriMatcher sURiMatcher=buildUriMatcher();
    private DataBase dbhelp;
    public static UriMatcher buildUriMatcher(){
        UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
        String auth= DataBase.AUTHORITY;
        matcher.addURI(auth, DataBase.path_Tasks,CODE_INSERT);
        matcher.addURI(auth, DataBase.path_Tasks+"/*",CODE_INSERT_ID);
        return matcher;
    }
    public MyContentProvider() {

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        long id;
        SQLiteDatabase databasehelper=dbhelp.getWritableDatabase();
        switch (sURiMatcher.match(uri))
        {
            case CODE_INSERT:
                id=databasehelper.insert(DataBase.tablename,null,values);
                break;
            default:throw new UnsupportedOperationException("uri not known"+uri);
        }
        Uri u=Uri.parse(""+id);
        return u;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbhelp=new DataBase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        Cursor cursor;
        switch (sURiMatcher.match(uri)){
            case CODE_INSERT:{
                cursor = dbhelp.getReadableDatabase().query(DataBase.tablename,
                        null, null,null, null, null, null);
                break;
            }
            case CODE_INSERT_ID: {
                cursor = dbhelp.getReadableDatabase().query(DataBase.tablename,
                        null, selection, selectionArgs, null, null, null);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri"+ uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
