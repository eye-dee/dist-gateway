package igc.dist.gateway.handler;

import igc.dist.gateway.service.DatabaseChooser;
import igc.dist.proto.Connection.ChooseDatabase;
import igc.dist.proto.Connection.CreateConnectionRequest;
import igc.dist.proto.Connection.CreateConnectionResponse;
import io.netty.channel.ChannelHandlerContext;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateConnectionHandler implements
    ReplyingHandler<CreateConnectionRequest, CreateConnectionResponse> {

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

    return CreateConnectionResponse.newBuilder()
        .setToken(token)
        .setHost(entry.getValue().getHost())
        .build();
  }

}
