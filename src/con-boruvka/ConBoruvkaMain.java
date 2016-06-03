package boruvka;

import noether.*;
import java.util.*;
import noethergraph.*;
import java.io.*;

public class ConBoruvkaMain {
   
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

   public static void main(String[] args) {
      final long startTime = System.currentTimeMillis();
      NoetherGraph G = getInput();
      BoruvkaWorkList wl = new BoruvkaWorkList(G);
      BoruvkaWorkItem.init(G, wl);
      Noether.doLoop(wl);
      System.out.println(wl.MSTWeight);
      final long endTime = System.currentTimeMillis();
      System.out.printf("Total execution time: %d \n", endTime-startTime);
   }
}
