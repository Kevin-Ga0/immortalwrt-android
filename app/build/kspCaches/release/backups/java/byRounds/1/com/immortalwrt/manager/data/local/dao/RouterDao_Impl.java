package com.immortalwrt.manager.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.immortalwrt.manager.data.local.Converters;
import com.immortalwrt.manager.data.local.entity.RouterEntity;
import com.immortalwrt.manager.domain.model.RouterScheme;
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
public final class RouterDao_Impl implements RouterDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RouterEntity> __insertionAdapterOfRouterEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<RouterEntity> __deletionAdapterOfRouterEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public RouterDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRouterEntity = new EntityInsertionAdapter<RouterEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `routers` (`id`,`name`,`host`,`port`,`scheme`,`endpoint`,`username`,`passwordSecretRef`,`certificateFingerprint`,`http_risk_accepted`,`last_success_at`,`last_failure_reason`,`created_at`,`updated_at`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RouterEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getHost());
        statement.bindLong(4, entity.getPort());
        final String _tmp = __converters.fromRouterScheme(entity.getScheme());
        statement.bindString(5, _tmp);
        if (entity.getEndpoint() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getEndpoint());
        }
        statement.bindString(7, entity.getUsername());
        statement.bindString(8, entity.getPasswordSecretRef());
        if (entity.getCertificateFingerprint() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getCertificateFingerprint());
        }
        final int _tmp_1 = entity.getHttpRiskAccepted() ? 1 : 0;
        statement.bindLong(10, _tmp_1);
        if (entity.getLastSuccessAt() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getLastSuccessAt());
        }
        if (entity.getLastFailureReason() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getLastFailureReason());
        }
        statement.bindLong(13, entity.getCreatedAt());
        statement.bindLong(14, entity.getUpdatedAt());
      }
    };
    this.__deletionAdapterOfRouterEntity = new EntityDeletionOrUpdateAdapter<RouterEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `routers` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RouterEntity entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM routers WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsert(final RouterEntity router, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRouterEntity.insert(router);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final RouterEntity router, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRouterEntity.handle(router);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
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
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<RouterEntity>> observeAll() {
    final String _sql = "SELECT * FROM routers ORDER BY created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"routers"}, new Callable<List<RouterEntity>>() {
      @Override
      @NonNull
      public List<RouterEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfHost = CursorUtil.getColumnIndexOrThrow(_cursor, "host");
          final int _cursorIndexOfPort = CursorUtil.getColumnIndexOrThrow(_cursor, "port");
          final int _cursorIndexOfScheme = CursorUtil.getColumnIndexOrThrow(_cursor, "scheme");
          final int _cursorIndexOfEndpoint = CursorUtil.getColumnIndexOrThrow(_cursor, "endpoint");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfPasswordSecretRef = CursorUtil.getColumnIndexOrThrow(_cursor, "passwordSecretRef");
          final int _cursorIndexOfCertificateFingerprint = CursorUtil.getColumnIndexOrThrow(_cursor, "certificateFingerprint");
          final int _cursorIndexOfHttpRiskAccepted = CursorUtil.getColumnIndexOrThrow(_cursor, "http_risk_accepted");
          final int _cursorIndexOfLastSuccessAt = CursorUtil.getColumnIndexOrThrow(_cursor, "last_success_at");
          final int _cursorIndexOfLastFailureReason = CursorUtil.getColumnIndexOrThrow(_cursor, "last_failure_reason");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<RouterEntity> _result = new ArrayList<RouterEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RouterEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpHost;
            _tmpHost = _cursor.getString(_cursorIndexOfHost);
            final int _tmpPort;
            _tmpPort = _cursor.getInt(_cursorIndexOfPort);
            final RouterScheme _tmpScheme;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfScheme);
            _tmpScheme = __converters.toRouterScheme(_tmp);
            final String _tmpEndpoint;
            if (_cursor.isNull(_cursorIndexOfEndpoint)) {
              _tmpEndpoint = null;
            } else {
              _tmpEndpoint = _cursor.getString(_cursorIndexOfEndpoint);
            }
            final String _tmpUsername;
            _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            final String _tmpPasswordSecretRef;
            _tmpPasswordSecretRef = _cursor.getString(_cursorIndexOfPasswordSecretRef);
            final String _tmpCertificateFingerprint;
            if (_cursor.isNull(_cursorIndexOfCertificateFingerprint)) {
              _tmpCertificateFingerprint = null;
            } else {
              _tmpCertificateFingerprint = _cursor.getString(_cursorIndexOfCertificateFingerprint);
            }
            final boolean _tmpHttpRiskAccepted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHttpRiskAccepted);
            _tmpHttpRiskAccepted = _tmp_1 != 0;
            final Long _tmpLastSuccessAt;
            if (_cursor.isNull(_cursorIndexOfLastSuccessAt)) {
              _tmpLastSuccessAt = null;
            } else {
              _tmpLastSuccessAt = _cursor.getLong(_cursorIndexOfLastSuccessAt);
            }
            final String _tmpLastFailureReason;
            if (_cursor.isNull(_cursorIndexOfLastFailureReason)) {
              _tmpLastFailureReason = null;
            } else {
              _tmpLastFailureReason = _cursor.getString(_cursorIndexOfLastFailureReason);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new RouterEntity(_tmpId,_tmpName,_tmpHost,_tmpPort,_tmpScheme,_tmpEndpoint,_tmpUsername,_tmpPasswordSecretRef,_tmpCertificateFingerprint,_tmpHttpRiskAccepted,_tmpLastSuccessAt,_tmpLastFailureReason,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getById(final String id, final Continuation<? super RouterEntity> $completion) {
    final String _sql = "SELECT * FROM routers WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<RouterEntity>() {
      @Override
      @Nullable
      public RouterEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfHost = CursorUtil.getColumnIndexOrThrow(_cursor, "host");
          final int _cursorIndexOfPort = CursorUtil.getColumnIndexOrThrow(_cursor, "port");
          final int _cursorIndexOfScheme = CursorUtil.getColumnIndexOrThrow(_cursor, "scheme");
          final int _cursorIndexOfEndpoint = CursorUtil.getColumnIndexOrThrow(_cursor, "endpoint");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfPasswordSecretRef = CursorUtil.getColumnIndexOrThrow(_cursor, "passwordSecretRef");
          final int _cursorIndexOfCertificateFingerprint = CursorUtil.getColumnIndexOrThrow(_cursor, "certificateFingerprint");
          final int _cursorIndexOfHttpRiskAccepted = CursorUtil.getColumnIndexOrThrow(_cursor, "http_risk_accepted");
          final int _cursorIndexOfLastSuccessAt = CursorUtil.getColumnIndexOrThrow(_cursor, "last_success_at");
          final int _cursorIndexOfLastFailureReason = CursorUtil.getColumnIndexOrThrow(_cursor, "last_failure_reason");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final RouterEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpHost;
            _tmpHost = _cursor.getString(_cursorIndexOfHost);
            final int _tmpPort;
            _tmpPort = _cursor.getInt(_cursorIndexOfPort);
            final RouterScheme _tmpScheme;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfScheme);
            _tmpScheme = __converters.toRouterScheme(_tmp);
            final String _tmpEndpoint;
            if (_cursor.isNull(_cursorIndexOfEndpoint)) {
              _tmpEndpoint = null;
            } else {
              _tmpEndpoint = _cursor.getString(_cursorIndexOfEndpoint);
            }
            final String _tmpUsername;
            _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            final String _tmpPasswordSecretRef;
            _tmpPasswordSecretRef = _cursor.getString(_cursorIndexOfPasswordSecretRef);
            final String _tmpCertificateFingerprint;
            if (_cursor.isNull(_cursorIndexOfCertificateFingerprint)) {
              _tmpCertificateFingerprint = null;
            } else {
              _tmpCertificateFingerprint = _cursor.getString(_cursorIndexOfCertificateFingerprint);
            }
            final boolean _tmpHttpRiskAccepted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHttpRiskAccepted);
            _tmpHttpRiskAccepted = _tmp_1 != 0;
            final Long _tmpLastSuccessAt;
            if (_cursor.isNull(_cursorIndexOfLastSuccessAt)) {
              _tmpLastSuccessAt = null;
            } else {
              _tmpLastSuccessAt = _cursor.getLong(_cursorIndexOfLastSuccessAt);
            }
            final String _tmpLastFailureReason;
            if (_cursor.isNull(_cursorIndexOfLastFailureReason)) {
              _tmpLastFailureReason = null;
            } else {
              _tmpLastFailureReason = _cursor.getString(_cursorIndexOfLastFailureReason);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new RouterEntity(_tmpId,_tmpName,_tmpHost,_tmpPort,_tmpScheme,_tmpEndpoint,_tmpUsername,_tmpPasswordSecretRef,_tmpCertificateFingerprint,_tmpHttpRiskAccepted,_tmpLastSuccessAt,_tmpLastFailureReason,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
