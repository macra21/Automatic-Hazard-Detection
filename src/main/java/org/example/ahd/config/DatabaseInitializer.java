package org.example.ahd.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public DatabaseInitializer(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("sql/CreateResponseTimeTable.sql"));

            // Used jdbcTemplate because ScriptUtils splits the script by semicolons and fails to run them

            String functionSql = StreamUtils.copyToString(new ClassPathResource("sql/CalculateResponseTimeFunction.sql").getInputStream(), StandardCharsets.UTF_8);
            jdbcTemplate.execute(functionSql);

            String triggerSql = StreamUtils.copyToString(new ClassPathResource("sql/HazardUpdateTrigger.sql").getInputStream(), StandardCharsets.UTF_8);
            jdbcTemplate.execute(triggerSql);

            System.out.println("Debug: Database initialization scripts executed successfully.");
        } catch (Exception e) {
            System.err.println("Error executing database initialization scripts: " + e.getMessage());
        }
    }
}
