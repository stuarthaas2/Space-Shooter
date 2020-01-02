package com.spaceshooter.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.spaceshooter.sprite.Animation;
import com.spaceshooter.utils.Assets;
import com.spaceshooter.utils.ID;

public class Bullet extends Entity{

	Animation animation;
	
	public Bullet(int x, int y, int width, int height, ID id){
		super(x, y, width, height, id);
		
		texture.loadImage(Assets.BULLET, width, height);
		animation = new Animation(8, true, texture.imageArray);
	}
	public void update() {
		animation.runAnimation();
		
		position = position.add(velocity);
	}
	public void render(Graphics g) {
		animation.drawAnimation(g, (int) position.getX(), (int) position.getY(), 8, 8);
	}
	public void drawBounds(Graphics g){
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(Color.white);
		
		g2d.draw(getEllipseBounds());
	}
}
