package com.example.demo.legacy;

import com.example.demo.web.GreetingController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Legacy JUnit 4 test using {@link SpringRunner}. Left here on purpose to mimic a
 * real codebase where some tests were never migrated to JUnit 5.
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
