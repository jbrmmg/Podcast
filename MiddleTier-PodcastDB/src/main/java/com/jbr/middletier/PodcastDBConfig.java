package com.jbr.middletier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Created by jason on 24/12/16.
 */
@Configuration
@ComponentScan("com.jbr.middletier")
class PodcastDBConfig {
    @Value("${middle.tier.podcast.db.url}")
    private String url;

    @Value("${middle.tier.podcast.db.username}")
    private String username;

    @Value("${middle.tier.podcast.db.password}")
    private String password;

    @Bean
    @Primary
    @SuppressWarnings("Duplicates")
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceBuilder.url(url);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);

        return dataSourceBuilder.build();
    }
}
