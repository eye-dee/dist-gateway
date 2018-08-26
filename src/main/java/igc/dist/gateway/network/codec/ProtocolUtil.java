package igc.dist.gateway.network.codec;

final class ProtocolUtil {

  static final int MESSAGE_ID_HEADER_SIZE = 2;
  private static final int MAX_MESSAGE_LENGTH = 16777215;
  private static final int MESSAGE_LENGTH_HEADER_SIZE = 3;
  static final int HEADER_SIZE = MESSAGE_ID_HEADER_SIZE + MESSAGE_LENGTH_HEADER_SIZE;

  static int decodeMessageId(final byte[] in) {
    if (in == null || in.length != MESSAGE_ID_HEADER_SIZE) {
      throw new IllegalArgumentException("MessageId decoder require 2 bytes.");
    }
    return ((in[0] & 0xff) << 8) | (in[1] & 0xff);
  }

  static int decodePayloadLength(final byte[] in) {
    if (in == null || in.length != MESSAGE_LENGTH_HEADER_SIZE) {
      throw new IllegalArgumentException("Payload size decoder require 3 bytes.");
    }
    return ((in[0] & 0xff) << 16) | ((in[1] & 0xff) << 8) | (in[2] & 0xff);
  }

  static byte[] messageToBytes(final int messageId, final byte[] message) {
    final int length = message.length;
    if (length > MAX_MESSAGE_LENGTH) {
      throw new IndexOutOfBoundsException(String.format(
          "Max message length is %s bytes, got %s bytes.",
          MAX_MESSAGE_LENGTH,
          length
      ));
    }

    final byte[] bytesMsg = new byte[MESSAGE_ID_HEADER_SIZE + MESSAGE_LENGTH_HEADER_SIZE + length];
    bytesMsg[0] = (byte) ((messageId >> 8) & 0xff);
    bytesMsg[1] = (byte) (messageId & 0xff);

    bytesMsg[2] = (byte) ((length >> 16) & 0xff);
    bytesMsg[3] = (byte) ((length >> 8) & 0xff);
    bytesMsg[4] = (byte) (length & 0xff);

    System.arraycopy(message, 0, bytesMsg, HEADER_SIZE, message.length);

    return bytesMsg;
  }
}
