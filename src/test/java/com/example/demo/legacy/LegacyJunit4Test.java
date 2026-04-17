package com.example.demo.legacy;

import com.example.demo.web.GreetingController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Migrated from JUnit 4 + {@code SpringRunner} to JUnit 5. {@code @SpringBootTest}
 * already wires in the Spring test context via {@code SpringExtension}, so no
 * explicit {@code @ExtendWith} is needed.
 */
@SpringBootTest
class LegacyJunit4Test {

    @Autowired
    private GreetingController greetingController;

    @Test
    void greetingControllerIsLoaded() {
        assertNotNull(greetingController);
    }
}
