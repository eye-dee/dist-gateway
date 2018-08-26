package igc.dist.gateway.service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class RandomElementChooser {

  private final Random generator = new Random();

  public <K, V> Map.Entry<K, V> getRandom(Map<K, V> map) {
    Object[] entries = map.entrySet().toArray();
    return (Entry<K, V>) entries[generator.nextInt(entries.length)];
  }

}
