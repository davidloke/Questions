import java.util.*; 

class Yucca {
  
  public static void main(String[] args) {
    
    List<String[]> globalTree = Arrays.asList(new String[][]{
      {"A", "null"},
      {"B", "A"},
      {"C", "B"},
      {"D", "B"},
      {"E", "A"},
      {"F", "E"}
    });

    Set<String> directAccess = new HashSet<String>();
    directAccess.add("C");
    directAccess.add("E");
 
    
    // Update the hash table representation of the tree 
    Hashtable<String,String> globalTreeHT = new Hashtable<>();
    populateHashTable(globalTreeHT, globalTree);

    // Figure out if the user has access
    System.out.println(hasAccess(globalTreeHT, directAccess, "D"));
  }

  private static boolean hasAccess(
      Hashtable<String,String> globalTreeHT,
      Set<String> directAccess,
      String targetNode) {
    
    // Special Case
    if (directAccess.contains(targetNode)) {
        return true;
    }

    // Normal Case
    Set<String> parentNodeSet = new HashSet<>();
    String parentNode = globalTreeHT.get(targetNode);
    while (!parentNode.equalsIgnoreCase("null")) {
      parentNodeSet.add(parentNode);
      parentNode = globalTreeHT.get(parentNode);
    }

    return !Collections.disjoint(parentNodeSet, directAccess);
  }
  
  private static void populateHashTable(
      Hashtable<String,String> globalTreeHT,
      List<String[]> globalTree) {

    // Clear out any legacy state
    globalTreeHT.clear();

    // Populate the hash table
    for (int i = 0; i < globalTree.size(); i++) {
      String[] pairAtIndex = globalTree.get(i);
      globalTreeHT.put(pairAtIndex[0], pairAtIndex[1]);
    }
  }
}
