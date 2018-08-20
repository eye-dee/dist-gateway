package igc.dist.gateway.handler;

import igc.dist.proto.Register.RegistrationRequest;
import igc.dist.proto.Register.RegistrationResponse;
import io.netty.channel.ChannelHandlerContext;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DatabaseRegistrationHandler implements
    ReplyingHandler<RegistrationRequest, RegistrationResponse> {

  public static final Map<String, DatabaseEntity> CONNECTED_DATABASES = new ConcurrentHashMap<>();

  @Override
  public RegistrationResponse handleRequest(RegistrationRequest registrationRequest,
      ChannelHandlerContext ctx) {
    var key = UUID.randomUUID().toString();
    var socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
    CONNECTED_DATABASES.put(key, DatabaseEntity.builder()
        .context(ctx)
        .host(socketAddress.getHostString())
        .build());
    log.info("new database connected with key {}", key);

    return RegistrationResponse.newBuilder().build();
  }

  @Data
  @Builder
  public static class DatabaseEntity {
    private final ChannelHandlerContext context;
    private final String host;
  }
}
