package noethergraph;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class NoetherNode {
   int label;
   ReentrantLock myLock;
   protected LinkedList<Edge> neighborhood;

   static double MSTWeight = 0;

   public NoetherNode(int label) {
      this.label = label;
      myLock = new ReentrantLock();
      neighborhood = new LinkedList<Edge>();
   }

   public void lock() {
      
      System.out.println("Thread " + Thread.currentThread().getId() + ": trying to lock node " + label);
      myLock.lock();
   }

   public boolean tryLock() {
      try {
      boolean val = myLock.tryLock(0, TimeUnit.SECONDS);
      System.out.println("Thread " + Thread.currentThread().getId() + ": called tryLock on node " + label + ", going to return " + val);
      return val;
      } catch (InterruptedException e) {System.exit(1);}
      return false;
   }

   public void unlock() {
      System.out.println("Thread " + Thread.currentThread().getId() + ": unlocking node " + label);
      myLock.unlock();
   }

   public int getLabel() {
      return label;
   }

   public Edge getMinNeighbor() {
      Edge min = new Edge (null, null, Double.MAX_VALUE);
      for (Edge e : neighborhood) {
         e.lock.lock();
         if (e.weight < min.weight) {
            min.lock.unlock();
            min = e;
         } else e.lock.unlock();
      }

      return min;
   }

   public LinkedList<Edge> lockNeighborhood() {
      LinkedList<Integer> gamma = new LinkedList<Integer>();
      for (Edge e : neighborhood) {
         NoetherNode u = (e.u == this)? e.v : e.u;
         gamma.add(u);
         
      }
      return gamma;
   }

   public void removeAllEdgesTo (int v) {
      if ( v == label) {
         for (Edge e : neighborhood) {
            if (e.u.label == v && e.v.label == v) neighborhood.remove(e);
         }
      } else {
         ListIterator<Edge> li = neighborhood.listIterator();
         while (li.hasNext()) {
            Edge e = li.next();
            if (e.u.label == v || e.v.label == v) li.remove();
         }
      }
   }

   protected void combineWith(NoetherNode v) {
      neighborhood.addAll(v.neighborhood);
   }

   protected void moveAllEdgesTo(NoetherNode v) {
      for (Edge e : neighborhood) {
         if (e.u == this) {
            e.u = v;
         }
         else e.v = v;
      }
   }

   protected void removeAllEdges() {
      for (Edge e : neighborhood) {
         //Is it possible that we have self loops?
         NoetherNode v = (e.u == this)? e.v : e.u;
         for (Edge f : v.neighborhood) {
            if (f.u == this || f.v == this) v.neighborhood.remove(f);
         }
      }
      neighborhood = null;
   }

   static void addEdge (NoetherNode u, NoetherNode v, double w) {
      Edge e = new Edge (u, v, w);
      Edge f = new Edge (v, u, w);
      u.neighborhood.add(e);
      v.neighborhood.add(f);
   }

   public String toString () {
      return (new Integer(label)).toString();
   }
}
