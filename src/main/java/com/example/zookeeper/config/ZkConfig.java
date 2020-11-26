package com.example.zookeeper.config;

import com.zaxxer.hikari.HikariDataSource;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class ZkConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    public DefaultListableBeanFactory defaultListableBeanFactory;

    @Bean
    public ZkClient zkClient(IZkDataListener iZkDataListener) {
        ZkClient zkClient = new ZkClient("linux121:2181");
        if (!zkClient.exists("/spring/datasource/url")) {
            zkClient.createPersistent("/spring/datasource/url", true);
            zkClient.writeData("/spring/datasource/url", "jdbc:mysql://${JDBC_HOST:linux123}:3306/hive?useSSL=false&useTimezone=true&serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true");
        }
        if (!zkClient.exists("/spring/datasource/username")) {
            zkClient.createPersistent("/spring/datasource/username", true);
            zkClient.writeData("/spring/datasource/username", "root");
        }
        if (!zkClient.exists("/spring/datasource/password")) {
            zkClient.createPersistent("/spring/datasource/password", true);
            zkClient.writeData("/spring/datasource/password", "12345678");
        }
        if (!zkClient.exists("/spring/datasource/hikari/connection-timeout")) {
            zkClient.createPersistent("/spring/datasource/hikari/connection-timeout", true);
            zkClient.writeData("/spring/datasource/hikari/connection-timeout", 20000l);
        }
        if (!zkClient.exists("/spring/datasource/hikari/minimum-idle")) {
            zkClient.createPersistent("/spring/datasource/hikari/minimum-idle", true);
            zkClient.writeData("/spring/datasource/hikari/minimum-idle", 10);
        }
        if (!zkClient.exists("/spring/datasource/hikari/maximum-pool-size")) {
            zkClient.createPersistent("/spring/datasource/hikari/maximum-pool-size", true);
            zkClient.writeData("/spring/datasource/hikari/maximum-pool-size", 15);
        }
        if (!zkClient.exists("/spring/datasource/hikari/idle-timeout")) {
            zkClient.createPersistent("/spring/datasource/hikari/idle-timeout", true);
            zkClient.writeData("/spring/datasource/hikari/idle-timeout", 10000l);
        }
        zkClient.subscribeDataChanges("/spring/datasource/hikari/connection-timeout", iZkDataListener);
        return zkClient;
    }

    @Bean
    public IZkDataListener iZkDataListener() {
        return new IZkDataListener() {
            @Override
            public void handleDataChange(String path, Object data) throws Exception {
                System.out.println(path + "data is changed, new data " + data);
                defaultListableBeanFactory.destroySingleton("dataSource");
            }

            @Override
            public void handleDataDeleted(String path) throws Exception {
                System.out.println(path + "data is deleted!");
            }
        };
    }

    @Bean
    public DataSource dataSource(ZkClient zkClient) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(zkClient.readData("/spring/datasource/url"));
        hikariDataSource.setUsername(zkClient.readData("/spring/datasource/username"));
        hikariDataSource.setPassword(zkClient.readData("/spring/datasource/password"));
        hikariDataSource.setConnectionTimeout(zkClient.readData("/spring/datasource/hikari/connection-timeout"));
        hikariDataSource.setMinimumIdle(zkClient.readData("/spring/datasource/hikari/minimum-idle"));
        hikariDataSource.setMaximumPoolSize(zkClient.readData("/spring/datasource/hikari/maximum-pool-size"));
        hikariDataSource.setIdleTimeout(zkClient.readData("/spring/datasource/hikari/idle-timeout"));
        return hikariDataSource;
    }

}