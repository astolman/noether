import java.util.*;
import java.lang.*;
public class WeightedGraph {

   public static class Edge implements Comparator<Edge>{
      public int dest;
      public double weight;
      public int src;

      Edge () {
         src = -1;
         dest = -1;
         weight = 0;
      }

      Edge (int u, int v, double w) {
         src = u;
         dest = v;
         weight = w;
      }
      
      @Override
      public int compare(Edge e1, Edge e2) {
         Double i1 = new Double (e1.weight);
         Double i2 = new Double (e2.weight);
         return i1.compareTo(i2);
      }
      
      public String toString() {
         return "(" + src +", " + dest + ", " + weight + ")";
      }
         
   }

   private final int N;
   HashMap<Integer, Vector<Edge>> adjList; 

   WeightedGraph(int n) {
      N = n;
      adjList = new HashMap<Integer, Vector<Edge>>(N);
      for (int i = 0; i < N; i++) {
         adjList.put(i, new Vector<Edge>());
      }
   }

   WeightedGraph (Scanner in) {
      adjList = new HashMap<Integer, Vector<Edge>>();
      N = in.nextInt();
      int E = in.nextInt();
      
      if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
      for (int i = 0; i < E; i++) {
         int u = in.nextInt();
         int v = in.nextInt();
         double w = in.nextDouble();
         addUEdge(u,v,w);
      }
   }

   public int size () {
      return N;
   }
   public void addDEdge (int u, int v, double w) throws IllegalArgumentException {
      if (u >= N || v >= N) throw new IllegalArgumentException();
      if (adjList.get(u) == null) adjList.put(u, new Vector<Edge>());
      if (adjList.get(v) == null) adjList.put(v, new Vector<Edge>());
      
      adjList.get(u).add(new Edge(u, v, w));
   }

   public void addUEdge (int u, int v, double w) {
      addDEdge(u, v, w);
      addDEdge(v, u, w);
   }

   public void removeDEdge(int u, int v) {
      Vector<Edge> gamma = adjList.get(u);
      for (int i = 0; i < gamma.size(); i++) {
         if (gamma.get(i).dest == v) {
            gamma.remove(i);
            break;
         }
      }
   }

   public void removeUEdge (int u, int v) {
      removeDEdge(u, v);
      removeUEdge(v, u);
   }

   public Vector<Integer> getNeighbors (int v) {
      Vector<Edge>  outneighb = adjList.get(v);
      Vector<Integer> gamma = new Vector<Integer>(outneighb.size());
      for (Edge e : outneighb) gamma.add(e.dest);
      return gamma;
   }

   public double getWeight (int u, int v) {
      for (Edge e : adjList.get(u)){
         if (e.dest == v) return e.weight;
      }
      return 0;
   }

   public TreeSet<TreeSet<Integer>> connected () {
      System.out.println(toString());
      TreeSet<TreeSet<Integer>> conn_components = new TreeSet<TreeSet<Integer>>(new TSComp());
      int[] visited = new int[N];
      for (int i = 0; i < N; i++) {
         if (visited[i] == 1) continue;
         TreeSet<Integer> component = new TreeSet<Integer>();
         LinkedList<Integer> queue = new LinkedList<Integer>();
         queue.add(i);
         while (queue.size() > 0) {
            int curr = queue.remove();
            visited[curr] = 1;
            component.add(curr);
            for (Edge e : adjList.get(curr)) 
               if (visited[e.dest] == 0) queue.add(e.dest);
         }
         conn_components.add(component);
      }
      return conn_components;
   }

   public String toString() {
      String str = new String("|V| = " + N + "\n");
      for (int i = 0; i < N; i++) {
         str += i + ": ";
         for (Edge e : adjList.get(i)) str += " " + e.toString() + " ";
         str += "\n";
      }
      return str;
   }
}
