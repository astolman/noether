import java.util.*;
import noether.*;
import concurrentgraph.*;

class PhaseOneItem implements noether.WorkItem {
   LinkedList<Integer> list;
   public static ConcurrentGraph G;

   public PhaseOneItem(LinkedList<Integer> l) {
      list = l;
   }

   public void doWork() {
      System.out.println(list);
      while (! list.isEmpty()) {
         int node = list.poll();
         ListIterator<Integer> li = G.locklessGetNeighbors(node);
         int minNeighbor = li.next();
         double minWeight = G.locklessGetWeight(node, minNeighbor);
         while (li.hasNext()) {
            int v = li.next();
            double w = G.locklessGetWeight (node, v);
            if (w < minWeight) {
               minWeight = w;
               minNeighbor = v;
            }
         }
         BoruvkaWorkList.incMSTWeight(minWeight);
         G.locklessContractEdge(node, minNeighbor);
      }
   }
}
