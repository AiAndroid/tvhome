package com.video.ui.idata;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * local database save
 */
public class iDataProvider extends ContentProvider {
    private static final String     TAG              = "iDataProvider";
    public static final String      DATABASE_NAME    = "idata.db";
    public static final int         DATABASE_VERSION = 10;
    public static final String      AUTHORITY        = iDataORM.AUTHORITY;
    public static SQLiteOpenHelper mOpenHelper;

    private static final String     TABLE_SETTINGS         = "settings";
    private static final String     TABLE_ALBUM            = "local_album";
    private static final String     TABLE_Downdload        = "download";
    private static final String     TABLE_Downdload_GROUP  = "downloadgroup";
    private static final String     TABLE_Downdload_GROUP_SPECIFIC  = "downloadgroup/#";
    private static final String     TABLE_SEARCH             = "search";

    private static final UriMatcher sURLMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int download_groupby          = 1;
    private static final int download_all              = 2;
    private static final int download_groupby_specific = 3;
    private static final int setting_id                = 4;

    static {
        sURLMatcher.addURI(AUTHORITY, TABLE_Downdload,                download_all);
        sURLMatcher.addURI(AUTHORITY, TABLE_Downdload_GROUP,          download_groupby);
        sURLMatcher.addURI(AUTHORITY, TABLE_Downdload_GROUP_SPECIFIC, download_groupby_specific);
        sURLMatcher.addURI(AUTHORITY, TABLE_SETTINGS,                 setting_id);
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL("CREATE TABLE " + TABLE_SETTINGS + " ("
                        + " _id   INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + " name  TEXT,"
                        + " value TEXT,"
                        + " application TEXT,"
                        + " date_time TEXT);");

                db.execSQL("CREATE TABLE " + TABLE_SEARCH + " ("
                        + " _id       INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + " key       TEXT,"
                        + " date_int  LONG ,"   //for sort
                        + " date_time TEXT);");

                db.execSQL("CREATE TABLE " + TABLE_ALBUM + " ("
                        + " _id      INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + " res_id   TEXT, "     //media id
                        + " ns       TEXT,"      //video
                        + " action   TEXT,"      //history, favor
                        + " value    TEXT,"      //json data
                        + " uploaded INTEGER DEFAULT 0,"   //uploaded to server
                        + " date_int LONG ,"     //for sort
                        + " offset   INTEGER,"   //for play history offset
                        + " date_time TEXT);");

                db.execSQL("CREATE TABLE " + TABLE_Downdload + " ("
                        + " _id           INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + " res_id        TEXT, "     //media id

                        + " download_id   TEXT, "     //in download manager
                        + " download_url  TEXT, "     //in download manager path
                        + " vendor_download_id  TEXT, "     //id in dex vendor
                        + " vendor_name  TEXT, "     // dex vendor name

                        + " sub_id        TEXT, "     //main video object id
                        + " sub_value     TEXT, "     //current episode,  it is json for episode

                        + " value    TEXT,"      //json   data
                        + " ns       TEXT,"      //video, apk

                        + " cp       TEXT,"      //cp json   data

                        + " download_status    INTEGER DEFAULT 0,"     //down status finished set it as 1, other is is 0
                        + " download_path      TEXT,"      //down path

                        + " uploaded INTEGER DEFAULT 0,"   //sync to server

                        + " totalsizebytes INTEGER DEFAULT 0,"   //
                        + " downloadbytes  INTEGER DEFAULT 0,"   //

                        + " date_int  LONG ,"   //for sort
                        + " date_time TEXT);");


            }catch (Exception ne){}
        }

        private void dropTables(SQLiteDatabase db) {
            try {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUM);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_Downdload);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH);

            }catch (Exception ne){}
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            try {
//                db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
//                db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUM);
//                db.execSQL("DROP TABLE IF EXISTS " + TABLE_Downdload);
//                db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH);
//            }catch (Exception ne){}

//            if(newVersion == 4){
//                create4Version(db);
//            }
//
//            onCreate(db);

            if (oldVersion < 9) {
                upgradeToVersion9(db);
            }

            if (oldVersion < 10) {
                upgradeToVersion10(db);
            }
        }

        private void upgradeToVersion9(SQLiteDatabase db) {
            db.execSQL("ALTER TABLE " + TABLE_Downdload + " ADD COLUMN "
                    + " vendor_download_id  TEXT;");
            db.execSQL("ALTER TABLE " + TABLE_Downdload + " ADD COLUMN "
                    + " vendor_name  TEXT;");
            db.execSQL("UPDATE " + TABLE_Downdload + " SET vendor_name='system', vendor_download_id=download_id;");
//            db.execSQL("UPDATE " + TABLE_Downdload + " SET download_path=download_url WHERE download_id='-100'");
        }

        private void upgradeToVersion10(SQLiteDatabase db) {
            db.execSQL("UPDATE " + TABLE_Downdload + " SET download_path=download_url WHERE download_id='-100'");
        }

        private void create4Version(SQLiteDatabase db){
            try {
                db.execSQL("CREATE TABLE " + TABLE_SEARCH + " ("
                        + " _id       INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + " key       TEXT,"
                        + " date_int  LONG ,"   //for sort
                        + " date_time TEXT);");
            }catch (Exception ne){}
        }


        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                try {
                    db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
                    db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUM);
                    db.execSQL("DROP TABLE IF EXISTS " + TABLE_Downdload);

                    db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH);

                }catch (Exception ne){}

                onCreate(db);
            } catch (Exception e) {
                dropTables(db);
                onCreate(db);
            }
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, final String selection,
                        final String[] selectionArgs, String sortOrder) {
        final SqlArguments args = new SqlArguments(uri, selection, selectionArgs);
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(args.table);

        int match = sURLMatcher.match(uri);
        if(match == download_groupby){
            args.groupby = "res_id";
            args.table = TABLE_Downdload;
            qb.setTables(args.table);
        }else if(match == download_groupby_specific){
            args.groupby = uri.getLastPathSegment();
            args.table = TABLE_Downdload;
            qb.setTables(args.table);
        }

        Cursor result = null;
        try {
            SQLiteDatabase db = mOpenHelper.getReadableDatabase();
            result = qb.query(db, projection, args.where, args.args,
                    args.groupby, null, sortOrder);
        }catch (Exception ne){
            ne.printStackTrace();
        }
        return result;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int count = 0;
        try {
            SqlArguments args = new SqlArguments(uri, selection, selectionArgs);

            //always update local database
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            count = db.update(args.table, values, args.where, args.args);
            if (count > 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }catch (Exception ne){
            ne.printStackTrace();
        }

        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SqlArguments args = new SqlArguments(uri, selection, selectionArgs);
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count = db.delete(args.table, args.where, args.args);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public String getType(Uri uri) {
        SqlArguments args = new SqlArguments(uri, null, null);
        if (TextUtils.isEmpty(args.where)) {
            return "vnd.android.cursor.dir/" + args.table;
        } else {
            return "vnd.android.cursor.item/" + args.table;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SqlArguments args = new SqlArguments(uri);
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final long rowId = db.insert(args.table, null, values);
        if (rowId <= 0)
            return null;
        else {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        uri = ContentUris.withAppendedId(uri, rowId);

        return uri;
    }

    static class SqlArguments {
        public String   table;
        public final String   where;
        public final String[] args;
        public String         groupby = null;

        SqlArguments(Uri url, String where, String[] args) {
            if (url.getPathSegments().size() == 1) {
                this.table = url.getPathSegments().get(0);
                this.where = where;
                this.args = args;
            } else if (url.getPathSegments().size() != 2) {
                throw new IllegalArgumentException("Invalid URI: " + url);
            } else if (!TextUtils.isEmpty(where)) {
                throw new UnsupportedOperationException(
                        "WHERE clause not supported: " + url);
            } else {
                this.table = url.getPathSegments().get(0);
                this.where = "_id=" + ContentUris.parseId(url);
                this.args = null;

            }
        }

        SqlArguments(Uri url) {
            if (url.getPathSegments().size() == 1) {
                table = url.getPathSegments().get(0);
                where = null;
                args = null;
            } else {
                throw new IllegalArgumentException("Invalid URI: " + url);
            }
        }
    }
}
