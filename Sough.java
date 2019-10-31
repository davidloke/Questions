import java.util.Hashtable;
import java.util.ListIterator;

class Pair {
  int x;
  int y;
  public Pair(int x, int y) {
    this.x = x;
    this.y = y;
  }
  public String toString() {
    return "(" + x + "," + y + ")";
  }
}

class Node {
  Node next;
  Node prev;
  String value;
  public Node(String value) {
    this.value = value;
  }
  public String toString() {
    return value;
  }
}

class Main {
  private static final int maxLength = 1000;
  private static final int maxWidth = 1000;
  private static Node head; 
  private static Node tail;
  private static Hashtable<String,Node> pairLookupTable;
  private static Hashtable<Node,Pair> nodeLookupTable;
  public static void main(String[] args) {
    
    // Provided 2D data structure 
    Node[][] grid = new Node[maxLength][maxWidth];
    pairLookupTable = new Hashtable<>();
    nodeLookupTable = new Hashtable<>();

    // Do some processing 
    Node node1 = new Node("1");
    Node node2 = new Node("2");
    Node node3 = new Node("3");
    Node node4 = new Node("4");

    Pair pair1 = new Pair(1,1);
    Pair pair2 = new Pair(2,2);
    Pair pair3 = new Pair(3,3);
    Pair pair4 = new Pair(4,4);

    // Update batch 1 - Linkedlist is HEAD-3-1-2-TAIL
    updateCoordinate(grid, node3, pair3);
    updateCoordinate(grid, node1, pair1);
    updateCoordinate(grid, node2, pair2);

    // Update batch 2 - Linkedlist is HEAD-2-4-3-1-TAIL
    updateCoordinate(grid, node4, pair4);
    updateCoordinate(grid, node3, pair3);
    updateCoordinate(grid, node1, pair1);

    // Oldest Coordinate is (2,2)
    Pair oldestPair = getOldestUpdatedCoordinate();
    System.out.println("oldestPair x: " + oldestPair.x);
    System.out.println("oldestPair y: " + oldestPair.y);
  }
  
  public static boolean updateCoordinate(
      Node[][] grid,
      Node newNode,
      Pair coordinate) {

    int x = coordinate.x;
    int y = coordinate.y;

    // Sanitize inputs 
    if (x < 0 || y < 0 || x >= maxLength || y >= maxWidth) {
      return  false;
    }

    grid[x][y] = newNode;
    updateNodeLinkedList(coordinate, newNode);
    updateLookupTables(coordinate, newNode);
    return true;
  }

  public static Pair getOldestUpdatedCoordinate() {
    // Special case - no coordinates were updated
    if (head == null) { 
      return null;
    }
    return nodeLookupTable.get(head);
  }

  private static void updateNodeLinkedList(Pair coordinate, Node node) {
    if (head == null) {
      //Special case - empty list 
      head = node;
      tail = node;
    } else {
      // if lookup table has an entry, remove it from the node linked list
      if (pairLookupTable.containsKey(coordinate.toString())) {
        Node nodeToRemove = pairLookupTable.get(coordinate.toString());
        if (nodeToRemove.prev != null)
          nodeToRemove.prev.next = nodeToRemove.next;
        else 
          head = nodeToRemove.next;
        if (nodeToRemove.next != null)
          nodeToRemove.next.prev = nodeToRemove.prev;
        else 
          tail = nodeToRemove.prev;
        nodeToRemove.next = null;
        nodeToRemove.prev = null;
        pairLookupTable.remove(coordinate.toString());
      }
      // Update tail of linked list 
      tail.next = node;
      node.prev = tail;
      tail = node;  
    }
    // Print the linkedlist afer every update
    printNodeLinkedList();
  }

  private static void updateLookupTables(Pair coordinate, Node node) {
    // Update the lookup table with the new index
    pairLookupTable.put(coordinate.toString(), node);
    nodeLookupTable.put(node, coordinate);
  }

  private static void printNodeLinkedList() {
    if (head != null) {
      Node current = head;
      String result = current.toString();
      
      current = current.next;
      while (current != null) { 
        result += ", " + current.toString();
        current = current.next;
      }
      System.out.println(result);
    }
  }
}
