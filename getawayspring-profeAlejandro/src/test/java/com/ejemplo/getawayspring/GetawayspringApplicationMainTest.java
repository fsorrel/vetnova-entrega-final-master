package com.ejemplo.getawayspring;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

class GetawayspringApplicationMainTest {

    @Test
    void testMainInvocaSpringApplicationRun() {
        String[] args = {"--server.port=0"};
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(GetawayspringApplication.class, args))
                    .thenReturn(mock(ConfigurableApplicationContext.class));

            GetawayspringApplication.main(args);

            mocked.verify(() -> SpringApplication.run(GetawayspringApplication.class, args));
        }
    }
}
