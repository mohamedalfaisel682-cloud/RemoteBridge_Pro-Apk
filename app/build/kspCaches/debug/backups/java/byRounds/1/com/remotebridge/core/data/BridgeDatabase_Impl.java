package com.remotebridge.core.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.remotebridge.core.data.dao.CachedFileDao;
import com.remotebridge.core.data.dao.CachedFileDao_Impl;
import com.remotebridge.core.data.dao.CachedSmsDao;
import com.remotebridge.core.data.dao.CachedSmsDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BridgeDatabase_Impl extends BridgeDatabase {
  private volatile CachedFileDao _cachedFileDao;

  private volatile CachedSmsDao _cachedSmsDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `cached_files` (`rowId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `remoteFileId` INTEGER NOT NULL, `name` TEXT NOT NULL, `size` INTEGER NOT NULL, `mimeType` TEXT NOT NULL, `dateAdded` INTEGER NOT NULL, `bucket` TEXT NOT NULL, `localPath` TEXT, `downloadedAt` INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `cached_sms` (`rowId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `remoteSmsId` INTEGER NOT NULL, `threadId` INTEGER NOT NULL, `address` TEXT NOT NULL, `body` TEXT NOT NULL, `date` INTEGER NOT NULL, `type` INTEGER NOT NULL, `contactName` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'ade3a0427147a3445626d9d545dc9129')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `cached_files`");
        db.execSQL("DROP TABLE IF EXISTS `cached_sms`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsCachedFiles = new HashMap<String, TableInfo.Column>(9);
        _columnsCachedFiles.put("rowId", new TableInfo.Column("rowId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedFiles.put("remoteFileId", new TableInfo.Column("remoteFileId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedFiles.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedFiles.put("size", new TableInfo.Column("size", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedFiles.put("mimeType", new TableInfo.Column("mimeType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedFiles.put("dateAdded", new TableInfo.Column("dateAdded", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedFiles.put("bucket", new TableInfo.Column("bucket", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedFiles.put("localPath", new TableInfo.Column("localPath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedFiles.put("downloadedAt", new TableInfo.Column("downloadedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCachedFiles = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCachedFiles = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCachedFiles = new TableInfo("cached_files", _columnsCachedFiles, _foreignKeysCachedFiles, _indicesCachedFiles);
        final TableInfo _existingCachedFiles = TableInfo.read(db, "cached_files");
        if (!_infoCachedFiles.equals(_existingCachedFiles)) {
          return new RoomOpenHelper.ValidationResult(false, "cached_files(com.remotebridge.core.data.entity.CachedFileEntity).\n"
                  + " Expected:\n" + _infoCachedFiles + "\n"
                  + " Found:\n" + _existingCachedFiles);
        }
        final HashMap<String, TableInfo.Column> _columnsCachedSms = new HashMap<String, TableInfo.Column>(8);
        _columnsCachedSms.put("rowId", new TableInfo.Column("rowId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedSms.put("remoteSmsId", new TableInfo.Column("remoteSmsId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedSms.put("threadId", new TableInfo.Column("threadId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedSms.put("address", new TableInfo.Column("address", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedSms.put("body", new TableInfo.Column("body", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedSms.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedSms.put("type", new TableInfo.Column("type", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedSms.put("contactName", new TableInfo.Column("contactName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCachedSms = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCachedSms = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCachedSms = new TableInfo("cached_sms", _columnsCachedSms, _foreignKeysCachedSms, _indicesCachedSms);
        final TableInfo _existingCachedSms = TableInfo.read(db, "cached_sms");
        if (!_infoCachedSms.equals(_existingCachedSms)) {
          return new RoomOpenHelper.ValidationResult(false, "cached_sms(com.remotebridge.core.data.entity.CachedSmsEntity).\n"
                  + " Expected:\n" + _infoCachedSms + "\n"
                  + " Found:\n" + _existingCachedSms);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "ade3a0427147a3445626d9d545dc9129", "c2fc12c6db90d28b602554dbbb7a00dc");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "cached_files","cached_sms");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `cached_files`");
      _db.execSQL("DELETE FROM `cached_sms`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(CachedFileDao.class, CachedFileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CachedSmsDao.class, CachedSmsDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public CachedFileDao cachedFileDao() {
    if (_cachedFileDao != null) {
      return _cachedFileDao;
    } else {
      synchronized(this) {
        if(_cachedFileDao == null) {
          _cachedFileDao = new CachedFileDao_Impl(this);
        }
        return _cachedFileDao;
      }
    }
  }

  @Override
  public CachedSmsDao cachedSmsDao() {
    if (_cachedSmsDao != null) {
      return _cachedSmsDao;
    } else {
      synchronized(this) {
        if(_cachedSmsDao == null) {
          _cachedSmsDao = new CachedSmsDao_Impl(this);
        }
        return _cachedSmsDao;
      }
    }
  }
}
