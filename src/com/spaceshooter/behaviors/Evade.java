package com.spaceshooter.behaviors;

import com.spaceshooter.entity.Entity;
import com.spaceshooter.math.Vector;
import com.spaceshooter.utils.ID;

public class Evade extends ABehavior{

	public Evade(Entity target, double evadeSpeed, double fleeThreshold) {
		super(target, ID.Evade);
		this.evadeSpeed = evadeSpeed;
		this.fleeThreshold = fleeThreshold;
	}

	@Override
	public Vector calculate(Entity object) {
		return Evade.calculate(object, target, evadeSpeed, fleeThreshold);
	}
	
	public static Vector calculate(Entity object, Entity target, double evadeSpeed, double fleeThreshold) {
		double lookAheadTime = object.position.dist(target.position) / evadeSpeed;
		Vector predictedTarget = target.position.add(target.velocity.multiply(lookAheadTime));
		return Flee.calculate(object, predictedTarget, fleeThreshold);
	}
}
