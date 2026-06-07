package com.malaika.backend;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


class BackendApplicationTests {

    @Test
    void contextLoads() {

        assertDoesNotThrow(BackendApplication::new);
    }

    @Test
    void main_invokesSpringApplicationRun() {
        try (MockedStatic<SpringApplication> mocked =
                     Mockito.mockStatic(SpringApplication.class)) {

            mocked.when(() -> SpringApplication.run(
                    eq(BackendApplication.class), any(String[].class))
            ).thenReturn(Mockito.mock(ConfigurableApplicationContext.class));

            assertDoesNotThrow(() -> BackendApplication.main(new String[]{}));

            mocked.verify(() ->
                    SpringApplication.run(
                            eq(BackendApplication.class),
                            any(String[].class)
                    )
            );
        }
    }
}