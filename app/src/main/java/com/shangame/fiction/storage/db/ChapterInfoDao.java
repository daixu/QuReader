package com.shangame.fiction.storage.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.shangame.fiction.storage.model.ChapterInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CHAPTER_INFO".
*/
public class ChapterInfoDao extends AbstractDao<ChapterInfo, Long> {

    public static final String TABLENAME = "CHAPTER_INFO";

    /**
     * Properties of entity ChapterInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Cid = new Property(1, long.class, "cid", false, "CID");
        public final static Property Bookid = new Property(2, long.class, "bookid", false, "BOOKID");
        public final static Property Title = new Property(3, String.class, "title", false, "TITLE");
        public final static Property Chaptecount = new Property(4, int.class, "chaptecount", false, "CHAPTECOUNT");
        public final static Property Cwordnumber = new Property(5, int.class, "cwordnumber", false, "CWORDNUMBER");
        public final static Property Bwordnumber = new Property(6, int.class, "bwordnumber", false, "BWORDNUMBER");
        public final static Property Parlength = new Property(7, int.class, "parlength", false, "PARLENGTH");
        public final static Property Lastcid = new Property(8, long.class, "lastcid", false, "LASTCID");
        public final static Property Nextcid = new Property(9, long.class, "nextcid", false, "NEXTCID");
        public final static Property Lastmodifytimes = new Property(10, int.class, "lastmodifytimes", false, "LASTMODIFYTIMES");
        public final static Property Bookshelves = new Property(11, int.class, "bookshelves", false, "BOOKSHELVES");
        public final static Property Chargingmode = new Property(12, int.class, "chargingmode", false, "CHARGINGMODE");
        public final static Property Buystatus = new Property(13, int.class, "buystatus", false, "BUYSTATUS");
        public final static Property Sort = new Property(14, int.class, "sort", false, "SORT");
        public final static Property Readmoney = new Property(15, long.class, "readmoney", false, "READMONEY");
        public final static Property Chapterprice = new Property(16, long.class, "chapterprice", false, "CHAPTERPRICE");
        public final static Property Bookname = new Property(17, String.class, "bookname", false, "BOOKNAME");
        public final static Property Author = new Property(18, String.class, "author", false, "AUTHOR");
        public final static Property Synopsis = new Property(19, String.class, "synopsis", false, "SYNOPSIS");
        public final static Property Bookcover = new Property(20, String.class, "bookcover", false, "BOOKCOVER");
        public final static Property Booksource = new Property(21, String.class, "booksource", false, "BOOKSOURCE");
        public final static Property Advertopen = new Property(22, int.class, "advertopen", false, "ADVERTOPEN");
        public final static Property Advertpage = new Property(23, int.class, "advertpage", false, "ADVERTPAGE");
        public final static Property Popopen = new Property(24, int.class, "popopen", false, "POPOPEN");
    }


    public ChapterInfoDao(DaoConfig config) {
        super(config);
    }
    
    public ChapterInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CHAPTER_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"CID\" INTEGER NOT NULL ," + // 1: cid
                "\"BOOKID\" INTEGER NOT NULL ," + // 2: bookid
                "\"TITLE\" TEXT," + // 3: title
                "\"CHAPTECOUNT\" INTEGER NOT NULL ," + // 4: chaptecount
                "\"CWORDNUMBER\" INTEGER NOT NULL ," + // 5: cwordnumber
                "\"BWORDNUMBER\" INTEGER NOT NULL ," + // 6: bwordnumber
                "\"PARLENGTH\" INTEGER NOT NULL ," + // 7: parlength
                "\"LASTCID\" INTEGER NOT NULL ," + // 8: lastcid
                "\"NEXTCID\" INTEGER NOT NULL ," + // 9: nextcid
                "\"LASTMODIFYTIMES\" INTEGER NOT NULL ," + // 10: lastmodifytimes
                "\"BOOKSHELVES\" INTEGER NOT NULL ," + // 11: bookshelves
                "\"CHARGINGMODE\" INTEGER NOT NULL ," + // 12: chargingmode
                "\"BUYSTATUS\" INTEGER NOT NULL ," + // 13: buystatus
                "\"SORT\" INTEGER NOT NULL ," + // 14: sort
                "\"READMONEY\" INTEGER NOT NULL ," + // 15: readmoney
                "\"CHAPTERPRICE\" INTEGER NOT NULL ," + // 16: chapterprice
                "\"BOOKNAME\" TEXT," + // 17: bookname
                "\"AUTHOR\" TEXT," + // 18: author
                "\"SYNOPSIS\" TEXT," + // 19: synopsis
                "\"BOOKCOVER\" TEXT," + // 20: bookcover
                "\"BOOKSOURCE\" TEXT," + // 21: booksource
                "\"ADVERTOPEN\" INTEGER NOT NULL ," + // 22: advertopen
                "\"ADVERTPAGE\" INTEGER NOT NULL ," + // 23: advertpage
                "\"POPOPEN\" INTEGER NOT NULL );"); // 24: popopen
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CHAPTER_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ChapterInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getCid());
        stmt.bindLong(3, entity.getBookid());
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(4, title);
        }
        stmt.bindLong(5, entity.getChaptecount());
        stmt.bindLong(6, entity.getCwordnumber());
        stmt.bindLong(7, entity.getBwordnumber());
        stmt.bindLong(8, entity.getParlength());
        stmt.bindLong(9, entity.getLastcid());
        stmt.bindLong(10, entity.getNextcid());
        stmt.bindLong(11, entity.getLastmodifytimes());
        stmt.bindLong(12, entity.getBookshelves());
        stmt.bindLong(13, entity.getChargingmode());
        stmt.bindLong(14, entity.getBuystatus());
        stmt.bindLong(15, entity.getSort());
        stmt.bindLong(16, entity.getReadmoney());
        stmt.bindLong(17, entity.getChapterprice());
 
        String bookname = entity.getBookname();
        if (bookname != null) {
            stmt.bindString(18, bookname);
        }
 
        String author = entity.getAuthor();
        if (author != null) {
            stmt.bindString(19, author);
        }
 
        String synopsis = entity.getSynopsis();
        if (synopsis != null) {
            stmt.bindString(20, synopsis);
        }
 
        String bookcover = entity.getBookcover();
        if (bookcover != null) {
            stmt.bindString(21, bookcover);
        }
 
        String booksource = entity.getBooksource();
        if (booksource != null) {
            stmt.bindString(22, booksource);
        }
        stmt.bindLong(23, entity.getAdvertopen());
        stmt.bindLong(24, entity.getAdvertpage());
        stmt.bindLong(25, entity.getPopopen());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ChapterInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getCid());
        stmt.bindLong(3, entity.getBookid());
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(4, title);
        }
        stmt.bindLong(5, entity.getChaptecount());
        stmt.bindLong(6, entity.getCwordnumber());
        stmt.bindLong(7, entity.getBwordnumber());
        stmt.bindLong(8, entity.getParlength());
        stmt.bindLong(9, entity.getLastcid());
        stmt.bindLong(10, entity.getNextcid());
        stmt.bindLong(11, entity.getLastmodifytimes());
        stmt.bindLong(12, entity.getBookshelves());
        stmt.bindLong(13, entity.getChargingmode());
        stmt.bindLong(14, entity.getBuystatus());
        stmt.bindLong(15, entity.getSort());
        stmt.bindLong(16, entity.getReadmoney());
        stmt.bindLong(17, entity.getChapterprice());
 
        String bookname = entity.getBookname();
        if (bookname != null) {
            stmt.bindString(18, bookname);
        }
 
        String author = entity.getAuthor();
        if (author != null) {
            stmt.bindString(19, author);
        }
 
        String synopsis = entity.getSynopsis();
        if (synopsis != null) {
            stmt.bindString(20, synopsis);
        }
 
        String bookcover = entity.getBookcover();
        if (bookcover != null) {
            stmt.bindString(21, bookcover);
        }
 
        String booksource = entity.getBooksource();
        if (booksource != null) {
            stmt.bindString(22, booksource);
        }
        stmt.bindLong(23, entity.getAdvertopen());
        stmt.bindLong(24, entity.getAdvertpage());
        stmt.bindLong(25, entity.getPopopen());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ChapterInfo readEntity(Cursor cursor, int offset) {
        ChapterInfo entity = new ChapterInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // cid
            cursor.getLong(offset + 2), // bookid
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // title
            cursor.getInt(offset + 4), // chaptecount
            cursor.getInt(offset + 5), // cwordnumber
            cursor.getInt(offset + 6), // bwordnumber
            cursor.getInt(offset + 7), // parlength
            cursor.getLong(offset + 8), // lastcid
            cursor.getLong(offset + 9), // nextcid
            cursor.getInt(offset + 10), // lastmodifytimes
            cursor.getInt(offset + 11), // bookshelves
            cursor.getInt(offset + 12), // chargingmode
            cursor.getInt(offset + 13), // buystatus
            cursor.getInt(offset + 14), // sort
            cursor.getLong(offset + 15), // readmoney
            cursor.getLong(offset + 16), // chapterprice
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // bookname
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // author
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // synopsis
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // bookcover
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // booksource
            cursor.getInt(offset + 22), // advertopen
            cursor.getInt(offset + 23), // advertpage
            cursor.getInt(offset + 24) // popopen
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ChapterInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCid(cursor.getLong(offset + 1));
        entity.setBookid(cursor.getLong(offset + 2));
        entity.setTitle(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setChaptecount(cursor.getInt(offset + 4));
        entity.setCwordnumber(cursor.getInt(offset + 5));
        entity.setBwordnumber(cursor.getInt(offset + 6));
        entity.setParlength(cursor.getInt(offset + 7));
        entity.setLastcid(cursor.getLong(offset + 8));
        entity.setNextcid(cursor.getLong(offset + 9));
        entity.setLastmodifytimes(cursor.getInt(offset + 10));
        entity.setBookshelves(cursor.getInt(offset + 11));
        entity.setChargingmode(cursor.getInt(offset + 12));
        entity.setBuystatus(cursor.getInt(offset + 13));
        entity.setSort(cursor.getInt(offset + 14));
        entity.setReadmoney(cursor.getLong(offset + 15));
        entity.setChapterprice(cursor.getLong(offset + 16));
        entity.setBookname(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setAuthor(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setSynopsis(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setBookcover(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setBooksource(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setAdvertopen(cursor.getInt(offset + 22));
        entity.setAdvertpage(cursor.getInt(offset + 23));
        entity.setPopopen(cursor.getInt(offset + 24));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ChapterInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ChapterInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ChapterInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
