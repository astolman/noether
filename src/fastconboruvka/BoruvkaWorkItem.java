import java.util.*;
import java.util.concurrent.*;
import noether.*;
import concurrentgraph.*;

class BoruvkaWorkItem implements noether.WorkItem{
   public static ConcurrentGraph G;
   int node;

   public BoruvkaWorkItem(int i) {
      node = i;
   }

   public void doWork() {
      ListIterator<Integer> li = G.getNeighbors(node);
      if (!li.hasNext()) System.out.println(G.toString());
      int minNeighbor = li.next();
      double minWeight = G.getWeight(node, minNeighbor);
      while (li.hasNext()) {
         int v = li.next();
         double w = G.getWeight(node, v);
         if (w < minWeight) {
            minWeight = w;
            minNeighbor = v;
         }
      }
      BoruvkaWorkList.incMSTWeight(minWeight);
      G.contractEdge(node, minNeighbor);
      G.unlockNeighborhood(minNeighbor);
   }

}
