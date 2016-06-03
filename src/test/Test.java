import noether.*;
import java.util.*;

public class Test {
   public static void main (String[] args) {
      TestWorkList wl = new TestWorkList();
      Noether.doLoop(wl);
   }
}
