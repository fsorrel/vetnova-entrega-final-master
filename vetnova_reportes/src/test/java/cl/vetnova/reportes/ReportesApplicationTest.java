package cl.vetnova.reportes;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

class ReportesApplicationTest {

    @Test
    void testMainInvocaSpringApplicationRun() {
        String[] args = {"--server.port=0"};
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(ReportesApplication.class, args))
                    .thenReturn(mock(ConfigurableApplicationContext.class));

            ReportesApplication.main(args);

            mocked.verify(() -> SpringApplication.run(ReportesApplication.class, args));
        }
    }
}
