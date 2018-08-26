package igc.dist.gateway;

public interface Server {

  void start() throws InterruptedException;

  void shutdown();
}
