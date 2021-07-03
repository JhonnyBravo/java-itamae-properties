package java_itamae_properties.domain.service.properties;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java_itamae_contents.domain.model.ContentsAttribute;
import java_itamae_contents.domain.repository.stream.StreamRepository;
import java_itamae_contents.domain.repository.stream.StreamRepositoryImpl;
import java_itamae_properties.domain.repository.properties.PropertiesRepository;
import java_itamae_properties.domain.repository.properties.PropertiesRepositoryImpl;
import javax.enterprise.inject.New;
import javax.inject.Inject;

public class PropertiesServiceImpl implements PropertiesService {
  @Inject
  @New(StreamRepositoryImpl.class)
  private StreamRepository sr;
  @Inject
  @New(PropertiesRepositoryImpl.class)
  private PropertiesRepository pr;
  private ContentsAttribute attr;

  @Override
  public void init(ContentsAttribute attr) {
    this.attr = attr;
  }

  @Override
  public String getProperty(String key) throws Exception {
    String value = null;

    try (Reader reader = sr.getReader(attr)) {
      final Map<String, String> properties = pr.getProperties(reader);

      if (!properties.containsKey(key)) {
        throw new Exception(key + " が見つかりません。");
      }

      value = properties.get(key);
    }

    return value;
  }

  @Override
  public boolean createProperty(String key, String value) throws Exception {
    boolean status = false;
    Map<String, String> properties = new HashMap<>();

    try (Reader reader = sr.getReader(attr)) {
      properties = pr.getProperties(reader);
    }

    if (properties.containsKey(key)) {
      throw new Exception(key + " は登録済みです。");
    }

    try (Writer writer = sr.getWriter(attr)) {
      properties.put(key, value);
      final String fileName = new File(attr.getPath()).getName();
      status = pr.updateProperties(writer, properties, fileName);
    }

    return status;
  }

  @Override
  public boolean updateProperty(String key, String value) throws Exception {
    boolean status = false;
    Map<String, String> properties = new HashMap<>();

    try (Reader reader = sr.getReader(attr)) {
      properties = pr.getProperties(reader);
    }

    if (!properties.containsKey(key)) {
      throw new Exception(key + " が見つかりません。");
    }

    if (!value.equals(properties.get(key))) {
      try (Writer writer = sr.getWriter(attr)) {
        properties.put(key, value);
        final String fileName = new File(attr.getPath()).getName();
        status = pr.updateProperties(writer, properties, fileName);
      }
    }

    return status;
  }

  @Override
  public boolean deleteProperty(String key) throws Exception {
    boolean status = false;
    Map<String, String> properties = new HashMap<>();

    try (Reader reader = sr.getReader(attr)) {
      properties = pr.getProperties(reader);
    }

    if (!properties.containsKey(key)) {
      throw new Exception(key + " が見つかりません。");
    }

    try (Writer writer = sr.getWriter(attr)) {
      properties.remove(key);
      final String fileName = new File(attr.getPath()).getName();
      status = pr.updateProperties(writer, properties, fileName);
    }

    return status;
  }
}
