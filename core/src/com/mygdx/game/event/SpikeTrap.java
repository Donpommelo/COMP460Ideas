package com.mygdx.game.event;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.comp460game;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.Schmuck;
import com.mygdx.game.entities.userdata.CharacterData;
import com.mygdx.game.event.userdata.EventData;
import com.mygdx.game.manager.AssetList;
import com.mygdx.game.server.Packets;
import com.mygdx.game.states.PlayState;
import com.mygdx.game.util.Constants;
import com.mygdx.game.util.b2d.BodyBuilder;

import box2dLight.RayHandler;

public class SpikeTrap extends Event {

	private float dps;
	private CharacterData perp;
	private boolean isUp = false;

	private static final String name = "Spike Trap";

	public SpikeTrap(PlayState state, World world, OrthographicCamera camera, RayHandler rays, int width, int height, 
			int x, int y, float dps, boolean synced) {
		super(state, world, camera, rays, name, width, height, x, y, synced);
		this.dps = dps;
		this.perp = state.worldDummy.getBodyData();
		eventSprite = new TextureRegion(new Texture(AssetList.SPIKE_DOWN.toString()));
		if (comp460game.serverMode) {
			comp460game.server.server.sendToAllTCP(new Packets.CreateSpikeTrapMessage(x, y, width, height, dps, entityID.toString()));
		}
	}

	public SpikeTrap(PlayState state, World world, OrthographicCamera camera, RayHandler rays, int width, int height,
					 int x, int y, float dps, boolean synced, String entityID) {
		super(state, world, camera, rays, name, width, height, x, y, synced, entityID);
		this.dps = dps;
		this.perp = state.worldDummy.getBodyData();
		eventSprite = new TextureRegion(new Texture(AssetList.SPIKE_DOWN.toString()));
	}
	
	public void create() {

		this.eventData = new EventData(world, this) {
			@Override
			public void onActivate(EventData activator) {
				for (Entity entity : eventData.schmucks) {
					if (entity instanceof Schmuck) {
						((Schmuck)entity).getBodyData().receiveDamage(dps, new Vector2(0, 0), perp, true);
						if (isUp) {
							eventSprite = new TextureRegion(new Texture(AssetList.SPIKE_DOWN.toString()));
							System.out.println("spike down");
						} else {
							eventSprite = new TextureRegion(new Texture(AssetList.SPIKE_UP.toString()));
							System.out.println("spike up");
						}
						isUp = !isUp;
					}
				}
			}
		};
		
		this.body = BodyBuilder.createBox(world, startX, startY, width, height, 1, 1, 0, true, true, Constants.Filters.BIT_SENSOR, 
				(short) (Constants.Filters.BIT_PLAYER | Constants.Filters.BIT_ENEMY),
				(short) 0, true, eventData);
	}
}
