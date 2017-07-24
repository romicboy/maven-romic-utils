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

    public static final String name = "com.mysql.jdbc.Driver";
    public static final String url = "jdbc:mysql://127.0.0.1/test";
    public static final String user = "root";
    public static final String password = "root";

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

    public int executeUpdate(String sql) throws SQLException {
        PreparedStatement pst = connection.prepareStatement(sql);
        int result = pst.executeUpdate();
        pst.close();
        return result;
    }

    public void close() throws SQLException {
        connection.close();
    }
}
