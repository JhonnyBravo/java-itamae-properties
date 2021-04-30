package java_itamae_properties.app.properties;

import java.util.function.BiFunction;
import java_itamae_contents.domain.model.ContentsAttribute;
import java_itamae_properties.domain.service.properties.PropertiesService;
import java_itamae_properties.domain.service.properties.PropertiesServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * プロパティの値を更新する。
 */
public class UpdateProperty implements BiFunction<String, String, Integer> {
  private final ContentsAttribute attr;

  /**
   * 初期化処理を実行する。
   *
   * @param attr 操作対象とするプロパティーファイルの情報を収めた {@link ContentsAttribute} を指定する。
   */
  public UpdateProperty(ContentsAttribute attr) {
    this.attr = attr;
  }

  /**
   * プロパティの値を更新する。
   *
   * @param keyName 操作対象とするプロパティのキー名を指定する。
   * @param keyValue キーの値を指定する。
   * @return result
   *         <ul>
   *         <li>0: 操作を実行しなかったことを表す。</li>
   *         <li>1: エラーが発生したことを表す。</li>
   *         <li>2: 操作を実行したことを表す。</li>
   *         </ul>
   */
  @Override
  public Integer apply(String keyName, String keyValue) {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    final PropertiesService service = new PropertiesServiceImpl(this.attr);

    try {
      final boolean result = service.updateProperty(keyName, keyValue);

      if (result) {
        return 2;
      } else {
        return 0;
      }
    } catch (final Exception e) {
      logger.warn(e.toString());
      return 1;
    }
  }
}
