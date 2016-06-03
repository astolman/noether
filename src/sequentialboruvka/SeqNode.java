package sequentialgraph;

import java.util.*;

class SeqNode {
   int label;
   LinkedList<SeqEdge> edges;

   SeqNode(int v) {
      label = v;
      edges = new LinkedList<SeqEdge>();
   }


   protected ListIterator<SeqEdge> getIt() throws ConcurrentModificationException {
      return edges.listIterator();
   }

   @Override
   public String toString() {
      return (new Integer(label)).toString();
   }
}
