package igc.dist.gateway.service;

import java.util.Map;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RandomDatabaseChooser implements DatabaseChooser{

  private final RandomElementChooser chooser;

  @Override
  public <K, V> Entry<K, V> chooseDatabase(Map<K, V> databases) {
    return chooser.getRandom(databases);
  }

}
