package io.github.thirumalx.repository.dao;

import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.simple.JdbcClient;

import io.github.thirumalx.exception.BadRequestException;
import io.github.thirumalx.exception.ResourceNotFoundException;
import jakarta.validation.constraints.NotNull;

/**
 * Base DAO class holding common JdbcClient and Environment configurations.
 */
public abstract class GenericDao {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final JdbcClient jdbcClient;

    protected String primaryKeyErr = "Not able to generate PK";

    GenericDao(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    protected String setInvalues(String query, String replaceString, Set<String> inValues) {
        if (inValues == null || inValues.isEmpty()) {
            throw new BadRequestException("List cannot be empty");
        }
        return query.replace(replaceString,
                "'" + inValues.stream().map(Object::toString).collect(Collectors.joining("','")) + "'");
    }
}
