package com.example.zbusst.Dao.BaseDao;

import android.app.Application;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class BaseDaoGoodList {

    private static String driver;
    private static String url;
    private static String username;
    private static String password;
    private static final String TAG = "chen";

    //从配置文件读取数据库信息
    static {
        Properties params = new Properties();
        String configfile = "/assets/database.properties";

        InputStream is = BaseDaoGoodList.class.getResourceAsStream(configfile);
        try {
//            params.load(is);
            params.load(is);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            throw new RuntimeException(e);
        }
        driver = params.getProperty("driver");
        url = params.getProperty("url") + params.getProperty("dbname") +
                params.getProperty("houzhui");
        username = params.getProperty("username");
        password = params.getProperty("password");
    }

    //获取数据库连接
    public Connection getConnection(){
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException | SQLException e) {
            Log.e(TAG, "getConnection: "+e );
            e.printStackTrace();
        }
        return conn;
    }

    //执行数据库
    public int excuteUpdate(String sql,Object...objects) {
        int count = 0;
        Connection connection = getConnection();
        PreparedStatement ps = null;

        try {
            //设置SQL参数
            ps = connection.prepareStatement(sql);

            for(int i=0;i<objects.length;i++)
                ps.setObject(i+1,objects[i]);

            count = ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return count;
    }

    //释放连接
    public void closeAll(ResultSet rs, Statement stmt, Connection con) {
        if(null != rs) {
            try {
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(null != stmt) {
            try {
                stmt.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(null != con) {
            try {
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }


}
