package display;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Renderer {

	private static final int subImageSize = 80;
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
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height,
				0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, image);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public void draw(Camera camera, int program) {
		GL20.glUseProgram(program);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		int subImageSizeUniform = GL20.glGetUniformLocation(program, "subImageSize");
		GL20.glUniform1i(subImageSizeUniform, subImageSize);
		FloatBuffer fb = BufferUtils.createFloatBuffer(3);
		fb.put(camera.x/radius);
		fb.put(camera.y/radius);
		fb.put(camera.z/radius);
		fb.flip();
		int positionUniform = GL20.glGetUniformLocation(program, "position");
		GL20.glUniform3(positionUniform, fb);
		fb = BufferUtils.createFloatBuffer(2);
		fb.put(camera.rx);
		fb.put(camera.ry);
		fb.flip();
		int rotationUniform = GL20.glGetUniformLocation(program, "rotation");
		GL20.glUniform2(rotationUniform, fb);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(-1, -1);
		GL11.glVertex2f(1, -1);
		GL11.glVertex2f(1, 1);
		GL11.glVertex2f(-1, 1);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL20.glUseProgram(0);
	}
	
	public void end() {
		GL11.glDeleteTextures(texId);
		texId = 0;
	}
}
