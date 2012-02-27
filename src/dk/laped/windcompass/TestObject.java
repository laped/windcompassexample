package dk.laped.windcompass;

public class TestObject {

	public TestObject(String name, int windDirections, double currentWindDirection, boolean isPerfectWindDirection) {
		this.name = name;
		this.windDirections = windDirections;
		this.currentWindDirection = currentWindDirection;
		this.isPerfectWindDirection = isPerfectWindDirection;
	}
	
	public String name;
	public Integer windDirections;
	public Boolean isPerfectWindDirection;
	public double currentWindDirection;

}
