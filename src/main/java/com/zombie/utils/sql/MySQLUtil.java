package com.zombie.utils.sql;

import com.zombie.utils.config.ConfigUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mySQL相关工具类<br>
 * <p>
 * 从config.properties中读取DB配置<br>
 * 所需要的信息有:<br>
 * dbURL,数据库地址及端口号,如:jdbc:mysql://localhost:3306 <br>
 * user,用户名<br>
 * password,密码
 * </p>
 */

public class MySQLUtil {
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String dbURL = ConfigUtil.get("dbURL");
    private static final String user = ConfigUtil.get("user");
    private static final String password = ConfigUtil.get("password");
    private static ResultSet resultSet = null;
    private static Connection connection = null;
    private static Statement statement = null;
    private static final Logger logger = LogManager.getLogger(MySQLUtil.class);

    /**
     * 建立链接
     *
     * @return connection
     */
    public static Connection getConnection() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(dbURL, user, password);
        } catch (Exception e) {
            logger.error("数据库连接失败,请检查数据库连接信息");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 执行查询
     *
     * @param sql 查询语句
     *
     * @return ResultSet
     */
    public static ResultSet executeQuery(String sql) {
        getConnection();
        logger.info("查询语句为:{}", sql);
        try {
            if (connection != null) {
                statement = connection.createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                resultSet = statement.executeQuery(sql);

            }
        } catch (SQLException e) {
            logger.error("执行SQL语句出错");
            e.printStackTrace();
        } finally {
            close();
        }
        return resultSet;

    }

    /**
     * 获取查询记录数
     *
     * @param sql 查询语句
     *
     * @return 记录数
     */
    public static int getRecordCount(String sql) {
        int count = 0;
        getConnection();
        logger.info("查询语句为:{}", sql);
        resultSet = executeQuery(sql);
        try {
            while (resultSet.next()) {
                count++;
            }
        } catch (SQLException e) {
            logger.error("执行SQL语句出错");
            e.printStackTrace();
        } finally {
            close();
        }
        return count;
    }

    /**
     * 执行DML操作
     *
     * @param sql update语句
     *
     * @return 0:DML操作什么也不返回 或者 DML操作所影响的记录数
     */
    public int executeUpdate(String sql) {
        int result = 0;
        getConnection();
        logger.info("查询语句为:{}", sql);
        try {
            statement = connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            result = statement.executeUpdate(sql);
        } catch (SQLException e) {
            logger.error("执行更新数据库操作出错");
            e.printStackTrace();
        } finally {
            close();
        }
        return result;
    }

    /**
     * 关闭数据库连接
     */
    private static void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("关闭连接出错");
            e.printStackTrace();
        }
    }

    /**
     * 执行SQL查询
     *
     * @param sql
     *
     * @return Map列表
     */
    public static List<Map<String, Object>> execSql(String sql) {
        if (connection == null) {
            getConnection();
        }
        logger.info("查询语句为:{}", sql);
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    map.put(resultSet.getMetaData().getColumnName(i), resultSet.getString(i));
                }
                result.add(map);
            }
        } catch (SQLException e) {
            logger.error("执行SQL语句出错");
            e.printStackTrace();
        } finally {
            close();
        }
        return result;
    }

    /**
     * 执行sql语句,并以字符串的形式返回其中的指定列的一条记录
     *
     * @param sql
     * @param column 需要返回的列名
     *
     * @return Object
     */
    public static Object execSqlGetOne(String sql, String column) {
        if (connection == null) {
            getConnection();
        }
        logger.info("查询语句为:{}", sql);
        Object result = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            result = resultSet.getObject(column);
        } catch (SQLException e) {
            logger.error("执行SQL语句出错");
            e.printStackTrace();
        } finally {
            close();
        }
        return result;
    }

    /**
     * 以列表的形式返回指定列的所有查询结果
     *
     * @param sql
     * @param column 需要返回的列名
     *
     * @return 查询结果列表
     */
    public static List<Object> getResultAsList(String sql, String column) {
        if (connection == null) {
            getConnection();
        }
        logger.info("查询语句为:{}", sql);

        List<Object> resultList = new ArrayList<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                resultList.add(resultSet.getObject(column));
            }
        } catch (SQLException e) {
            logger.error("执行SQL语句出错");
            e.printStackTrace();
        } finally {
            close();
        }
        return resultList;
    }
}
