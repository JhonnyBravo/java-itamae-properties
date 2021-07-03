package java_itamae_properties.domain.repository.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java_itamae_contents.domain.model.ContentsAttribute;
import java_itamae_contents.domain.repository.stream.StreamRepository;
import java_itamae_contents.domain.repository.stream.StreamRepositoryImpl;
import javax.inject.Inject;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * プロパティファイルが空である場合のテスト。
 */
@RunWith(CdiRunner.class)
@AdditionalClasses({PropertiesRepositoryImpl.class, StreamRepositoryImpl.class})
public class EmptyFile {
  @Inject
  private PropertiesRepository pr;
  @Inject
  private StreamRepository sr;
  private File file;

  private final BooleanSupplier isWindows = () -> {
    boolean result = false;
    final String osName = System.getProperty("os.name");

    if (osName.substring(0, 3).equals("Win")) {
      result = true;
    }

    return result;
  };

  @Before
  public void setUp() throws Exception {
    file = new File("test.properties");
    file.createNewFile();
  }

  @After
  public void tearDown() throws Exception {
    file.delete();
  }

  /**
   * {@link PropertiesRepository#getProperties(java.io.Reader)} 実行時に空の {@link Map} が返されること。
   *
   * @throws Exception {@link Exception}
   */
  @Test
  public void test1() throws Exception {
    final ContentsAttribute attr = new ContentsAttribute();
    attr.setPath("test.properties");

    try (Reader reader = sr.getReader(attr)) {
      final Map<String, String> properties = pr.getProperties(reader);
      assertThat(properties.size(), is(0));
    }
  }

  /**
   * {@link PropertiesRepository#updateProperties(java.io.Writer, Map, String)}
   * 実行時にファイルへプロパティの書込みができること。
   *
   * @throws Exception {@link Exception}
   */
  @Test
  public void test2() throws Exception {
    final Map<String, String> newProps = new HashMap<>();
    newProps.put("property1", "1 つ目のプロパティ");
    newProps.put("property2", "2 つ目のプロパティ");

    final ContentsAttribute attr = new ContentsAttribute();
    attr.setPath("test.properties");

    try (Writer writer = sr.getWriter(attr)) {
      final boolean status = pr.updateProperties(writer, newProps, attr.getPath());
      assertThat(status, is(true));
    }

    try (Reader reader = sr.getReader(attr)) {
      final Map<String, String> curProps = pr.getProperties(reader);
      assertThat(curProps.size(), is(2));
      assertThat(curProps.get("property1"), is("1 つ目のプロパティ"));
      assertThat(curProps.get("property2"), is("2 つ目のプロパティ"));
    }
  }

  /**
   * 文字エンコーディングを指定してプロパティの読み書きができること。
   *
   * @throws Exception {@link Exception}
   */
  @Test
  public void test3() throws Exception {
    final Map<String, String> newProps = new HashMap<>();
    newProps.put("property1", "1 つ目のプロパティ");
    newProps.put("property2", "2 つ目のプロパティ");

    final ContentsAttribute attr = new ContentsAttribute();
    attr.setPath("test.properties");

    if (isWindows.getAsBoolean()) {
      attr.setEncoding("UTF-8");
    } else {
      attr.setEncoding("MS932");
    }

    try (Writer writer = sr.getWriter(attr)) {
      final boolean status = pr.updateProperties(writer, newProps, attr.getPath());
      assertThat(status, is(true));
    }

    try (Reader reader = sr.getReader(attr)) {
      final Map<String, String> curProps = pr.getProperties(reader);
      assertThat(curProps.size(), is(2));
      assertThat(curProps.get("property1"), is("1 つ目のプロパティ"));
      assertThat(curProps.get("property2"), is("2 つ目のプロパティ"));
    }
  }
}
