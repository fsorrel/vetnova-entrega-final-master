package cl.vetnova.laboratorio;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

class LaboratorioServiceApplicationTest {

    @Test
    void testMainInvocaSpringApplicationRun() {
        String[] args = {"--server.port=0"};
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(LaboratorioServiceApplication.class, args))
                    .thenReturn(mock(ConfigurableApplicationContext.class));

            LaboratorioServiceApplication.main(args);

            mocked.verify(() -> SpringApplication.run(LaboratorioServiceApplication.class, args));
        }
    }
}
