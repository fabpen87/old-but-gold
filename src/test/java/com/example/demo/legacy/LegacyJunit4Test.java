package com.example.demo.legacy;

import com.example.demo.web.GreetingController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

/**
 * Legacy JUnit 4 test using {@link SpringRunner}. Left here on purpose to mimic a
 * real codebase where some tests were never migrated to JUnit 5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LegacyJunit4Test {

    @Autowired
    private GreetingController greetingController;

    @Test
    public void greetingControllerIsLoaded() {
        assertNotNull(greetingController);
    }
}
