package igc.dist.gateway.handler;

import igc.dist.proto.Connection.MessageAccepted;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

@Service
public class EmptyHandler implements PacketHandler<MessageAccepted> {

  @Override
  public void handle(MessageAccepted packet, ChannelHandlerContext ctx) {

  }
}
