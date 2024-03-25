/** */
package kr.ac.kaist.wala.hybridroid.frontend;

import kr.ac.kaist.wala.hybridroid.frontend.bridge.BridgeInfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author Sungho Lee
 */
public class Shell {
  public static long START;

  public static void main(String[] args) {
    long start = System.currentTimeMillis();
    START = start;

    kr.ac.kaist.wala.hybridroid.frontend.Driver d = new kr.ac.kaist.wala.hybridroid.frontend.Driver();

    Map<File, Set<BridgeInfo>> m = d.analyzeBridgeMapping(args[0], args[1]);
    kr.ac.kaist.wala.hybridroid.frontend.JSONOut out = new kr.ac.kaist.wala.hybridroid.frontend.JSONOut(m);

    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(args[2]));
      bw.write(out.toJSONString());
      bw.flush();
      bw.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    long end = System.currentTimeMillis();
    System.out.println("#time: " + (end - start));
  }
}
