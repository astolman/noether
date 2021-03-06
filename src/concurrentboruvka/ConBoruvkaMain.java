import java.util.*;
import concurrentgraph.*;
import java.io.*;
import noether.*;

public class ConBoruvkaMain {
   
   static ConcurrentGraph getInput() {
      Scanner in;
      try {
         in = new Scanner(new File("graph.txt"));
      } catch (FileNotFoundException e) {
         System.err.println("Couldn't open input.txt");
         in = new Scanner(System.in);
      }
      ConcurrentGraph G = new ConcurrentGraph(in);
      in.close();
      return G;
   }

   public static void main(String[] args) {
      ConcurrentGraph G = getInput();
      BoruvkaWorkList wl = new BoruvkaWorkList(G);
      BoruvkaWorkItem.G = G;
      G.unlockAll();
      final long startTime = System.currentTimeMillis();
      Noether.doLoop(wl);
      System.out.println(BoruvkaWorkList.MSTWeight);
      final long endTime = System.currentTimeMillis();
      System.out.printf("Total execution time: %d \n", endTime-startTime);
   }
}
