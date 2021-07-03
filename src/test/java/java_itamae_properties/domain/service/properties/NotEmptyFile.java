package java_itamae_properties.domain.service.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.File;
import java_itamae_contents.domain.model.ContentsAttribute;
import javax.inject.Inject;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * ファイルが存在して空ではない場合のテスト。
 */
@RunWith(CdiRunner.class)
@AdditionalClasses(PropertiesServiceImpl.class)
public class NotEmptyFile {
  @Inject
  private PropertiesService service;
  private File file;

  @Before
  public void setUp() throws Exception {
    file = new File("test.properties");
    file.createNewFile();

    final ContentsAttribute attr = new ContentsAttribute();
    attr.setPath("test.properties");
    service.init(attr);

    service.createProperty("property1", "1 つ目のプロパティ");
    service.createProperty("property2", "2 つ目のプロパティ");
  }

  @After
  public void tearDown() throws Exception {
    file.delete();
  }

  /**
   * {@link PropertiesService#getProperty(String)} 実行時にプロパティファイルから値を取得できること。
   *
   * @throws Exception {@link Exception}
   */
  @Test
  public void test1() throws Exception {
    final ContentsAttribute attr = new ContentsAttribute();
    attr.setPath("test.properties");
    service.init(attr);

    assertThat(service.getProperty("property1"), is("1 つ目のプロパティ"));
    assertThat(service.getProperty("property2"), is("2 つ目のプロパティ"));
  }

  /**
   * 既に存在するプロパティ名を指定して {@link PropertiesService#createProperty(String, String))} を実行した場合に
   * {@link Exception} が送出されること。
   *
   * @throws Exception {@link Exception}
   */
  @Test(expected = Exception.class)
  public void test2() throws Exception {
    final ContentsAttribute attr = new ContentsAttribute();
    attr.setPath("test.properties");
    service.init(attr);

    try {
      service.createProperty("property1", "登録テスト");
    } catch (final Exception e) {
      assertThat(service.getProperty("property1"), is("1 つ目のプロパティ"));
      System.err.println(e);
      throw e;
    }
  }

  /**
   * {@link PropertiesService#updateProperty(String, String)} 実行時にプロパティの値を上書きできて終了ステータスが true であること。
   *
   * @throws Exception {@link Exception}
   */
  @Test
  public void test3() throws Exception {
    final ContentsAttribute attr = new ContentsAttribute();
    attr.setPath("test.properties");
    service.init(attr);

    final boolean status = service.updateProperty("property2", "更新テスト");
    assertThat(status, is(true));
    assertThat(service.getProperty("property1"), is("1 つ目のプロパティ"));
    assertThat(service.getProperty("property2"), is("更新テスト"));
  }

  /**
   * {@link PropertiesService#updateProperty(String, String)} 実行時にプロパティの値が変更前と同一である場合に終了ステータスが false
   * であること。
   *
   * @throws Exception {@link Exception}
   */
  @Test
  public void test4() throws Exception {
    final ContentsAttribute attr = new ContentsAttribute();
    attr.setPath("test.properties");
    service.init(attr);

    final boolean status = service.updateProperty("property1", "1 つ目のプロパティ");
    assertThat(status, is(false));
    assertThat(service.getProperty("property1"), is("1 つ目のプロパティ"));
    assertThat(service.getProperty("property2"), is("2 つ目のプロパティ"));
  }

  /**
   * {@link PropertiesService#deleteProperty(String)} 実行時にプロパティを削除できて終了ステータスが true であること。
   *
   * @throws Exception {@link Exception}
   */
  @Test
  public void test5() throws Exception {
    final ContentsAttribute attr = new ContentsAttribute();
    attr.setPath("test.properties");
    service.init(attr);

    final boolean status = service.deleteProperty("property1");
    assertThat(status, is(true));
    assertThat(service.getProperty("property2"), is("2 つ目のプロパティ"));

    try {
      service.getProperty("property1");
    } catch (final Exception e) {
      System.err.println(e);
    }
  }
}
