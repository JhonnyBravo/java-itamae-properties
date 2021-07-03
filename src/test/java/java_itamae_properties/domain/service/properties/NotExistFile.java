package java_itamae_properties.domain.service.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.File;
import java.io.FileNotFoundException;
import java_itamae_contents.domain.model.ContentsAttribute;
import javax.inject.Inject;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * ファイルが存在しない場合のテスト。
 */
@RunWith(CdiRunner.class)
@AdditionalClasses(PropertiesServiceImpl.class)
public class NotExistFile {
  @Inject
  private PropertiesService service;
  private File file;

  @Before
  public void setUp() throws Exception {
    final ContentsAttribute attr = new ContentsAttribute();
    attr.setPath("NotExist.txt");
    service.init(attr);

    file = new File(attr.getPath());
  }

  /**
   * {@link PropertiesService#getProperty(String)} 実行時に {@link FileNotFoundException} が送出されること。
   *
   * @throws Exception {@link Exception}
   */
  @Test(expected = FileNotFoundException.class)
  public void test1() throws Exception {
    try {
      service.getProperty("test");
    } catch (final Exception e) {
      System.err.println(e);
      throw e;
    }
  }

  /**
   * {@link PropertiesService#createProperty(String, String))} 実行時に {@link FileNotFoundException}
   * が送出されること。
   *
   * @throws Exception {@link Exception}
   */
  @Test(expected = FileNotFoundException.class)
  public void test2() throws Exception {
    try {
      service.createProperty("test", "登録テスト");
    } catch (final Exception e) {
      assertThat(file.isFile(), is(false));
      System.err.println(e);
      throw e;
    }
  }

  /**
   * {@link PropertiesService#updateProperty(String, String)} 実行時に {@link FileNotFoundException}
   * が送出されること。
   *
   * @throws Exception {@link Exception}
   */
  @Test(expected = FileNotFoundException.class)
  public void test3() throws Exception {
    try {
      service.updateProperty("test", "更新テスト");
    } catch (final Exception e) {
      assertThat(file.isFile(), is(false));
      System.err.println(e);
      throw e;
    }
  }

  /**
   * {@link PropertiesService#deleteProperty(String)} 実行時に {@link FileNotFoundException} が送出されること。
   *
   * @throws Exception {@link Exception}
   */
  @Test(expected = FileNotFoundException.class)
  public void test4() throws Exception {
    try {
      service.deleteProperty("test");
    } catch (final Exception e) {
      assertThat(file.isFile(), is(false));
      System.err.println(e);
      throw e;
    }
  }
}
