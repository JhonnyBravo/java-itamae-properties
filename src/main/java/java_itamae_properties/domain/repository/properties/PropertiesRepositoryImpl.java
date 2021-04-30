package java_itamae_properties.domain.repository.properties;

import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesRepositoryImpl implements PropertiesRepository {

  @Override
  public Map<String, String> getProperties(Reader reader) throws Exception {
    final Properties properties = new Properties();
    properties.load(reader);

    final Map<String, String> map = new HashMap<>();

    for (final Map.Entry<Object, Object> entry : properties.entrySet()) {
      map.put(entry.getKey().toString(), entry.getValue().toString());
    }

    return map;
  }

  @Override
  public boolean updateProperties(Writer writer, Map<String, String> map, String comment)
      throws Exception {
    boolean status = false;
    final Properties properties = new Properties();

    for (final Map.Entry<String, String> entry : map.entrySet()) {
      properties.setProperty(entry.getKey(), entry.getValue());
    }

    properties.store(writer, comment);
    status = true;

    return status;
  }

  @Override
  public boolean deleteProperties(Writer writer, String comment) throws Exception {
    boolean status = false;

    final Properties properties = new Properties();
    properties.store(writer, comment);

    status = true;
    return status;
  }
}
