package igc.dist.gateway.network.codec;

import static igc.dist.gateway.network.codec.ProtocolDecoder.ProcessingState.MESSAGE_HEADER;
import static igc.dist.gateway.network.codec.ProtocolDecoder.ProcessingState.PAYLOAD;
import static igc.dist.gateway.network.codec.ProtocolUtil.HEADER_SIZE;
import static igc.dist.gateway.network.codec.ProtocolUtil.MESSAGE_ID_HEADER_SIZE;
import static igc.dist.gateway.network.codec.ProtocolUtil.decodeMessageId;
import static igc.dist.gateway.network.codec.ProtocolUtil.decodePayloadLength;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
public class ProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {

  private final Map<Integer, GeneratedMessageV3> messages;
  private ProcessingState currentState = MESSAGE_HEADER;
  private int messageId;
  private int payloadLength = -1;
  private int bufferIndex;
  private byte[] buffer;
  private int headerIndex;
  private byte[] header;

  @Override
  protected void decode(final ChannelHandlerContext ctx, final ByteBuf msg, final List<Object> out)
      throws Exception {
    int incomingBytes;
    while ((incomingBytes = msg.readableBytes()) != 0) {

      if (MESSAGE_HEADER.equals(currentState)) {
        allocateMemoryForHeader();

        if (headerIndex + incomingBytes < HEADER_SIZE) {
          msg.getBytes(msg.readerIndex(), header, headerIndex, incomingBytes);
          msg.skipBytes(incomingBytes);
          headerIndex += incomingBytes;
        } else {
          var needRead = HEADER_SIZE - headerIndex;
          msg.getBytes(msg.readerIndex(), header, headerIndex, needRead);
          msg.skipBytes(needRead);
          headerIndex += needRead;
        }

        if (headerRead()) {
          messageId = decodeMessageId(Arrays.copyOfRange(header, 0, MESSAGE_ID_HEADER_SIZE));
          payloadLength = decodePayloadLength(Arrays.copyOfRange(
              header, MESSAGE_ID_HEADER_SIZE, HEADER_SIZE));
          toPayload();
        }

      }

      if (PAYLOAD.equals(currentState)) {
        allocateMemoryForPayload();
        var length = payloadLength - bufferIndex;
        var readableBytes = msg.readableBytes();
        var needToRead = readableBytes > length ? length : readableBytes;

        msg.getBytes(msg.readerIndex(), buffer, bufferIndex, needToRead);
        bufferIndex += needToRead;
        msg.skipBytes(needToRead);
      }

      if (payloadRead()) {
        final GeneratedMessageV3 prototype = messages.get(messageId);
        if (prototype != null) {
          out.add(prototype.getParserForType().parseFrom(buffer));
          resetStates();
        } else {
          var ipAddress = getIpAddress(ctx);
          log.error("Protobuf decoder not found for messageId: {}, ip: {}", messageId, ipAddress);
          ctx.channel().close();
          break;
        }
      }
    }
  }

  private void toPayload() {
    currentState = PAYLOAD;
    header = null;
    headerIndex = 0;
  }

  private void resetStates() {
    currentState = MESSAGE_HEADER;
    buffer = null;
    bufferIndex = 0;
    messageId = 0;
    payloadLength = -1;
  }

  private boolean payloadRead() {
    return bufferIndex == payloadLength && payloadLength >= 0;
  }

  private void allocateMemoryForPayload() {
    if (buffer == null) {
      buffer = new byte[payloadLength];
      bufferIndex = 0;
    }
  }

  private boolean headerRead() {
    return headerIndex == HEADER_SIZE;
  }

  private void allocateMemoryForHeader() {
    if (header == null) {
      header = new byte[HEADER_SIZE];
      headerIndex = 0;
    }
  }

  private String getIpAddress(final ChannelHandlerContext ctx) {
    var address = ctx.channel().remoteAddress();
    if (address instanceof InetSocketAddress) {
      var socketAddress = (InetSocketAddress) address;
      var inetAddress = socketAddress.getAddress();
      return inetAddress.getHostAddress();
    }
    return "can't pull ip address";
  }

  private byte[] readBytes(final ByteBuf msg, final int countOfBytes) {
    final byte[] array = new byte[countOfBytes];
    msg.getBytes(msg.readerIndex(), array, 0, countOfBytes);

    return array;
  }

  protected enum ProcessingState {
    MESSAGE_HEADER, PAYLOAD
  }

}
