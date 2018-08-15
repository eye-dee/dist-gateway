package igc.dist.gateway.network.codec;

import static igc.dist.gateway.network.codec.ProtocolUtil.messageToBytes;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProtocolEncoder extends MessageToMessageEncoder<GeneratedMessageV3> {

  private final Map<Class, Integer> messages;

  @Override
  protected void encode(final ChannelHandlerContext ctx, final GeneratedMessageV3 msg,
      final List<Object> out) {

    final Integer messageId = messages.get(msg.getClass());
    if (messageId != null) {
      out.add(Unpooled.wrappedBuffer(messageToBytes(messageId, msg.toByteArray())));
    } else {
      throw new RuntimeException("Not found");
    }
  }
}
