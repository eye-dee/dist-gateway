package igc.dist.gateway.service;

import java.util.Map;
import java.util.Map.Entry;

public interface DatabaseChooser {

  <K,V> Entry<K, V> chooseDatabase(Map<K, V> databases);
}
