package com.shangame.fiction.storage.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.shangame.fiction.storage.model.BookMark;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BOOK_MARK".
*/
public class BookMarkDao extends AbstractDao<BookMark, String> {

    public static final String TABLENAME = "BOOK_MARK";

    /**
     * Properties of entity BookMark.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property MarkId = new Property(0, String.class, "markId", true, "MARK_ID");
        public final static Property Index = new Property(1, long.class, "index", false, "INDEX");
        public final static Property Userid = new Property(2, long.class, "userid", false, "USERID");
        public final static Property Bookid = new Property(3, long.class, "bookid", false, "BOOKID");
        public final static Property Chapterid = new Property(4, long.class, "chapterid", false, "CHAPTERID");
        public final static Property Pid = new Property(5, long.class, "pid", false, "PID");
        public final static Property Title = new Property(6, String.class, "title", false, "TITLE");
        public final static Property Content = new Property(7, String.class, "content", false, "CONTENT");
        public final static Property Createtime = new Property(8, String.class, "createtime", false, "CREATETIME");
    }


    public BookMarkDao(DaoConfig config) {
        super(config);
    }
    
    public BookMarkDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BOOK_MARK\" (" + //
                "\"MARK_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: markId
                "\"INDEX\" INTEGER NOT NULL ," + // 1: index
                "\"USERID\" INTEGER NOT NULL ," + // 2: userid
                "\"BOOKID\" INTEGER NOT NULL ," + // 3: bookid
                "\"CHAPTERID\" INTEGER NOT NULL ," + // 4: chapterid
                "\"PID\" INTEGER NOT NULL ," + // 5: pid
                "\"TITLE\" TEXT," + // 6: title
                "\"CONTENT\" TEXT," + // 7: content
                "\"CREATETIME\" TEXT);"); // 8: createtime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BOOK_MARK\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, BookMark entity) {
        stmt.clearBindings();
 
        String markId = entity.getMarkId();
        if (markId != null) {
            stmt.bindString(1, markId);
        }
        stmt.bindLong(2, entity.getIndex());
        stmt.bindLong(3, entity.getUserid());
        stmt.bindLong(4, entity.getBookid());
        stmt.bindLong(5, entity.getChapterid());
        stmt.bindLong(6, entity.getPid());
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(7, title);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(8, content);
        }
 
        String createtime = entity.getCreatetime();
        if (createtime != null) {
            stmt.bindString(9, createtime);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, BookMark entity) {
        stmt.clearBindings();
 
        String markId = entity.getMarkId();
        if (markId != null) {
            stmt.bindString(1, markId);
        }
        stmt.bindLong(2, entity.getIndex());
        stmt.bindLong(3, entity.getUserid());
        stmt.bindLong(4, entity.getBookid());
        stmt.bindLong(5, entity.getChapterid());
        stmt.bindLong(6, entity.getPid());
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(7, title);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(8, content);
        }
 
        String createtime = entity.getCreatetime();
        if (createtime != null) {
            stmt.bindString(9, createtime);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public BookMark readEntity(Cursor cursor, int offset) {
        BookMark entity = new BookMark( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // markId
            cursor.getLong(offset + 1), // index
            cursor.getLong(offset + 2), // userid
            cursor.getLong(offset + 3), // bookid
            cursor.getLong(offset + 4), // chapterid
            cursor.getLong(offset + 5), // pid
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // title
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // content
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // createtime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, BookMark entity, int offset) {
        entity.setMarkId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setIndex(cursor.getLong(offset + 1));
        entity.setUserid(cursor.getLong(offset + 2));
        entity.setBookid(cursor.getLong(offset + 3));
        entity.setChapterid(cursor.getLong(offset + 4));
        entity.setPid(cursor.getLong(offset + 5));
        entity.setTitle(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setContent(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setCreatetime(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    @Override
    protected final String updateKeyAfterInsert(BookMark entity, long rowId) {
        return entity.getMarkId();
    }
    
    @Override
    public String getKey(BookMark entity) {
        if(entity != null) {
            return entity.getMarkId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(BookMark entity) {
        return entity.getMarkId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
