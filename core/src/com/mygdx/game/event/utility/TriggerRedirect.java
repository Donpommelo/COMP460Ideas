package com.mygdx.game.event.utility;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.event.Event;
import com.mygdx.game.event.userdata.EventData;
import com.mygdx.game.states.PlayState;
import com.mygdx.game.util.Constants;
import com.mygdx.game.util.b2d.BodyBuilder;

import box2dLight.RayHandler;

/**
 * A Redirecttrigger is an event that can trigger another event while saying that another event did it.
 * 
 * @author Zachary Tu
 *
 */
public class TriggerRedirect extends Event {

	private static final String name = "RedirectTrigger";

	private Event blame ;
	
	public TriggerRedirect(PlayState state, World world, OrthographicCamera camera, RayHandler rays, int width, int height,
			int x, int y) {
		super(state, world, camera, rays, name, width, height, x, y, false);
	}
	
	@Override
	public void create() {
		this.eventData = new EventData(world, this) {
			
			@Override
			public void onActivate(EventData activator) {
				if (event.getConnectedEvent() != null && blame != null) {
					event.getConnectedEvent().eventData.onActivate(blame.eventData);
				}
			}
		};
		
		this.body = BodyBuilder.createBox(world, startX, startY, width, height, 1, 1, 0, true, true, Constants.Filters.BIT_SENSOR, 
				(short) 0, (short) 0, true, eventData);
	}
	
	public void setBlame(Event e) {
		blame = e;
	}
}
