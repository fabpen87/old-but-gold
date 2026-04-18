package com.example.demo.legacy;

import com.example.demo.web.GreetingController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class LegacyJunit4Test {

    @Autowired
    private GreetingController greetingController;

    @Test
    void greetingControllerIsLoaded() {
        assertNotNull(greetingController);
    }
}
