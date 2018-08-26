package igc.dist.gateway.network;

import com.google.protobuf.GeneratedMessageV3;
import igc.dist.gateway.handler.PacketHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.Map;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
class GatewayPacketHandler extends SimpleChannelInboundHandler<Object> {

  private final Executor handlerExecutor;
  private final Map<Class, PacketHandler> handlers;

  @Override
  protected void messageReceived(ChannelHandlerContext ctx, Object msg) {
    final PacketHandler handler = handlers.get(msg.getClass());

    if (!(msg instanceof GeneratedMessageV3)) {
      log.error("msg is not instance of GeneratedMessageV3, but {}", msg.getClass());
      return;
    }

    if (handler != null) {
      handlerExecutor.execute(() ->
          handleMessageWithMetrics(ctx, (GeneratedMessageV3) msg, handler));
    } else {
      log.error("Handler is null for class {}", msg.getClass().getName());
    }
  }

  @SuppressWarnings("unchecked")
  private void handleMessageWithMetrics(
      final ChannelHandlerContext ctx, final GeneratedMessageV3 msg, final PacketHandler handler) {
    handler.handle(msg, ctx);
  }

  @Override
  public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
    log.error("Packet handler error, going to close connection.", cause);
    ctx.close();
  }


}
