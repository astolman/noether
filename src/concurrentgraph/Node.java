package concurrentgraph;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

class Node {
   int label;
   LinkedList<Edge> edges;
   ReentrantLock lock;

   Node(int v) {
      label = v;
      edges = new LinkedList<Edge>();
      lock = new ReentrantLock();
      lock.lock();
   }

   protected void lock() {
      lock.lock();
   }

   protected void unlock() throws IllegalMonitorStateException{
      if (! lock.isHeldByCurrentThread()) throw new IllegalMonitorStateException();
      while (lock.isHeldByCurrentThread())
      lock.unlock();
   }

   protected boolean tryLock() {
      return lock.tryLock();
   }

   protected ListIterator<Edge> getIt() throws ConcurrentModificationException {
      if (! lock.isHeldByCurrentThread()) 
         throw new ConcurrentModificationException
            ("tried to get list iterator of node " + label + " and current thread does not hold the lock.");
      return edges.listIterator();
   }

   protected void lockCheck() throws ConcurrentModificationException {
      if (! lock.isHeldByCurrentThread())
         throw new ConcurrentModificationException("Tried to modify node "+label + "w/o the lock.");
   }

   @Override
   public String toString() {
      return (new Integer(label)).toString();
   }
}
