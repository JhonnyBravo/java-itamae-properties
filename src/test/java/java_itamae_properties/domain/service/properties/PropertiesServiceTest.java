package java_itamae_properties.domain.service.properties;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java_itamae_contents.domain.model.ContentsAttribute;

@RunWith(Enclosed.class)
public class PropertiesServiceTest {
    public static class ファイルが存在しない場合 {
        private PropertiesService service;
        private File file;

        @Before
        public void setUp() throws Exception {
            final ContentsAttribute attr = new ContentsAttribute();
            attr.setPath("NotExist.txt");

            service = new PropertiesServiceImpl(attr);
            file = new File(attr.getPath());
        }

        @Test(expected = FileNotFoundException.class)
        public void getProperty実行時にFileNotFoundExceptionが送出されること() throws Exception {
            try {
                service.getProperty("test");
            } catch (final Exception e) {
                System.err.println(e);
                throw e;
            }
        }

        @Test(expected = FileNotFoundException.class)
        public void createProperty実行時にFileNotFoundExceptionが送出されること() throws Exception {
            try {
                service.createProperty("test", "登録テスト");
            } catch (final Exception e) {
                assertThat(file.isFile(), is(false));
                System.err.println(e);
                throw e;
            }
        }

        @Test(expected = FileNotFoundException.class)
        public void updateProperty実行時にFileNotFoundExceptionが送出されること() throws Exception {
            try {
                service.updateProperty("test", "更新テスト");
            } catch (final Exception e) {
                assertThat(file.isFile(), is(false));
                System.err.println(e);
                throw e;
            }
        }

        @Test(expected = FileNotFoundException.class)
        public void deleteProperty実行時にFileNotFoundExceptionが送出されること() throws Exception {
            try {
                service.deleteProperty("test");
            } catch (final Exception e) {
                assertThat(file.isFile(), is(false));
                System.err.println(e);
                throw e;
            }
        }
    }

    public static class ファイルが存在して空である場合 {
        private File file;

        private boolean isWindows() {
            boolean result = false;
            final String osName = System.getProperty("os.name");

            if (osName.substring(0, 3).equals("Win")) {
                result = true;
            }

            return result;
        }

        @Before
        public void setUp() throws Exception {
            file = new File("test.properties");
            file.createNewFile();
        }

        @After
        public void tearDown() throws Exception {
            file.delete();
        }

        @Test(expected = Exception.class)
        public void getProperty実行時にExceptionが送出されること() throws Exception {
            final ContentsAttribute attr = new ContentsAttribute();
            attr.setPath("test.properties");

            final PropertiesService service = new PropertiesServiceImpl(attr);

            try {
                service.getProperty("test");
            } catch (final Exception e) {
                System.err.println(e);
                throw e;
            }
        }

        @Test
        public void createProperty実行時にプロパティファイルへの書込みができて終了ステータスがtrueであること() throws Exception {
            final ContentsAttribute attr = new ContentsAttribute();
            attr.setPath("test.properties");

            final PropertiesService service = new PropertiesServiceImpl(attr);
            final boolean status = service.createProperty("test", "登録テスト");
            assertThat(status, is(true));
            assertThat(service.getProperty("test"), is("登録テスト"));
        }

        @Test
        public void 文字エンコーディングを指定してプロパティファイルの読み書きができること() throws Exception {
            final ContentsAttribute attr = new ContentsAttribute();
            attr.setPath("test.properties");

            if (isWindows()) {
                attr.setEncoding("UTF8");
            } else {
                attr.setEncoding("MS932");
            }

            final PropertiesService service = new PropertiesServiceImpl(attr);
            final boolean status = service.createProperty("test", "登録テスト");
            assertThat(status, is(true));
            assertThat(service.getProperty("test"), is("登録テスト"));
        }

        @Test(expected = Exception.class)
        public void updateProperty実行時にExceptionが送出されること() throws Exception {
            final ContentsAttribute attr = new ContentsAttribute();
            attr.setPath("test.txt");

            final PropertiesService service = new PropertiesServiceImpl(attr);

            try {
                service.updateProperty("test", "更新テスト");
            } catch (final Exception e) {
                System.err.println(e);
                throw e;
            }
        }

        @Test(expected = Exception.class)
        public void deleteProperty実行時にExceptionが送出されること() throws Exception {
            final ContentsAttribute attr = new ContentsAttribute();
            attr.setPath("test.txt");

            final PropertiesService service = new PropertiesServiceImpl(attr);

            try {
                service.deleteProperty("test");
            } catch (final Exception e) {
                System.err.println(e);
                throw e;
            }
        }
    }

    public static class ファイルが存在して空ではない場合 {
        private File file;

        @Before
        public void setUp() throws Exception {
            file = new File("test.txt");
            file.createNewFile();

            final ContentsAttribute attr = new ContentsAttribute();
            attr.setPath("test.txt");

            final PropertiesService service = new PropertiesServiceImpl(attr);
            service.createProperty("property1", "1 つ目のプロパティ");
            service.createProperty("property2", "2 つ目のプロパティ");
        }

        @After
        public void tearDown() throws Exception {
            file.delete();
        }

        @Test
        public void getProperty実行時にプロパティファイルから値を取得できること() throws Exception {
            final ContentsAttribute attr = new ContentsAttribute();
            attr.setPath("test.txt");

            final PropertiesService service = new PropertiesServiceImpl(attr);
            assertThat(service.getProperty("property1"), is("1 つ目のプロパティ"));
            assertThat(service.getProperty("property2"), is("2 つ目のプロパティ"));
        }

        @Test(expected = Exception.class)
        public void 既に存在するプロパティ名を指定してcreatePropertyを実行した場合にExceptionが送出されること() throws Exception {
            final ContentsAttribute attr = new ContentsAttribute();
            attr.setPath("test.txt");

            final PropertiesService service = new PropertiesServiceImpl(attr);

            try {
                service.createProperty("property1", "登録テスト");
            } catch (final Exception e) {
                assertThat(service.getProperty("property1"), is("1 つ目のプロパティ"));
                System.err.println(e);
                throw e;
            }
        }

        @Test
        public void updateProperty実行時にプロパティの値を上書きできて終了ステータスがtrueであること() throws Exception {
            final ContentsAttribute attr = new ContentsAttribute();
            attr.setPath("test.txt");

            final PropertiesService service = new PropertiesServiceImpl(attr);
            final boolean status = service.updateProperty("property2", "更新テスト");
            assertThat(status, is(true));
            assertThat(service.getProperty("property2"), is("更新テスト"));
            assertThat(service.getProperty("property1"), is("1 つ目のプロパティ"));
        }

        @Test
        public void updateProperty実行時にプロパティの値が変更前と同一である場合に終了ステータスがfalseであること() throws Exception {
            final ContentsAttribute attr = new ContentsAttribute();
            attr.setPath("test.txt");

            final PropertiesService service = new PropertiesServiceImpl(attr);
            final boolean status = service.updateProperty("property1", "1 つ目のプロパティ");
            assertThat(status, is(false));
            assertThat(service.getProperty("property1"), is("1 つ目のプロパティ"));
            assertThat(service.getProperty("property2"), is("2 つ目のプロパティ"));
        }

        @Test
        public void deleteProperty実行時にプロパティを削除できて終了ステータスがtrueであること() throws Exception {
            final ContentsAttribute attr = new ContentsAttribute();
            attr.setPath("test.txt");

            final PropertiesService service = new PropertiesServiceImpl(attr);
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
}
