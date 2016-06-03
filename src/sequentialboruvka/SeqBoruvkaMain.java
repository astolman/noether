import java.util.*;
import sequentialgraph.*;
import java.io.*;

public class SeqBoruvkaMain {
   
   static SequentialGraph getInput() {
      Scanner in;
      try {
         in = new Scanner(new File("graph.txt"));
      } catch (FileNotFoundException e) {
         System.err.println("Couldn't open input.txt");
         in = new Scanner(System.in);
      }
      SequentialGraph G = new SequentialGraph(in);
      in.close();
      return G;
   }

   public static void main(String[] args) {
      double MSTWeight=0;
      SequentialGraph G = getInput();
      Integer[] nodes = G.nodes().toArray(new Integer[0]);
      final long startTime = System.currentTimeMillis();
      for (int i = 0; i < nodes.length -1; i++) {
         int node = nodes[i];
         ListIterator<Integer> li = G.getNeighbors(node);
         int minNeighbor = li.next();
         double minWeight = G.getWeight(node, minNeighbor);
         while (li.hasNext()) {
            int v = li.next();
            double w = G.getWeight(node, v);
            if (w < minWeight) {
               minWeight = w;
               minNeighbor = v;
            }
         }
         MSTWeight += minWeight;
         G.contractEdge (node, minNeighbor);
      }
      System.out.println(MSTWeight);
      final long endTime = System.currentTimeMillis();
      System.out.printf("Total execution time: %d \n", endTime-startTime);
   }
}
