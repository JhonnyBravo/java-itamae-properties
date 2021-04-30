package java_itamae_properties.domain.repository.properties;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java_itamae_contents.domain.model.ContentsAttribute;
import java_itamae_contents.domain.repository.stream.StreamRepository;
import java_itamae_contents.domain.repository.stream.StreamRepositoryImpl;

@RunWith(Enclosed.class)
public class PropertiesRepositoryTest {
    public static class プロパティファイルが空である場合 {
        private PropertiesRepository pr;
        private StreamRepository sr;
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
            pr = new PropertiesRepositoryImpl();
            sr = new StreamRepositoryImpl();
            file = new File("test.properties");
            file.createNewFile();
        }

        @After
        public void tearDown() throws Exception {
            file.delete();
        }

        @Test
        public void getProperties実行時に空のMapが返されること() throws Exception {
            final ContentsAttribute attr = new ContentsAttribute();
            attr.setPath("test.properties");

            try (final Reader reader = sr.getReader(attr)) {
                final Map<String, String> properties = pr.getProperties(reader);
                assertThat(properties.size(), is(0));
            }
        }

        @Test
        public void setProperties実行時にファイルへプロパティの書込みができること() throws Exception {
            final Map<String, String> newProps = new HashMap<>();
            newProps.put("property1", "1 つ目のプロパティ");
            newProps.put("property2", "2 つ目のプロパティ");

            final ContentsAttribute attr = new ContentsAttribute();
            attr.setPath("test.properties");

            try (final Writer writer = sr.getWriter(attr)) {
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

        @Test
        public void 文字エンコーディングを指定してプロパティの読み書きができること() throws Exception {
            final Map<String, String> newProps = new HashMap<>();
            newProps.put("property1", "1 つ目のプロパティ");
            newProps.put("property2", "2 つ目のプロパティ");

            final ContentsAttribute attr = new ContentsAttribute();
            attr.setPath("test.properties");

            if (isWindows()) {
                attr.setEncoding("UTF8");
            } else {
                attr.setEncoding("MS932");
            }

            try (final Writer writer = sr.getWriter(attr)) {
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

    public static class プロパティファイルが空ではない場合 {
        private PropertiesRepository pr;
        private StreamRepository sr;
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
            pr = new PropertiesRepositoryImpl();
            sr = new StreamRepositoryImpl();
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

        @Test
        public void getProperties実行時にプロパティファイルをMap変換できること() throws Exception {
            final ContentsAttribute attr = new ContentsAttribute();
            attr.setPath("test.properties");

            try (final Reader reader = sr.getReader(attr)) {
                final Map<String, String> properties = pr.getProperties(reader);
                assertThat(properties.size(), is(2));
                assertThat(properties.get("property1"), is("1 つ目のプロパティ"));
                assertThat(properties.get("property2"), is("2 つ目のプロパティ"));
            }
        }

        @Test
        public void setProperties実行時にプロパティファイルの上書きができること() throws Exception {
            final Map<String, String> newProps = new HashMap<>();
            newProps.put("update", "更新テスト");

            final ContentsAttribute attr = new ContentsAttribute();
            attr.setPath("test.properties");

            try (final Writer writer = sr.getWriter(attr)) {
                final boolean status = pr.updateProperties(writer, newProps, attr.getPath());
                assertThat(status, is(true));
            }

            try (Reader reader = sr.getReader(attr)) {
                final Map<String, String> curProps = pr.getProperties(reader);
                assertThat(curProps.size(), is(1));
                assertThat(curProps.get("update"), is("更新テスト"));
            }
        }

        @Test
        public void 文字エンコーディングを指定してプロパティの読み書きができること() throws Exception {
            final Map<String, String> newProps = new HashMap<>();
            newProps.put("encoding", "文字コードテスト");

            final ContentsAttribute attr = new ContentsAttribute();
            attr.setPath("test.properties");

            if (isWindows()) {
                attr.setEncoding("UTF8");
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

        @Test
        public void deleteProperties実行時にプロパティファイルを空にできること() throws Exception {
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
}
