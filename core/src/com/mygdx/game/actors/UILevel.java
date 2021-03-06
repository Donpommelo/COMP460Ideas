package com.mygdx.game.actors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mygdx.game.comp460game;
import com.mygdx.game.actors.UITag.uiType;
import com.mygdx.game.states.PlayState;

public class UILevel extends A460Actor{

	private PlayState state;
	private BitmapFont font;
	
	private final static int x = 200;
	private final static int y = 10;
	
	private ArrayList<UITag> uiTags;
	
	private int score;
	private float timer;
	
	public UILevel(AssetManager assetManager, PlayState state) {
		super(assetManager);
		this.state = state;
		this.font = comp460game.SYSTEM_FONT_UI;
		
		uiTags = new ArrayList<UITag>();
		uiTags.add(new UITag(uiType.SCORE, "", 0.4f));
	}
	
	@Override
    public void draw(Batch batch, float alpha) {
		batch.setProjectionMatrix(state.hud.combined);

		font.getData().setScale(0.4f);
		
		String text = "";
		
		for (UITag tag : uiTags) {
			switch(tag.getType()) {
			case SCORE:
				text = text.concat("SCORE: " + score + "\n");
				break;
			case TIMER:
				text = text.concat("TIMER: " + timer + " S\n");
				break;
			case MISC:
				text = text.concat(tag.getMisc() +"\n");
				break;
			default:
				break;
			}	
		}
		font.draw(batch, text, comp460game.CONFIG_WIDTH - x, comp460game.CONFIG_HEIGHT - y);
	}
	
	public void changeTypes(int changeType, uiType... types) {
		
		ArrayList<UITag> tags = new ArrayList<UITag>();
		
		for (uiType type : types) {
			tags.add(new UITag(type));
		}
		
		changeTypes(changeType, tags);
	}
	
	public void changeTypes(int changeType, UITag... tags) {
		changeTypes(changeType, Arrays.asList(tags));
	}
	
	public void changeTypes(int changeType, List<UITag> tags) {
		
		if (changeType == 0) {
			uiTags.clear();
		}
		
		for (UITag tag : tags) {
			switch(changeType) {
			case -1:
				uiTags.remove(tag);
				break;
			case 0:
			case 1:
				uiTags.add(tag);
				break;
			}
		}
	}

	public int getScore() {
		return score;
	}
	
	public void incrementScore(int i) {
		score += i;
	}
	
	public float getTimer() {
		return timer;
	}

	public void incrementTimer(float timer) {
		this.timer += timer;
	}
}
