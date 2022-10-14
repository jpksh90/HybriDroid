package kr.ac.kaist.wala.hybridroid.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class TestConfig {

  public static Properties testProperties;
  public static String TEST_DIR = "test_dir";
  public static String LIB_JAR = "android_jar";

  static {
    testProperties = new Properties();
    try {
      testProperties.load(new FileInputStream(new File("test.config")));
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      System.out.println("Working Dir: " + new File(".").getAbsolutePath());
      e.printStackTrace();
      System.exit(-1);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static String getTestDir() {
    return testProperties.getProperty(TEST_DIR);
  }

  public static String getLibPath() {
    return testProperties.getProperty(LIB_JAR);
  }
}
