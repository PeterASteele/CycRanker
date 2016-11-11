import java.util.ArrayList;


public class Cycle {
	ArrayList<Integer> elementsOfCycle = new ArrayList<Integer>();
	public Cycle(ArrayList<Integer> cycle){
		//Take a cycle, and rotate it to the earliest point numerically. For instance, the cycle (2 3 1) would rotate to be (1 2 3).
		//This rotation takes O(n) time, where n is the length of the cycle.
		
		int minElement = Integer.MAX_VALUE;
		int minIdx = -1;
		for(int a = 0; a < cycle.size(); a++){
			if(cycle.get(a) < minElement){
				minElement = cycle.get(a);
				minIdx = a;
			}
		}
		//apply rotation
		elementsOfCycle = new ArrayList<Integer>();
		for(int a = minIdx; a < minIdx + cycle.size(); a++){
			elementsOfCycle.add(cycle.get(a%cycle.size()));
		}
		
	}
	public String toString(){
		return elementsOfCycle.toString();
	}
	
	@Override
	public boolean equals(Object o){
		Cycle temp = (Cycle) o;
		return temp.elementsOfCycle.equals(elementsOfCycle);
	}
	
	@Override
	public int hashCode(){
		return elementsOfCycle.hashCode();
	}
}
