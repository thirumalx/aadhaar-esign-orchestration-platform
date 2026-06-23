package io.github.thirumalx.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Thirumal M
 *         Loads queries.sql into the Spring Environment.
 */
@Configuration
@PropertySource(value = "classpath:queries.sql", ignoreResourceNotFound = true)
public class QueriesConfig {
}
