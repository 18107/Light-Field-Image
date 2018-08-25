import java.awt.image.BufferedImage;

public class Renderer {

	public BufferedImage generate(World world, float centreX, float centreY, float centreZ,
			float radius, int yResolution, int angleResolution) {
		BufferedImage image = new BufferedImage(yResolution*2/*angleResolution*/,
				yResolution/*angleResolution*/, BufferedImage.TYPE_INT_RGB);
		
		for (int yPix = 0; yPix < yResolution; yPix++) {
			double yRadians = yPix/(float)(yResolution-1)*Math.PI-Math.PI/2;
			float yPos = centreY + radius*(float)(Math.sin(yRadians));
			for (int xPix = 0; xPix < yResolution*2; xPix++) {
				double xRadians = xPix/(float)(yResolution*2)*2*Math.PI-Math.PI;
				float xPos = centreX + radius*(float)(Math.sin(xRadians)*Math.cos(yRadians));
				float zPos = centreZ + radius*(float)(Math.cos(xRadians)*Math.cos(yRadians));
				/*for (int yAng = 0; yAng < angleResolution; yAng++) {
					for (int xAng = 0; xAng < angleResolution; xAng++) {
						
					}
				}*/
				byte[] pixel = trace(world.getWorld(), world.getWorldSize(), xPos, yPos, zPos, (float)xRadians, (float)yRadians);
				image.setRGB(xPix, yResolution-yPix-1, (pixel[0]&255)<<16 | (pixel[1]&255)<<8 | (pixel[2]&255));
			}
		}
		
		return image;
	}
	
	private static byte[] xOut = new byte[] {127,127,127,-1};
	private static byte[] yOut = new byte[] {-1,-1,-1,-1};
	private static byte[] zOut = new byte[] {-64,-64,-64,-1};
	
	private byte[] trace(byte[][][][] world, int worldSize, float xPos, float yPos, float zPos,
			float xRadians, float yRadians) {
		int xCube = (int)Math.floor(xPos);
		int yCube = (int)Math.floor(yPos);
		int zCube = (int)Math.floor(zPos);
		
		if (xCube >= worldSize || xCube < 0) return xOut;
		if (yCube >= worldSize || yCube < 0) return yOut;
		if (zCube >= worldSize || zCube < 0) return zOut;
		
		if (world[xCube][yCube][zCube][3] != 0) {
			return world[xCube][yCube][zCube];
		}
		
		float xDir = (float)(Math.sin(xRadians)*Math.cos(yRadians));
		float yDir = (float)(Math.sin(yRadians));
		float zDir = (float)(Math.cos(xRadians)*Math.cos(yRadians));
		
		float xDist = (float)((xDir > 0 ? Math.ceil(xPos) : Math.floor(xPos)) -xPos)/xDir;
		float yDist = (float)((yDir > 0 ? Math.ceil(yPos) : Math.floor(yPos)) -yPos)/yDir;
		float zDist = (float)((zDir > 0 ? Math.ceil(zPos) : Math.floor(zPos)) -zPos)/zDir;
		
		float xInc = Math.abs(1/xDir);
		float yInc = Math.abs(1/yDir);
		float zInc = Math.abs(1/zDir);
		
		int xIinc = xDir > 0 ? 1 : -1;
		int yIinc = yDir > 0 ? 1 : -1;
		int zIinc = zDir > 0 ? 1 : -1;
		
		while (true) {
			if (xDist < yDist) {
				if (xDist < zDist) {
					xDist += xInc;
					xCube += xIinc;
					if (xCube >= worldSize || xCube < 0) return xOut;
				} else {
					zDist += zInc;
					zCube += zIinc;
					if (zCube >= worldSize || zCube < 0) return zOut;
				}
			} else {
				if (yDist < zDist) {
					yDist += yInc;
					yCube += yIinc;
					if (yCube >= worldSize || yCube < 0) return yOut;
				} else {
					zDist += zInc;
					zCube += zIinc;
					if (zCube >= worldSize || zCube < 0) return zOut;
				}
			}
			
			if (world[xCube][yCube][zCube][3] != 0) {
				return world[xCube][yCube][zCube];
			}
		}
	}
}
