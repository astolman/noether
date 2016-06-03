package noether;

import java.util.concurrent.locks.ReentrantLock;

public abstract class WorkList {
   final ReentrantLock WorkListLock =  new ReentrantLock();
   public int abortTolerance;
   public abstract WorkItem getWorkItem();
   public abstract boolean stopCondition();
}
