package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.minlog.Log;
import com.mygdx.game.comp460game;
import com.mygdx.game.manager.AssetList;
import com.mygdx.game.server.Packets;
import com.mygdx.game.states.PlayState;

import box2dLight.RayHandler;
import com.mygdx.game.util.Constants;

import static com.mygdx.game.util.Constants.PPM;

/**
 * A HitboxImage is a hitbox that is represented by a still image sprite.
 * @author Zachary Tu
 *
 */
public class HitboxImage extends Hitbox {
	public static final int ENTITY_TYPE = Constants.EntityTypes.HITBOX_IMAGE;
	protected static TextureAtlas atlas;
	protected Animation<TextureRegion> projectileSprite;
	
	/**
	 * Same as normal hitbox 
	 */
	/*public HitboxImage(PlayState state, float x, float y, int width, int height, float lifespan, int dura, float rest,
						Vector2 startVelo, short filter, boolean sensor, World world, OrthographicCamera camera, RayHandler rays, Schmuck creator,
						String spriteId) {
		super(state, x, y, width / 2, height / 2, lifespan, dura, rest, startVelo, filter, sensor, world, camera, rays, creator);
		atlas = (TextureAtlas) comp460game.assetManager.get(AssetList.PROJ_1_ATL.toString());
		projectileSprite = atlas.findRegion(spriteId);
		if (comp460game.serverMode) {
            comp460game.server.server.sendToAllTCP(new Packets.CreateHitboxImage(x, y, width, height, lifespan, dura,
					rest, startVelo, filter, sensor, creator.entityID.toString(), spriteId, entityID.toString()));
        }
	}*/

	public HitboxImage(PlayState state, float x, float y, int width, int height, float lifespan, int dura, float rest,
					   Vector2 startVelo, short filter, boolean sensor, World world, OrthographicCamera camera, RayHandler rays, Schmuck creator,
					   String spriteId, boolean synced, String id, int playerDataNumber) {
		super(state, x, y, width / 2, height / 2, lifespan, dura, rest, startVelo, filter, sensor, world, camera, rays, creator, synced, id);
		atlas = (TextureAtlas) comp460game.assetManager.get(AssetList.PROJ_1_ATL.toString());

		if (spriteId.equals("boom")) {
			projectileSprite = new Animation<TextureRegion>(0.05f, 
					((TextureAtlas) comp460game.assetManager.get(AssetList.BOOM_1_ATL.toString())).findRegions(spriteId));

		} else {
			projectileSprite = new Animation<TextureRegion>(0.05f, atlas.findRegions(spriteId));

		}
		
		if (comp460game.serverMode) {
		    //Log.info("Sending new hitbox image sync, playerdatanumber = " + playerDataNumber);
            comp460game.server.server.sendToAllTCP(new Packets.CreateHitboxImage(x, y, width, height, lifespan, dura, rest,
                    startVelo, filter, sensor, creator.entityID.toString(), spriteId, entityID.toString(), playerDataNumber));
        }
	}
	
	@Override
	public void controller(float delta) {
		super.controller(delta);
		increaseAnimationTime(delta);
	}
	
	@Override
	public void render(SpriteBatch batch) {
			
		batch.setProjectionMatrix(state.sprite.combined);

		if (projectileSprite != null) {
			batch.draw((TextureRegion)projectileSprite.getKeyFrame(animationTime, true), 
					body.getPosition().x * PPM - width / 2, 
					body.getPosition().y * PPM - height / 2, 
					width / 2, height / 2,
					width, height, 1, 1, 
					(float) Math.toDegrees(body.getAngle()) + 180);
		}
	}



}
