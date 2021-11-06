package demo.config.db;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * 2021/7/2821:35
 **/
public class MybatisPlusGenerator {

    /**
     * mybatis-plus 代码生成器
     * @param args 入参
     */
    public static void main(String[] args) {
        //1、代码生成器
        AutoGenerator generator = new AutoGenerator();
        //2、全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");//获取项目所在路径
        gc.setOutputDir(projectPath + "/src/main/java");//生成文件的输出目录
        gc.setAuthor("zyq");//开发人员
        gc.setOpen(false);//生成后是否打开资源管理器
        gc.setFileOverride(true);//重新生成文件时是否覆盖
        gc.setBaseResultMap(true);//开启baseResult
        gc.setBaseColumnList(false); // 开启通用查询结果列
        gc.setDateType(DateType.ONLY_DATE);//定义生成的实体类中日期类型

        //调整xml生成目录路径
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() { }
        };
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        generator.setCfg(cfg);
        //自定义文件命名，注意%s会自动填充表实体属性
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sContrller");
        generator.setGlobalConfig(gc);


        //3、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/demo?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&useSSL=false&serverTimezone=Asia/Shanghai");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        generator.setDataSource(dsc);

        //4、包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(null);
        pc.setParent("demo");
        pc.setController("controller");
        pc.setEntity("model");
        pc.setService("service");
        pc.setMapper("dao");
        generator.setPackageInfo(pc);

        //5、策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel); //表名生成策略,下划线转驼峰命名
        String[] tableNames = {"user"};//需要生成的表,多个表可用","隔开
        strategy.setInclude(tableNames);
        // strategy.setExclude("demo"); //排除需要生成的表
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);//数据库表映射到实体的命名策略
        strategy.setTablePrefix(pc.getModuleName() + "_");//生成实体时去掉表前缀
        strategy.setEntityLombokModel(true);//lombok 模型 @Accessorc(chain = true) setter链式操作
        generator.setStrategy(strategy);

        //6、执行
        generator.execute();


    }
}
