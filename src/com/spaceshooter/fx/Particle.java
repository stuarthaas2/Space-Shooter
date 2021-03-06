package com.spaceshooter.fx;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Random;

import com.spaceshooter.entity.Entity;
import com.spaceshooter.math.Vector;

public class Particle extends Entity {
	
	double size, maxSize, growth, direction, life, lifeRate;
	float alpha;
	Color currentColor;
	Color[] colors;
	Random r = new Random();

	public Particle(int x, int y, double angle, double speed, double friction,
			double size, double maxSize, double growth, double life,
			double lifeRate, float alpha, Color[] colors){
		super(x, y, 0, 0, null, null);

		this.position.set(x, y);
		this.size = size;
		this.maxSize = maxSize;
		this.life = life;
		this.alpha = alpha;
		this.colors = colors;
		this.currentColor = colors[r.nextInt(colors.length - 1)];

		this.friction = friction;
		this.growth = r.nextDouble() * growth;
		this.lifeRate = r.nextDouble() * lifeRate;
		this.direction = r.nextDouble() * getSpreadAngle(angle);
		double vx = Math.cos(direction) * speed;
		double vy = Math.sin(direction) * speed;
		velocity = new Vector(vx, vy);
	}
	
	public double getSpreadAngle(double angle) {
		if(angle == 0)
			return r.nextDouble() * 5 + 5;
		return angle;
	}
	
	public void update() {

		if (alpha > life) alpha -= (life - lifeRate);
		else expired = true;

		size += growth;
		if (size >= maxSize) size = maxSize;
		if (size <= 0) size = 0;

		velocity = velocity.multiply(friction);
		position = position.add(velocity);
	}
	
	public void render(Graphics2D g) {
		g.setComposite(makeTransparent(alpha));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(currentColor);
		g.fillOval((int) position.getX() - (int) (size / 2), (int) position.getY()
				- (int) (size / 2), (int) size, (int) size);
		g.setComposite(makeTransparent(1));
		g.dispose();
	}
	
	private AlphaComposite makeTransparent(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type, alpha));
	}
}
