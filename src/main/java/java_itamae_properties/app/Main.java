package java_itamae_properties.app;

import java.io.File;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import java_itamae_contents.domain.model.ContentsAttribute;
import java_itamae_properties.domain.service.properties.PropertiesService;
import java_itamae_properties.domain.service.properties.PropertiesServiceImpl;

/**
 * プロパティファイルの読み書きを実行する。
 */
public class Main {
    /**
     * @param args
     *            <ul>
     *            <li>--path, -p &lt;path&gt;: 操作対象とするファイルのパスを指定する。</li>
     *            <li>--encoding, -e &lt;encoding&gt;: 文字エンコーディングを指定する。</li>
     *            <li>--key, -k &lt;key&gt;: 操作対象とするプロパティのキー名を指定する。</li>
     *            <li>--value, -v &lt;value&gt;: プロパティへ書込む値を指定する。</li>
     *            <li>--get-property, -g: ファイルからプロパティの値を取得して標準出力へ出力する。</li>
     *            <li>--create-property, -c: プロパティを新規登録する。</li>
     *            <li>--update-property, -u: プロパティの値を更新する。</li>
     *            <li>--delete-property, -d: プロパティを削除する。</li>
     *            </ul>
     */
    public static void main(String[] args) {
        final LongOpt[] longopts = new LongOpt[8];
        longopts[0] = new LongOpt("path", LongOpt.REQUIRED_ARGUMENT, null, 'p');
        longopts[1] = new LongOpt("encoding", LongOpt.REQUIRED_ARGUMENT, null,
                'e');
        longopts[2] = new LongOpt("key", LongOpt.REQUIRED_ARGUMENT, null, 'k');
        longopts[3] = new LongOpt("value", LongOpt.REQUIRED_ARGUMENT, null,
                'v');
        longopts[4] = new LongOpt("get-property", LongOpt.NO_ARGUMENT, null,
                'g');
        longopts[5] = new LongOpt("create-property", LongOpt.NO_ARGUMENT, null,
                'c');
        longopts[6] = new LongOpt("update-property", LongOpt.NO_ARGUMENT, null,
                'u');
        longopts[7] = new LongOpt("delete-property", LongOpt.NO_ARGUMENT, null,
                'd');

        final Getopt options = new Getopt("Main", args, "p:e:k:v:gcud",
                longopts);
        final Logger logger = LoggerFactory.getLogger(Main.class);

        final ContentsAttribute attr = new ContentsAttribute();

        int c;
        int getFlag = 0;
        int createFlag = 0;
        int updateFlag = 0;
        int deleteFlag = 0;

        String key = null;
        String value = null;

        while ((c = options.getopt()) != -1) {
            switch (c) {
                case 'p' :
                    attr.setPath(options.getOptarg());
                    break;
                case 'e' :
                    attr.setEncoding(options.getOptarg());
                    break;
                case 'k' :
                    key = options.getOptarg();
                    break;
                case 'v' :
                    value = options.getOptarg();
                    break;
                case 'g' :
                    getFlag = 1;
                    break;
                case 'c' :
                    createFlag = 1;
                    break;
                case 'u' :
                    updateFlag = 1;
                    break;
                case 'd' :
                    deleteFlag = 1;
                    break;
            }
        }

        int status = 0;

        if (getFlag == 1 || createFlag == 1 || updateFlag == 1
                || deleteFlag == 1) {
            if (attr.getPath() == null) {
                logger.warn("path オプションを指定してください。");
                status = 1;
            }

            if (key == null) {
                logger.warn("key オプションを指定してください。");
                status = 1;
            }
        }

        if (createFlag == 1 || updateFlag == 1) {
            if (value == null) {
                logger.warn("value オプションを指定してください。");
                status = 1;
            }
        }

        final PropertiesService ps = new PropertiesServiceImpl(attr);

        Function<String, Integer> getProperty = keyName -> {
            try {
                String keyValue = ps.getProperty(keyName);

                if (keyValue != null && !keyValue.isEmpty()) {
                    System.out.println(keyValue);
                    return 2;
                } else {
                    return 0;
                }
            } catch (Exception e) {
                logger.warn(e.toString());
                return 1;
            }
        };

        BiFunction<String, String, Integer> createProperty = (keyName,
                keyValue) -> {
            try {
                boolean result = ps.createProperty(keyName, keyValue);

                if (result) {
                    return 2;
                } else {
                    return 0;
                }
            } catch (Exception e) {
                logger.warn(e.toString());
                return 1;
            }
        };

        BiFunction<String, String, Integer> updateProperty = (keyName,
                keyValue) -> {
            try {
                boolean result = ps.updateProperty(keyName, keyValue);

                if (result) {
                    return 2;
                } else {
                    return 0;
                }
            } catch (Exception e) {
                logger.warn(e.toString());
                return 1;
            }
        };

        Function<String, Integer> deleteProperty = keyName -> {
            try {
                boolean result = ps.deleteProperty(keyName);

                if (result) {
                    return 2;
                } else {
                    return 0;
                }
            } catch (Exception e) {
                logger.warn(e.toString());
                return 1;
            }
        };

        try {
            if (getFlag == 1) {
                if (status != 1) {
                    status = getProperty.apply(key);
                }
            } else if (createFlag == 1) {
                if (status != 1) {
                    final File file = new File(attr.getPath());

                    if (!file.isFile()) {
                        file.createNewFile();
                    }

                    status = createProperty.apply(key, value);
                }
            } else if (updateFlag == 1) {
                if (status != 1) {
                    status = updateProperty.apply(key, value);
                }
            } else if (deleteFlag == 1) {
                if (status != 1) {
                    status = deleteProperty.apply(key);
                }
            }
        } catch (final Exception e) {
            logger.warn(e.toString());
            status = 1;
        }

        System.exit(status);
    }
}
