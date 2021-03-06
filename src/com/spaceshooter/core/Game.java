package com.spaceshooter.core;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import com.spaceshooter.input.KeyInput;
import com.spaceshooter.input.MouseInput;
import com.spaceshooter.map.Id;
import com.spaceshooter.map.Layer;
import com.spaceshooter.map.Grid;
import com.spaceshooter.utils.Profiler;

public class Game extends Canvas implements Runnable{

	public static int CANVAS_WIDTH, CANVAS_HEIGHT;
	public static int IMAGE_WIDTH, IMAGE_HEIGHT;
	public static int GRID_WIDTH, GRID_HEIGHT;
	public static int GRID_X, GRID_Y;
	public static int FPS = 0;
	public static int WINDOW_WIDTH = 800, WINDOW_HEIGHT = WINDOW_WIDTH / 12 * 9;
	private static final long serialVersionUID = 1L;

	private static String GAME_TITLE = "Space Shooter";
	
	public static void main(String[] args) {
		new Window(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE, new Game());
	}
	
	Thread thread;
	BufferStrategy bs;
	Graphics g;
	Camera camera;
	EntityManager entityManager;
	Grid grid;
	Profiler profiler;
	int fps = 60;
	long interval = 1000 / fps;
	long currentTime = 0;
	long lastTime = System.nanoTime();
	long delta = 0;
	boolean running = false;

	public Game() {}
	
	public synchronized void start() {

		if (running) return;
		create();
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop() {

		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {

		currentTime = System.nanoTime();
		delta = (currentTime - lastTime) / interval;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (delta > interval && running){
			
			update();
			render();
			
			frames++;

			if (System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				FPS = frames;
				frames = 0;
			}
			
			lastTime = currentTime - (delta % interval);
		}
	}
	
	private void create() {
		
		this.requestFocus();
		this.addKeyListener(KeyInput.getInstance());
		this.addMouseListener(MouseInput.getInstance());

		CANVAS_WIDTH = getWidth();
		CANVAS_HEIGHT = getHeight();

		entityManager = EntityManager.getInstance();
		camera = Camera.getInstance(0, 0, 0.05f);
	
		grid = Grid.getInstance();
		
		
		grid.load("/levels/Tilemap_Walkable Layer.csv");
		grid.addNodes(grid.getGrid(), "/sprite_sheets/tallgrass.png", Id.PathNode, Layer.Path);
		
		grid.load("/levels/Tilemap_Collision Layer.csv");
		grid.addNodes(grid.getGrid(), "/sprite_sheets/fence.png", Id.CollisionNode, Layer.Collision);
		
		entityManager.addEntity(EntityFactory.playerInstance(256, 256, 32, 32, Id.Player, Layer.Player));

        entityManager.addEntity(EntityFactory.seekerInstance(512, 512, 32, 32, Id.Seeker, Layer.Enemy));
		
		profiler = new Profiler(650, 0, Id.Player);
	}
	
	private void update() {
		camera.update(entityManager.getEntityById(Id.Player).position);
	}
	
	private void render() {

		bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		Graphics2D context = (Graphics2D) g;

		context.setColor(Color.black);
		context.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

		context.translate(-camera.position.getX(), -camera.position.getY());

		entityManager.render(context);

		context.translate(camera.position.getX(), camera.position.getY());

		profiler.render(g);

	    context.dispose();
		bs.show();
	}
}
