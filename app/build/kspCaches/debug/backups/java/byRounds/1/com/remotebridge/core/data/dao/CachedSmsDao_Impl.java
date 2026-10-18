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
import com.remotebridge.core.data.entity.CachedSmsEntity;
import java.lang.Class;
import java.lang.Exception;
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
public final class CachedSmsDao_Impl implements CachedSmsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CachedSmsEntity> __insertionAdapterOfCachedSmsEntity;

  private final SharedSQLiteStatement __preparedStmtOfClear;

  public CachedSmsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCachedSmsEntity = new EntityInsertionAdapter<CachedSmsEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `cached_sms` (`rowId`,`remoteSmsId`,`threadId`,`address`,`body`,`date`,`type`,`contactName`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CachedSmsEntity entity) {
        statement.bindLong(1, entity.getRowId());
        statement.bindLong(2, entity.getRemoteSmsId());
        statement.bindLong(3, entity.getThreadId());
        statement.bindString(4, entity.getAddress());
        statement.bindString(5, entity.getBody());
        statement.bindLong(6, entity.getDate());
        statement.bindLong(7, entity.getType());
        if (entity.getContactName() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getContactName());
        }
      }
    };
    this.__preparedStmtOfClear = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM cached_sms";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<CachedSmsEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCachedSmsEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
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
  public Flow<List<CachedSmsEntity>> getAll() {
    final String _sql = "SELECT * FROM cached_sms ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cached_sms"}, new Callable<List<CachedSmsEntity>>() {
      @Override
      @NonNull
      public List<CachedSmsEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRowId = CursorUtil.getColumnIndexOrThrow(_cursor, "rowId");
          final int _cursorIndexOfRemoteSmsId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteSmsId");
          final int _cursorIndexOfThreadId = CursorUtil.getColumnIndexOrThrow(_cursor, "threadId");
          final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
          final int _cursorIndexOfBody = CursorUtil.getColumnIndexOrThrow(_cursor, "body");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfContactName = CursorUtil.getColumnIndexOrThrow(_cursor, "contactName");
          final List<CachedSmsEntity> _result = new ArrayList<CachedSmsEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CachedSmsEntity _item;
            final long _tmpRowId;
            _tmpRowId = _cursor.getLong(_cursorIndexOfRowId);
            final long _tmpRemoteSmsId;
            _tmpRemoteSmsId = _cursor.getLong(_cursorIndexOfRemoteSmsId);
            final long _tmpThreadId;
            _tmpThreadId = _cursor.getLong(_cursorIndexOfThreadId);
            final String _tmpAddress;
            _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
            final String _tmpBody;
            _tmpBody = _cursor.getString(_cursorIndexOfBody);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final int _tmpType;
            _tmpType = _cursor.getInt(_cursorIndexOfType);
            final String _tmpContactName;
            if (_cursor.isNull(_cursorIndexOfContactName)) {
              _tmpContactName = null;
            } else {
              _tmpContactName = _cursor.getString(_cursorIndexOfContactName);
            }
            _item = new CachedSmsEntity(_tmpRowId,_tmpRemoteSmsId,_tmpThreadId,_tmpAddress,_tmpBody,_tmpDate,_tmpType,_tmpContactName);
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
