package com.example.influxdbdemo.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Data
public class InfluxConnection {
    private String userName;
    private String password;
    private String url;
    private String database;
    private String retentionPolicy;
    private String retentionPolicyTime;
    private InfluxDB influxDB;

    /**
     * 获取influxDB实例,创建默认数据保存策略
     */


    public InfluxConnection(String userName, String password, String url, String database, String retentionPolicy, String retentionPolicyTime) {
        this.userName = userName;
        this.password = password;
        this.url = url;
        this.database = database;
        this.retentionPolicy = retentionPolicy == null || "".equals(retentionPolicy) ? "autogen" : retentionPolicy;
        this.retentionPolicyTime = retentionPolicyTime == null || "".equals(retentionPolicyTime) ? "30d" : retentionPolicyTime;
        this.influxDB = influxDbBuild();
        createRetentionPolicy();
    }

    /**
     * 连接时序库，创建influx实例
     */

    private InfluxDB influxDbBuild() {
        if (influxDB == null) {
            influxDB = InfluxDBFactory.connect(url, userName, password);
            influxDB.query(new Query("CREATE DATABASE " + database));
            influxDB.setDatabase(database);
        }
        return influxDB;
    }

    /**
     * 设置数据保存策略 defalut 策略名 /database 数据库名/ 30d 数据保存时限30天/ 1 副本个数为1/ 结尾DEFAULT
     * 表示 设为默认的策略
     */
    public void createRetentionPolicy() {
        String command = String.format("CREATE RETENTION POLICY \"%s\" ON \"%s\" DURATION %s REPLICATION %s DEFAULT",
                retentionPolicy, database, retentionPolicyTime, 1);
        this.query(command);
    }

    /**
     * 查询
     *
     * @param command 查询语句
     * @return
     */
    public QueryResult query(String command) {
        return influxDB.query(new Query(command, database));
    }

    /**
     * 插入
     *
     * @param measurement 表
     * @param tags        标签
     * @param fields      字段
     */
    public void insert(String measurement, Map<String, String> tags, Map<String, Object> fields) {
        Point.Builder builder = Point.measurement(measurement);
        // 纳秒时会出现异常信息：partial write: points beyond retention policy dropped=1
        // builder.time(System.nanoTime(), TimeUnit.NANOSECONDS);
        builder.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        builder.tag(tags);
        builder.fields(fields);
        influxDB.write(database, "", builder.build());
    }

}
