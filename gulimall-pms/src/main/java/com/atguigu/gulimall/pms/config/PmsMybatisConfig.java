package com.atguigu.gulimall.pms.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

import com.zaxxer.hikari.HikariDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

//Spring boot方式
@EnableTransactionManagement
@Configuration

public class PmsMybatisConfig {



    //Spring boot方式
    @EnableTransactionManagement
    @Configuration

    public class SmsMybatisConfig {

        /**
         * 分页插件
         */
        @Bean
        public PaginationInterceptor paginationInterceptor() {
            PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
            // paginationInterceptor.setLimit(你的最大单页限制数量，默认 500 条，小于 0 如 -1 不受限制);
            return paginationInterceptor;
        }
        @Bean
        @ConfigurationProperties(prefix = "spring.datasource")
        public DataSource originDataSource(@Value("${spring.datasource.url}") String url
        ){
            HikariDataSource hikariDataSource = new HikariDataSource();
            hikariDataSource.setJdbcUrl(url);

            return hikariDataSource;
        }

        @Bean
        @Primary//这个返回的对象是默认的数据源
        public DataSource dataSource(DataSource dataSource){

            return new DataSourceProxy(dataSource);
        }
    }

}
