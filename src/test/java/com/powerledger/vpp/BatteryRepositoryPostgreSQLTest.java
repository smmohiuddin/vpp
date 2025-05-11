package com.powerledger.vpp;

import com.powerledger.vpp.model.Battery;
import com.powerledger.vpp.repository.BatteryRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Testcontainers
public class BatteryRepositoryPostgreSQLTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.3")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private BatteryRepository batteryRepository;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @BeforeAll
    static void init() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void tearDown() {
        postgreSQLContainer.stop();
    }

    @BeforeEach
    void setUp() {
        batteryRepository.deleteAll();
    }

    @Test
    void findsBatteriesWithinPostcodeRangeAndCapacity() {
        Battery battery1 = new Battery("Battery1", "2000", 100);
        Battery battery2 = new Battery("Battery2", "2001", 200);
        Battery battery3 = new Battery("Battery3", "3000", 300);

        batteryRepository.saveAll(List.of(battery1, battery2, battery3));

        List<Battery> result = batteryRepository.findByPostcodeBetweenAndCapacityOrderByNameASC("2000", "2001", 50, 250);

        assertEquals(2, result.size());
        assertEquals("Battery1", result.get(0).getName());
        assertEquals("Battery2", result.get(1).getName());
    }

    @Test
    void returnsEmptyListWhenNoBatteriesMatchCriteria() {
        Battery battery1 = new Battery("Battery1", "4000", 400);

        batteryRepository.save(battery1);

        List<Battery> result = batteryRepository.findByPostcodeBetweenAndCapacityOrderByNameASC("2000", "3000", 50, 250);

        assertEquals(0, result.size());
    }

}
