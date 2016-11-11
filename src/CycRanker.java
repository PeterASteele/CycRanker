import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
public class CycRanker {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(new File(args[0]));

		ArrayList<HashSet<Edge>> edges = new ArrayList<HashSet<Edge>>();
		ArrayList<HashSet<Edge>> reverseEdges = new ArrayList<HashSet<Edge>>();
		int cycleCount = 0;
		// These hashmaps map strings for proteins like "P04355" to integers, and
		// vice versa
		// Overflow warning: These integers should never be multiplied, without
		// converting to longs.
		HashMap<String, Integer> mapToInt = new HashMap<String, Integer>();
		HashMap<Integer, String> reverseMap = new HashMap<Integer, String>();
		HashSet<Cycle> cycles = new HashSet<Cycle>();
		int count = 0;
//		Scanner in2 = new Scanner(System.in);
		while(input.hasNextLine()){
//			in2.nextLine();
			String inStr = input.nextLine();
			String[] arr = inStr.split(" ");
			
			//map the proteins to ints
			ArrayList<Integer> newNodes = new ArrayList<Integer>();
			
			
			for(int a = 3; a <= arr.length-2; a++){ 
				if(!mapToInt.containsKey(arr[a])){
					mapToInt.put(arr[a], count);
					reverseMap.put(count, arr[a]);
					newNodes.add(count);
					count++;
					edges.add(new HashSet<Edge>());
					reverseEdges.add(new HashSet<Edge>());
				}
			}
			
			for(int a = 3; a < arr.length-2; a++){ 
				String start = arr[a];
				String end = arr[a+1];
				edges.get(mapToInt.get(start)).add(new Edge(mapToInt.get(end), 1));
				reverseEdges.get(mapToInt.get(end)).add(new Edge(mapToInt.get(start), 1));
			}
			
			//calculate new cycles from adding the xth node.
			//newNodes are all the new start points.
			
			ArrayList<Cycle> cyclesOnNewNodes = getCyclesConstrainedByDepth(newNodes, edges, 5);
			for(Cycle temp: cyclesOnNewNodes){
				int tempSize = cycles.size();
				cycles.add(temp);
				if(tempSize != cycles.size()){
					cycleCount++;
					System.out.println(cycleCount + ": " + mapBack(temp, reverseMap));
				}
			}
//			System.out.println(edges);
//			System.out.println(reverseEdges);
		}
//		System.out.println(edges);
//		System.out.println(reverseEdges);
	}

	private static String mapBack(Cycle temp, HashMap<Integer, String> reverseMap) {
		StringBuilder output = new StringBuilder();
		output.append("(");
		boolean start = true;
		for(Integer i:temp.elementsOfCycle){
			if(!start){
				output.append(" ");
			}
			start = false;
			output.append(reverseMap.get(i));
		}
		output.append(")");
		return output.toString();
	}

	private static ArrayList<Cycle> getCyclesConstrainedByDepth(
			ArrayList<Integer> newNodes, ArrayList<HashSet<Edge>> edges, int i) {
		
		ArrayList<ArrayList<Integer>> paths = getAllPathsConstrainedByLengthWrap(newNodes, edges, i);
		ArrayList<Cycle> output = new ArrayList<Cycle>();
		for(ArrayList<Integer> temp: paths){
			Cycle cyc = getCycleIfExists(temp);
			if(cyc != null){
				output.add(cyc);
			}
		}
		return output;
	}

	private static Cycle getCycleIfExists(ArrayList<Integer> temp) {
		ArrayList<Integer> cycForward = new ArrayList<Integer>();
		boolean containsCycle = false;
		for(Integer i:temp){
			if(cycForward.contains(i)){
				cycForward.add(i);
				containsCycle = true;
				break;
			}
			cycForward.add(i);
		}
		if(containsCycle == false){
			return null;
		}
		Collections.reverse(cycForward);
		ArrayList<Integer> cycBackward = new ArrayList<Integer>();
		for(Integer i:cycForward){
			if(cycBackward.contains(i)){
				break;
			}
			cycBackward.add(i);
		}
		return new Cycle(cycBackward);
	}

	private static ArrayList<ArrayList<Integer>> getAllPathsConstrainedByLengthWrap(
			ArrayList<Integer> newNodes, ArrayList<HashSet<Edge>> edges, int i) {
		ArrayList<ArrayList<Integer>> start = new ArrayList<ArrayList<Integer>>();
		for(Integer temp:newNodes){
			start.add(new ArrayList<Integer>());
			start.get(start.size()-1).add(temp);
		}
		return getAllPathsConstrainedByLength(start, edges, i);
	}

	private static ArrayList<ArrayList<Integer>> getAllPathsConstrainedByLength(
			ArrayList<ArrayList<Integer>> start,
			ArrayList<HashSet<Edge>> edges, int i) {
		
		if(i == 1){
			return start;
		}
		ArrayList<ArrayList<Integer>> nextRound = new ArrayList<ArrayList<Integer>>();
		for(ArrayList<Integer> path : start){
			for(Edge e:edges.get(path.get(path.size()-1))){
				ArrayList<Integer> temp = (ArrayList<Integer>) path.clone();
				temp.add(e.end);
				nextRound.add(temp);
			}
		}
	
		return getAllPathsConstrainedByLength(nextRound, edges, i-1);
	}
}
