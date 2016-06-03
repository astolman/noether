package boruvka; 

import noether.*;
import noethergraph.*;
import java.util.concurrent.*;
import java.util.*;
import java.util.concurrent.locks.*;

class BoruvkaWorkItem implements noether.WorkItem{
   static NoetherGraph G;

   int v;
   public BoruvkaWorkItem (int n) {
      v = n;
   }

   public static void init (NoetherGraph g, WorkList w) {
      G = g;
   }

   public boolean doWork() {
      long tid = Thread.currentThread().getId();
      System.out.println("Thread " + tid + ": entering doWork with node " + v);
      if (! G.lockNeighborhoodOf(v)) return false;
      System.out.println("Thread " + tid + ": got locks on node " + v);
      BoruvkaWorkList.incMST(G.contractMinEdge(v));
      System.out.println("Thread " + tid + ": bout to release locks on node " + v);
      G.unlockNeighborhoodOf(v);
      return true;
   }

}
