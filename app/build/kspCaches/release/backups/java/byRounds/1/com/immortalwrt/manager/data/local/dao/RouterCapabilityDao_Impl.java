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
import com.immortalwrt.manager.data.local.entity.RouterCapabilityEntity;
import java.lang.Class;
import java.lang.Exception;
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
public final class RouterCapabilityDao_Impl implements RouterCapabilityDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RouterCapabilityEntity> __insertionAdapterOfRouterCapabilityEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public RouterCapabilityDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRouterCapabilityEntity = new EntityInsertionAdapter<RouterCapabilityEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `router_capabilities` (`router_id`,`has_session_login`,`has_system_board`,`has_system_info`,`has_network_interface_dump`,`has_network_interface_status`,`has_network_device_status`,`has_luci_rpc`,`has_host_hints`,`has_dhcp_leases`,`has_realtime_stats`,`has_temp_info`,`can_read_uci`,`can_reboot`,`can_change_password`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RouterCapabilityEntity entity) {
        statement.bindString(1, entity.getRouterId());
        final int _tmp = entity.getHasSessionLogin() ? 1 : 0;
        statement.bindLong(2, _tmp);
        final int _tmp_1 = entity.getHasSystemBoard() ? 1 : 0;
        statement.bindLong(3, _tmp_1);
        final int _tmp_2 = entity.getHasSystemInfo() ? 1 : 0;
        statement.bindLong(4, _tmp_2);
        final int _tmp_3 = entity.getHasNetworkInterfaceDump() ? 1 : 0;
        statement.bindLong(5, _tmp_3);
        final int _tmp_4 = entity.getHasNetworkInterfaceStatus() ? 1 : 0;
        statement.bindLong(6, _tmp_4);
        final int _tmp_5 = entity.getHasNetworkDeviceStatus() ? 1 : 0;
        statement.bindLong(7, _tmp_5);
        final int _tmp_6 = entity.getHasLuciRpc() ? 1 : 0;
        statement.bindLong(8, _tmp_6);
        final int _tmp_7 = entity.getHasHostHints() ? 1 : 0;
        statement.bindLong(9, _tmp_7);
        final int _tmp_8 = entity.getHasDhcpLeases() ? 1 : 0;
        statement.bindLong(10, _tmp_8);
        final int _tmp_9 = entity.getHasRealtimeStats() ? 1 : 0;
        statement.bindLong(11, _tmp_9);
        final int _tmp_10 = entity.getHasTempInfo() ? 1 : 0;
        statement.bindLong(12, _tmp_10);
        final int _tmp_11 = entity.getCanReadUci() ? 1 : 0;
        statement.bindLong(13, _tmp_11);
        final int _tmp_12 = entity.getCanReboot() ? 1 : 0;
        statement.bindLong(14, _tmp_12);
        final int _tmp_13 = entity.getCanChangePassword() ? 1 : 0;
        statement.bindLong(15, _tmp_13);
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM router_capabilities WHERE router_id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsert(final RouterCapabilityEntity cap,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRouterCapabilityEntity.insert(cap);
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
      final Continuation<? super RouterCapabilityEntity> $completion) {
    final String _sql = "SELECT * FROM router_capabilities WHERE router_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, routerId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<RouterCapabilityEntity>() {
      @Override
      @Nullable
      public RouterCapabilityEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRouterId = CursorUtil.getColumnIndexOrThrow(_cursor, "router_id");
          final int _cursorIndexOfHasSessionLogin = CursorUtil.getColumnIndexOrThrow(_cursor, "has_session_login");
          final int _cursorIndexOfHasSystemBoard = CursorUtil.getColumnIndexOrThrow(_cursor, "has_system_board");
          final int _cursorIndexOfHasSystemInfo = CursorUtil.getColumnIndexOrThrow(_cursor, "has_system_info");
          final int _cursorIndexOfHasNetworkInterfaceDump = CursorUtil.getColumnIndexOrThrow(_cursor, "has_network_interface_dump");
          final int _cursorIndexOfHasNetworkInterfaceStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "has_network_interface_status");
          final int _cursorIndexOfHasNetworkDeviceStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "has_network_device_status");
          final int _cursorIndexOfHasLuciRpc = CursorUtil.getColumnIndexOrThrow(_cursor, "has_luci_rpc");
          final int _cursorIndexOfHasHostHints = CursorUtil.getColumnIndexOrThrow(_cursor, "has_host_hints");
          final int _cursorIndexOfHasDhcpLeases = CursorUtil.getColumnIndexOrThrow(_cursor, "has_dhcp_leases");
          final int _cursorIndexOfHasRealtimeStats = CursorUtil.getColumnIndexOrThrow(_cursor, "has_realtime_stats");
          final int _cursorIndexOfHasTempInfo = CursorUtil.getColumnIndexOrThrow(_cursor, "has_temp_info");
          final int _cursorIndexOfCanReadUci = CursorUtil.getColumnIndexOrThrow(_cursor, "can_read_uci");
          final int _cursorIndexOfCanReboot = CursorUtil.getColumnIndexOrThrow(_cursor, "can_reboot");
          final int _cursorIndexOfCanChangePassword = CursorUtil.getColumnIndexOrThrow(_cursor, "can_change_password");
          final RouterCapabilityEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpRouterId;
            _tmpRouterId = _cursor.getString(_cursorIndexOfRouterId);
            final boolean _tmpHasSessionLogin;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfHasSessionLogin);
            _tmpHasSessionLogin = _tmp != 0;
            final boolean _tmpHasSystemBoard;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHasSystemBoard);
            _tmpHasSystemBoard = _tmp_1 != 0;
            final boolean _tmpHasSystemInfo;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfHasSystemInfo);
            _tmpHasSystemInfo = _tmp_2 != 0;
            final boolean _tmpHasNetworkInterfaceDump;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfHasNetworkInterfaceDump);
            _tmpHasNetworkInterfaceDump = _tmp_3 != 0;
            final boolean _tmpHasNetworkInterfaceStatus;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfHasNetworkInterfaceStatus);
            _tmpHasNetworkInterfaceStatus = _tmp_4 != 0;
            final boolean _tmpHasNetworkDeviceStatus;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfHasNetworkDeviceStatus);
            _tmpHasNetworkDeviceStatus = _tmp_5 != 0;
            final boolean _tmpHasLuciRpc;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfHasLuciRpc);
            _tmpHasLuciRpc = _tmp_6 != 0;
            final boolean _tmpHasHostHints;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfHasHostHints);
            _tmpHasHostHints = _tmp_7 != 0;
            final boolean _tmpHasDhcpLeases;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfHasDhcpLeases);
            _tmpHasDhcpLeases = _tmp_8 != 0;
            final boolean _tmpHasRealtimeStats;
            final int _tmp_9;
            _tmp_9 = _cursor.getInt(_cursorIndexOfHasRealtimeStats);
            _tmpHasRealtimeStats = _tmp_9 != 0;
            final boolean _tmpHasTempInfo;
            final int _tmp_10;
            _tmp_10 = _cursor.getInt(_cursorIndexOfHasTempInfo);
            _tmpHasTempInfo = _tmp_10 != 0;
            final boolean _tmpCanReadUci;
            final int _tmp_11;
            _tmp_11 = _cursor.getInt(_cursorIndexOfCanReadUci);
            _tmpCanReadUci = _tmp_11 != 0;
            final boolean _tmpCanReboot;
            final int _tmp_12;
            _tmp_12 = _cursor.getInt(_cursorIndexOfCanReboot);
            _tmpCanReboot = _tmp_12 != 0;
            final boolean _tmpCanChangePassword;
            final int _tmp_13;
            _tmp_13 = _cursor.getInt(_cursorIndexOfCanChangePassword);
            _tmpCanChangePassword = _tmp_13 != 0;
            _result = new RouterCapabilityEntity(_tmpRouterId,_tmpHasSessionLogin,_tmpHasSystemBoard,_tmpHasSystemInfo,_tmpHasNetworkInterfaceDump,_tmpHasNetworkInterfaceStatus,_tmpHasNetworkDeviceStatus,_tmpHasLuciRpc,_tmpHasHostHints,_tmpHasDhcpLeases,_tmpHasRealtimeStats,_tmpHasTempInfo,_tmpCanReadUci,_tmpCanReboot,_tmpCanChangePassword);
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
