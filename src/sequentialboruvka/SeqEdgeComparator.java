package sequentialgraph;

import java.util.*;

class SeqEdgeComparator implements Comparator<SeqEdge> {
   SeqNode v;

   protected SeqEdgeComparator(SeqNode v) {
      this.v = v;
   }

   public int compare(SeqEdge e1, SeqEdge e2) {
      int x = e1.other(v).label;
      int y = e2.other(v).label;
      if (x != y) {
         return (new Integer(x)).compareTo(new Integer(y));
      }
      else return (new Double(e1.weight)).compareTo(new Double(e2.weight));
   }
}
