package property_resource;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import content_resource.ContentResource;
import content_resource.IOStreamResource;

/**
 * プロパティファイルの読み書きを管理する。
 */
public class PropertyResource extends IOStreamResource implements ContentResource<Map<String, String>> {
    private final File file;

    /**
     * @param path 操作対象とするファイルのパスを指定する。
     */
    public PropertyResource(String path) {
        super(path);
        file = new File(path);
    }

    /**
     * プロパティファイルを上書きする。
     *
     * @param model 書込み対象とする Map オブジェクトを指定する。
     */
    @Override
    public boolean setContent(Map<String, String> model) throws Exception {
        boolean status = false;
        final Properties prop = new Properties();

        for (final Map.Entry<String, String> entry : model.entrySet()) {
            prop.setProperty(entry.getKey(), entry.getValue());
        }

        try (Writer writer = getWriter()) {
            prop.store(writer, file.getName());
        }

        status = true;
        return status;
    }

    /**
     * @return content プロパティの一覧を Map に変換して返す。
     */
    @Override
    public Map<String, String> getContent() throws Exception {
        final Map<String, String> map = new HashMap<String, String>();
        final Properties prop = new Properties();

        try (Reader reader = getReader()) {
            prop.load(reader);

            for (final Map.Entry<Object, Object> entry : prop.entrySet()) {
                map.put(entry.getKey().toString(), entry.getValue().toString());
            }
        }

        return map;
    }

    /**
     * 全プロパティを削除する。
     */
    @Override
    public boolean deleteContent() throws Exception {
        boolean status = false;
        final Properties prop = new Properties();

        try (Writer writer = getWriter()) {
            prop.store(writer, file.getName());
        }

        status = true;
        return status;
    }
}
