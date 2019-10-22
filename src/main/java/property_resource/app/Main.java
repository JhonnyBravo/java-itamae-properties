package property_resource.app;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import content_resource.ContentResource;
import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import property_resource.domain.repository.PropertyResource;

/**
 * プロパティファイルの読み書きを管理する。
 */
public class Main {
    /**
     * @param args
     *             <ul>
     *             <li>-p, --path &lt;path&gt; 操作対象とするファイルのパスを指定する。</li>
     *             <li>-k, --key &lt;key&gt; 操作対象とするプロパティの名前を指定する。</li>
     *             <li>-v, --value &lt;value&gt; プロパティへ設定する値を指定する。</li>
     *             <li>-s, --set key に指定したプロパティの値を上書きする。</li>
     *             <li>-g, --get key に指定したプロパティの値を取得する。</li>
     *             <li>-d, --delete key に指定したプロパティを削除する。</li>
     *             </ul>
     */
    public static void main(String[] args) {
        final LongOpt[] longopts = new LongOpt[6];
        longopts[0] = new LongOpt("path", LongOpt.REQUIRED_ARGUMENT, null, 'p');
        longopts[1] = new LongOpt("key", LongOpt.REQUIRED_ARGUMENT, null, 'k');
        longopts[2] = new LongOpt("value", LongOpt.REQUIRED_ARGUMENT, null, 'v');
        longopts[3] = new LongOpt("set", LongOpt.NO_ARGUMENT, null, 's');
        longopts[4] = new LongOpt("get", LongOpt.NO_ARGUMENT, null, 'g');
        longopts[5] = new LongOpt("delete", LongOpt.NO_ARGUMENT, null, 'd');

        final Getopt options = new Getopt("Main", args, "p:k:v:sgd", longopts);
        final Logger logger = LoggerFactory.getLogger(Main.class);

        String path = null;
        String key = null;
        String value = null;

        int c;
        int setFlag = 0;
        int deleteFlag = 0;
        int getFlag = 0;

        while ((c = options.getopt()) != -1) {
            switch (c) {
            case 'p':
                path = options.getOptarg();
                break;
            case 'k':
                key = options.getOptarg();
                break;
            case 'v':
                value = options.getOptarg();
                break;
            case 's':
                setFlag = 1;
                break;
            case 'd':
                deleteFlag = 1;
                break;
            case 'g':
                getFlag = 1;
                break;
            }
        }

        if (setFlag == 1 || deleteFlag == 1 || getFlag == 1) {
            if (path == null) {
                logger.warn("path を指定してください。");
                System.exit(1);
            }

            if (key == null) {
                logger.warn("key を指定してください。");
                System.exit(1);
            }

            if (setFlag == 1 && value == null) {
                logger.warn("value を指定してください。");
                System.exit(1);
            }
        }

        final ContentResource<Map<String, String>> resource = new PropertyResource(path);
        boolean status = false;

        try {
            if (setFlag == 1) {
                final File file = new File(path);

                if (!file.isFile()) {
                    file.createNewFile();
                }

                final Map<String, String> contents = resource.getContent();
                contents.put(key, value);
                status = resource.setContent(contents);
            } else if (getFlag == 1) {
                final Map<String, String> contents = resource.getContent();

                if (contents.containsKey(key)) {
                    System.out.println(key + ": " + contents.get(key));
                    status = true;
                } else {
                    logger.info(key + " は見つかりませんでした。");
                }
            } else if (deleteFlag == 1) {
                final Map<String, String> contents = resource.getContent();

                if (contents.containsKey(key)) {
                    contents.remove(key);
                    status = resource.setContent(contents);
                } else {
                    logger.info(key + " は見つかりませんでした。");
                }
            }

            if (status) {
                System.exit(2);
            } else {
                System.exit(0);
            }
        } catch (final Exception e) {
            logger.warn("エラーが発生しました。", e);
            System.exit(1);
        }
    }
}
