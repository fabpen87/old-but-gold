package com.example.demo.legacy;

import com.example.demo.web.GreetingController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Formerly a JUnit 4 test using SpringRunner. Migrated to JUnit 5.
 */
@SpringBootTest
public class LegacyJunit4Test {

    @Autowired
    private GreetingController greetingController;

    @Test
    public void greetingControllerIsLoaded() {
        assertNotNull(greetingController);
    }
}
