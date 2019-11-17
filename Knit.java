public class Knit {

  public static void main(String[] args) {

    // Given a sorted arrray in ascending order that has been rotated 0 or more times, find if an element exists within the array
    int[] intArray = new int[]{7, 8, 9, 10, -2, -1, 0, 5};
    int toFind = 12;

    System.out.println("Final result: " + elementExistsInArray(intArray, toFind, 0, (intArray.length -1)));

  }

  private static boolean elementExistsInArray(int[] intArray, int toFind, int startIndex, int endIndex) {
    
    String intArrayString = "";
    for(int item:intArray) {
        intArrayString += (item + ", ");
    }
    intArrayString = intArrayString.substring(0, intArrayString.length()-2);
    System.out.println("The array: " + intArrayString);
    System.out.println("StartIndex: " + startIndex);
    System.out.println("endIndex: " + endIndex);

    // Sanitize input 
    if (intArray.length == 0 || startIndex < 0 || endIndex < 0) {
      return false;
    }

    // Special case - When only 1 element 
    if (startIndex == endIndex) {
      return intArray[startIndex] == toFind;
    }

    // Speical case - when pivot index is the desired element
    int middleIndex = (endIndex+startIndex)/2;
    if (intArray[middleIndex] == toFind)
      return true;
    
    
    // Do work
    // If left half is sorted
    if (intArray[startIndex] <= intArray[middleIndex]) {
        // If left half contains the element 
        if (intArray[startIndex] <= toFind && intArray[middleIndex] >= toFind) {
          return elementExistsInArray(intArray, toFind, startIndex, middleIndex-1);
        }
        // Otherwise the right half contains the element 
        else {
          return elementExistsInArray(intArray, toFind, middleIndex+1, endIndex);
        }

    }
    // Otherwise right half is sorted
    else { 
        // If right half contains the element 
        if (intArray[middleIndex] <= toFind && intArray[endIndex] >= toFind) {
          return elementExistsInArray(intArray, toFind, middleIndex+1, endIndex);
        }
        // Otherwise the left half contains the element 
        else {
          return elementExistsInArray(intArray, toFind, startIndex, middleIndex-1);
        }
    }
  }
}