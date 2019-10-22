package property_resource.domain.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import content_resource.ContentResource;
import property_resource.domain.repository.PropertyResource;

@RunWith(Enclosed.class)
public class PropertyResourceTest {
    public static class ファイルが存在しない場合 {
        private final ContentResource<Map<String, String>> resource = new PropertyResource("not_exist.properties");

        @Test(expected = FileNotFoundException.class)
        public void getContent実行時にFileNotFoundExceptionが送出されること() throws Exception {
            try {
                resource.getContent();
            } catch (final Exception e) {
                System.err.println(e);
                throw e;
            }
        }

        @Test(expected = FileNotFoundException.class)
        public void setContent実行時にFileNotFoundExceptionが送出されること() throws Exception {
            final Map<String, String> map = new HashMap<String, String>();
            map.put("error_test", "test");

            try {
                resource.setContent(map);
            } catch (final Exception e) {
                System.err.println(e);
                throw e;
            }
        }
    }

    public static class ファイルが存在するが空である場合 {
        private final PropertyResource resource = new PropertyResource("test.properties");
        private final File file = new File("test.properties");

        @Before
        public void setUp() throws Exception {
            file.createNewFile();
        }

        @After
        public void tearDown() throws Exception {
            file.delete();
        }

        @Test
        public void getContent実行時に空のマップが返されること() throws Exception {
            final Map<String, String> map = resource.getContent();
            assertThat(map.size(), is(0));
        }

        @Test
        public void setContent実行時にファイルへプロパティを追記できること() throws Exception {
            final Map<String, String> map = new HashMap<String, String>();
            map.put("property1", "1 つ目のプロパティ");
            map.put("property2", "2 つ目のプロパティ");

            final boolean status = resource.setContent(map);
            assertThat(status, is(true));

            final Map<String, String> curMap = resource.getContent();
            assertThat(curMap.size(), is(2));
            assertThat(curMap.get("property1"), is("1 つ目のプロパティ"));
            assertThat(curMap.get("property2"), is("2 つ目のプロパティ"));
        }

        @Test
        public void 文字エンコーディングを指定してファイルの読み書きができること() throws Exception {
            String encoding = null;
            final String osName = System.getProperty("os.name");

            if (osName.substring(0, 3).equals("Win")) {
                encoding = "UTF8";
            } else {
                encoding = "MS932";
            }

            resource.setEncoding(encoding);

            final Map<String, String> map = new HashMap<String, String>();
            map.put("property1", "1 つ目のプロパティ");
            map.put("property2", "2 つ目のプロパティ");

            final boolean status = resource.setContent(map);
            assertThat(status, is(true));

            final Map<String, String> curMap = resource.getContent();
            assertThat(curMap.size(), is(2));
            assertThat(curMap.get("property1"), is("1 つ目のプロパティ"));
            assertThat(curMap.get("property2"), is("2 つ目のプロパティ"));
        }
    }

    public static class ファイルが存在して空ではない場合 {
        private final PropertyResource resource = new PropertyResource("test.properties");
        private final File file = new File("test.properties");

        @Before
        public void setUp() throws Exception {
            file.createNewFile();

            final Map<String, String> map = new HashMap<String, String>();
            map.put("property1", "1 つ目のプロパティ");
            map.put("property2", "2 つ目のプロパティ");

            resource.setContent(map);
        }

        @After
        public void tearDown() throws Exception {
            file.delete();
        }

        @Test
        public void getContent実行時に全プロパティを取得できること() throws Exception {
            Map<String, String> map = new HashMap<String, String>();
            map = resource.getContent();

            assertThat(map.size(), is(2));
            assertThat(map.get("property1"), is("1 つ目のプロパティ"));
            assertThat(map.get("property2"), is("2 つ目のプロパティ"));
        }

        @Test
        public void setContent実行時にプロパティを上書きできること() throws Exception {
            final Map<String, String> map = resource.getContent();
            map.put("property1", "上書きテスト");
            map.put("property3", "追加テスト");

            final boolean status = resource.setContent(map);
            assertThat(status, is(true));

            final Map<String, String> curMap = resource.getContent();
            assertThat(curMap.size(), is(3));
            assertThat(curMap.get("property1"), is("上書きテスト"));
            assertThat(curMap.get("property2"), is("2 つ目のプロパティ"));
            assertThat(curMap.get("property3"), is("追加テスト"));
        }

        @Test
        public void 文字エンコーディングを指定してファイルの読み書きができること() throws Exception {
            String encoding = null;
            final String osName = System.getProperty("os.name");

            if (osName.substring(0, 3).equals("Win")) {
                encoding = "UTF8";
            } else {
                encoding = "MS932";
            }

            final Map<String, String> map = resource.getContent();
            map.remove("property2");

            resource.setEncoding(encoding);
            final boolean status = resource.setContent(map);
            assertThat(status, is(true));

            final Map<String, String> curMap = resource.getContent();
            assertThat(curMap.size(), is(1));
            assertThat(curMap.get("property1"), is("1 つ目のプロパティ"));
        }

        @Test
        public void deleteContent実行時に全プロパティを削除できること() throws Exception {
            final boolean status = resource.deleteContent();
            assertThat(status, is(true));

            final Map<String, String> map = resource.getContent();
            assertThat(map.size(), is(0));
        }
    }

    public static class 不正な文字エンコーディングを指定した場合 {
        private final File file = new File("encoding_test.properties");
        PropertyResource resource = new PropertyResource("encoding_test.properties");

        @Before
        public void setUp() throws Exception {
            file.createNewFile();
            resource.setEncoding("NotExist");
        }

        @After
        public void tearDown() throws Exception {
            file.delete();
        }

        @Test(expected = UnsupportedEncodingException.class)
        public void getContent実行時にUnsupportedEncodingExceptionが送出されること() throws Exception {
            try {
                resource.getContent();
            } catch (final Exception e) {
                System.err.println(e);
                throw e;
            }
        }

        @Test(expected = UnsupportedEncodingException.class)
        public void setContent実行時にUnsupportedEncodingExceptionが送出されること() throws Exception {
            final Map<String, String> map = new HashMap<String, String>();
            map.put("test", "文字エンコーディングのテスト");

            try {
                resource.setContent(map);
            } catch (final Exception e) {
                System.err.println(e);
                throw e;
            }
        }
    }
}
