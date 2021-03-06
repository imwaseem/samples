package com.mm.boot.multidb;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@EnableJpaRepositories(
		 	entityManagerFactoryRef = "customerEntityManager",
		    transactionManagerRef = "customerTransactionManager",
		    basePackages = {"com.mm.boot.multidb.repository.customer"})
public class CustomerDbConfig {
	
	@Bean(name = "customerEntityManager")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		em.setPackagesToScan(new String[] {"com.mm.boot.multidb.model.customer"});
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalJpaProperties());
		em.setPersistenceUnitName("customers");

		return em;
	}
	
	Properties additionalJpaProperties(){
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", "update");
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		properties.setProperty("hibernate.show_sql", "true");
		
		return properties;
	}
	
	@Bean
	public DataSource dataSource(){
		return DataSourceBuilder.create()
				.url("jdbc:mysql://localhost:3306/customer")
				.driverClassName("com.mysql.jdbc.Driver")
				.username("root")
				.password("swisher459")
				.build();
	}	
	
	@Bean(name = "customerTransactionManager")
	public JpaTransactionManager transactionManager(EntityManagerFactory customerEntityManager){
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(customerEntityManager);
		
		return transactionManager;
	}
}