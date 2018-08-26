package display;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

public class Shaders {

	private int program;
	private int vertexShader;
	private int fragmentShader;
	
	public Shaders(String vertexPath, String fragmentPath) throws IOException {
		vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		
		byte[] bytes = Files.readAllBytes(Paths.get(vertexPath));
		ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length);
		buffer.put(bytes);
		buffer.flip();
		GL20.glShaderSource(vertexShader, buffer);
		
		bytes = Files.readAllBytes(Paths.get(fragmentPath));
		buffer = BufferUtils.createByteBuffer(bytes.length);
		buffer.put(bytes);
		buffer.flip();
		GL20.glShaderSource(fragmentShader, buffer);
		
		GL20.glCompileShader(vertexShader);
		GL20.glCompileShader(fragmentShader);
		
		program = GL20.glCreateProgram();
		
		GL20.glAttachShader(program, vertexShader);
		GL20.glAttachShader(program, fragmentShader);
		GL20.glLinkProgram(program);
	}
	
	public void end() {
		GL20.glDetachShader(program, fragmentShader);
		GL20.glDetachShader(program, vertexShader);
		GL20.glDeleteProgram(program);
		GL20.glDeleteShader(fragmentShader);
		GL20.glDeleteShader(vertexShader);
	}
	
	public int getProgram() {
		return program;
	}
}
