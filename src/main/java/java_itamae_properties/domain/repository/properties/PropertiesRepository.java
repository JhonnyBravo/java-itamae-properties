package java_itamae_properties.domain.repository.properties;

import java.io.Reader;
import java.io.Writer;
import java.util.Map;

/**
 * プロパティファイルの読み書きを操作する。
 */
public interface PropertiesRepository {
  /**
   * プロパティファイルを読込んで Map に変換して返す。
   *
   * @param reader 操作対象とするプロパティファイルの Reader を指定する。
   * @return properties 変換された Map を返す。
   * @throws Exception {@link java.lang.Exception}
   */
  Map<String, String> getProperties(Reader reader) throws Exception;

  /**
   * プロパティファイルへの書込みを実行する。
   *
   * @param writer 操作対象とするプロパティファイルの Writer を指定する。
   * @param map 書込み対象とする Map を指定する。
   * @param comment プロパティファイルへ書込むコメントを指定する。
   * @return status
   *         <ul>
   *         <li>true: 書込みに成功したことを表す。</li>
   *         <li>delete: 書込みを実行しなかったことを表す。</li>
   *         </ul>
   * @throws Exception {@link java.lang.Exception}
   */
  boolean updateProperties(Writer writer, Map<String, String> map, String comment) throws Exception;

  /**
   * プロパティファイルから全てのプロパティを削除する。
   *
   * @param writer 操作対象とするプロパティファイルの Writer を指定する。
   * @param comment プロパティファイルへ書込むコメントを指定する。
   * @return status
   *         <ul>
   *         <li>true: 削除に成功したことを表す。</li>
   *         <li>false: 削除を実行しなかったことを表す。</li>
   *         </ul>
   * @throws Exception {@link java.lang.Exception}
   */
  boolean deleteProperties(Writer writer, String comment) throws Exception;
}
