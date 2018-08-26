package generate;

public class World {

	private byte[][][][] array;
	
	public World(int size) {
		array = new byte[size][size][size][4];
	}
	
	public void addCube(int x, int y, int z, int r, int g, int b, int a) {
		array[x][y][z][0] = (byte)r;
		array[x][y][z][1] = (byte)g;
		array[x][y][z][2] = (byte)b;
		array[x][y][z][3] = (byte)a;
	}
	
	public byte[][][][] getWorld() {
		return array;
	}
	
	public int getWorldSize() {
		return array.length;
	}
}
