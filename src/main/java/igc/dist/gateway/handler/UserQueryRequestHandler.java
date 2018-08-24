package igc.dist.gateway.handler;

import static igc.dist.gateway.handler.CreateConnectionHandler.USER_SESSION;

import igc.dist.proto.Query.QueryRequest;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserQueryRequestHandler implements PacketHandler<QueryRequest> {

  @Override
  public void handle(QueryRequest request, ChannelHandlerContext ctx) {
    log.info("request received {}", request);
    USER_SESSION.computeIfPresent(ctx.channel().id(), (id, db) -> {
      log.info("request {}", request);
      db.getContext().writeAndFlush(request);
      return db;
    });
  }
}
