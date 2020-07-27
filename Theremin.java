import java.util.*;

class Theremin
{
    /**
     *  Question: Lets assume we are analyzing degree programs of universities around the world. 
     *  We are given an unsorted list of courses (i.e. modules) from several degree programs, 
     *  as well as their prerequisite course(s) that a student must first complete. 
     *  Write a program that will group courses for each degree program together.
     */
    public static void main (String[] args) throws java.lang.Exception
    {
        List<String> courses = new ArrayList<String>() {{
           add("C");
           add("B");
           add("A");
           add("W");
           add("X");
           add("B2B");
           add("A1A");
           add("Z");
           add("D4D"); 
        }};
        
        Map<String,String[]> prerequisites = new HashMap<String,String[]>() {{
            put("A", new String[] {"B"});
            put("B", new String[] {"A"});
            put("C", new String[] {"W"});
            put("B2B", new String[] {"D4D"});
            put("W", new String[] {"Z", "A"});
            put("X", new String[] {"Z"});
            put("A1A", new String[] {"B2B"});
        }};
        
        System.out.println("List of Courses by Degree Program:");
        for (HashSet<String> degree : findCoursesByDegree(courses, prerequisites)) {
            System.out.println("- " + degree);
        }
    }
    
    public static List<HashSet<String>> findCoursesByDegree(List<String> courses, Map<String,String[]> prerequisites) {
        
        List<HashSet<String>> allDegrees = new ArrayList<>();
        
        /**
         *   STEP 1: BUILD ADJACENCY LIST
         **/
        
        HashMap<String, HashSet<String>> adjacencyList = new HashMap<>();
         
        // Iterate through all prerequisites relationships
        for(String currentCourse : prerequisites.keySet()) {
            
            List<String> prerequisiteCourses = Arrays.asList(prerequisites.get(currentCourse));
            
            // For every prerequisite of the currentCourse, add currentCourse as one of its adjacent nodes
            for (String currentPrerequisiteCourse : prerequisiteCourses) {
                HashSet<String> childVertexAdjacencyList = adjacencyList.getOrDefault(currentPrerequisiteCourse, new HashSet<String>());
                childVertexAdjacencyList.add(currentCourse);
                adjacencyList.put(currentPrerequisiteCourse, childVertexAdjacencyList);
            }
            
            // Merge any existing list in adjacency list HashMap for currentCourse, with those found in the original prerequisites relationship that was provided to us 
            HashSet<String> thisCourseVertexAdjacencyList = adjacencyList.getOrDefault(currentCourse, new HashSet<String>());
            thisCourseVertexAdjacencyList.addAll(prerequisiteCourses);
            adjacencyList.put(currentCourse, thisCourseVertexAdjacencyList);
        }
        
        /**
         *   STEP 2: FIND CONNECTED COMPONENTS 
         **/
        
        // This set stores the vertices we have already visited
        HashSet<String> alreadyProcessedCourses = new HashSet<>();
        
        for(String thisCourse : courses) {
            
            // Handles vertices that we have visited before in a previous connected component 
            if (alreadyProcessedCourses.contains(thisCourse)) {
                continue;
            }
            
            // If we have not visited this node before, it must belong to a new connected component
            HashSet<String> thisDegree = new HashSet<>();
            
            // Create intermediate data structure for BFS algorithm
            LinkedList<String> intermediateVertices = new LinkedList<>();
            
            // Add current node into the data structure for processing
            intermediateVertices.add(thisCourse);
            
            // Run BFS algorithm
            while(!intermediateVertices.isEmpty()){
                String currentCourse = intermediateVertices.poll(); // poll() will get & dequeue first element
                
                // Add current vertex to the connected component set
                thisDegree.add(currentCourse);
                
                // Then add all unvisited (excluding soon-to-be-visited) adjacent vertices into the intermediate data structure for processing
                for(String thisCourseAdjacentVertex : adjacencyList.getOrDefault(currentCourse, new HashSet<String>())) {
                    if (thisDegree.contains(thisCourseAdjacentVertex) || 
                        intermediateVertices.contains(thisCourseAdjacentVertex)) {
                        // Handling for cyclic dependencies
                        continue; 
                    } else {
                        intermediateVertices.add(thisCourseAdjacentVertex);
                    }
                }

            }
            
            // Save this connected component to the result set for returning 
            allDegrees.add(thisDegree);
            
            // Add all vertices of this connected component to the "visited" set to avoid duplication in the results
            alreadyProcessedCourses.addAll(thisDegree);
            
        }
        
        return allDegrees;
    }
}
