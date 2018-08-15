package igc.dist.gateway;

import igc.dist.gateway.network.GatewayServerInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GatewayServer extends AbstractServer {

  public GatewayServer(
      @Value("${server.login-server.port:6666}") final int serverPort,
      final GatewayServerInitializer gatewayServerInitializer) {
    super(serverPort, new NioEventLoopGroup(4), new NioEventLoopGroup(),
        gatewayServerInitializer, new LoggingHandler(LogLevel.INFO), NioServerSocketChannel.class);
  }

}
