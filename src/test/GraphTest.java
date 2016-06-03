import noethergraph.*;
import java.util.*;
import java.io.*;

class GraphTest {

   static NoetherGraph getInput() {
      Scanner in;
      try {
         in = new Scanner(new File("graph.txt"));
      } catch (FileNotFoundException e) {
         System.err.println("Couldn't open input.txt");
         in = new Scanner(System.in);
      }
      NoetherGraph G = new NoetherGraph(in);
      in.close();
      return G;
   }

   public static void main (String[] args) {
      
      NoetherGraph G = getInput();
      System.out.println(G.getNode(833));
      LinkedList<Integer> neighbs = G.getNode(833).getNeighborhood();
      System.out.println(neighbs);
   }

}
