package igc.dist.gateway.handler;

import igc.dist.gateway.handler.DatabaseRegistrationHandler.DatabaseEntity;
import igc.dist.gateway.service.DatabaseChooser;
import igc.dist.proto.Connection.ChooseDatabase;
import igc.dist.proto.Connection.CreateConnectionRequest;
import igc.dist.proto.Connection.CreateConnectionResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateConnectionHandler implements
    ReplyingHandler<CreateConnectionRequest, CreateConnectionResponse> {

  public static final Map<ChannelId, DatabaseEntity> USER_SESSION = new ConcurrentHashMap<>();
  private final DatabaseChooser databaseChooser;

  @Override
  public CreateConnectionResponse handleRequest(CreateConnectionRequest connection,
      ChannelHandlerContext ctx) {
    var entry = databaseChooser.chooseDatabase(DatabaseRegistrationHandler.CONNECTED_DATABASES);

    final String token = UUID.randomUUID().toString();
    entry.getValue().getContext()
        .writeAndFlush(ChooseDatabase.newBuilder()
            .setToken(token)
            .build());

    USER_SESSION.putIfAbsent(ctx.channel().id(), entry.getValue());

    return CreateConnectionResponse.newBuilder()
        .setToken(token)
        .setHost(entry.getValue().getHost())
        .setPort(entry.getValue().getPort())
        .build();
  }

}
