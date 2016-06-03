package concurrentgraph;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.io.*;

public class ConcurrentGraph {
   ConcurrentHashMap<Integer, Node> adjList;
   
   public ConcurrentGraph (Scanner in) {
      adjList = new ConcurrentHashMap<Integer, Node>(in.nextInt());
      int E = in.nextInt();
      if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
      for (int i = 0; i < E; i++) {
         addEdge(in.nextInt(), in.nextInt(), in.nextDouble());
      }
   }

   public boolean setupOperator (int v) {
      Node vert = adjList.get(v);
      if (vert == null) return false;
      return lockNeighborhood(vert);
   }

   @Override
   public String toString() {
      String s = "";
      Enumeration<Node> nodes = adjList.elements();
      while (nodes.hasMoreElements()) {
         Node n = nodes.nextElement();
         s = s + n.toString() + ": " +n.edges.toString();
         s = s + "\n";
      }
      return s;
   }

   public boolean lockNeighborhood (Node vert) {
      try {
         if (! vert.tryLock()){ 
            return false;
         }
         ListIterator<Edge> li = vert.getIt();
         while (li.hasNext()) {
            Edge e = li.next();
            if (! e.tryLock() || !e.other(vert).tryLock()) {
               if (e.other(vert).lock.isHeldByCurrentThread()) e.other(vert).unlock();
               if (e.lock.isHeldByCurrentThread()) e.unlock();
               li.previous();
               while (li.hasPrevious()) {
                  Edge f = li.previous();
                  f.other(vert).unlock();
                  f.unlock();
               }
               vert.unlock();
               return false;
            }
         }
         return true;
      } catch (ConcurrentModificationException e) {
         e.printStackTrace();
         System.exit(1);
         return false;
      }
   }

   public void unlockNeighborhood(int v) throws NoSuchElementException {
      Node vert = adjList.get(v);
      if (vert == null) throw new NoSuchElementException();
      for (Edge e : vert.edges) {
         if (e.lock.isHeldByCurrentThread()) e.unlock();
         if (e.other(vert).lock.isHeldByCurrentThread()) e.other(vert).unlock();
      }
      vert.unlock();
   }

   public void contractEdge (int u, int v) throws ConcurrentModificationException, NoSuchElementException {
      Node v1 = adjList.get(u);
      Node v2 = adjList.get(v);
      if (v1 == null || v2 == null) throw new NoSuchElementException();
      if (! lockNeighborhood(v1)) throw new ConcurrentModificationException();
      ListIterator<Edge> li = v1.getIt();
      while (li.hasNext()) {
         Edge e = li.next();
         if (e.u == v2 || e.v == v2) {
            li.remove();
            v2.edges.remove(e);
         }
         else {
            Node cur;
            if (e.u == v1) {
               e.u = v2;
               cur = e.v;
            }
            else {
               e.v = v2;
               cur = e.u;
            }
            v2.edges.add(e);
            Collections.sort(cur.edges, new EdgeComparator(cur));
            ListIterator<Edge> neighbIt = cur.getIt();
            Node nprev = null;
            if (neighbIt.hasNext()) {
               nprev = neighbIt.next().other(cur);
            } else 
               System.out.println("here?");
            while (neighbIt.hasNext()) {
               Node ncur = neighbIt.next().other(cur);
               if (ncur == nprev) neighbIt.remove();
               nprev = ncur;
            }
         }
      }
      
      adjList.remove(u);
      //need to remove duplicate edges, only keep the lightest one
      Collections.sort(v2.edges, new EdgeComparator(v2));
      li = v2.getIt();
      if (! li.hasNext()) return;
      Node prev = li.next().other(v2);
      while (li.hasNext()) {
         Edge e = li.next();
         Node cur = e.other(v2);
         if (cur.label == prev.label) {
            li.remove();
            /*
            Collections.sort(cur.edges, new EdgeComparator(cur));
            ListIterator<Edge> neighbIt = cur.getIt();
            Node nprev = neighbIt.next().other(cur);
            while (neighbIt.hasNext()) {
               Node ncur = neighbIt.next().other(cur);
               if (ncur == nprev) neighbIt.remove();
               nprev = ncur;
            }
         */
            cur.edges.remove(e);
         } else if ( cur==v1) {
            li.remove();
         }
         prev = cur;
      }
   }

   public double getWeight (int u, int v) throws ConcurrentModificationException, 
           NoSuchElementException  {
      Node n1 = adjList.get(u);
      Node n2 = adjList.get(v);
      if (n1 == null) throw new NoSuchElementException("Node " + u + " not in graph.");
      if (n2 == null) {
         System.out.println(toString());
         throw new NoSuchElementException("Node " + v + " not in graph.");
      }
      n1.lockCheck();
      n2.lockCheck();
      try {
         ListIterator<Edge> li = n1.getIt();
         while(li.hasNext()){
         Edge e = li.next();
            if (e.other(n1) == n2) return e.weight;
         }
         throw new NoSuchElementException ("Edge " + u + ", " + v +" not in graph.");
      } catch (ConcurrentModificationException e) {
         e.printStackTrace();
         System.exit(1);
      }
      return 0;
   }
      
   public ListIterator<Integer> getNeighbors(int v) throws ConcurrentModificationException, NoSuchElementException {
      Node vert = adjList.get(v);
      if (vert == null) throw new NoSuchElementException("Can't get neighbors of node " + v + ", it's not in the graph.");
      if (! lockNeighborhood(vert)) throw new ConcurrentModificationException("Can't return iterator if you don't have the locks.");
      ListIterator<Edge> edgeIt = vert.getIt();
      LinkedList<Integer> intIt = new LinkedList<Integer>();
      while (edgeIt.hasNext()) {
         Edge e = edgeIt.next();
         intIt.add(e.other(vert).label);
      }
      return intIt.listIterator();
   }

   public void addEdge (int u, int v, double w) throws ConcurrentModificationException {
      Node v1 = adjList.get(u);
      Node v2 = adjList.get(v);
      if (v1 == null) {
         v1 = new Node(u);
         adjList.put(u, v1);
      }
      else 
         if (! lockNeighborhood(v1)) throw new ConcurrentModificationException("Need ownership of lock to modify edges of node " + u);
      if (v2 == null) {
         v2 = new Node(v);
         adjList.put(v, v2);
      }
      else 
         if (! lockNeighborhood(v2)) throw new ConcurrentModificationException("Need ownership of lock to modify edges of node " + v);
      ListIterator<Edge> v1It = v1.getIt();
      ListIterator<Edge> v2It = v2.getIt();
      while (v1It.hasNext()) {
         Edge e = v1It.next();
         if (e.other(v1) == v2) {
            e.weight = w;
            return;
         }
      }
 
      Edge e = new Edge(v1, v2, w);
      v1.edges.add(e);
      v2.edges.add(e);
   }

   public synchronized void unlockAll() {
      Enumeration<Node> nodes = adjList.elements();
      while(nodes.hasMoreElements()) {
         Node v = nodes.nextElement();
         ListIterator<Edge> It = v.getIt();
         while (It.hasNext()) {
            Edge e = It.next();
            if (e.lock.isLocked()) e.unlock();
         }
         v.unlock();
      }
   }

   public synchronized void lockAll() throws ConcurrentModificationException{
      Enumeration<Node> nodes = adjList.elements();
      while (nodes.hasMoreElements()) {
         Node v = nodes.nextElement();
         if (! lockNeighborhood(v)) throw new ConcurrentModificationException();
      }
   }

   public Set<Integer> nodes() throws ConcurrentModificationException {
      Set<Integer> nodes = adjList.keySet();
      for (int i : nodes) {
         if (! setupOperator(i)) throw new ConcurrentModificationException();
      }
      return nodes;
   }
}
