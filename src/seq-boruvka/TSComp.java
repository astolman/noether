import java.util.*;

public class TSComp implements Comparator<TreeSet<Integer>> {

   @Override
   public int compare (TreeSet<Integer> t1, TreeSet<Integer> t2) {
      int value = 0;
      if (t1.size() != t2.size()) {
         if (t1.size() > t2.size()) return 1;
         else return -1;
      } else {
         Iterator<Integer> i1 = t1.iterator();
         Iterator<Integer> i2 = t2.iterator();
         while (i1.hasNext()) {
            value = i1.next().compareTo(i2.next());
            if (value != 0) return value;
         }
         return value;
      }
   }
}
