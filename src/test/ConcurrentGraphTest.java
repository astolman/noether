import concurrentgraph.*;
import java.util.*;
import java.io.*;
class ConcurrentGraphTest {
   
   static ConcurrentGraph getInput() {
      Scanner in;
      try {
         in = new Scanner(new File("graph.txt"));
      } catch (FileNotFoundException e) {
         in = new Scanner(System.in);
      }
      ConcurrentGraph G = new ConcurrentGraph(in);
      in.close();
      return G;
   }

   public static void main(String[] args) {
      ConcurrentGraph G = getInput();
      G.unlockAll();
      G.lockAll();
      G.setupOperator(5);
      G.contractEdge(5, 8);
      G.unlockAll();
      System.out.println(G.toString());
   }
}
