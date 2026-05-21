package com.immortalwrt.manager.data.local;

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
import com.immortalwrt.manager.data.local.dao.DiagnosticEventDao;
import com.immortalwrt.manager.data.local.dao.DiagnosticEventDao_Impl;
import com.immortalwrt.manager.data.local.dao.RouterCacheDao;
import com.immortalwrt.manager.data.local.dao.RouterCacheDao_Impl;
import com.immortalwrt.manager.data.local.dao.RouterCapabilityDao;
import com.immortalwrt.manager.data.local.dao.RouterCapabilityDao_Impl;
import com.immortalwrt.manager.data.local.dao.RouterDao;
import com.immortalwrt.manager.data.local.dao.RouterDao_Impl;
import com.immortalwrt.manager.data.local.dao.RouterEnvironmentDao;
import com.immortalwrt.manager.data.local.dao.RouterEnvironmentDao_Impl;
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
public final class AppDatabase_Impl extends AppDatabase {
  private volatile RouterDao _routerDao;

  private volatile RouterEnvironmentDao _routerEnvironmentDao;

  private volatile RouterCapabilityDao _routerCapabilityDao;

  private volatile RouterCacheDao _routerCacheDao;

  private volatile DiagnosticEventDao _diagnosticEventDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `routers` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `host` TEXT NOT NULL, `port` INTEGER NOT NULL, `scheme` TEXT NOT NULL, `endpoint` TEXT, `username` TEXT NOT NULL, `passwordSecretRef` TEXT NOT NULL, `certificateFingerprint` TEXT, `http_risk_accepted` INTEGER NOT NULL, `last_success_at` INTEGER, `last_failure_reason` TEXT, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `router_environments` (`router_id` TEXT NOT NULL, `distribution` TEXT, `version` TEXT, `revision` TEXT, `kernel_version` TEXT, `luci_available` INTEGER, `rpcd_available` INTEGER, `endpoint_mode` TEXT, PRIMARY KEY(`router_id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `router_capabilities` (`router_id` TEXT NOT NULL, `has_session_login` INTEGER NOT NULL, `has_system_board` INTEGER NOT NULL, `has_system_info` INTEGER NOT NULL, `has_network_interface_dump` INTEGER NOT NULL, `has_network_interface_status` INTEGER NOT NULL, `has_network_device_status` INTEGER NOT NULL, `has_luci_rpc` INTEGER NOT NULL, `has_host_hints` INTEGER NOT NULL, `has_dhcp_leases` INTEGER NOT NULL, `has_realtime_stats` INTEGER NOT NULL, `has_temp_info` INTEGER NOT NULL, `can_read_uci` INTEGER NOT NULL, `can_reboot` INTEGER NOT NULL, `can_change_password` INTEGER NOT NULL, PRIMARY KEY(`router_id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `system_snapshots` (`router_id` TEXT NOT NULL, `hostname` TEXT, `model` TEXT, `architecture` TEXT, `target_platform` TEXT, `firmware_version` TEXT, `kernel_version` TEXT, `local_time` INTEGER, `uptime` INTEGER, `load_average` TEXT NOT NULL, `memory_total` INTEGER NOT NULL, `memory_free` INTEGER NOT NULL, `memory_buffered` INTEGER NOT NULL, `swap_total` INTEGER NOT NULL, `swap_free` INTEGER NOT NULL, `temperature` TEXT, `cpu_usage` TEXT, `updated_at` INTEGER NOT NULL, PRIMARY KEY(`router_id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `interface_caches` (`id` TEXT NOT NULL, `router_id` TEXT NOT NULL, `logical_name` TEXT NOT NULL, `device_name` TEXT, `display_name` TEXT NOT NULL, `up` INTEGER NOT NULL, `mac` TEXT, `ip_addrs` TEXT NOT NULL, `ip6_addrs` TEXT NOT NULL, `rx_bytes` INTEGER NOT NULL, `tx_bytes` INTEGER NOT NULL, `rx_packets` INTEGER NOT NULL, `tx_packets` INTEGER NOT NULL, `mtu` INTEGER, `dev_type` TEXT, `dns` TEXT NOT NULL, `gateway` TEXT, `updated_at` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `device_caches` (`id` TEXT NOT NULL, `router_id` TEXT NOT NULL, `mac` TEXT, `ip` TEXT, `hostname` TEXT, `interface_name` TEXT, `sources` TEXT NOT NULL, `last_seen_at` INTEGER, `is_online` INTEGER, `updated_at` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `diagnostic_events` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `router_id` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `layer` TEXT NOT NULL, `status` TEXT NOT NULL, `message` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'b761ac71bb2d55b7baee1e81d7e857f6')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `routers`");
        db.execSQL("DROP TABLE IF EXISTS `router_environments`");
        db.execSQL("DROP TABLE IF EXISTS `router_capabilities`");
        db.execSQL("DROP TABLE IF EXISTS `system_snapshots`");
        db.execSQL("DROP TABLE IF EXISTS `interface_caches`");
        db.execSQL("DROP TABLE IF EXISTS `device_caches`");
        db.execSQL("DROP TABLE IF EXISTS `diagnostic_events`");
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
        final HashMap<String, TableInfo.Column> _columnsRouters = new HashMap<String, TableInfo.Column>(14);
        _columnsRouters.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouters.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouters.put("host", new TableInfo.Column("host", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouters.put("port", new TableInfo.Column("port", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouters.put("scheme", new TableInfo.Column("scheme", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouters.put("endpoint", new TableInfo.Column("endpoint", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouters.put("username", new TableInfo.Column("username", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouters.put("passwordSecretRef", new TableInfo.Column("passwordSecretRef", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouters.put("certificateFingerprint", new TableInfo.Column("certificateFingerprint", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouters.put("http_risk_accepted", new TableInfo.Column("http_risk_accepted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouters.put("last_success_at", new TableInfo.Column("last_success_at", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouters.put("last_failure_reason", new TableInfo.Column("last_failure_reason", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouters.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouters.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRouters = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRouters = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRouters = new TableInfo("routers", _columnsRouters, _foreignKeysRouters, _indicesRouters);
        final TableInfo _existingRouters = TableInfo.read(db, "routers");
        if (!_infoRouters.equals(_existingRouters)) {
          return new RoomOpenHelper.ValidationResult(false, "routers(com.immortalwrt.manager.data.local.entity.RouterEntity).\n"
                  + " Expected:\n" + _infoRouters + "\n"
                  + " Found:\n" + _existingRouters);
        }
        final HashMap<String, TableInfo.Column> _columnsRouterEnvironments = new HashMap<String, TableInfo.Column>(8);
        _columnsRouterEnvironments.put("router_id", new TableInfo.Column("router_id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouterEnvironments.put("distribution", new TableInfo.Column("distribution", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouterEnvironments.put("version", new TableInfo.Column("version", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouterEnvironments.put("revision", new TableInfo.Column("revision", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouterEnvironments.put("kernel_version", new TableInfo.Column("kernel_version", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouterEnvironments.put("luci_available", new TableInfo.Column("luci_available", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouterEnvironments.put("rpcd_available", new TableInfo.Column("rpcd_available", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouterEnvironments.put("endpoint_mode", new TableInfo.Column("endpoint_mode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRouterEnvironments = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRouterEnvironments = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRouterEnvironments = new TableInfo("router_environments", _columnsRouterEnvironments, _foreignKeysRouterEnvironments, _indicesRouterEnvironments);
        final TableInfo _existingRouterEnvironments = TableInfo.read(db, "router_environments");
        if (!_infoRouterEnvironments.equals(_existingRouterEnvironments)) {
          return new RoomOpenHelper.ValidationResult(false, "router_environments(com.immortalwrt.manager.data.local.entity.RouterEnvironmentEntity).\n"
                  + " Expected:\n" + _infoRouterEnvironments + "\n"
                  + " Found:\n" + _existingRouterEnvironments);
        }
        final HashMap<String, TableInfo.Column> _columnsRouterCapabilities = new HashMap<String, TableInfo.Column>(15);
        _columnsRouterCapabilities.put("router_id", new TableInfo.Column("router_id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouterCapabilities.put("has_session_login", new TableInfo.Column("has_session_login", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouterCapabilities.put("has_system_board", new TableInfo.Column("has_system_board", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouterCapabilities.put("has_system_info", new TableInfo.Column("has_system_info", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouterCapabilities.put("has_network_interface_dump", new TableInfo.Column("has_network_interface_dump", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouterCapabilities.put("has_network_interface_status", new TableInfo.Column("has_network_interface_status", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouterCapabilities.put("has_network_device_status", new TableInfo.Column("has_network_device_status", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouterCapabilities.put("has_luci_rpc", new TableInfo.Column("has_luci_rpc", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouterCapabilities.put("has_host_hints", new TableInfo.Column("has_host_hints", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouterCapabilities.put("has_dhcp_leases", new TableInfo.Column("has_dhcp_leases", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouterCapabilities.put("has_realtime_stats", new TableInfo.Column("has_realtime_stats", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouterCapabilities.put("has_temp_info", new TableInfo.Column("has_temp_info", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouterCapabilities.put("can_read_uci", new TableInfo.Column("can_read_uci", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouterCapabilities.put("can_reboot", new TableInfo.Column("can_reboot", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRouterCapabilities.put("can_change_password", new TableInfo.Column("can_change_password", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRouterCapabilities = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRouterCapabilities = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRouterCapabilities = new TableInfo("router_capabilities", _columnsRouterCapabilities, _foreignKeysRouterCapabilities, _indicesRouterCapabilities);
        final TableInfo _existingRouterCapabilities = TableInfo.read(db, "router_capabilities");
        if (!_infoRouterCapabilities.equals(_existingRouterCapabilities)) {
          return new RoomOpenHelper.ValidationResult(false, "router_capabilities(com.immortalwrt.manager.data.local.entity.RouterCapabilityEntity).\n"
                  + " Expected:\n" + _infoRouterCapabilities + "\n"
                  + " Found:\n" + _existingRouterCapabilities);
        }
        final HashMap<String, TableInfo.Column> _columnsSystemSnapshots = new HashMap<String, TableInfo.Column>(18);
        _columnsSystemSnapshots.put("router_id", new TableInfo.Column("router_id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemSnapshots.put("hostname", new TableInfo.Column("hostname", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemSnapshots.put("model", new TableInfo.Column("model", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemSnapshots.put("architecture", new TableInfo.Column("architecture", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemSnapshots.put("target_platform", new TableInfo.Column("target_platform", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemSnapshots.put("firmware_version", new TableInfo.Column("firmware_version", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemSnapshots.put("kernel_version", new TableInfo.Column("kernel_version", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemSnapshots.put("local_time", new TableInfo.Column("local_time", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemSnapshots.put("uptime", new TableInfo.Column("uptime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemSnapshots.put("load_average", new TableInfo.Column("load_average", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemSnapshots.put("memory_total", new TableInfo.Column("memory_total", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemSnapshots.put("memory_free", new TableInfo.Column("memory_free", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemSnapshots.put("memory_buffered", new TableInfo.Column("memory_buffered", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemSnapshots.put("swap_total", new TableInfo.Column("swap_total", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemSnapshots.put("swap_free", new TableInfo.Column("swap_free", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemSnapshots.put("temperature", new TableInfo.Column("temperature", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemSnapshots.put("cpu_usage", new TableInfo.Column("cpu_usage", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemSnapshots.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSystemSnapshots = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSystemSnapshots = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSystemSnapshots = new TableInfo("system_snapshots", _columnsSystemSnapshots, _foreignKeysSystemSnapshots, _indicesSystemSnapshots);
        final TableInfo _existingSystemSnapshots = TableInfo.read(db, "system_snapshots");
        if (!_infoSystemSnapshots.equals(_existingSystemSnapshots)) {
          return new RoomOpenHelper.ValidationResult(false, "system_snapshots(com.immortalwrt.manager.data.local.entity.SystemSnapshotEntity).\n"
                  + " Expected:\n" + _infoSystemSnapshots + "\n"
                  + " Found:\n" + _existingSystemSnapshots);
        }
        final HashMap<String, TableInfo.Column> _columnsInterfaceCaches = new HashMap<String, TableInfo.Column>(18);
        _columnsInterfaceCaches.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInterfaceCaches.put("router_id", new TableInfo.Column("router_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInterfaceCaches.put("logical_name", new TableInfo.Column("logical_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInterfaceCaches.put("device_name", new TableInfo.Column("device_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInterfaceCaches.put("display_name", new TableInfo.Column("display_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInterfaceCaches.put("up", new TableInfo.Column("up", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInterfaceCaches.put("mac", new TableInfo.Column("mac", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInterfaceCaches.put("ip_addrs", new TableInfo.Column("ip_addrs", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInterfaceCaches.put("ip6_addrs", new TableInfo.Column("ip6_addrs", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInterfaceCaches.put("rx_bytes", new TableInfo.Column("rx_bytes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInterfaceCaches.put("tx_bytes", new TableInfo.Column("tx_bytes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInterfaceCaches.put("rx_packets", new TableInfo.Column("rx_packets", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInterfaceCaches.put("tx_packets", new TableInfo.Column("tx_packets", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInterfaceCaches.put("mtu", new TableInfo.Column("mtu", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInterfaceCaches.put("dev_type", new TableInfo.Column("dev_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInterfaceCaches.put("dns", new TableInfo.Column("dns", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInterfaceCaches.put("gateway", new TableInfo.Column("gateway", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInterfaceCaches.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysInterfaceCaches = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesInterfaceCaches = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoInterfaceCaches = new TableInfo("interface_caches", _columnsInterfaceCaches, _foreignKeysInterfaceCaches, _indicesInterfaceCaches);
        final TableInfo _existingInterfaceCaches = TableInfo.read(db, "interface_caches");
        if (!_infoInterfaceCaches.equals(_existingInterfaceCaches)) {
          return new RoomOpenHelper.ValidationResult(false, "interface_caches(com.immortalwrt.manager.data.local.entity.InterfaceCacheEntity).\n"
                  + " Expected:\n" + _infoInterfaceCaches + "\n"
                  + " Found:\n" + _existingInterfaceCaches);
        }
        final HashMap<String, TableInfo.Column> _columnsDeviceCaches = new HashMap<String, TableInfo.Column>(10);
        _columnsDeviceCaches.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceCaches.put("router_id", new TableInfo.Column("router_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceCaches.put("mac", new TableInfo.Column("mac", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceCaches.put("ip", new TableInfo.Column("ip", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceCaches.put("hostname", new TableInfo.Column("hostname", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceCaches.put("interface_name", new TableInfo.Column("interface_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceCaches.put("sources", new TableInfo.Column("sources", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceCaches.put("last_seen_at", new TableInfo.Column("last_seen_at", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceCaches.put("is_online", new TableInfo.Column("is_online", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceCaches.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDeviceCaches = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDeviceCaches = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDeviceCaches = new TableInfo("device_caches", _columnsDeviceCaches, _foreignKeysDeviceCaches, _indicesDeviceCaches);
        final TableInfo _existingDeviceCaches = TableInfo.read(db, "device_caches");
        if (!_infoDeviceCaches.equals(_existingDeviceCaches)) {
          return new RoomOpenHelper.ValidationResult(false, "device_caches(com.immortalwrt.manager.data.local.entity.DeviceCacheEntity).\n"
                  + " Expected:\n" + _infoDeviceCaches + "\n"
                  + " Found:\n" + _existingDeviceCaches);
        }
        final HashMap<String, TableInfo.Column> _columnsDiagnosticEvents = new HashMap<String, TableInfo.Column>(6);
        _columnsDiagnosticEvents.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiagnosticEvents.put("router_id", new TableInfo.Column("router_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiagnosticEvents.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiagnosticEvents.put("layer", new TableInfo.Column("layer", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiagnosticEvents.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiagnosticEvents.put("message", new TableInfo.Column("message", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDiagnosticEvents = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDiagnosticEvents = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDiagnosticEvents = new TableInfo("diagnostic_events", _columnsDiagnosticEvents, _foreignKeysDiagnosticEvents, _indicesDiagnosticEvents);
        final TableInfo _existingDiagnosticEvents = TableInfo.read(db, "diagnostic_events");
        if (!_infoDiagnosticEvents.equals(_existingDiagnosticEvents)) {
          return new RoomOpenHelper.ValidationResult(false, "diagnostic_events(com.immortalwrt.manager.data.local.entity.DiagnosticEventEntity).\n"
                  + " Expected:\n" + _infoDiagnosticEvents + "\n"
                  + " Found:\n" + _existingDiagnosticEvents);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "b761ac71bb2d55b7baee1e81d7e857f6", "4bacfe1be76a3bb7db391e09d9bc3b05");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "routers","router_environments","router_capabilities","system_snapshots","interface_caches","device_caches","diagnostic_events");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `routers`");
      _db.execSQL("DELETE FROM `router_environments`");
      _db.execSQL("DELETE FROM `router_capabilities`");
      _db.execSQL("DELETE FROM `system_snapshots`");
      _db.execSQL("DELETE FROM `interface_caches`");
      _db.execSQL("DELETE FROM `device_caches`");
      _db.execSQL("DELETE FROM `diagnostic_events`");
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
    _typeConvertersMap.put(RouterDao.class, RouterDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RouterEnvironmentDao.class, RouterEnvironmentDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RouterCapabilityDao.class, RouterCapabilityDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RouterCacheDao.class, RouterCacheDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DiagnosticEventDao.class, DiagnosticEventDao_Impl.getRequiredConverters());
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
  public RouterDao routerDao() {
    if (_routerDao != null) {
      return _routerDao;
    } else {
      synchronized(this) {
        if(_routerDao == null) {
          _routerDao = new RouterDao_Impl(this);
        }
        return _routerDao;
      }
    }
  }

  @Override
  public RouterEnvironmentDao routerEnvironmentDao() {
    if (_routerEnvironmentDao != null) {
      return _routerEnvironmentDao;
    } else {
      synchronized(this) {
        if(_routerEnvironmentDao == null) {
          _routerEnvironmentDao = new RouterEnvironmentDao_Impl(this);
        }
        return _routerEnvironmentDao;
      }
    }
  }

  @Override
  public RouterCapabilityDao routerCapabilityDao() {
    if (_routerCapabilityDao != null) {
      return _routerCapabilityDao;
    } else {
      synchronized(this) {
        if(_routerCapabilityDao == null) {
          _routerCapabilityDao = new RouterCapabilityDao_Impl(this);
        }
        return _routerCapabilityDao;
      }
    }
  }

  @Override
  public RouterCacheDao routerCacheDao() {
    if (_routerCacheDao != null) {
      return _routerCacheDao;
    } else {
      synchronized(this) {
        if(_routerCacheDao == null) {
          _routerCacheDao = new RouterCacheDao_Impl(this);
        }
        return _routerCacheDao;
      }
    }
  }

  @Override
  public DiagnosticEventDao diagnosticEventDao() {
    if (_diagnosticEventDao != null) {
      return _diagnosticEventDao;
    } else {
      synchronized(this) {
        if(_diagnosticEventDao == null) {
          _diagnosticEventDao = new DiagnosticEventDao_Impl(this);
        }
        return _diagnosticEventDao;
      }
    }
  }
}
