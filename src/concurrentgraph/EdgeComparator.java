package concurrentgraph;

import java.util.*;

class EdgeComparator implements Comparator<Edge> {
   Node v;

   protected EdgeComparator(Node v) {
      this.v = v;
   }

   public int compare(Edge e1, Edge e2) {
      int x = e1.other(v).label;
      int y = e2.other(v).label;
      if (x != y) {
         return (new Integer(x)).compareTo(new Integer(y));
      }
      else return (new Double(e1.weight)).compareTo(new Double(e2.weight));
   }
}
