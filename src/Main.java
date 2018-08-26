import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import display.Camera;
import display.Shaders;
import generate.World;

public class Main {
	
	public static void display() throws IOException {
		try {
			Display.setDisplayMode(new DisplayMode(800, 400));
			Display.setTitle("Light Field");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			return;
		}
		
		BufferedImage image = ImageIO.read(new File("image4.png"));
		byte[] pixels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
		if (pixels.length != image.getHeight()*image.getWidth()*3) {
			throw new RuntimeException("unexpected image size: got " + pixels.length +
					", expected " + (image.getHeight()*image.getWidth()*3));
		}
		ByteBuffer bBuffer = BufferUtils.createByteBuffer(pixels.length);
		bBuffer.put(pixels);
		bBuffer.flip();
		
		Camera camera = new Camera();
		display.Renderer renderer = new display.Renderer();
		renderer.init(bBuffer, image.getWidth(), image.getHeight());
		Shaders shaders = new Shaders("Vertex.vs", "Fragment.fs");
		int program = shaders.getProgram();
		
		while (!Display.isCloseRequested()) {
			camera.update();
			renderer.draw(camera, program);
			Display.update();
			Display.sync(60);
		}
		
		renderer.end();
		Display.destroy();
	}
	
	public static void generate() throws IOException {
		World world = new World(16);
		world.addCube(7, 7, 12, 0, 0, -1, -1);
		world.addCube(8, 7, 12, -1, 0, 0, -1);
		world.addCube(8, 8, 12, -1, 0, -1, -1);
		world.addCube(2, 6, 7, 0, -1, 0, -1);
		world.addCube(13, 8, 7, 0, -1, 0, -1);
		world.addCube(7, 0, 0, -1, 0, 0, -1);
		
		generate.Renderer renderer = new generate.Renderer();
		BufferedImage image = renderer.generate(world, 7.5f, 7.5f, 7.5f, 1, 80, 80);
		ImageIO.write(image, "png", new File("image.png"));
		System.out.println("done");
	}

	public static void main(String[] args) throws IOException {
		display();
	}
}
