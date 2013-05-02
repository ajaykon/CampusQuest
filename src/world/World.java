package world;
/*
 * World contains information about different grid locations in the world
 * 
 * Contains a list of locations and methods to access them.
 */

public class World {
	
	public static final int mapDimension = 3;
	
	double northwestCornerLatitude;
	double northwestCornerLongitude;
	
	// Busch campus: 40.527305, -74.470997
	
	double southeastCornerLatitude;
	double southeastCornerLongitude;
	
	// Busch campus: 40.518106, -74.459925
	
	public Location [][] grid;
	
	public World(	double northwestCornerLatitude,
					double northwestCornerLongitude,
					double southeastCornerLatitude,
					double southeastCornerLongitude){
		this.northwestCornerLatitude = northwestCornerLatitude;
		this.northwestCornerLongitude = northwestCornerLongitude;
		this.southeastCornerLatitude = southeastCornerLatitude;
		this.southeastCornerLongitude = southeastCornerLongitude;
		
		Location [][] grid = new Location[mapDimension][mapDimension];
		
		for (int i = 0; i < mapDimension; i++){
			for (int j = 0; j < mapDimension; j++){
				grid[i][j] = new Location(i,j);
			}
		}
		
		this.grid = grid;
	}
	
	public Location getCoordinateLocation(double latitude, double longitude){
		double latitudeDiffRef = southeastCornerLatitude - northwestCornerLatitude;
		double longitudeDiffRef = southeastCornerLongitude - northwestCornerLongitude;
				
		double latitudeDiff = latitude - northwestCornerLatitude;
		double longitudeDiff = longitude - northwestCornerLongitude;
		
		double latFactor = (latitudeDiff/latitudeDiffRef) * mapDimension;
		double longFactor = (longitudeDiff/longitudeDiffRef) * mapDimension;
		
		System.out.println(latFactor);
		System.out.println(longFactor);
		
		int row = (int)Math.floor(latFactor);
		int column = (int)Math.floor(longFactor);
		
		return this.grid[row][column];
	}
	
	public boolean validateLocation(double latitude, double longitude){
		double latitudeDiffRef = southeastCornerLatitude - northwestCornerLatitude;
		double longitudeDiffRef = southeastCornerLongitude - northwestCornerLongitude;
		
		double latitudeDiff = latitude - northwestCornerLatitude;
		double longitudeDiff = longitude - northwestCornerLatitude;
		
		double latFactor = (latitudeDiff/latitudeDiffRef) * mapDimension;
		double longFactor = (longitudeDiff/longitudeDiffRef) * mapDimension;

		if ((latFactor>mapDimension) ||(latFactor<0.0)) return false;
		if ((longFactor>mapDimension) ||(longFactor<0.0)) return false;
		
		return true;
	}
}
