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
 * プロパティファイルが空ではない場合のテスト。
 */
@RunWith(CdiRunner.class)
@AdditionalClasses({PropertiesRepositoryImpl.class, StreamRepositoryImpl.class})
public class NotEmptyFile {
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

    final ContentsAttribute attr = new ContentsAttribute();
    attr.setPath("test.properties");

    final Map<String, String> properties = new HashMap<>();
    properties.put("property1", "1 つ目のプロパティ");
    properties.put("property2", "2 つ目のプロパティ");

    try (Writer writer = sr.getWriter(attr)) {
      pr.updateProperties(writer, properties, attr.getPath());
    }
  }

  @After
  public void tearDown() throws Exception {
    file.delete();
  }

  /**
   * {@link PropertiesRepository#getProperties(java.io.Reader)} 実行時にプロパティファイルの内容を {@link Map}
   * 変換できること。
   *
   * @throws Exception {@link Exception}
   */
  @Test
  public void test1() throws Exception {
    final ContentsAttribute attr = new ContentsAttribute();
    attr.setPath("test.properties");

    try (Reader reader = sr.getReader(attr)) {
      final Map<String, String> properties = pr.getProperties(reader);
      assertThat(properties.size(), is(2));
      assertThat(properties.get("property1"), is("1 つ目のプロパティ"));
      assertThat(properties.get("property2"), is("2 つ目のプロパティ"));
    }
  }

  /**
   * {@link PropertiesRepository#updateProperties(java.io.Writer, Map, String)}
   * 実行時にプロパティファイルの上書きができること。
   *
   * @throws Exception {@link Exception}
   */
  @Test
  public void test2() throws Exception {
    final Map<String, String> newProps = new HashMap<>();
    newProps.put("update", "更新テスト");

    final ContentsAttribute attr = new ContentsAttribute();
    attr.setPath("test.properties");

    try (Writer writer = sr.getWriter(attr)) {
      final boolean status = pr.updateProperties(writer, newProps, attr.getPath());
      assertThat(status, is(true));
    }

    try (Reader reader = sr.getReader(attr)) {
      final Map<String, String> curProps = pr.getProperties(reader);
      assertThat(curProps.size(), is(1));
      assertThat(curProps.get("update"), is("更新テスト"));
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
    newProps.put("encoding", "文字コードテスト");

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
      assertThat(curProps.size(), is(1));
      assertThat(curProps.get("encoding"), is("文字コードテスト"));
    }
  }

  /**
   * {@link PropertiesRepository#deleteProperties(Writer, String)} 実行時にプロパティファイルを空にできること。
   *
   * @throws Exception {@link Exception}
   */
  @Test
  public void test4() throws Exception {
    final ContentsAttribute attr = new ContentsAttribute();
    attr.setPath("test.properties");

    try (Writer writer = sr.getWriter(attr)) {
      final boolean status = pr.deleteProperties(writer, attr.getPath());
      assertThat(status, is(true));
    }

    try (Reader reader = sr.getReader(attr)) {
      final Map<String, String> properties = pr.getProperties(reader);
      assertThat(properties.size(), is(0));
    }
  }
}
