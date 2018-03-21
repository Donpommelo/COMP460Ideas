package com.mygdx.game.actors;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Queue;
import com.mygdx.game.comp460game;
import com.mygdx.game.event.userdata.EventData;
import com.mygdx.game.manager.GameStateManager;

public class DialogueBox extends A460Actor {

	private BitmapFont font;
//	private Color color;
	
	private float scale = 0.5f;

	JsonReader json;
	JsonValue base;
	
	private Queue<Dialogue> dialogues;

	private GameStateManager gsm;
	
	private float durationCount = 0;
	
	public DialogueBox(AssetManager assetManager, GameStateManager stateManager, int x, int y) {
		super(assetManager, x, y);
		
		this.gsm = stateManager;

		json = new JsonReader();
		base = json.parse(Gdx.files.internal("text/Dialogue.json"));
		
		dialogues = new Queue<Dialogue>();
		
		font = comp460game.SYSTEM_FONT_UI;
//		color = HadalGame.DEFAULT_TEXT_COLOR;
		font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		updateHitBox();
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if (durationCount > 0) {
			durationCount -= delta;
			
			if(durationCount <= 0) {
				nextDialogue();
			}
		}
	}
	
	public void addDialogue(String id, EventData radio, EventData trigger) {
		
		JsonValue dialog = base.get(id);
		
		for (JsonValue d : dialog) {
			
			if (dialogues.size == 0) {
				durationCount = d.getFloat("Duration", 0);
			}
			
			dialogues.addLast(new Dialogue(d.getString("Name"), d.getString("Text"), d.getBoolean("End", false),
					d.getFloat("Duration", 0), radio, trigger));
		}		
	}
	
	public void nextDialogue() {
		if (dialogues.size != 0) {
			
			if (dialogues.first().isEnd() && dialogues.first().getTriggered() != null && dialogues.first().getTrigger() != null) {
				dialogues.first().getTriggered().onActivate(dialogues.first().getTrigger());
			}
			
			dialogues.removeFirst();
			
			if (dialogues.size != 0) {
				durationCount = dialogues.first().getDuration();
			}
		}
	}
	
	@Override
    public void draw(Batch batch, float alpha) {
		 font.getData().setScale(scale);
		 font.setColor(Color.WHITE);
		 
		 if (dialogues.size != 0) {
			 gsm.patch.draw(batch, getX(), getY() - 200, 1000, 200);
	         font.draw(batch, dialogues.first().getName() +": " + dialogues.first().getText(), getX() + 20, getY() - 20);
		 }
		 
         //Return scale and color to default values.
         font.getData().setScale(1.0f);
         font.setColor(comp460game.DEFAULT_TEXT_COLOR);
    }	
}