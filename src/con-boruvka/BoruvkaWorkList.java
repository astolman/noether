package boruvka;

import noether.*;
import noethergraph.*;
import java.util.*;
import java.util.concurrent.*;

class BoruvkaWorkList extends noether.WorkList {
   private Integer[] wl;
   NoetherGraph G;
   static volatile double MSTWeight = 0;

   public BoruvkaWorkList (NoetherGraph G) {
      this.G = G;
      wl = G.nodes().toArray(new Integer[0]);
      abortTolerance = 50;
   }

   public BoruvkaWorkItem getWorkItem() {
      return new BoruvkaWorkItem(wl[(new Random()).nextInt(wl.length)]);
   }

   public boolean stopCondition() {
      wl = G.nodes().toArray(new Integer[0]);
      return (wl.length == 1);
   }

   static void incMST(double w) {
      MSTWeight += w;
   }
}
