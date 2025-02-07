package com.batch.batch_quartz.config;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "serviceEntityManager", transactionManagerRef = "serviceTransactionManager", basePackages = {"com.batch"})
@EnableJpaAuditing
@RequiredArgsConstructor
public class JpaConfig {

    private final JpaProperties jpaProperties;
    private final HibernateProperties hibernateProperties;

    @Bean(name = "serviceEntityManager")
    public LocalContainerEntityManagerFactoryBean serviceEntityManager(@Qualifier("serviceDataSource") DataSource dataSource){        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("com.batch");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Map<String, Object> prop = hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(),
				new HibernateSettings());

        factoryBean.setJpaPropertyMap(prop);

        return factoryBean;
    }

    @Bean(name = "serviceTransactionManager")
    public PlatformTransactionManager serviceTransactionManager(@Qualifier("serviceEntityManager") LocalContainerEntityManagerFactoryBean serviceEntityManager){
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(serviceEntityManager.getObject());
        
        return jpaTransactionManager;
    }
}
