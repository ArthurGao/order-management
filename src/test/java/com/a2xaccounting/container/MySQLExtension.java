package com.a2xaccounting.container;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.TestcontainersConfiguration;

public class MySQLExtension implements BeforeAllCallback, AfterAllCallback {

  public static final MySQLContainer<?> mySqlDB;

  static {
    mySqlDB =
        new MySQLContainer<>
            ("mysql:5.7.37").withReuse(true)
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("testpassword")
            .withCommand("--sql-mode=")
            .withInitScript("sql/table.sql");
    mySqlDB.start();
    mySqlDB.withReuse(true);
    Runtime.getRuntime()
        .addShutdownHook(new Thread(mySqlDB::stop));
  }

  public static void setDynamicPropertySource(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mySqlDB::getJdbcUrl);
    registry.add("spring.datasource.username", mySqlDB::getUsername);
    registry.add("spring.datasource.password", mySqlDB::getPassword);
  }

  @Override
  public void beforeAll(ExtensionContext context) {
    TestcontainersConfiguration.getInstance().updateUserConfig("testcontainers.reuse.enable", "true");
  }

  @Override
  public void afterAll(ExtensionContext context) {
  }
}
