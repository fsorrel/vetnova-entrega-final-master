package cl.vetnova.envio;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

class EnvioServiceApplicationTest {

    @Test
    void testMainInvocaSpringApplicationRun() {
        String[] args = {"--server.port=0"};
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(EnvioServiceApplication.class, args))
                    .thenReturn(mock(ConfigurableApplicationContext.class));

            EnvioServiceApplication.main(args);

            mocked.verify(() -> SpringApplication.run(EnvioServiceApplication.class, args));
        }
    }
}
