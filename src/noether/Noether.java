package noether;
import java.util.*;
import java.util.concurrent.*;

public class Noether {
   
   
   public static void doLoop(WorkList wl) {
      int numThreads = Runtime.getRuntime().availableProcessors();
      //int numThreads=1;
      NoetherThread[] threads = new NoetherThread[numThreads];
      ExecutorService threadPool = Executors.newFixedThreadPool(1);
      for (int i = 0; i < numThreads; i++) {
         threads[i] = new NoetherThread(wl);
         threads[i].start();
      }  
      try {
      for (int i = 0; i < numThreads; i++) {
         threads[i].join();
      }
      } catch (InterruptedException e) {
         System.err.println("Interrupted.");
         System.exit(1);
      }
   }
}
