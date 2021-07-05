package java_itamae_properties.app;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 * {@link Application} を起動する。
 */
public class Main {
  /**
   * {@link Application} を起動する。
   *
   * @param args CLI から {@link Application} へ渡すコマンドライン引数を指定する。
   */
  public static void main(String[] args) {
    final Weld weld = new Weld();
    int status = 0;

    try (WeldContainer container = weld.initialize()) {
      final Application app = container.select(Application.class).get();
      status = app.main(args);
    }

    System.exit(status);
  }
}
