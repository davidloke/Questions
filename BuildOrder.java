import java.util.*;
import java.lang.*;
import java.io.*;

/* 
 * Question: Given a list of packages and their dependencies in arbitrary order, return the build order of the packages 
 * 
 * Possible Clarification Questions: 
 * - What is the input and outputs of the question?
 * - Is the list of packages unique? (i.e. every package only appears once)
 * - Is the list of packages complete? (i.e. every package that is required is in the list)
 * - What is the expected behavior if cyclic dependencies exist? (e.g. "A depends on B", and "B depends on A")
 * - What is the expected behavior if the list is not complete? 
 * 
 */
 
class BuildOrder
{
	public static void main (String[] args) throws java.lang.Exception
	{
		// Lets assume that the input is a 2D array of dependencies, and each index represents a package
		
		// No cyclic Dependencies:
		//Integer[][] inputDependencyList = { {1}, {3}, {1, 4}, {}, {3}, {0} };
		Integer[][] inputDependencyList = { {3}, {3}, {1}, {}, {0, 2}};
		
		// Has cyclic dependencies: (should throw exception)
		//Integer[][] inputDependencyList = { {1}, {5, 3}, {1, 4}, {}, {3}, {0} };
		//Integer[][] inputDependencyList = { {1}, {0} };
		
		System.out.println("Build Order:");
		for (Integer i: findBuildOrder(inputDependencyList)) {
		    System.out.println(i);
		}
		
		
	}
	
	public static ArrayList<Integer> findBuildOrder (Integer[][] inputDependencyList) throws Exception {
	    
	    // Create an output list that stores the build order
	    Stack<Integer> stack = new Stack<>();
	    
	    // Step 1: Build the in-degree List
	    // Create a data struct to store the in-degree values
	    int[] inDegree = new int[inputDependencyList.length];
	    
	    // loop through all the dependencies 
	    for (int i = 0; i < inputDependencyList.length; i++) {
	        for (int j = 0; j < inputDependencyList[i].length; j++) {
	            // for each dependency, increase indegree count 
	            // e.g. if A depends on B, then inDegree[B]++
	            inDegree[inputDependencyList[i][j]]++;
	        }
	    }
	    
	    // Initialize the queue with first node(s) that have no indegree (i.e. the last package to build)
	    LinkedList<Integer> toProcessNext = new LinkedList<>();
	    toProcessNext.addAll(getPackagesWithNoInDegree(inDegree));
	    
	    // Step 2: do topological sort
	    // I.e.: - get first node in queue, and add it to stack to signify its reverse build order
	    //       - Reduce the indegrees for its dependency nodes
	    //       - add any node that now has indegree == 0 to queue
	    while (!toProcessNext.isEmpty()) {
	        Integer next = toProcessNext.poll();
	        stack.add(next);
	        for (int i = 0; i < inputDependencyList[next].length; i++) {
	            inDegree[inputDependencyList[next][i]]--;
	        }
	        toProcessNext.addAll(getPackagesWithNoInDegree(inDegree));
	    }
	    
	    // Error handling for cyclic dependencies 
	    for (int i = 0; i < inDegree.length; i++) {
	        if (inDegree[i] > 0) {
	            throw new Exception("Can't generate build order as there exist a cyclic dependency!");
	        } 
	    }
	    
	    // Post processing: put the build order into a list. top element in stack == first package to build
	    ArrayList<Integer> buildOrder = new ArrayList<>();
	    while (!stack.isEmpty()) {
	        buildOrder.add(stack.pop());
	    }
	    
	    return buildOrder;
	}
	
	// return a list of indexes that have inDegree[i] == 0. Used to enqueue nodes for topological sort
    public static List<Integer> getPackagesWithNoInDegree(int[] inDegree){
        ArrayList<Integer> noInDegree = new ArrayList<>();
        for (int i = 0; i < inDegree.length; i++) {
	        if (inDegree[i] == 0) {
	            noInDegree.add(i);
	            inDegree[i]--; // special handling for visited nodes: decrease it once more to -1, so that it won't be processed next time
	        }
	    }
	    return noInDegree;
    }
}
