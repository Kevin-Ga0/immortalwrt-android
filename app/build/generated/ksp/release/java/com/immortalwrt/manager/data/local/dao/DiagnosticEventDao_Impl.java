package com.immortalwrt.manager.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.immortalwrt.manager.data.local.entity.DiagnosticEventEntity;
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
public final class DiagnosticEventDao_Impl implements DiagnosticEventDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DiagnosticEventEntity> __insertionAdapterOfDiagnosticEventEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByRouter;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public DiagnosticEventDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDiagnosticEventEntity = new EntityInsertionAdapter<DiagnosticEventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `diagnostic_events` (`id`,`router_id`,`timestamp`,`layer`,`status`,`message`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DiagnosticEventEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getRouterId());
        statement.bindLong(3, entity.getTimestamp());
        statement.bindString(4, entity.getLayer());
        statement.bindString(5, entity.getStatus());
        statement.bindString(6, entity.getMessage());
      }
    };
    this.__preparedStmtOfDeleteByRouter = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM diagnostic_events WHERE router_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM diagnostic_events";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final DiagnosticEventEntity event,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDiagnosticEventEntity.insert(event);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByRouter(final String router_id,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByRouter.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, router_id);
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
          __preparedStmtOfDeleteByRouter.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
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
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getRecent(final String router_id, final int limit,
      final Continuation<? super List<DiagnosticEventEntity>> $completion) {
    final String _sql = "SELECT * FROM diagnostic_events WHERE router_id = ? ORDER BY timestamp DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, router_id);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<DiagnosticEventEntity>>() {
      @Override
      @NonNull
      public List<DiagnosticEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRouterId = CursorUtil.getColumnIndexOrThrow(_cursor, "router_id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfLayer = CursorUtil.getColumnIndexOrThrow(_cursor, "layer");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final List<DiagnosticEventEntity> _result = new ArrayList<DiagnosticEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DiagnosticEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpRouterId;
            _tmpRouterId = _cursor.getString(_cursorIndexOfRouterId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpLayer;
            _tmpLayer = _cursor.getString(_cursorIndexOfLayer);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            _item = new DiagnosticEventEntity(_tmpId,_tmpRouterId,_tmpTimestamp,_tmpLayer,_tmpStatus,_tmpMessage);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<DiagnosticEventEntity>> observeRecent(final String router_id, final int limit) {
    final String _sql = "SELECT * FROM diagnostic_events WHERE router_id = ? ORDER BY timestamp DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, router_id);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"diagnostic_events"}, new Callable<List<DiagnosticEventEntity>>() {
      @Override
      @NonNull
      public List<DiagnosticEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRouterId = CursorUtil.getColumnIndexOrThrow(_cursor, "router_id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfLayer = CursorUtil.getColumnIndexOrThrow(_cursor, "layer");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final List<DiagnosticEventEntity> _result = new ArrayList<DiagnosticEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DiagnosticEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpRouterId;
            _tmpRouterId = _cursor.getString(_cursorIndexOfRouterId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpLayer;
            _tmpLayer = _cursor.getString(_cursorIndexOfLayer);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            _item = new DiagnosticEventEntity(_tmpId,_tmpRouterId,_tmpTimestamp,_tmpLayer,_tmpStatus,_tmpMessage);
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
