package com.chen.mysql;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author chenwh
 * @date 2021/12/30
 */

@Slf4j
public class Main {


    
    public static DruidPooledConnection getConnection() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://10.20.1.10:30053/dcpp_test?characterEncoding=utf-8&rewriteBatchedStatements=true&useSSL=false");
        druidDataSource.setUsername("dcpp_cloud");
        druidDataSource.setPassword("Dcpp_cloud1234");
        DruidPooledConnection connection = druidDataSource.getConnection();
        return connection;

    }


    public static void main(String[] args) throws SQLException {
        DruidPooledConnection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from test1");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            log.info("{} , {}", resultSet.getString("id"),resultSet.getString("name"));
        }
        connection.close();
    }
}
