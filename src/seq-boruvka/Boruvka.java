import java.util.*;
import java.lang.*;
import java.io.*;

class Boruvka {
   static WeightedGraph getInput(){
      Scanner in;
      try {
         in = new Scanner(new File("graph.txt"));
      } catch (FileNotFoundException e) {
         System.out.println("Couldn't open input.txt");
         in = new Scanner(System.in);
      }
      WeightedGraph G = new WeightedGraph(in);
      in.close();
      return G;
   }

   public static void main(String[] args) {
      final long startTime = System.currentTimeMillis();
      double MSTWeight=0;
      WeightedGraph G = getInput();
      if (G.connected().size() != 1) 
         throw new IllegalArgumentException ("Input graph must be connected.");
      WeightedGraph forest = new WeightedGraph (G.size());
      TreeSet<TreeSet<Integer>> wl = forest.connected();
      while (wl.size() > 1) {
         PriorityQueue<WeightedGraph.Edge> edgeSet = new PriorityQueue<WeightedGraph.Edge>(G.size(), new WeightedGraph.Edge());
         TreeSet<Integer> curr_comp = wl.pollFirst();
         for (int i : curr_comp) {
            double min = Double.MAX_VALUE;
            int minSrc = -1;
            int minDest = -1;
            for (int v : G.getNeighbors(i)) {
               if (! curr_comp.contains(v) && G.getWeight(i, v) < min){ 
                  min = G.getWeight(i, v);
                  minSrc = i;
                  minDest = v;
               }
            }
            edgeSet.add(new WeightedGraph.Edge(minSrc, minDest, min));
            MSTWeight += min;
         }
         forest.addUEdge(edgeSet.peek().src, edgeSet.peek().dest, edgeSet.peek().weight);
         wl = forest.connected();
      }
      System.out.println(MSTWeight);
      final long endTime = System.currentTimeMillis();
      System.out.println("Total execution time: " + (endTime - startTime));
   }

}
