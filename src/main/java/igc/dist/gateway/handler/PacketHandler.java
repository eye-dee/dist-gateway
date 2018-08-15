package igc.dist.gateway.handler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;

public interface PacketHandler<T extends GeneratedMessageV3> {

  void handle(T packet, ChannelHandlerContext ctx);
}
