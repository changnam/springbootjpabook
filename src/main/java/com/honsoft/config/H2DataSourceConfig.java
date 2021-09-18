package com.honsoft.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
//@MapperScan(value = { "com.honsoft.mapper" }, sqlSessionFactoryRef = "h2SqlSessionFactory")
//@EnableJpaRepositories(basePackages = {
//		"com.honsoft.repository" }, entityManagerFactoryRef = "h2EntityManagerFactory", transactionManagerRef = "h2JpaTransactionManager")
public class H2DataSourceConfig {
	@Autowired
	private Environment env;

	@Bean
	@ConfigurationProperties(prefix = "h2.datasource")
	public DataSourceProperties h2DataSourceProperties() {
		DataSourceProperties properties = new DataSourceProperties();

		return properties;
	}

	@Bean
	@Primary
	public DataSource h2DataSource() {
		return h2DataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}

	@Bean
	public DataSourceInitializer h2DataSourceInitializer(@Qualifier("h2DataSource") DataSource dataSource) {
		DataSourceInitializer initializer = new DataSourceInitializer();
		initializer.setDataSource(dataSource);
		initializer.setEnabled(env.getProperty("h2.datasource.initialize", Boolean.class, false));

		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.setSqlScriptEncoding(env.getProperty("h2.datasource.sql-script-encoding"));
		populator.addScript(new ClassPathResource("schema-h2.sql"));
		populator.addScript(new ClassPathResource("data-h2.sql"));

		initializer.setDatabasePopulator(populator);

		return initializer;
	}

	@Bean(name = "h2EntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean h2EntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(h2DataSource());
		factory.setPersistenceUnitName("h2Unit");
		factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		factory.setJpaDialect(new HibernateJpaDialect());

		Properties properties = new Properties();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("h2.jpa.hibernate.ddl-auto"));
		properties.put("hibernate.show_sql", env.getProperty("h2.jpa.hibernate.show_sql"));
		properties.put("hibernate.format_sql", env.getProperty("h2.jpa.hibernate.format_sql"));
		properties.put("hibernate.dialect", env.getProperty("h2.jpa.hibernate.dialect"));

		factory.setJpaProperties(properties);
		factory.setPackagesToScan("com.honsoft.entity");

		return factory;
	}

	@Bean(name = "h2JpaTransactionManager")
	public PlatformTransactionManager h2JpaTransactionManager() {
		EntityManagerFactory factory = h2EntityManagerFactory().getObject();
		return new JpaTransactionManager(factory);
	}

	// mybatis
	@Bean(name = "h2SqlSessionFactory")
	public SqlSessionFactory h2SqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(h2DataSource());
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setMapUnderscoreToCamelCase(true);
		configuration.setJdbcTypeForNull(JdbcType.NULL);
		sqlSessionFactoryBean.setConfiguration(configuration);

		return sqlSessionFactoryBean.getObject();
	}

	@Bean(name = "h2SqlSessiontemplate")
	public SqlSessionTemplate h2SqlSessionTemplate() throws Exception {
		return new SqlSessionTemplate(h2SqlSessionFactory());
	}

}
