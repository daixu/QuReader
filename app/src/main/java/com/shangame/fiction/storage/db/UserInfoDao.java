package com.shangame.fiction.storage.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.shangame.fiction.storage.model.UserInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER_INFO".
*/
public class UserInfoDao extends AbstractDao<UserInfo, Long> {

    public static final String TABLENAME = "USER_INFO";

    /**
     * Properties of entity UserInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Userid = new Property(0, long.class, "userid", true, "_id");
        public final static Property Account = new Property(1, String.class, "account", false, "ACCOUNT");
        public final static Property Nickname = new Property(2, String.class, "nickname", false, "NICKNAME");
        public final static Property Money = new Property(3, long.class, "money", false, "MONEY");
        public final static Property Headimgurl = new Property(4, String.class, "headimgurl", false, "HEADIMGURL");
        public final static Property Mobilephone = new Property(5, String.class, "mobilephone", false, "MOBILEPHONE");
        public final static Property Sex = new Property(6, int.class, "sex", false, "SEX");
        public final static Property Channel = new Property(7, int.class, "channel", false, "CHANNEL");
        public final static Property Birthday = new Property(8, String.class, "birthday", false, "BIRTHDAY");
        public final static Property Synopsis = new Property(9, String.class, "synopsis", false, "SYNOPSIS");
        public final static Property Province = new Property(10, String.class, "province", false, "PROVINCE");
        public final static Property City = new Property(11, String.class, "city", false, "CITY");
        public final static Property Coin = new Property(12, int.class, "coin", false, "COIN");
        public final static Property Newvip = new Property(13, int.class, "newvip", false, "NEWVIP");
        public final static Property Regtype = new Property(14, int.class, "regtype", false, "REGTYPE");
        public final static Property Cashmoney = new Property(15, double.class, "cashmoney", false, "CASHMONEY");
        public final static Property Receive = new Property(16, int.class, "receive", false, "RECEIVE");
        public final static Property ReadTime = new Property(17, long.class, "readTime", false, "READ_TIME");
        public final static Property Authorid = new Property(18, int.class, "authorid", false, "AUTHORID");
        public final static Property AgentId = new Property(19, int.class, "agentId", false, "AGENT_ID");
        public final static Property AgentGrade = new Property(20, int.class, "agentGrade", false, "AGENT_GRADE");
        public final static Property SysAgentGrade = new Property(21, int.class, "sysAgentGrade", false, "SYS_AGENT_GRADE");
    }


    public UserInfoDao(DaoConfig config) {
        super(config);
    }
    
    public UserInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: userid
                "\"ACCOUNT\" TEXT," + // 1: account
                "\"NICKNAME\" TEXT," + // 2: nickname
                "\"MONEY\" INTEGER NOT NULL ," + // 3: money
                "\"HEADIMGURL\" TEXT," + // 4: headimgurl
                "\"MOBILEPHONE\" TEXT," + // 5: mobilephone
                "\"SEX\" INTEGER NOT NULL ," + // 6: sex
                "\"CHANNEL\" INTEGER NOT NULL ," + // 7: channel
                "\"BIRTHDAY\" TEXT," + // 8: birthday
                "\"SYNOPSIS\" TEXT," + // 9: synopsis
                "\"PROVINCE\" TEXT," + // 10: province
                "\"CITY\" TEXT," + // 11: city
                "\"COIN\" INTEGER NOT NULL ," + // 12: coin
                "\"NEWVIP\" INTEGER NOT NULL ," + // 13: newvip
                "\"REGTYPE\" INTEGER NOT NULL ," + // 14: regtype
                "\"CASHMONEY\" REAL NOT NULL ," + // 15: cashmoney
                "\"RECEIVE\" INTEGER NOT NULL ," + // 16: receive
                "\"READ_TIME\" INTEGER NOT NULL ," + // 17: readTime
                "\"AUTHORID\" INTEGER NOT NULL ," + // 18: authorid
                "\"AGENT_ID\" INTEGER NOT NULL ," + // 19: agentId
                "\"AGENT_GRADE\" INTEGER NOT NULL ," + // 20: agentGrade
                "\"SYS_AGENT_GRADE\" INTEGER NOT NULL );"); // 21: sysAgentGrade
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, UserInfo entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getUserid());
 
        String account = entity.getAccount();
        if (account != null) {
            stmt.bindString(2, account);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(3, nickname);
        }
        stmt.bindLong(4, entity.getMoney());
 
        String headimgurl = entity.getHeadimgurl();
        if (headimgurl != null) {
            stmt.bindString(5, headimgurl);
        }
 
        String mobilephone = entity.getMobilephone();
        if (mobilephone != null) {
            stmt.bindString(6, mobilephone);
        }
        stmt.bindLong(7, entity.getSex());
        stmt.bindLong(8, entity.getChannel());
 
        String birthday = entity.getBirthday();
        if (birthday != null) {
            stmt.bindString(9, birthday);
        }
 
        String synopsis = entity.getSynopsis();
        if (synopsis != null) {
            stmt.bindString(10, synopsis);
        }
 
        String province = entity.getProvince();
        if (province != null) {
            stmt.bindString(11, province);
        }
 
        String city = entity.getCity();
        if (city != null) {
            stmt.bindString(12, city);
        }
        stmt.bindLong(13, entity.getCoin());
        stmt.bindLong(14, entity.getNewvip());
        stmt.bindLong(15, entity.getRegtype());
        stmt.bindDouble(16, entity.getCashmoney());
        stmt.bindLong(17, entity.getReceive());
        stmt.bindLong(18, entity.getReadTime());
        stmt.bindLong(19, entity.getAuthorid());
        stmt.bindLong(20, entity.getAgentId());
        stmt.bindLong(21, entity.getAgentGrade());
        stmt.bindLong(22, entity.getSysAgentGrade());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, UserInfo entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getUserid());
 
        String account = entity.getAccount();
        if (account != null) {
            stmt.bindString(2, account);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(3, nickname);
        }
        stmt.bindLong(4, entity.getMoney());
 
        String headimgurl = entity.getHeadimgurl();
        if (headimgurl != null) {
            stmt.bindString(5, headimgurl);
        }
 
        String mobilephone = entity.getMobilephone();
        if (mobilephone != null) {
            stmt.bindString(6, mobilephone);
        }
        stmt.bindLong(7, entity.getSex());
        stmt.bindLong(8, entity.getChannel());
 
        String birthday = entity.getBirthday();
        if (birthday != null) {
            stmt.bindString(9, birthday);
        }
 
        String synopsis = entity.getSynopsis();
        if (synopsis != null) {
            stmt.bindString(10, synopsis);
        }
 
        String province = entity.getProvince();
        if (province != null) {
            stmt.bindString(11, province);
        }
 
        String city = entity.getCity();
        if (city != null) {
            stmt.bindString(12, city);
        }
        stmt.bindLong(13, entity.getCoin());
        stmt.bindLong(14, entity.getNewvip());
        stmt.bindLong(15, entity.getRegtype());
        stmt.bindDouble(16, entity.getCashmoney());
        stmt.bindLong(17, entity.getReceive());
        stmt.bindLong(18, entity.getReadTime());
        stmt.bindLong(19, entity.getAuthorid());
        stmt.bindLong(20, entity.getAgentId());
        stmt.bindLong(21, entity.getAgentGrade());
        stmt.bindLong(22, entity.getSysAgentGrade());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public UserInfo readEntity(Cursor cursor, int offset) {
        UserInfo entity = new UserInfo( //
            cursor.getLong(offset + 0), // userid
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // account
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // nickname
            cursor.getLong(offset + 3), // money
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // headimgurl
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // mobilephone
            cursor.getInt(offset + 6), // sex
            cursor.getInt(offset + 7), // channel
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // birthday
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // synopsis
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // province
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // city
            cursor.getInt(offset + 12), // coin
            cursor.getInt(offset + 13), // newvip
            cursor.getInt(offset + 14), // regtype
            cursor.getDouble(offset + 15), // cashmoney
            cursor.getInt(offset + 16), // receive
            cursor.getLong(offset + 17), // readTime
            cursor.getInt(offset + 18), // authorid
            cursor.getInt(offset + 19), // agentId
            cursor.getInt(offset + 20), // agentGrade
            cursor.getInt(offset + 21) // sysAgentGrade
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, UserInfo entity, int offset) {
        entity.setUserid(cursor.getLong(offset + 0));
        entity.setAccount(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNickname(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMoney(cursor.getLong(offset + 3));
        entity.setHeadimgurl(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setMobilephone(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setSex(cursor.getInt(offset + 6));
        entity.setChannel(cursor.getInt(offset + 7));
        entity.setBirthday(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setSynopsis(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setProvince(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setCity(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setCoin(cursor.getInt(offset + 12));
        entity.setNewvip(cursor.getInt(offset + 13));
        entity.setRegtype(cursor.getInt(offset + 14));
        entity.setCashmoney(cursor.getDouble(offset + 15));
        entity.setReceive(cursor.getInt(offset + 16));
        entity.setReadTime(cursor.getLong(offset + 17));
        entity.setAuthorid(cursor.getInt(offset + 18));
        entity.setAgentId(cursor.getInt(offset + 19));
        entity.setAgentGrade(cursor.getInt(offset + 20));
        entity.setSysAgentGrade(cursor.getInt(offset + 21));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(UserInfo entity, long rowId) {
        entity.setUserid(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(UserInfo entity) {
        if(entity != null) {
            return entity.getUserid();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(UserInfo entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}