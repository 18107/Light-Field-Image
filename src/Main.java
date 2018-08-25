import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Main {

	public static void main(String[] args) throws IOException {
		World world = new World(16);
		world.addCube(7, 7, 12, 0, 0, -1, -1);
		world.addCube(8, 7, 12, -1, 0, 0, -1);
		world.addCube(8, 8, 12, -1, 0, -1, -1);
		world.addCube(2, 6, 7, 0, -1, 0, -1);
		world.addCube(13, 8, 7, 0, -1, 0, -1);
		world.addCube(7, 0, 0, -1, 0, 0, -1);
		
		Renderer renderer = new Renderer();
		BufferedImage image = renderer.generate(world, 7.5f, 7.5f, 7.5f, 1, 80, 80);
		ImageIO.write(image, "png", new File("image.png"));
		System.out.println("done");
	}
}
