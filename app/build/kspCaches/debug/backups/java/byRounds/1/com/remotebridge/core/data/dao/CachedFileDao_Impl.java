package com.remotebridge.core.data.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.remotebridge.core.data.entity.CachedFileEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CachedFileDao_Impl implements CachedFileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CachedFileEntity> __insertionAdapterOfCachedFileEntity;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  private final SharedSQLiteStatement __preparedStmtOfClear;

  public CachedFileDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCachedFileEntity = new EntityInsertionAdapter<CachedFileEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `cached_files` (`rowId`,`remoteFileId`,`name`,`size`,`mimeType`,`dateAdded`,`bucket`,`localPath`,`downloadedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CachedFileEntity entity) {
        statement.bindLong(1, entity.getRowId());
        statement.bindLong(2, entity.getRemoteFileId());
        statement.bindString(3, entity.getName());
        statement.bindLong(4, entity.getSize());
        statement.bindString(5, entity.getMimeType());
        statement.bindLong(6, entity.getDateAdded());
        statement.bindString(7, entity.getBucket());
        if (entity.getLocalPath() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getLocalPath());
        }
        if (entity.getDownloadedAt() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getDownloadedAt());
        }
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM cached_files WHERE rowId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClear = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM cached_files";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<CachedFileEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCachedFileEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final long rowId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, rowId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDelete.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clear(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClear.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClear.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CachedFileEntity>> getAll() {
    final String _sql = "SELECT * FROM cached_files ORDER BY dateAdded DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cached_files"}, new Callable<List<CachedFileEntity>>() {
      @Override
      @NonNull
      public List<CachedFileEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRowId = CursorUtil.getColumnIndexOrThrow(_cursor, "rowId");
          final int _cursorIndexOfRemoteFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteFileId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSize = CursorUtil.getColumnIndexOrThrow(_cursor, "size");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfDateAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "dateAdded");
          final int _cursorIndexOfBucket = CursorUtil.getColumnIndexOrThrow(_cursor, "bucket");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "localPath");
          final int _cursorIndexOfDownloadedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadedAt");
          final List<CachedFileEntity> _result = new ArrayList<CachedFileEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CachedFileEntity _item;
            final long _tmpRowId;
            _tmpRowId = _cursor.getLong(_cursorIndexOfRowId);
            final long _tmpRemoteFileId;
            _tmpRemoteFileId = _cursor.getLong(_cursorIndexOfRemoteFileId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final long _tmpSize;
            _tmpSize = _cursor.getLong(_cursorIndexOfSize);
            final String _tmpMimeType;
            _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            final long _tmpDateAdded;
            _tmpDateAdded = _cursor.getLong(_cursorIndexOfDateAdded);
            final String _tmpBucket;
            _tmpBucket = _cursor.getString(_cursorIndexOfBucket);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final Long _tmpDownloadedAt;
            if (_cursor.isNull(_cursorIndexOfDownloadedAt)) {
              _tmpDownloadedAt = null;
            } else {
              _tmpDownloadedAt = _cursor.getLong(_cursorIndexOfDownloadedAt);
            }
            _item = new CachedFileEntity(_tmpRowId,_tmpRemoteFileId,_tmpName,_tmpSize,_tmpMimeType,_tmpDateAdded,_tmpBucket,_tmpLocalPath,_tmpDownloadedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<CachedFileEntity>> getByBucket(final String bucket) {
    final String _sql = "SELECT * FROM cached_files WHERE bucket = ? ORDER BY dateAdded DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, bucket);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cached_files"}, new Callable<List<CachedFileEntity>>() {
      @Override
      @NonNull
      public List<CachedFileEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRowId = CursorUtil.getColumnIndexOrThrow(_cursor, "rowId");
          final int _cursorIndexOfRemoteFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteFileId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSize = CursorUtil.getColumnIndexOrThrow(_cursor, "size");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfDateAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "dateAdded");
          final int _cursorIndexOfBucket = CursorUtil.getColumnIndexOrThrow(_cursor, "bucket");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "localPath");
          final int _cursorIndexOfDownloadedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadedAt");
          final List<CachedFileEntity> _result = new ArrayList<CachedFileEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CachedFileEntity _item;
            final long _tmpRowId;
            _tmpRowId = _cursor.getLong(_cursorIndexOfRowId);
            final long _tmpRemoteFileId;
            _tmpRemoteFileId = _cursor.getLong(_cursorIndexOfRemoteFileId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final long _tmpSize;
            _tmpSize = _cursor.getLong(_cursorIndexOfSize);
            final String _tmpMimeType;
            _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            final long _tmpDateAdded;
            _tmpDateAdded = _cursor.getLong(_cursorIndexOfDateAdded);
            final String _tmpBucket;
            _tmpBucket = _cursor.getString(_cursorIndexOfBucket);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final Long _tmpDownloadedAt;
            if (_cursor.isNull(_cursorIndexOfDownloadedAt)) {
              _tmpDownloadedAt = null;
            } else {
              _tmpDownloadedAt = _cursor.getLong(_cursorIndexOfDownloadedAt);
            }
            _item = new CachedFileEntity(_tmpRowId,_tmpRemoteFileId,_tmpName,_tmpSize,_tmpMimeType,_tmpDateAdded,_tmpBucket,_tmpLocalPath,_tmpDownloadedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<CachedFileEntity>> getDownloaded() {
    final String _sql = "SELECT * FROM cached_files WHERE localPath IS NOT NULL ORDER BY downloadedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cached_files"}, new Callable<List<CachedFileEntity>>() {
      @Override
      @NonNull
      public List<CachedFileEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRowId = CursorUtil.getColumnIndexOrThrow(_cursor, "rowId");
          final int _cursorIndexOfRemoteFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteFileId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSize = CursorUtil.getColumnIndexOrThrow(_cursor, "size");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfDateAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "dateAdded");
          final int _cursorIndexOfBucket = CursorUtil.getColumnIndexOrThrow(_cursor, "bucket");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "localPath");
          final int _cursorIndexOfDownloadedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadedAt");
          final List<CachedFileEntity> _result = new ArrayList<CachedFileEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CachedFileEntity _item;
            final long _tmpRowId;
            _tmpRowId = _cursor.getLong(_cursorIndexOfRowId);
            final long _tmpRemoteFileId;
            _tmpRemoteFileId = _cursor.getLong(_cursorIndexOfRemoteFileId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final long _tmpSize;
            _tmpSize = _cursor.getLong(_cursorIndexOfSize);
            final String _tmpMimeType;
            _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            final long _tmpDateAdded;
            _tmpDateAdded = _cursor.getLong(_cursorIndexOfDateAdded);
            final String _tmpBucket;
            _tmpBucket = _cursor.getString(_cursorIndexOfBucket);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final Long _tmpDownloadedAt;
            if (_cursor.isNull(_cursorIndexOfDownloadedAt)) {
              _tmpDownloadedAt = null;
            } else {
              _tmpDownloadedAt = _cursor.getLong(_cursorIndexOfDownloadedAt);
            }
            _item = new CachedFileEntity(_tmpRowId,_tmpRemoteFileId,_tmpName,_tmpSize,_tmpMimeType,_tmpDateAdded,_tmpBucket,_tmpLocalPath,_tmpDownloadedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
