import concurrentgraph.*;
import java.util.*;
import java.util.concurrent.*;
import noether.*;

class BoruvkaWorkList extends noether.WorkList{

   public static volatile double MSTWeight = 0;
   LinkedList<Integer> nodes;
   static ConcurrentGraph G;

   public BoruvkaWorkList(ConcurrentGraph G) {
      nodes = new LinkedList<Integer>(G.nodes());
      abortTolerance = nodes.size() * nodes.size();
      BoruvkaWorkList.G = G;
   }

   public boolean stopCondition() {
      return (nodes.size() == 1);
   }

   public BoruvkaWorkItem getWorkItem() {
      int base = nodes.poll();
      if (! G.setupOperator(base)) {
         nodes.add(base);
         return null;
      } else return new BoruvkaWorkItem(base);
   }

   public static synchronized void incMSTWeight(double w) {
      MSTWeight += w;
   }
}
