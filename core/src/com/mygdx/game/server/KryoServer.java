package com.mygdx.game.server;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.KryoSerialization;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mygdx.game.manager.GameStateManager;
import com.mygdx.game.states.PlayState;
import com.mygdx.game.states.TitleState;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;

public class KryoServer {

	int serverPort = 25565;
	int players = 0;
	public int[] playerIDs = {0,0};
	public Server server;
	GameStateManager gsm;
	boolean setMaster = true;

	public KryoServer(GameStateManager gameStateManager) {
		Kryo kryo = new Kryo();
		kryo.setReferences(true);
		KryoSerialization serialization = new KryoSerialization(kryo);
		this.server = new Server(16384, 2048, serialization);

		gsm = gameStateManager;

		server.addListener(new Listener() {
			public void disconnected(Connection c) {
				// This message should be sent when a player disconnects from the game
                players = 0;
                server.sendToAllExceptTCP(c.getID(), new Packets.DisconnectMessage());
                Gdx.app.postRunnable(new Runnable() {
                     @Override
                     public void run() {
                         gsm.addState(GameStateManager.State.TITLE, PlayState.class);
                     }
                 });
			}

			public void received(Connection c, Object o) {
				//Log.info("" + (o.getClass().getName()));
				if (o instanceof Packets.PlayerConnect) {
					// We have received a player connection message.
					Packets.PlayerConnect p = (Packets.PlayerConnect) o;

					// Ignore the object if the name is invalid.
					String name = p.message;
					if (name == null) {
						server.sendToTCP(c.getID(), new Packets.PlayerConnect("Invalid Player name."));
						return;
					}
					name = name.trim();
					if (name.length() == 0) {
						server.sendToTCP(c.getID(), new Packets.PlayerConnect("Cannot have empty player name."));
						return;
					}
					Packets.PlayerConnect newPlayer = new Packets.PlayerConnect( name + " has joined the game server.");
					Log.info(name + " has joined the game.");
					server.sendToAllExceptTCP(c.getID(), newPlayer);
					server.sendToTCP(c.getID(), new Packets.IDMessage(c.getID()));
					setMaster = false;

				}

				else if (o instanceof Packets.KeyPressOrRelease) {
					// We have received a player movement message.
					Packets.KeyPressOrRelease p = (Packets.KeyPressOrRelease) o;
					server.sendToAllTCP(p);
//                    Packets.KeyPressOrRelease p = (Packets.KeyPressOrRelease) o;
                    if (gsm.states.peek() instanceof PlayState) {
                        PlayState ps = (PlayState) gsm.states.peek();
                        if (p.message == Input.Keys.W) {
                            if (p.playerID == playerIDs[0]) {
                                if (p.pressOrRelease == Packets.KeyPressOrRelease.PRESSED) {
                                    ps.player.wPressed = true;
                                } else {
                                    ps.player.wPressed = false;
                                }
                            } else {
                                if (p.pressOrRelease == Packets.KeyPressOrRelease.PRESSED) {
                                    ps.player.wPressed2 = true;
                                    //Log.info("W2 pressed");
                                } else {
                                    ps.player.wPressed2 = false;
                                    //Log.info("W2 released");

                                }
                            }
                        } else if (p.message == Input.Keys.A) {
                            if (p.playerID == playerIDs[0]) {
                                if (p.pressOrRelease == Packets.KeyPressOrRelease.PRESSED) {
                                    ps.player.aPressed = true;
                                } else {
                                    ps.player.aPressed = false;
                                }
                            } else {
                                if (p.pressOrRelease == Packets.KeyPressOrRelease.PRESSED) {
                                    ps.player.aPressed2 = true;
                                } else {
                                    ps.player.aPressed2 = false;
                                }
                            }
                        } else if (p.message == Input.Keys.S) {
                            if (p.playerID == playerIDs[0]) {
                                if (p.pressOrRelease == Packets.KeyPressOrRelease.PRESSED) {
                                    ps.player.sPressed = true;
                                } else {
                                    ps.player.sPressed = false;
                                }
                            } else {
                                if (p.pressOrRelease == Packets.KeyPressOrRelease.PRESSED) {
                                    ps.player.sPressed2 = true;
                                } else {
                                    ps.player.sPressed2 = false;
                                }
                            }
                        } else if (p.message == Input.Keys.D) {
                            if (p.playerID == playerIDs[0]) {
                                if (p.pressOrRelease == Packets.KeyPressOrRelease.PRESSED) {
                                    ps.player.dPressed = true;
                                } else {
                                    ps.player.dPressed = false;
                                }
                            } else {
                                if (p.pressOrRelease == Packets.KeyPressOrRelease.PRESSED) {
                                    ps.player.dPressed2 = true;
                                } else {
                                    ps.player.dPressed2 = false;
                                }
                            }
                        } else if (p.message == Input.Keys.Q) {
                            if (p.playerID == playerIDs[0]) {
                                if (p.pressOrRelease == Packets.KeyPressOrRelease.PRESSED) {
                                    ps.player.qPressed = true;
                                } else {
                                    ps.player.qPressed = false;
                                }
                            } else {
                                if (p.pressOrRelease == Packets.KeyPressOrRelease.PRESSED) {
                                    ps.player.qPressed2 = true;
                                } else {
                                    ps.player.qPressed2 = false;
                                }
                            }
                        } else if (p.message == Input.Keys.E) {
                            if (p.playerID == playerIDs[0]) {
                                if (p.pressOrRelease == Packets.KeyPressOrRelease.PRESSED) {
                                    ps.player.ePressed = true;
                                } else {
                                    ps.player.ePressed = false;
                                }
                            } else {
                                if (p.pressOrRelease == Packets.KeyPressOrRelease.PRESSED) {
                                    ps.player.ePressed2 = true;
                                } else {
                                    ps.player.ePressed2 = false;
                                }
                            }
                        } else if (p.message == Input.Keys.SPACE) {
                            if (p.playerID == playerIDs[0]) {
                                if (p.pressOrRelease == Packets.KeyPressOrRelease.PRESSED) {
                                    ps.player.spacePressed = true;
                                } else {
                                    ps.player.spacePressed = false;
                                }
                            } else {
                                if (p.pressOrRelease == Packets.KeyPressOrRelease.PRESSED) {
                                    ps.player.spacePressed = true;
                                } else {
                                    ps.player.spacePressed = false;
                                }
                            }
                        }
                    }
				}

				else if (o instanceof Packets.MousePressOrRelease) {
                    Packets.MousePressOrRelease p = (Packets.MousePressOrRelease) o;
                    if (gsm.states.peek() instanceof  PlayState) {
                        PlayState ps = (PlayState) gsm.states.peek();
                        if (p.message == Input.Buttons.LEFT) {
                            if (p.playerID == playerIDs[0]) {
                                if (p.pressOrRelease == Packets.MousePressOrRelease.PRESSED) {
                                    ps.player.mousePressed = true;
                                } else {
                                    ps.player.mousePressed = false;
                                }
                                ps.player.mousePosX = p.x;
                                ps.player.mousePosY = p.y;
                            } else {
                                if (p.pressOrRelease == Packets.MousePressOrRelease.PRESSED) {
                                    ps.player.mousePressed2 = true;
                                } else {
                                    ps.player.mousePressed2 = false;
                                }
                                //TODO: THIS SHOULD BE MOUSEPOS2 (for player 2).
                                ps.player.mousePosX = p.x;
                                ps.player.mousePosY = p.y;
                            }
                        }
                    }
                }

//				else if (o instanceof Packets.Shoot) {
//					// We have received a mouse click.
//					Packets.Shoot p = (Packets.Shoot) o;
//					server.sendToAllTCP(p);
//				}

				else if (o instanceof Packets.ReadyToPlay) {
					//Log.info("Server received ReadyToPlay");
				    Packets.ReadyToPlay p = (Packets.ReadyToPlay) o;
				    playerIDs[players] = c.getID();
                    players += 1;
					Log.info("Player " + c.getID() + " ready.");
				    if (players == 2) {
				        server.sendToAllTCP(new Packets.EnterPlayState());
				        players = 0;
//						Gdx.app.postRunnable(new Runnable() {
//							public void run() {
//								gsm.addState(GameStateManager.State.PLAY, TitleState.class);
//							}
//						});
                    }
                }

                else if (o instanceof Packets.ClientCreatedPlayState) {
                    //Log.info("Server received ReadyToPlay");
                    Packets.ClientCreatedPlayState p = (Packets.ClientCreatedPlayState) o;
                    players += 1;
                    if (players == 2) {
                        Gdx.app.postRunnable(new Runnable() {
                            public void run() {
                                gsm.addState(GameStateManager.State.PLAY, TitleState.class);
                            }
                        });
                    }
                }

                else if (o instanceof Packets.SyncPlayState) {
					//Log.info("Syncing PlayStates...");
					Packets.SyncPlayState p = (Packets.SyncPlayState) o;
					server.sendToAllExceptTCP(c.getID(),p);
				}

				else if (o instanceof Packets.SyncHitbox) {
					//Log.info("Syncing Hitbox...");
					Packets.SyncHitbox p = (Packets.SyncHitbox) o;
					server.sendToAllTCP(p);
				}

				else if (o instanceof Packets.SyncCreateSchmuck) {
					//Log.info("Syncing Schmuck Creation...");
					Packets.SyncCreateSchmuck p = (Packets.SyncCreateSchmuck) o;
					server.sendToAllExceptTCP(c.getID(),p);
				}
			}
		});

		try {
			server.bind(serverPort);
		} catch (IOException e) {
			e.printStackTrace();
		}

		registerPackets();

		server.start();
	}

	private void registerPackets() {
		Kryo kryo = server.getKryo();
		Packets.allPackets(kryo);
	}
}

