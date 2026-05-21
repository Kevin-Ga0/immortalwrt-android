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
import com.immortalwrt.manager.data.local.entity.DeviceCacheEntity;
import com.immortalwrt.manager.data.local.entity.InterfaceCacheEntity;
import com.immortalwrt.manager.data.local.entity.SystemSnapshotEntity;
import com.immortalwrt.manager.domain.model.DeviceSource;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RouterCacheDao_Impl implements RouterCacheDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SystemSnapshotEntity> __insertionAdapterOfSystemSnapshotEntity;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<InterfaceCacheEntity> __insertionAdapterOfInterfaceCacheEntity;

  private final EntityInsertionAdapter<DeviceCacheEntity> __insertionAdapterOfDeviceCacheEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSystemSnapshot;

  private final SharedSQLiteStatement __preparedStmtOfDeleteInterfaceCaches;

  private final SharedSQLiteStatement __preparedStmtOfDeleteDeviceCaches;

  public RouterCacheDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSystemSnapshotEntity = new EntityInsertionAdapter<SystemSnapshotEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `system_snapshots` (`router_id`,`hostname`,`model`,`architecture`,`target_platform`,`firmware_version`,`kernel_version`,`local_time`,`uptime`,`load_average`,`memory_total`,`memory_free`,`memory_buffered`,`swap_total`,`swap_free`,`temperature`,`cpu_usage`,`updated_at`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SystemSnapshotEntity entity) {
        statement.bindString(1, entity.getRouterId());
        if (entity.getHostname() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getHostname());
        }
        if (entity.getModel() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getModel());
        }
        if (entity.getArchitecture() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getArchitecture());
        }
        if (entity.getTargetPlatform() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTargetPlatform());
        }
        if (entity.getFirmwareVersion() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getFirmwareVersion());
        }
        if (entity.getKernelVersion() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getKernelVersion());
        }
        if (entity.getLocalTime() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getLocalTime());
        }
        if (entity.getUptime() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getUptime());
        }
        final String _tmp = __converters.fromFloatList(entity.getLoadAverage());
        statement.bindString(10, _tmp);
        statement.bindLong(11, entity.getMemoryTotal());
        statement.bindLong(12, entity.getMemoryFree());
        statement.bindLong(13, entity.getMemoryBuffered());
        statement.bindLong(14, entity.getSwapTotal());
        statement.bindLong(15, entity.getSwapFree());
        if (entity.getTemperature() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getTemperature());
        }
        if (entity.getCpuUsage() == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, entity.getCpuUsage());
        }
        statement.bindLong(18, entity.getUpdatedAt());
      }
    };
    this.__insertionAdapterOfInterfaceCacheEntity = new EntityInsertionAdapter<InterfaceCacheEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `interface_caches` (`id`,`router_id`,`logical_name`,`device_name`,`display_name`,`up`,`mac`,`ip_addrs`,`ip6_addrs`,`rx_bytes`,`tx_bytes`,`rx_packets`,`tx_packets`,`mtu`,`dev_type`,`dns`,`gateway`,`updated_at`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final InterfaceCacheEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getRouterId());
        statement.bindString(3, entity.getLogicalName());
        if (entity.getDeviceName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDeviceName());
        }
        statement.bindString(5, entity.getDisplayName());
        final int _tmp = entity.getUp() ? 1 : 0;
        statement.bindLong(6, _tmp);
        if (entity.getMac() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getMac());
        }
        final String _tmp_1 = __converters.fromStringList(entity.getIpAddrs());
        statement.bindString(8, _tmp_1);
        final String _tmp_2 = __converters.fromStringList(entity.getIp6Addrs());
        statement.bindString(9, _tmp_2);
        statement.bindLong(10, entity.getRxBytes());
        statement.bindLong(11, entity.getTxBytes());
        statement.bindLong(12, entity.getRxPackets());
        statement.bindLong(13, entity.getTxPackets());
        if (entity.getMtu() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getMtu());
        }
        if (entity.getDevType() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getDevType());
        }
        final String _tmp_3 = __converters.fromStringList(entity.getDns());
        statement.bindString(16, _tmp_3);
        if (entity.getGateway() == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, entity.getGateway());
        }
        statement.bindLong(18, entity.getUpdatedAt());
      }
    };
    this.__insertionAdapterOfDeviceCacheEntity = new EntityInsertionAdapter<DeviceCacheEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `device_caches` (`id`,`router_id`,`mac`,`ip`,`hostname`,`interface_name`,`sources`,`last_seen_at`,`is_online`,`updated_at`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DeviceCacheEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getRouterId());
        if (entity.getMac() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getMac());
        }
        if (entity.getIp() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getIp());
        }
        if (entity.getHostname() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getHostname());
        }
        if (entity.getInterfaceName() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getInterfaceName());
        }
        final String _tmp = __converters.fromDeviceSources(entity.getSources());
        statement.bindString(7, _tmp);
        if (entity.getLastSeenAt() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getLastSeenAt());
        }
        final Integer _tmp_1 = entity.isOnline() == null ? null : (entity.isOnline() ? 1 : 0);
        if (_tmp_1 == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, _tmp_1);
        }
        statement.bindLong(10, entity.getUpdatedAt());
      }
    };
    this.__preparedStmtOfDeleteSystemSnapshot = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM system_snapshots WHERE router_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteInterfaceCaches = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM interface_caches WHERE router_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteDeviceCaches = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM device_caches WHERE router_id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsertSystemSnapshot(final SystemSnapshotEntity snapshot,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSystemSnapshotEntity.insert(snapshot);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertInterfaces(final List<InterfaceCacheEntity> interfaces,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfInterfaceCacheEntity.insert(interfaces);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertDevices(final List<DeviceCacheEntity> devices,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDeviceCacheEntity.insert(devices);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSystemSnapshot(final String router_id,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSystemSnapshot.acquire();
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
          __preparedStmtOfDeleteSystemSnapshot.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteInterfaceCaches(final String router_id,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteInterfaceCaches.acquire();
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
          __preparedStmtOfDeleteInterfaceCaches.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteDeviceCaches(final String router_id,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteDeviceCaches.acquire();
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
          __preparedStmtOfDeleteDeviceCaches.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getSystemSnapshot(final String router_id,
      final Continuation<? super SystemSnapshotEntity> $completion) {
    final String _sql = "SELECT * FROM system_snapshots WHERE router_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, router_id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SystemSnapshotEntity>() {
      @Override
      @Nullable
      public SystemSnapshotEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRouterId = CursorUtil.getColumnIndexOrThrow(_cursor, "router_id");
          final int _cursorIndexOfHostname = CursorUtil.getColumnIndexOrThrow(_cursor, "hostname");
          final int _cursorIndexOfModel = CursorUtil.getColumnIndexOrThrow(_cursor, "model");
          final int _cursorIndexOfArchitecture = CursorUtil.getColumnIndexOrThrow(_cursor, "architecture");
          final int _cursorIndexOfTargetPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "target_platform");
          final int _cursorIndexOfFirmwareVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "firmware_version");
          final int _cursorIndexOfKernelVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "kernel_version");
          final int _cursorIndexOfLocalTime = CursorUtil.getColumnIndexOrThrow(_cursor, "local_time");
          final int _cursorIndexOfUptime = CursorUtil.getColumnIndexOrThrow(_cursor, "uptime");
          final int _cursorIndexOfLoadAverage = CursorUtil.getColumnIndexOrThrow(_cursor, "load_average");
          final int _cursorIndexOfMemoryTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "memory_total");
          final int _cursorIndexOfMemoryFree = CursorUtil.getColumnIndexOrThrow(_cursor, "memory_free");
          final int _cursorIndexOfMemoryBuffered = CursorUtil.getColumnIndexOrThrow(_cursor, "memory_buffered");
          final int _cursorIndexOfSwapTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "swap_total");
          final int _cursorIndexOfSwapFree = CursorUtil.getColumnIndexOrThrow(_cursor, "swap_free");
          final int _cursorIndexOfTemperature = CursorUtil.getColumnIndexOrThrow(_cursor, "temperature");
          final int _cursorIndexOfCpuUsage = CursorUtil.getColumnIndexOrThrow(_cursor, "cpu_usage");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final SystemSnapshotEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpRouterId;
            _tmpRouterId = _cursor.getString(_cursorIndexOfRouterId);
            final String _tmpHostname;
            if (_cursor.isNull(_cursorIndexOfHostname)) {
              _tmpHostname = null;
            } else {
              _tmpHostname = _cursor.getString(_cursorIndexOfHostname);
            }
            final String _tmpModel;
            if (_cursor.isNull(_cursorIndexOfModel)) {
              _tmpModel = null;
            } else {
              _tmpModel = _cursor.getString(_cursorIndexOfModel);
            }
            final String _tmpArchitecture;
            if (_cursor.isNull(_cursorIndexOfArchitecture)) {
              _tmpArchitecture = null;
            } else {
              _tmpArchitecture = _cursor.getString(_cursorIndexOfArchitecture);
            }
            final String _tmpTargetPlatform;
            if (_cursor.isNull(_cursorIndexOfTargetPlatform)) {
              _tmpTargetPlatform = null;
            } else {
              _tmpTargetPlatform = _cursor.getString(_cursorIndexOfTargetPlatform);
            }
            final String _tmpFirmwareVersion;
            if (_cursor.isNull(_cursorIndexOfFirmwareVersion)) {
              _tmpFirmwareVersion = null;
            } else {
              _tmpFirmwareVersion = _cursor.getString(_cursorIndexOfFirmwareVersion);
            }
            final String _tmpKernelVersion;
            if (_cursor.isNull(_cursorIndexOfKernelVersion)) {
              _tmpKernelVersion = null;
            } else {
              _tmpKernelVersion = _cursor.getString(_cursorIndexOfKernelVersion);
            }
            final Long _tmpLocalTime;
            if (_cursor.isNull(_cursorIndexOfLocalTime)) {
              _tmpLocalTime = null;
            } else {
              _tmpLocalTime = _cursor.getLong(_cursorIndexOfLocalTime);
            }
            final Long _tmpUptime;
            if (_cursor.isNull(_cursorIndexOfUptime)) {
              _tmpUptime = null;
            } else {
              _tmpUptime = _cursor.getLong(_cursorIndexOfUptime);
            }
            final List<Float> _tmpLoadAverage;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfLoadAverage);
            _tmpLoadAverage = __converters.toFloatList(_tmp);
            final long _tmpMemoryTotal;
            _tmpMemoryTotal = _cursor.getLong(_cursorIndexOfMemoryTotal);
            final long _tmpMemoryFree;
            _tmpMemoryFree = _cursor.getLong(_cursorIndexOfMemoryFree);
            final long _tmpMemoryBuffered;
            _tmpMemoryBuffered = _cursor.getLong(_cursorIndexOfMemoryBuffered);
            final long _tmpSwapTotal;
            _tmpSwapTotal = _cursor.getLong(_cursorIndexOfSwapTotal);
            final long _tmpSwapFree;
            _tmpSwapFree = _cursor.getLong(_cursorIndexOfSwapFree);
            final String _tmpTemperature;
            if (_cursor.isNull(_cursorIndexOfTemperature)) {
              _tmpTemperature = null;
            } else {
              _tmpTemperature = _cursor.getString(_cursorIndexOfTemperature);
            }
            final String _tmpCpuUsage;
            if (_cursor.isNull(_cursorIndexOfCpuUsage)) {
              _tmpCpuUsage = null;
            } else {
              _tmpCpuUsage = _cursor.getString(_cursorIndexOfCpuUsage);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new SystemSnapshotEntity(_tmpRouterId,_tmpHostname,_tmpModel,_tmpArchitecture,_tmpTargetPlatform,_tmpFirmwareVersion,_tmpKernelVersion,_tmpLocalTime,_tmpUptime,_tmpLoadAverage,_tmpMemoryTotal,_tmpMemoryFree,_tmpMemoryBuffered,_tmpSwapTotal,_tmpSwapFree,_tmpTemperature,_tmpCpuUsage,_tmpUpdatedAt);
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

  @Override
  public Object getInterfaceCaches(final String router_id,
      final Continuation<? super List<InterfaceCacheEntity>> $completion) {
    final String _sql = "SELECT * FROM interface_caches WHERE router_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, router_id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<InterfaceCacheEntity>>() {
      @Override
      @NonNull
      public List<InterfaceCacheEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRouterId = CursorUtil.getColumnIndexOrThrow(_cursor, "router_id");
          final int _cursorIndexOfLogicalName = CursorUtil.getColumnIndexOrThrow(_cursor, "logical_name");
          final int _cursorIndexOfDeviceName = CursorUtil.getColumnIndexOrThrow(_cursor, "device_name");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "display_name");
          final int _cursorIndexOfUp = CursorUtil.getColumnIndexOrThrow(_cursor, "up");
          final int _cursorIndexOfMac = CursorUtil.getColumnIndexOrThrow(_cursor, "mac");
          final int _cursorIndexOfIpAddrs = CursorUtil.getColumnIndexOrThrow(_cursor, "ip_addrs");
          final int _cursorIndexOfIp6Addrs = CursorUtil.getColumnIndexOrThrow(_cursor, "ip6_addrs");
          final int _cursorIndexOfRxBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "rx_bytes");
          final int _cursorIndexOfTxBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "tx_bytes");
          final int _cursorIndexOfRxPackets = CursorUtil.getColumnIndexOrThrow(_cursor, "rx_packets");
          final int _cursorIndexOfTxPackets = CursorUtil.getColumnIndexOrThrow(_cursor, "tx_packets");
          final int _cursorIndexOfMtu = CursorUtil.getColumnIndexOrThrow(_cursor, "mtu");
          final int _cursorIndexOfDevType = CursorUtil.getColumnIndexOrThrow(_cursor, "dev_type");
          final int _cursorIndexOfDns = CursorUtil.getColumnIndexOrThrow(_cursor, "dns");
          final int _cursorIndexOfGateway = CursorUtil.getColumnIndexOrThrow(_cursor, "gateway");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<InterfaceCacheEntity> _result = new ArrayList<InterfaceCacheEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final InterfaceCacheEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpRouterId;
            _tmpRouterId = _cursor.getString(_cursorIndexOfRouterId);
            final String _tmpLogicalName;
            _tmpLogicalName = _cursor.getString(_cursorIndexOfLogicalName);
            final String _tmpDeviceName;
            if (_cursor.isNull(_cursorIndexOfDeviceName)) {
              _tmpDeviceName = null;
            } else {
              _tmpDeviceName = _cursor.getString(_cursorIndexOfDeviceName);
            }
            final String _tmpDisplayName;
            _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            final boolean _tmpUp;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfUp);
            _tmpUp = _tmp != 0;
            final String _tmpMac;
            if (_cursor.isNull(_cursorIndexOfMac)) {
              _tmpMac = null;
            } else {
              _tmpMac = _cursor.getString(_cursorIndexOfMac);
            }
            final List<String> _tmpIpAddrs;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfIpAddrs);
            _tmpIpAddrs = __converters.toStringList(_tmp_1);
            final List<String> _tmpIp6Addrs;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfIp6Addrs);
            _tmpIp6Addrs = __converters.toStringList(_tmp_2);
            final long _tmpRxBytes;
            _tmpRxBytes = _cursor.getLong(_cursorIndexOfRxBytes);
            final long _tmpTxBytes;
            _tmpTxBytes = _cursor.getLong(_cursorIndexOfTxBytes);
            final long _tmpRxPackets;
            _tmpRxPackets = _cursor.getLong(_cursorIndexOfRxPackets);
            final long _tmpTxPackets;
            _tmpTxPackets = _cursor.getLong(_cursorIndexOfTxPackets);
            final Integer _tmpMtu;
            if (_cursor.isNull(_cursorIndexOfMtu)) {
              _tmpMtu = null;
            } else {
              _tmpMtu = _cursor.getInt(_cursorIndexOfMtu);
            }
            final String _tmpDevType;
            if (_cursor.isNull(_cursorIndexOfDevType)) {
              _tmpDevType = null;
            } else {
              _tmpDevType = _cursor.getString(_cursorIndexOfDevType);
            }
            final List<String> _tmpDns;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfDns);
            _tmpDns = __converters.toStringList(_tmp_3);
            final String _tmpGateway;
            if (_cursor.isNull(_cursorIndexOfGateway)) {
              _tmpGateway = null;
            } else {
              _tmpGateway = _cursor.getString(_cursorIndexOfGateway);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new InterfaceCacheEntity(_tmpId,_tmpRouterId,_tmpLogicalName,_tmpDeviceName,_tmpDisplayName,_tmpUp,_tmpMac,_tmpIpAddrs,_tmpIp6Addrs,_tmpRxBytes,_tmpTxBytes,_tmpRxPackets,_tmpTxPackets,_tmpMtu,_tmpDevType,_tmpDns,_tmpGateway,_tmpUpdatedAt);
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
  public Object getDeviceCaches(final String router_id,
      final Continuation<? super List<DeviceCacheEntity>> $completion) {
    final String _sql = "SELECT * FROM device_caches WHERE router_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, router_id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<DeviceCacheEntity>>() {
      @Override
      @NonNull
      public List<DeviceCacheEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRouterId = CursorUtil.getColumnIndexOrThrow(_cursor, "router_id");
          final int _cursorIndexOfMac = CursorUtil.getColumnIndexOrThrow(_cursor, "mac");
          final int _cursorIndexOfIp = CursorUtil.getColumnIndexOrThrow(_cursor, "ip");
          final int _cursorIndexOfHostname = CursorUtil.getColumnIndexOrThrow(_cursor, "hostname");
          final int _cursorIndexOfInterfaceName = CursorUtil.getColumnIndexOrThrow(_cursor, "interface_name");
          final int _cursorIndexOfSources = CursorUtil.getColumnIndexOrThrow(_cursor, "sources");
          final int _cursorIndexOfLastSeenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "last_seen_at");
          final int _cursorIndexOfIsOnline = CursorUtil.getColumnIndexOrThrow(_cursor, "is_online");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<DeviceCacheEntity> _result = new ArrayList<DeviceCacheEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DeviceCacheEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpRouterId;
            _tmpRouterId = _cursor.getString(_cursorIndexOfRouterId);
            final String _tmpMac;
            if (_cursor.isNull(_cursorIndexOfMac)) {
              _tmpMac = null;
            } else {
              _tmpMac = _cursor.getString(_cursorIndexOfMac);
            }
            final String _tmpIp;
            if (_cursor.isNull(_cursorIndexOfIp)) {
              _tmpIp = null;
            } else {
              _tmpIp = _cursor.getString(_cursorIndexOfIp);
            }
            final String _tmpHostname;
            if (_cursor.isNull(_cursorIndexOfHostname)) {
              _tmpHostname = null;
            } else {
              _tmpHostname = _cursor.getString(_cursorIndexOfHostname);
            }
            final String _tmpInterfaceName;
            if (_cursor.isNull(_cursorIndexOfInterfaceName)) {
              _tmpInterfaceName = null;
            } else {
              _tmpInterfaceName = _cursor.getString(_cursorIndexOfInterfaceName);
            }
            final Set<DeviceSource> _tmpSources;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSources);
            _tmpSources = __converters.toDeviceSources(_tmp);
            final Long _tmpLastSeenAt;
            if (_cursor.isNull(_cursorIndexOfLastSeenAt)) {
              _tmpLastSeenAt = null;
            } else {
              _tmpLastSeenAt = _cursor.getLong(_cursorIndexOfLastSeenAt);
            }
            final Boolean _tmpIsOnline;
            final Integer _tmp_1;
            if (_cursor.isNull(_cursorIndexOfIsOnline)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getInt(_cursorIndexOfIsOnline);
            }
            _tmpIsOnline = _tmp_1 == null ? null : _tmp_1 != 0;
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new DeviceCacheEntity(_tmpId,_tmpRouterId,_tmpMac,_tmpIp,_tmpHostname,_tmpInterfaceName,_tmpSources,_tmpLastSeenAt,_tmpIsOnline,_tmpUpdatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
