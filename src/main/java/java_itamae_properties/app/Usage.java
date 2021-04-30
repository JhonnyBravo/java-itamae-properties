package java_itamae_properties.app;

import java.util.ArrayList;
import java.util.List;

/**
 * CLI コマンドの使用法を表示する。
 */
public class Usage implements Runnable {
  /**
   * CLI コマンドの使用法を表示する。
   */
  @Override
  public void run() {
    final List<String> optionList = new ArrayList<>();

    optionList.add("--path, -p <path>: 指定したプロパティーファイルに対して操作を実行します。");
    optionList.add("--encoding, -e <encoding>: 文字エンコーディングを指定します。");
    optionList.add("--key, -k <key_name>: 操作対象とするキーの名前を指定します。");
    optionList.add("--value, -v <key_value>: 操作対象とするキーの値を指定します。");
    optionList.add("--get-property, -g: ファイルからプロパティの値を取得して標準出力へ出力します。");
    optionList.add("--create-property, -c: ファイルへプロパティを新規登録します。");
    optionList.add("--update-property, -u: プロパティの値を更新します。");
    optionList.add("--delete-property, -d: ファイルからプロパティを削除します。");

    optionList.forEach(option -> {
      System.out.println(option);
    });
  }
}
