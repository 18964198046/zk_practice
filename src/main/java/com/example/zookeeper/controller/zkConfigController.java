package com.example.zookeeper.controller;

import com.zaxxer.hikari.HikariDataSource;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.sql.DataSource;

@RestController
@RequestMapping("/zk/config")
public class zkConfigController {

    /**
     * 主要的逻辑在ZkConfig配置中，项目的说明和问题写在了README.md文件中，
     * 烦请老师帮忙解答一下疑问，谢谢！
     */

    @Autowired
    private ZkClient zkClient;

    @Autowired
    private ApplicationContext applicationContext;

    @PutMapping
    public void updateZkConfig() {
        long connectionTimeout = zkClient.readData("/spring/datasource/hikari/connection-timeout");
        zkClient.writeData("/spring/datasource/hikari/connection-timeout", connectionTimeout + 1);
    }

    @GetMapping("/datasource/connection-timeout")
    public String getDataSource() {
        return "数据库连接超时时间: " + applicationContext.getBean(HikariDataSource.class).getConnectionTimeout();
    }

}
