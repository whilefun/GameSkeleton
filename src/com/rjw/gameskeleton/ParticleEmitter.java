package com.rjw.gameskeleton;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class ParticleEmitter {

	private Particle[] _particles;
	private int _originX;
	private int _originY;
	private int _radius;
	private Graphics emitterGraphics;
	private Timer _updateTimer;
	
	public ParticleEmitter(int numberOfParticles, int originX, int originY, int radius){
		
		_particles = new Particle[numberOfParticles];
		
		_originX = originX;
		_originY = originY;
		_radius = radius;
		
		_updateTimer = new Timer(30);
		
		for(int particleCount = 0; particleCount < _particles.length; particleCount++){
			//_particles[particleCount] = new Particle(_originX, _originY, (int)(Math.random()*10), (int)(Math.random()*10), 2, null, Color.blue, 0);
			_particles[particleCount] = new Particle(
					_originX+(_radius/2),
					_originY+(_radius/2),
					(Math.random() <= 0.5 ? 1+particleCount: -(1+particleCount)),
					(Math.random() <= 0.5 ? 1+particleCount: -(1+particleCount)),
					//0,
					//(Math.random() <= 0.5 ? 1+particleCount/2: 1+particleCount/3),
					2,
					new Sprite("sprite_smoke_particle_test.png"),
					Color.blue,
					0);
		}
		
		//(Math.random() <= 0.5? 1+particleCount: -(1+particleCount))
		
	}//constructor
	
	public void updateParticles(long elapsedTime){
		
		_updateTimer.update(elapsedTime);
		
		if(_updateTimer.isDone()){
		
			for(int particleCount = 0; particleCount < _particles.length; particleCount++){
			
				
				// if particles are outside their radius of movement, reset them
				if( (_particles[particleCount].getX() > (_originX + _radius)) || (_particles[particleCount].getY() > (_originY + _radius)) ){
				
					//GameSkeleton.printDebugMessage("ParticleEmitter: Particle outside radius, resetting");
					_particles[particleCount].setX(_originX+(_radius/2));
					_particles[particleCount].setY(_originY+(_radius/2));
					
				}else{
				
			
					//GameSkeleton.printDebugMessage("moving particle " + particleCount);
					_particles[particleCount].moveParticleBy(_particles[particleCount].getXVel(), _particles[particleCount].getYVel());
					//_particles[particleCount].setX(_particles[particleCount].getXVel());
					//_particles[particleCount].setY(_particles[particleCount].getYVel());
					
				}//else
				
			}//for
			
			_updateTimer.reset();
			
		}//if

	}//updateParticles
	
	public BufferedImage drawParticles(){
		
		BufferedImage emitterImage = new BufferedImage(_radius*2, _radius*2, BufferedImage.TYPE_INT_ARGB);
		emitterGraphics = emitterImage.getGraphics();
		
		emitterGraphics.setColor(Color.YELLOW);
		emitterGraphics.drawOval(_originX, _originY, _radius, _radius);
		emitterGraphics.drawRect(_originX, _originY, _radius-1, _radius-1);
		
		
		for(int particleCount = 0; particleCount < _particles.length; particleCount++){
			
			//check character bounds, since we only support the first 128 ASCII chars

			
			//emitterGraphics.setColor(_particles[particleCount].getColor());
			//emitterGraphics.drawRect(_particles[particleCount].getX(), _particles[particleCount].getY(), 2, 2);
			
			// test sprite drawing
			emitterGraphics.drawImage(_particles[particleCount].getSprite().getImage(), _particles[particleCount].getX(), _particles[particleCount].getY(), null);
				
		}
		
		//emitterGraphics.set
		
		return emitterImage;

	}//drawParticles

	public int getOriginX() {
		return _originX;
	}

	public void setOriginX(int originX) {
		_originX = originX;
	}

	public int getOriginY() {
		return _originY;
	}

	public void setOriginY(int originY) {
		_originY = originY;
	}
	
}//ParticleEmitter
