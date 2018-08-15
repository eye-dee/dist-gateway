package igc.dist.gateway.config;

import static igc.dist.gateway.utils.ReflectionUtils.getGenericParameterFromClass;

import igc.dist.gateway.handler.PacketHandler;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class HandlerConfig {

  @Bean
  public Map<Class, PacketHandler> handlers(final List<PacketHandler> packetHandlerList) {
    return packetHandlerList.stream()
        .collect(Collectors.toMap(
            v -> getGenericParameterFromClass(v.getClass(), 0),
            Function.identity(),
            (v1, v2) -> {
              log.warn("Two equivalent classes {} and {}",
                  v1.getClass().getName(), v2.getClass().getName());
              return v1;
            }
        ));
  }

}
