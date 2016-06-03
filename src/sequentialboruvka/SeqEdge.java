package sequentialgraph;

import java.util.ConcurrentModificationException;

class SeqEdge {
   SeqNode u;
   SeqNode v;
   double weight;

   protected SeqEdge(SeqNode u, SeqNode v, double w) {
      this.u = u;
      this.v = v;
      weight = w;
   }


   protected SeqNode other (SeqNode vert) {
      if (u != vert && v != vert) return null;
      return (u == vert)? v : u;
   }

   public String toString() {
      return "(" + u + ", " + v + ", " + weight + ")";
   }
}
