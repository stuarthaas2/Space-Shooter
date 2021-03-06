package com.spaceshooter.core;

import com.spaceshooter.math.Mathf;
import com.spaceshooter.math.Vector;

public class Camera {

	public static int X, Y, OFFSET_X, OFFSET_Y;
	
	static Camera instance;
	
	public static Camera getInstance(float x, float y, float scrollSpeed) {
		if(instance == null) {
			instance = new Camera(x, y, scrollSpeed);
			return instance;
		}
		return instance;
		
	}
	
	public Vector position = new Vector();
	
	float scrollSpeed;

	private Camera(float x, float y, float scrollSpeed) {
		position.set(x, y);
		this.scrollSpeed = scrollSpeed;
	}
	
	public void update(Vector target) {
		
		OFFSET_X = (int) (target.getX() - Camera.X);
		OFFSET_Y = (int) (target.getY() - Camera.Y);

		position.setX(position.getX() + ((target.getX() - Game.CANVAS_WIDTH * 0.5) - position.getX()) * scrollSpeed);
		position.setY(position.getY() + ((target.getY() - Game.CANVAS_HEIGHT * 0.5) - position.getY()) * scrollSpeed);
		
		position.setX(Mathf.clamp(position.getX(), Game.GRID_X, Game.GRID_WIDTH - Game.WINDOW_WIDTH));
		position.setY(Mathf.clamp(position.getY(), Game.GRID_Y, ((Game.GRID_HEIGHT - Game.WINDOW_HEIGHT) + (Game.WINDOW_HEIGHT - Game.CANVAS_HEIGHT))));
		
		X = (int) position.getX();
		Y = (int) position.getY();
	}
	
	public static boolean inViewPort(Vector position) {
		if(
			position.getX() < Camera.OFFSET_X - Game.CANVAS_WIDTH * 0.5 ||
			position.getX() > Camera.X + Game.CANVAS_WIDTH ||
			position.getY() < Camera.OFFSET_Y - Game.CANVAS_HEIGHT * 0.5 ||
			position.getY() > Camera.Y + Game.CANVAS_HEIGHT
		) {
			return false;
		}
		return true;	
	}
}
