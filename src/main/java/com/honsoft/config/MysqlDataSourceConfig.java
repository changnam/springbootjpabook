package com.honsoft.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories(basePackages = {"com.honsoft.repository"},entityManagerFactoryRef = "mysqlEntityManagerFactory",transactionManagerRef = "mysqlJpaTransactionManager")
public class MysqlDataSourceConfig {
	@Autowired
	private Environment env;
	
	@Bean
	@ConfigurationProperties(prefix = "mysql.datasource.hikari")
	public DataSourceProperties mysqlDataSourceProperties() {
		DataSourceProperties properties = new DataSourceProperties();
		
		return properties;
	}
	
	@Bean
	public DataSource mysqlDataSource() {
		return mysqlDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}

	@Bean
	public DataSourceInitializer mysqlDataSourceInitializer(@Qualifier("mysqlDataSource")DataSource dataSource) {
		DataSourceInitializer initializer = new DataSourceInitializer();
		initializer.setDataSource(dataSource);
		initializer.setEnabled(env.getProperty("mysql.datasource.initialize",Boolean.class,false));
		
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("schema-mysql.sql"));
		populator.addScript(new ClassPathResource("data-mysql.sql"));
		
		initializer.setDatabasePopulator(populator);
		
		return initializer;
	}
	
	@Bean(name="mysqlEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactory(@Qualifier("mysqlDataSource")DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource);
		factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		
		Properties properties = new Properties();
		properties.put("hibernate.hbm2ddl.auto","none");
		properties.put("hibernate.show_sql","true");
		properties.put("hibernate.format_sql","true");
		
		factory.setJpaProperties(properties);
		factory.setPackagesToScan("com.honsoft.entity");
		return factory;
	}
	
	@Bean
	public PlatformTransactionManager mysqlJpaTransactionManager(@Qualifier("mysqlEntityManagerFactory") EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}
}
