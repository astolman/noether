package noethergraph;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

class Edge{
   NoetherNode u;
   NoetherNode v;
   double weight;
   ReentrantLock lock;
   
   protected Edge(NoetherNode u, NoetherNode v, double weight) {
      this.u=u;
      this.v=v;
      this.weight=weight;
      lock = new ReentrantLock();      
   }

   public String toString() {
      return "(" + u +", " + v + ", " + weight + ")";
   }
}
