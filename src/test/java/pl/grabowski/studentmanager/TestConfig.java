package pl.grabowski.studentmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application-test.properties")
public class TestConfig {

    @Autowired
    private Environment env;


    @Bean
    @Profile(value = "test")
    public LocalSessionFactoryBean entityManagerFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(env.getProperty("spring.jpa.packages"));
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    @Profile(value = "test")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName(env.getProperty("org.h2.Driver"))
                .url(env.getProperty("spring.datasource.url"))
                .username(env.getProperty("spring.datasource.username"))
                .password(env.getProperty("spring.datasource.password"))
                .build();
    }

    @Profile(value = "test")
    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        /*hibernateProperties.setProperty(
                "hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));*/
        hibernateProperties.setProperty(
                "hibernate.dialect", env.getProperty("hibernate.dialect")
        );

        return hibernateProperties;
    }
}