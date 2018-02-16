package com.mygdx.game.states;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.client.KryoClient;
import com.mygdx.game.manager.GameStateManager;
import com.mygdx.game.comp460game;
import com.mygdx.game.actors.Text;

public class VictoryState extends GameState {

	private Stage stage;
	
	//Temporary links to other modules for testing.
	private Actor playOption, title;
	
	public VictoryState(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void show() {
		stage = new Stage() {
			{
				playOption = new Text(comp460game.assetManager, "TITLE?", 150, comp460game.CONFIG_HEIGHT - 180);
				
				playOption.addListener(new ClickListener() {
			        public void clicked(InputEvent e, float x, float y) {
			        	gsm.removeState(VictoryState.class);
			        }
			    });
				playOption.setScale(0.5f);
				
				addActor(playOption);
				addActor(new Text(comp460game.assetManager, "VICTORY?", 150, comp460game.CONFIG_HEIGHT - 300));
			}
		};
		app.newMenu(stage);
	}
	
	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		stage.dispose();	
	}

}