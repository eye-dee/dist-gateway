package igc.dist.gateway.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.protobuf.GeneratedMessageV3;
import igc.dist.proto.Connection.ChooseDatabase;
import igc.dist.proto.Connection.CreateConnection;
import igc.dist.proto.Connection.CreateConnectionRequest;
import igc.dist.proto.Connection.CreateConnectionResponse;
import igc.dist.proto.Connection.MessageAccepted;
import igc.dist.proto.Query.QueryRequest;
import igc.dist.proto.Query.QueryResponse;
import igc.dist.proto.Register.RegistrationRequest;
import igc.dist.proto.Register.RegistrationResponse;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PacketConfig {

  @Bean
  public static BiMap<Integer, GeneratedMessageV3> messages() {
    final BiMap<Integer, GeneratedMessageV3> messages = HashBiMap.create();

    messages.put(1000, CreateConnectionRequest.getDefaultInstance());
    messages.put(1001, ChooseDatabase.getDefaultInstance());
    messages.put(1002, MessageAccepted.getDefaultInstance());
    messages.put(1003, CreateConnectionResponse.getDefaultInstance());
    messages.put(1004, CreateConnection.getDefaultInstance());

    messages.put(1005, QueryRequest.getDefaultInstance());
    messages.put(1006, QueryResponse.getDefaultInstance());

    messages.put(1007, RegistrationRequest.getDefaultInstance());
    messages.put(1008, RegistrationResponse.getDefaultInstance());

    return messages;
  }

  @Bean
  public static Map<Class, Integer> messageIds() {
    return messages().entrySet()
        .stream()
        .collect(Collectors.toMap(entry -> entry.getValue().getClass(), Entry::getKey));
  }
}
