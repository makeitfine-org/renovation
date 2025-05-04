package renovation.event.service.config.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.SystemHealth;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;

@Profile("!test")
@Component
@Slf4j
public class HealthStatusMetrics implements MeterBinder {

    private final HealthEndpoint healthEndpoint;

    public HealthStatusMetrics(HealthEndpoint healthEndpoint) {
        this.healthEndpoint = healthEndpoint;
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        HealthComponent overall = healthEndpoint.health();
        if (overall instanceof SystemHealth systemHealth) {
            Map<String, HealthComponent> components = systemHealth.getComponents();
            components.forEach((name, ignored) -> {
                registry.gauge("health_component_status",
                        Tags.of("component", name),
                        healthEndpoint,
                        endpoint -> getStatusAsNumber(endpoint, name));
            });
        }
    }

    private double getStatusAsNumber(HealthEndpoint endpoint, String componentName) {
        HealthComponent hc = endpoint.health();
        if (hc instanceof SystemHealth systemHealth) {
            HealthComponent component = systemHealth.getComponents().get(componentName);
            if (component != null) {
                String status = component.getStatus().getCode();
                return "UP".equalsIgnoreCase(status) ? 1.0 : 0.0;
            }
        }
        return 0.0; // Unknown or missing component is DOWN
    }
}
