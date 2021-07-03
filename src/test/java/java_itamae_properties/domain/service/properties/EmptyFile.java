package java_itamae_properties.domain.service.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.File;
import java.util.function.BooleanSupplier;
import java_itamae_contents.domain.model.ContentsAttribute;
import javax.inject.Inject;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * ファイルが存在して空である場合のテスト。
 */
@RunWith(CdiRunner.class)
@AdditionalClasses(PropertiesServiceImpl.class)
public class EmptyFile {
  @Inject
  private PropertiesService service;
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
   * {@link PropertiesService#getProperty(String)} 実行時に {@link Exception} が送出されること。
   *
   * @throws Exception {@link Exception}
   */
  @Test(expected = Exception.class)
  public void test1() throws Exception {
    final ContentsAttribute attr = new ContentsAttribute();
    attr.setPath("test.properties");
    service.init(attr);

    try {
      service.getProperty("test");
    } catch (final Exception e) {
      System.err.println(e);
      throw e;
    }
  }

  /**
   * {@link PropertiesService#createProperty(String, String))} 実行時にプロパティファイルへの書込みができて終了ステータスが true
   * であること。
   *
   * @throws Exception {@link Exception}
   */
  @Test
  public void test2() throws Exception {
    final ContentsAttribute attr = new ContentsAttribute();
    attr.setPath("test.properties");
    service.init(attr);

    final boolean status = service.createProperty("test", "登録テスト");
    assertThat(status, is(true));
    assertThat(service.getProperty("test"), is("登録テスト"));
  }

  /**
   * 文字エンコーディングを指定してプロパティファイルの読み書きができること。
   *
   * @throws Exception {@link Exception}
   */
  @Test
  public void test3() throws Exception {
    final ContentsAttribute attr = new ContentsAttribute();
    attr.setPath("test.properties");

    if (isWindows.getAsBoolean()) {
      attr.setEncoding("UTF-8");
    } else {
      attr.setEncoding("MS932");
    }

    service.init(attr);

    final boolean status = service.createProperty("test", "登録テスト");
    assertThat(status, is(true));
    assertThat(service.getProperty("test"), is("登録テスト"));
  }

  /**
   * {@link PropertiesService#updateProperty(String, String)} 実行時に {@link Exception} が送出されること。
   *
   * @throws Exception {@link Exception}
   */
  @Test(expected = Exception.class)
  public void test4() throws Exception {
    final ContentsAttribute attr = new ContentsAttribute();
    attr.setPath("test.properties");
    service.init(attr);

    try {
      service.updateProperty("test", "更新テスト");
    } catch (final Exception e) {
      System.err.println(e);
      throw e;
    }
  }

  /**
   * {@link PropertiesService#deleteProperty(String)} 実行時に {@link Exception} が送出されること。
   *
   * @throws Exception {@link Exception}
   */
  @Test(expected = Exception.class)
  public void test5() throws Exception {
    final ContentsAttribute attr = new ContentsAttribute();
    attr.setPath("test.properties");
    service.init(attr);

    try {
      service.deleteProperty("test");
    } catch (final Exception e) {
      System.err.println(e);
      throw e;
    }
  }
}
