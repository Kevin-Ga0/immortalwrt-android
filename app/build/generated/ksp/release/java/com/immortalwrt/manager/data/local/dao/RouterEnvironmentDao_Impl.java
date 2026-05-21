package com.immortalwrt.manager.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.immortalwrt.manager.data.local.Converters;
import com.immortalwrt.manager.data.local.entity.RouterEnvironmentEntity;
import com.immortalwrt.manager.domain.model.EndpointMode;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RouterEnvironmentDao_Impl implements RouterEnvironmentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RouterEnvironmentEntity> __insertionAdapterOfRouterEnvironmentEntity;

  private final Converters __converters = new Converters();

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public RouterEnvironmentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRouterEnvironmentEntity = new EntityInsertionAdapter<RouterEnvironmentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `router_environments` (`router_id`,`distribution`,`version`,`revision`,`kernel_version`,`luci_available`,`rpcd_available`,`endpoint_mode`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RouterEnvironmentEntity entity) {
        statement.bindString(1, entity.getRouterId());
        if (entity.getDistribution() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getDistribution());
        }
        if (entity.getVersion() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getVersion());
        }
        if (entity.getRevision() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getRevision());
        }
        if (entity.getKernelVersion() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getKernelVersion());
        }
        final Integer _tmp = entity.getLuciAvailable() == null ? null : (entity.getLuciAvailable() ? 1 : 0);
        if (_tmp == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, _tmp);
        }
        final Integer _tmp_1 = entity.getRpcdAvailable() == null ? null : (entity.getRpcdAvailable() ? 1 : 0);
        if (_tmp_1 == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, _tmp_1);
        }
        final String _tmp_2;
        if (entity.getEndpointMode() == null) {
          _tmp_2 = null;
        } else {
          _tmp_2 = __converters.fromEndpointMode(entity.getEndpointMode());
        }
        if (_tmp_2 == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp_2);
        }
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM router_environments WHERE router_id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsert(final RouterEnvironmentEntity env,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRouterEnvironmentEntity.insert(env);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final String routerId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, routerId);
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
  public Object getById(final String routerId,
      final Continuation<? super RouterEnvironmentEntity> $completion) {
    final String _sql = "SELECT * FROM router_environments WHERE router_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, routerId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<RouterEnvironmentEntity>() {
      @Override
      @Nullable
      public RouterEnvironmentEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRouterId = CursorUtil.getColumnIndexOrThrow(_cursor, "router_id");
          final int _cursorIndexOfDistribution = CursorUtil.getColumnIndexOrThrow(_cursor, "distribution");
          final int _cursorIndexOfVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "version");
          final int _cursorIndexOfRevision = CursorUtil.getColumnIndexOrThrow(_cursor, "revision");
          final int _cursorIndexOfKernelVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "kernel_version");
          final int _cursorIndexOfLuciAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "luci_available");
          final int _cursorIndexOfRpcdAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "rpcd_available");
          final int _cursorIndexOfEndpointMode = CursorUtil.getColumnIndexOrThrow(_cursor, "endpoint_mode");
          final RouterEnvironmentEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpRouterId;
            _tmpRouterId = _cursor.getString(_cursorIndexOfRouterId);
            final String _tmpDistribution;
            if (_cursor.isNull(_cursorIndexOfDistribution)) {
              _tmpDistribution = null;
            } else {
              _tmpDistribution = _cursor.getString(_cursorIndexOfDistribution);
            }
            final String _tmpVersion;
            if (_cursor.isNull(_cursorIndexOfVersion)) {
              _tmpVersion = null;
            } else {
              _tmpVersion = _cursor.getString(_cursorIndexOfVersion);
            }
            final String _tmpRevision;
            if (_cursor.isNull(_cursorIndexOfRevision)) {
              _tmpRevision = null;
            } else {
              _tmpRevision = _cursor.getString(_cursorIndexOfRevision);
            }
            final String _tmpKernelVersion;
            if (_cursor.isNull(_cursorIndexOfKernelVersion)) {
              _tmpKernelVersion = null;
            } else {
              _tmpKernelVersion = _cursor.getString(_cursorIndexOfKernelVersion);
            }
            final Boolean _tmpLuciAvailable;
            final Integer _tmp;
            if (_cursor.isNull(_cursorIndexOfLuciAvailable)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(_cursorIndexOfLuciAvailable);
            }
            _tmpLuciAvailable = _tmp == null ? null : _tmp != 0;
            final Boolean _tmpRpcdAvailable;
            final Integer _tmp_1;
            if (_cursor.isNull(_cursorIndexOfRpcdAvailable)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getInt(_cursorIndexOfRpcdAvailable);
            }
            _tmpRpcdAvailable = _tmp_1 == null ? null : _tmp_1 != 0;
            final EndpointMode _tmpEndpointMode;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfEndpointMode)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfEndpointMode);
            }
            if (_tmp_2 == null) {
              _tmpEndpointMode = null;
            } else {
              _tmpEndpointMode = __converters.toEndpointMode(_tmp_2);
            }
            _result = new RouterEnvironmentEntity(_tmpRouterId,_tmpDistribution,_tmpVersion,_tmpRevision,_tmpKernelVersion,_tmpLuciAvailable,_tmpRpcdAvailable,_tmpEndpointMode);
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
