package com.imooc.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * HBase操作工具类：Java工具类建议采用单例模式封装
 */
public class HBaseUtils {
    HBaseAdmin hadmin = null;
    Configuration configuration;

    // 单例模式1 创建私有变量
    private static HBaseUtils instance = null;

    // 单例模式2 创建私有构造方法
    private HBaseUtils() {
        configuration = new Configuration();
        // 这里的值和 hbase-site.xml 相关
        configuration.set("hbase.zookeeper.quorum", "hadoop000:2181");
        configuration.set("hbase.rootdir", "hdfs://hadoop000:8020/hbase");
        try {
            hadmin = new HBaseAdmin(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 单例模式3 初始化私有变量(synchronized是为了线程安全)
    public static synchronized HBaseUtils getInstance() {
        if (instance == null) {
            instance = new HBaseUtils();
        }
        return instance;
    }

    // 根据表名, 获取 HTable 对象
    public HTable getTable(String tableName) {
        HTable hTable = null;
        try {
            hTable = new HTable(configuration, tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hTable;
    }

    /**
     * 按表名和天查询数据
     */
    public Map<String, Long> scanPrefixRows(String tname,String day) throws Exception {
        String cf = "info";
        String column = "c1";
        Map<String, Long> map = new HashMap<>();

        HTable table = getTable(tname);
        Scan scan = new Scan();
        PrefixFilter prefixFilter = new PrefixFilter(day.getBytes());
        scan.setFilter(prefixFilter);

        ResultScanner results = table.getScanner(scan);
        for (Result result:results) {
            String rowKey = Bytes.toString(result.getRow());
            Long value = Bytes.toLong(result.getValue(cf.getBytes(), column.getBytes()));
            map.put(rowKey,value);
        }
        return map;
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("hadoop.home.dir","/home/xxx/Downloads/winutils-master/hadoop-2.6.0");
        // 查询数据
        String tableName = "imooc_course_clickcount";
        String day = "20220517";
        Map<String, Long> map = HBaseUtils.getInstance().scanPrefixRows(tableName,day);
        for (Map.Entry<String,Long> entry : map.entrySet()) {
            System.out.println(entry.getKey()+":"+entry.getValue().toString());
        }
    }
}
