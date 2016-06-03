package noether;

import java.util.*;

class NoetherThread extends Thread{
   WorkList wl;
   int abortCount;
   protected NoetherThread (WorkList foo) {
      foo.WorkListLock.lock();
      wl = foo;
      foo.WorkListLock.unlock();
      abortCount = 0;
   }

   private static int getThreadCount () {
      int count = 0;
      for (Thread t : Thread.getAllStackTraces().keySet()) {
         if (t.getState() == Thread.State.RUNNABLE) count++;
      }
      return count;
   }

   @Override
   public void run() {
      while (true ) {
         if (abortCount > wl.abortTolerance && getPriority() > Thread.MIN_PRIORITY) {
            setPriority(getPriority()  -1);
            System.out.println("Abort count is " + abortCount);
            abortCount=0;
         }
         wl.WorkListLock.lock();
         if (wl.stopCondition()) {
            wl.WorkListLock.unlock();
            break;
         }
         WorkItem item = wl.getWorkItem();
         wl.WorkListLock.unlock();
         if (item == null) abortCount ++;
         else {
            item.doWork();
            abortCount = 0;
         }
      }
      System.out.println("Abortcount is " + abortCount);
   }
}
