package id.playable.frompassiontoaction.components;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import id.playable.frompassiontoaction.Model.BookmarkModel;
import id.playable.frompassiontoaction.Model.ContentModel;

public class DatabaseHandler extends SQLiteOpenHelper {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;
 
    // Database Name
    private static final String DATABASE_NAME = "fpta";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_BOOKMARK_TABLE = "CREATE TABLE bookmark ("
                + "id INTEGER PRIMARY KEY,"
                + "bab INTEGER,"
                + "time INTEGER,"
                + "message TEXT"
        		+ ")";
        db.execSQL(CREATE_BOOKMARK_TABLE);

        String CREATE_CONTENT_TABLE = "CREATE TABLE content ("
                + "id INTEGER PRIMARY KEY,"
                + "bab INTEGER,"
                + "chapter INTEGER,"
                + "title TEXT,"
                + "downloaded INTEGER DEFAULT 0"
                + ")";
        db.execSQL(CREATE_CONTENT_TABLE);

        initializeContent(db);

    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + BookmarkModel.tablename);

        db.execSQL("DROP TABLE IF EXISTS " + ContentModel.tablename);

        // Create tables again
        onCreate(db);
    }

    public void initializeContent(SQLiteDatabase db) {

        ArrayList<ContentModel> contents = new ArrayList<>();

        contents.add(new ContentModel(1, 0, "Pengantar Penulis"));
        contents.add(new ContentModel(1, 1, "Awali dengan niat yang lurus"));
        contents.add(new ContentModel(1, 2, "Sukses dan Bermanfaat"));
        contents.add(new ContentModel(1, 3, "Sukses VS Bermanfaat"));
        contents.add(new ContentModel(1, 4, "Semakin Bermanfaat"));
        contents.add(new ContentModel(1, 5, "Bermanfaat dunia dan akhirat"));
        contents.add(new ContentModel(1, 6, "Bermanfaat akan membuat anda sukses dan kaya raya"));
        contents.add(new ContentModel(1, 7, "Kenali diri melalui passion"));
        contents.add(new ContentModel(1, 8, "Kebahagiaan"));


        contents.add(new ContentModel(2, 0, "Kebahagiaan Emosi"));
        contents.add(new ContentModel(2, 1, "kebahagiaan Memaknai"));
        contents.add(new ContentModel(2, 2, "Habiskan Kegagalanmu"));
        contents.add(new ContentModel(2, 3, "Mencoba menjadi aktor layar lebar"));
        contents.add(new ContentModel(2, 4, "Berkali-kali Gagal"));
        contents.add(new ContentModel(2, 5, "Berserah pada-Nya"));
        contents.add(new ContentModel(2, 6, "Kenali diri melalui passion"));
        contents.add(new ContentModel(2, 7, "Cara menemukan passion"));
        contents.add(new ContentModel(2, 8, "Vision"));
        contents.add(new ContentModel(2, 9, "The power of dreams"));
        contents.add(new ContentModel(2, 10, "Visi dunia dan visi akhirat"));
        contents.add(new ContentModel(2, 11, "Orang termiskin didunia bukanlah orang yang tidak memiliki uang"));
        contents.add(new ContentModel(2, 12, "Misi dunia dan akhirat"));


        contents.add(new ContentModel(3, 0, "Lakukan sekarang juga"));
        contents.add(new ContentModel(3, 1, "Belajar dari ibadah haji"));
        contents.add(new ContentModel(3, 2, "Lakukan segera, buang segala alasan"));
        contents.add(new ContentModel(3, 3, "Ikuti kata hatimu"));
        contents.add(new ContentModel(3, 4, "Perjalanan hidupku"));
        contents.add(new ContentModel(3, 5, "Sukses adalah  1% bakat"));
        contents.add(new ContentModel(3, 6, "Memancing rezeki dengan sedekah"));

        for(ContentModel content : contents){
            ContentValues values = new ContentValues();
            values.put("bab", content.getBab());
            values.put("chapter", content.getChapter());
            values.put("title", content.getTitle());

            // Inserting Row
            db.insert(ContentModel.tablename, null, values);
        }
    }

    public ArrayList<ContentModel> getContents(int bab) {
        ArrayList<ContentModel> contents = new ArrayList<ContentModel>();

        String where = "";
        if(bab>0)
            where = " where bab="+bab;

        // Select All Query
        String selectQuery = "SELECT  id, bab, chapter, title, downloaded FROM " + ContentModel.tablename +where+" ORDER BY bab,chapter";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ContentModel content = new ContentModel(cursor);
                contents.add(content);
            } while (cursor.moveToNext());
        }

        return contents;
    }

    public ContentModel getContent(int bab, int chapter) {
        ArrayList<ContentModel> contents = new ArrayList<ContentModel>();

        // Select All Query
        String selectQuery = "SELECT  id, bab, chapter, title, downloaded FROM " + ContentModel.tablename +" where bab="+bab+" and chapter="+chapter;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            return new ContentModel(cursor);
        }

        return null;
    }

    public void updateContent(ContentModel content) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("downloaded", content.isDownloaded() ? 1 : 0);
        db.update(ContentModel.tablename, values, "id = ?", new String[]{String.valueOf(content.getId())});
        db.close();
    }


    public void addBookmark(BookmarkModel bookmark) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("bab", bookmark.getBab());
        values.put("time", bookmark.getTime());
        values.put("message", bookmark.getMessage());

        // Inserting Row
        db.insert(BookmarkModel.tablename, null, values);
        db.close(); // Closing database connection
    }

    public void deleteBookmark(BookmarkModel bookmark) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(BookmarkModel.tablename, "id = ?",
                new String[]{String.valueOf(bookmark.getId())});
        db.close();
    }

    public void deleteSelectedBookmark(String[] ids){
        String args = TextUtils.join(", ", ids);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(String.format("DELETE FROM bookmark WHERE id IN (%s);", args));
        db.close();
    }

    public void clearBookmarks() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(BookmarkModel.tablename, "", null);
        db.close();
    }

    public ArrayList<BookmarkModel> getBookmarks() {
        ArrayList<BookmarkModel> bookmarks = new ArrayList<BookmarkModel>();
        // Select All Query
        String selectQuery = "SELECT  id, bab, time, message FROM " + BookmarkModel.tablename + " ORDER BY bab,time";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BookmarkModel bookmark = new BookmarkModel(cursor);
                bookmarks.add(bookmark);
            } while (cursor.moveToNext());
        }

        db.close();

        // return contact list
        return bookmarks;
    }

	public void clearDatabase() {
		SQLiteDatabase db = this.getWritableDatabase();
        db.delete(BookmarkModel.tablename, "", null);
        db.close();
	}

    public void updateBookmark(BookmarkModel bookmark) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("message", bookmark.getMessage());
        db.update(BookmarkModel.tablename, values, "id = ?", new String[]{String.valueOf(bookmark.getId())});
        db.close();
    }
}
