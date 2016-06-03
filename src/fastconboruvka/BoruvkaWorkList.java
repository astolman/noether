import concurrentgraph.*;
import java.util.*;
import java.util.concurrent.*;
import noether.*;

class BoruvkaWorkList extends noether.WorkList{
   boolean phase;
   public static volatile double MSTWeight = 0;
   ArrayDeque<LinkedList<Integer>> multiList;
   LinkedList<Integer> nodes;
   static ConcurrentGraph G;
   int numThreads;
   int currentTurn;

   public BoruvkaWorkList(ConcurrentGraph G, int numThreads) {
      nodes = null;
      BoruvkaWorkList.G = G;
      phase = true;
      multiList = new ArrayDeque<LinkedList<Integer>>(G.partition(numThreads));
      abortTolerance = 10000;
      
   }

   public boolean stopCondition() {
      if (phase && multiList.isEmpty()) {
         phase = false;
         nodes = new LinkedList<Integer>(G.nodes());
         return false;
      } else 
         if (phase) return false;
         else
            return (nodes.size() == 1);
   }

   public WorkItem getWorkItem() {
      if (phase) {
         return new PhaseOneItem(multiList.pop());
      }
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
