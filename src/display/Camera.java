package display;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Camera {

	public float x;
	public float y;
	public float z;
	public float rx;
	public float ry;
	
	public void update() {
		while(Keyboard.next()) {
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
				if(!Keyboard.getEventKeyState()) {
					Mouse.setGrabbed(!Mouse.isGrabbed());
					Mouse.setCursorPosition(Display.getWidth()/2, Display.getHeight()/2);
				}
			}
		}
		
		int x = 0;
		int y = 0;
		int z = 0;
		if(Mouse.isGrabbed()) {
			if(Keyboard.isKeyDown(Keyboard.KEY_W)) z = -1;
			if(Keyboard.isKeyDown(Keyboard.KEY_S)) z = 1;
			if(Keyboard.isKeyDown(Keyboard.KEY_A)) x = -1;
			if(Keyboard.isKeyDown(Keyboard.KEY_D)) x = 1;
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) y = 1;
			if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) y = -1;
			
			ry += Mouse.getDX()/4;
			rx -= Mouse.getDY()/4;
			Mouse.setCursorPosition(Display.getWidth()/2, Display.getHeight()/2);
			
			if (ry > 360) ry -= 360;
			else if (ry < 0) ry += 360;
			if (rx < -90) rx = -90;
			else if (rx > 90) rx = 90;
		}
		
		this.x += 0.25f*(Math.cos(Math.toRadians(ry)) * x - Math.sin(Math.toRadians(ry)) * z);
		this.y += 0.25f*y;
		this.z += 0.25f*(Math.cos(Math.toRadians(ry)) * z + Math.sin(Math.toRadians(ry)) * x);
	}
}
