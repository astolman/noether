import noether.*;
import java.lang.*;

public class TestWorkItem implements noether.WorkItem {
   String s;
   public TestWorkItem(String s) {
      this.s = s;
   }

   public boolean setUp() {
      return true;
   }

   public void doWork() {
      System.out.println(s);
   }
}
