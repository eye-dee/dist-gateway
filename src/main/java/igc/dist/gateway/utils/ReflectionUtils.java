package igc.dist.gateway.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class ReflectionUtils {

  /**
   * Method returns generic parameter
   *
   * @param clazz - Class, witch implements some interface or extends some class
   * @param index - index of generic list in declaration
   */
  public static Class getGenericParameterFromClass(final Class clazz, final int index) {
    final Type[] genericInterfaces = clazz.getGenericInterfaces();
    for (final Type genericInterface : genericInterfaces) {
      if (genericInterface instanceof ParameterizedType) {
        return (Class) ((ParameterizedType) genericInterface)
            .getActualTypeArguments()[index];
      }
    }
    return (Class) ((ParameterizedType) clazz.getGenericSuperclass())
        .getActualTypeArguments()[index];
  }
}
