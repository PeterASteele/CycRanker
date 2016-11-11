public class Edge {
	int end;
	double dist;
	
	public Edge(int end, double dist) {
		this.end = end;
		this.dist = dist;
	}
	public String toString(){
		return end+"";
	}
	@Override
	public int hashCode(){
		return end;
	}
	@Override
	public boolean equals(Object o){
		Edge e = (Edge) o;
		return end == e.end;
	}
}