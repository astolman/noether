package sequentialgraph;

import java.util.*;
import java.io.*;

public class SequentialGraph {
   HashMap<Integer, SeqNode> adjList;
   
   public SequentialGraph (Scanner in) {
      adjList = new HashMap<Integer, SeqNode>(in.nextInt());
      int E = in.nextInt();
      if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
      for (int i = 0; i < E; i++) {
         addEdge(in.nextInt(), in.nextInt(), in.nextDouble());
      }
   }


   @Override
   public String toString() {
      String s = "";
      Iterator<SeqNode> nodes = adjList.values().iterator();
      while (nodes.hasNext()) {
         SeqNode n = nodes.next();
         s = s + n.toString() + ": " +n.edges.toString();
         s = s + "\n";
      }
      return s;
   }

   public void contractEdge (int u, int v) throws ConcurrentModificationException, NoSuchElementException {
      SeqNode v1 = adjList.get(u);
      SeqNode v2 = adjList.get(v);
      if (v1 == null || v2 == null) throw new NoSuchElementException();
      ListIterator<SeqEdge> li = v1.getIt();
      while (li.hasNext()) {
         SeqEdge e = li.next();
         if (e.u == v2 || e.v == v2) {
            li.remove();
            v2.edges.remove(e);
         }
         else {
            SeqNode cur;
            if (e.u == v1) {
               e.u = v2;
               cur = e.v;
            }
            else {
               e.v = v2;
               cur = e.u;
            }
            v2.edges.add(e);
            Collections.sort(cur.edges, new SeqEdgeComparator(cur));
            ListIterator<SeqEdge> neighbIt = cur.getIt();
            SeqNode nprev = null;
            if (neighbIt.hasNext()) {
               nprev = neighbIt.next().other(cur);
            } 
            while (neighbIt.hasNext()) {
               SeqNode ncur = neighbIt.next().other(cur);
               if (ncur == nprev) neighbIt.remove();
               nprev = ncur;
            }
         }
      }
      
      adjList.remove(u);
      //need to remove duplicate edges, only keep the lightest one
      Collections.sort(v2.edges, new SeqEdgeComparator(v2));
      li = v2.getIt();
      if (! li.hasNext()) return;
      SeqNode prev = li.next().other(v2);
      while (li.hasNext()) {
         SeqEdge e = li.next();
         SeqNode cur = e.other(v2);
         if (cur.label == prev.label) {
            li.remove();
            /*
            Collections.sort(cur.edges, new EdgeComparator(cur));
            ListIterator<Edge> neighbIt = cur.getIt();
            SeqNode nprev = neighbIt.next().other(cur);
            while (neighbIt.hasNext()) {
               SeqNode ncur = neighbIt.next().other(cur);
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

   public double getWeight (int u, int v) throws 
           NoSuchElementException  {
      SeqNode n1 = adjList.get(u);
      SeqNode n2 = adjList.get(v);
      if (n1 == null) throw new NoSuchElementException("Node " + u + " not in graph.");
      if (n2 == null) {
         System.out.println(toString());
         throw new NoSuchElementException("Node " + v + " not in graph.");
      }
      try {
         ListIterator<SeqEdge> li = n1.getIt();
         while(li.hasNext()){
         SeqEdge e = li.next();
            if (e.other(n1) == n2) return e.weight;
         }
         throw new NoSuchElementException ("Edge " + u + ", " + v +" not in graph.");
      } catch (ConcurrentModificationException e) {
         e.printStackTrace();
         System.exit(1);
      }
      return 0;
   }
      
   public ListIterator<Integer> getNeighbors(int v) throws NoSuchElementException {
      SeqNode vert = adjList.get(v);
      if (vert == null) throw new NoSuchElementException("Can't get neighbors of node " + v + ", it's not in the graph.");
      ListIterator<SeqEdge> edgeIt = vert.getIt();
      LinkedList<Integer> intIt = new LinkedList<Integer>();
      while (edgeIt.hasNext()) {
         SeqEdge e = edgeIt.next();
         intIt.add(e.other(vert).label);
      }
      return intIt.listIterator();
   }

   public void addEdge (int u, int v, double w){
      SeqNode v1 = adjList.get(u);
      SeqNode v2 = adjList.get(v);
      if (v1 == null) {
         v1 = new SeqNode(u);
         adjList.put(u, v1);
      }
      if (v2 == null) {
         v2 = new SeqNode(v);
         adjList.put(v, v2);
      }
      ListIterator<SeqEdge> v1It = v1.getIt();
      ListIterator<SeqEdge> v2It = v2.getIt();
      while (v1It.hasNext()) {
         SeqEdge e = v1It.next();
         if (e.other(v1) == v2) {
            e.weight = w;
            return;
         }
      }
 
      SeqEdge e = new SeqEdge(v1, v2, w);
      v1.edges.add(e);
      v2.edges.add(e);
   }

   public Set<Integer> nodes(){
      Set<Integer> nodes = adjList.keySet();
      return nodes;
   }
}
