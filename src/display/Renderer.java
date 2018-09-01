package display;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;

public class Renderer {

	private static final int subImageSize = 50;
	private static int width;
	private static int height;
	private static final float radius = 4;
	
	private int texId;
	
	public void init(ByteBuffer image, int width, int height) {
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(-1, 1, -1, 1, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		texId = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height,
				0, GL12.GL_BGR, GL11.GL_UNSIGNED_BYTE, image);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		Renderer.width = width;
		Renderer.height = height;
	}
	
	public void draw(Camera camera, int program) {
		GL20.glUseProgram(program);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		int subImageSizeUniform = GL20.glGetUniformLocation(program, "subImageSize");
		GL20.glUniform1i(subImageSizeUniform, subImageSize);
		
		int imageSizeUniform = GL20.glGetUniformLocation(program, "imageSize");
		GL20.glUniform2i(imageSizeUniform, width/subImageSize, height/subImageSize);
		
		int positionUniform = GL20.glGetUniformLocation(program, "position");
		GL20.glUniform3f(positionUniform, camera.x/radius, camera.y/radius, camera.z/radius);
		
		float r = (float)Math.sqrt(camera.x*camera.x + camera.y*camera.y + camera.z*camera.z);
		int positionPolarUniform = GL20.glGetUniformLocation(program, "positionPolar");
		GL20.glUniform3f(positionPolarUniform,
				(float)Math.atan2(camera.x, -camera.z), (float)Math.asin(camera.y/r), r);
		
		int rotationUniform = GL20.glGetUniformLocation(program, "rotation");
		GL20.glUniform2f(rotationUniform, -camera.rx, camera.ry);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 0); GL11.glVertex2f(-1, -1);
		GL11.glTexCoord2f(1, 0); GL11.glVertex2f(1, -1);
		GL11.glTexCoord2f(1, 1); GL11.glVertex2f(1, 1);
		GL11.glTexCoord2f(0, 1); GL11.glVertex2f(-1, 1);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL20.glUseProgram(0);
	}
	
	public void end() {
		GL11.glDeleteTextures(texId);
		texId = 0;
	}
}
