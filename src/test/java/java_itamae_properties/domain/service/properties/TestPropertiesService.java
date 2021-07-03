package java_itamae_properties.domain.service.properties;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({NotExistFile.class, EmptyFile.class, NotEmptyFile.class})
public class TestPropertiesService {
}
