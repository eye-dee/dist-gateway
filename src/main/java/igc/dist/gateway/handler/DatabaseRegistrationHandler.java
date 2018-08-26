package igc.dist.gateway.handler;

import igc.dist.proto.Register.RegistrationRequest;
import igc.dist.proto.Register.RegistrationResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DatabaseRegistrationHandler implements
    ReplyingHandler<RegistrationRequest, RegistrationResponse> {

  public static final Map<ChannelId, DatabaseEntity> CONNECTED_DATABASES = new ConcurrentHashMap<>();

  @Override
  public RegistrationResponse handleRequest(RegistrationRequest request,
      ChannelHandlerContext ctx) {
    var key = ctx.channel().id();
    CONNECTED_DATABASES.put(key, DatabaseEntity.builder()
        .context(ctx)
        .host(request.getHost())
        .port(request.getPort())
        .build());
    log.info("new database connected with key {}", key);

    return RegistrationResponse.newBuilder().build();
  }

  @Getter
  @Builder
  public static class DatabaseEntity {

    private final ChannelHandlerContext context;
    private final String host;
    private final int port;
  }
}
