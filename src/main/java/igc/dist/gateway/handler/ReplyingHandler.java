package igc.dist.gateway.handler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;

public interface ReplyingHandler<Request extends GeneratedMessageV3,
    Response extends GeneratedMessageV3> extends PacketHandler<Request> {

  @Override
  default void handle(Request packet, ChannelHandlerContext ctx) {
    ctx.writeAndFlush(handleRequest(packet, ctx));
  }

  Response handleRequest(Request request, ChannelHandlerContext ctx);
}
