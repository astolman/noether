package concurrentgraph;

import java.util.concurrent.locks.ReentrantLock;
import java.util.ConcurrentModificationException;

class Edge {
   Node u;
   Node v;
   ReentrantLock lock;
   double weight;

   protected Edge(Node u, Node v, double w) {
      this.u = u;
      this.v = v;
      lock = new ReentrantLock();
      weight = w;
      lock.lock();
   }

   protected void lock() {
      lock.lock();
   }

   protected void unlock() throws IllegalMonitorStateException{
      if (!lock.isHeldByCurrentThread()) throw new IllegalMonitorStateException();
      while (lock.isHeldByCurrentThread())
         lock.unlock();
   }

   protected boolean tryLock() {
      return lock.tryLock();
   }

   protected Node other (Node vert) {
      if (u != vert && v != vert) return null;
      return (u == vert)? v : u;
   }

   protected void lockCheck() throws ConcurrentModificationException {
      if (!lock.isHeldByCurrentThread()) 
         throw new ConcurrentModificationException("Don't have the lock for edge " + toString());
   }

   public String toString() {
      return "(" + u + ", " + v + ", " + weight + ")";
   }
}
