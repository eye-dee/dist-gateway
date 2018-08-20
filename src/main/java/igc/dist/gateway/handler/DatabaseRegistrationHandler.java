package igc.dist.gateway.handler;

import igc.dist.proto.Register.RegistrationRequest;
import igc.dist.proto.Register.RegistrationResponse;
import io.netty.channel.ChannelHandlerContext;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DatabaseRegistrationHandler implements
    ReplyingHandler<RegistrationRequest, RegistrationResponse> {

  public static final Map<String, ChannelHandlerContext> CONNECTED_DATABASES = new ConcurrentHashMap<>();

  @Override
  public RegistrationResponse handleRequest(RegistrationRequest registrationRequest,
      ChannelHandlerContext ctx) {
    final String key = UUID.randomUUID().toString();
    CONNECTED_DATABASES.put(key, ctx);
    log.info("new database connected with key {}", key);

    return RegistrationResponse.newBuilder().build();
  }
}
