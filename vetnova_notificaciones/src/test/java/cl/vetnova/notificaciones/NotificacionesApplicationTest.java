package cl.vetnova.notificaciones;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

class NotificacionesApplicationTest {

    @Test
    void testMainInvocaSpringApplicationRun() {
        String[] args = {"--server.port=0"};
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(NotificacionesApplication.class, args))
                    .thenReturn(mock(ConfigurableApplicationContext.class));

            NotificacionesApplication.main(args);

            mocked.verify(() -> SpringApplication.run(NotificacionesApplication.class, args));
        }
    }
}
