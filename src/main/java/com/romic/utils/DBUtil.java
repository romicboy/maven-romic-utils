package com.romic.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by romic on 2017/1/18.
 */
public class DBUtil {

//    public static final String url = "jdbc:mysql://192.168.11.220/agh";
//    public static final String name = "com.mysql.jdbc.Driver";
//    public static final String user = "agh";
//    public static final String password = "sApOglPZkDHL";
//    public static final String url = "jdbc:mysql://rdszmjbzqzmjbzq.mysql.rds.aliyuncs.com:3306/agh";
//    public static final String url = "jdbc:mysql://rdszmjbzqzmjbzq.mysql.rds.aliyuncs.com:3306/short_url";
//    public static final String name = "com.mysql.jdbc.Driver";
//    public static final String user = "landray";
//    public static final String password = "landray123";

    public static final String url = "jdbc:mysql://s3-i.care001.cn:3306/agh_dt3";
    public static final String name = "com.mysql.jdbc.Driver";
    public static final String user = "agh_admin";
    public static final String password = "rNzadqKASVJjKwVQ";

    private Connection connection = null;

    private static DBUtil instance = null;

    public static DBUtil getInstance(String url, String user, String password) throws SQLException, ClassNotFoundException {
        if (instance == null) {
            Class.forName(name);    //指定连接类型
            instance = new DBUtil(DriverManager.getConnection(url, user, password));
        }
        return instance;
    }

    private DBUtil(Connection connection) {
        this.connection = connection;
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM agh6_user_corp limit 2";

        DBUtil instance = DBUtil.getInstance(DBUtil.url, DBUtil.user, DBUtil.password);
        List<Map<String, String>> list = instance.select(sql);
        System.out.printf(list.toString());
        Map<String, String> stringStringMap = instance.find(sql);
        System.out.printf(stringStringMap.toString());
    }

    public Map<String, String> find(String sql) throws SQLException, ClassNotFoundException {
        List<Map<String, String>> list = select(sql);
        return list.get(0);
    }

    public List<Map<String, String>> select(String sql) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        // 获取数据
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        while (resultSet.next()) {
            Map<String, String> item = new HashMap<String, String>();
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                int index = i+1;
                String name = metaData.getColumnName(index);
                String value = resultSet.getString(index);
                item.put(name,value);
            }
            list.add(item);
        }
        preparedStatement.close();
        return list;
    }

    public static void close() throws SQLException {
        instance.close();
    }
}
