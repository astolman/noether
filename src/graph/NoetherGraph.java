//Data structure that supports undirected weighted graphs
package noethergraph;
import java.util.*;
import java.util.concurrent.*;

public class NoetherGraph {
   ConcurrentHashMap<Integer, NoetherNode> adjList;

   public NoetherGraph(int n) {
      adjList = new ConcurrentHashMap<Integer, NoetherNode>(n);
      for (int i = 0; i < n; i++) {
         adjList.put(i, new NoetherNode(i));
      }
   }

   public NoetherGraph (Scanner in) {
      adjList = new ConcurrentHashMap<Integer, NoetherNode>(in.nextInt());
      int E = in.nextInt();
      if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
      for (int i = 0; i < E; i++) {
         addEdge(in.nextInt(), in.nextInt(), in.nextDouble());
      }
   }
   
   //contractEdge - NOTE: This is NOT thread safe
   //the first operand is the one that is destroyed
   public void contractEdge (int u, int v) {
      adjList.get(u).removeAllEdgesTo(v);
      adjList.get(v).removeAllEdgesTo(u);
      adjList.get(u).moveAllEdgesTo(adjList.get(v));
      adjList.get(v).combineWith(adjList.get(u));
      adjList.remove(u);
   }

   public void addEdge (int u, int v, double w) {
      if (!adjList.containsKey(u)) addNode(u);
      if (!adjList.containsKey(v)) addNode(v);
      NoetherNode uVert = adjList.get(u);
      NoetherNode vVert = adjList.get(v);
      NoetherNode.addEdge(uVert, vVert, w);
   }

   public void addNode (int v) {
      if (adjList.containsKey(v)) return;
      adjList.put(v, new NoetherNode(v));
   }

   public String toString() {
      String s = new String();
      Iterator<Integer> itor = adjList.keySet().iterator();
      while (itor.hasNext()) {
         int curr = itor.next();
         NoetherNode currNode = adjList.get(curr);
         s += curr + ": ";
         ListIterator<Edge> thisRow = currNode.neighborhood.listIterator();
         while (thisRow.hasNext())
            s+= thisRow.next().toString();
         s += "\n";
      }
      return s;
   }

   public Set<Integer> nodes() {
      return adjList.keySet();
   }

   public NoetherNode getNode(int i) {
      NoetherNode vert = adjList.get(i);
      if (vert == null || ! vert.tryLock()) return null;
      return vert;
   }

   public double getWeight(int u, int v) {
      NoetherNode start = adjList.get(u);
      for (Edge e : start.neighborhood) {
         if (e.u.label == v || e.v.label == v) return e.weight;
      }
      return 0;
   }

   public double contractMinEdge(int v) {
      NoetherNode vert = adjList.get(v);
      if (vert == null) {
         System.out.printf("Thread %d: somehow got a null vert here, %d\n", Thread.currentThread().getId(), v);
         System.out.print(toString());
      }
      Edge e = adjList.get(v).getMinNeighbor();
      double w = e.weight;
      if (e.v == null || e.u == null) return 0;
      int u = (e.u.label == v)?  e.v.label : e.u.label;
      contractEdge (v, u);
      return w;
   }

   /*public void unlockNode(int i) {
      NoetherNode vert = adjList.get(i);
      if (vert != null) vert.unlock();
   }
*/
   public boolean lockNeighborhoodOf(int v) {
      NoetherNode vert = adjList.get(v);
      if (vert == null) return false;
      if (! vert.tryLock()) return false;
      ListIterator<Edge> li = vert.neighborhood;
      while (li.hasNext()) {
         Edge e = li.next();
         if (! e.lock.tryLock() || 
}
 

