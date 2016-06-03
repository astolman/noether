import java.util.*;

class foo {
   public static class Edge{
      public Integer u;
      public Integer v;
      public double weight;
   }
   
   public static class Node {
      public Vector<Edge> neighbors;
   }
   public static void main (String[] args) {
      Node n1 = new Node();
      Node n2 = new Node();
      Edge e = new Edge();
      e.u = 1;
      e.v = 2;
      e.weight = 3.5;
      n1.neighbors = new Vector<Edge>();
      n2.neighbors = new Vector<Edge>();
      n1.neighbors.add(e);
      n2.neighbors.add(e);
      e.weight = 4.0;
      System.out.println(n1.neighbors.firstElement().weight);
   }

}
