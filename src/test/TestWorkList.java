import noether.*;
import java.util.*;
import java.lang.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
public class TestWorkList extends noether.WorkList {

   String[] wl;
   int index;

   public TestWorkList() {
      wl = new String[] {
      "Item 1",
      "Item 2",
      "Item 3",
      "Item 4",
      "Item 5",
      "Item 6",
      "Item 7",
      "Item 8"
      };
      index = 0;
   }   

   public boolean stopCondition() {
      return index == 8;
   }

   public WorkItem getWorkItem() {
      WorkItem wi = new TestWorkItem(wl[index]);
      index ++;
      return wi;
   }
}
