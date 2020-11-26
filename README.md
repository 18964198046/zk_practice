1. 项目说明
(1) 通过调用 zkConfigController 下的Put方法修改数据库链接配置
(2) 通过调用 zkConfigController 下的Get方法获取数据库链接配置

2. 项目问题:
(1) 当修改了数据库连接池后，新的数据库连接池无法从原先的@Autowired属性中获取，例如:
    # 删除数据库连接池
    defaultListableBeanFactory.destroySingleton("dataSource");
    
    # 获取DataSource属性报错，对象不存在
    @Qualifier
    private HikariDataSource hikariDataSource;
        
    @GetMapping("/datasource/connection-timeout")
    public String getDataSource() {
        return "数据库连接超时时间: " + hikariDataSource.getConnectionTimeout();
    }
       
