package trl.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import trl.entity.player.Ranger;
import trl.gamestate.GameStateManager;
import trl.gamestate.GameplayState;
import trl.gamestate.LoseGameState;
import trl.gamestate.MenuState;

public class KeyManager implements KeyListener{

	// Controller of the game in the Key Manager class
	private GameStateManager gsm;
	private final static Logger LOGGER = Logger.getLogger( KeyManager.class.getName() );

	public KeyManager ( GameStateManager gsm ){

		this.gsm = gsm;
	}

	// Action event from the keyboard
	public void keyPressed ( KeyEvent e ){
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Key pressed");

		if ( this.gsm.getGameState() == 0 ){

			if ( e.getKeyCode() == KeyEvent.VK_UP ){

				MenuState.up = true;
			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_DOWN ){

				MenuState.down = true;
			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_ENTER ){

				MenuState.enter = false;
			}else{
				// Nothing
			}
		}else{
			// Nothing
		}
		if ( this.gsm.getGameState() == 2 ){
			if ( e.getKeyCode() == KeyEvent.VK_UP ){

				LoseGameState.up = true;
			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_DOWN ){

				LoseGameState.down = true;
			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_ENTER ){

				LoseGameState.enter = false;
			}else{
				// Nothing
			}
		}else{
			// Nothing
		}
	}

	public void keyReleased ( KeyEvent e ){
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Key released");

		if ( this.gsm.getGameState() == 1 && Game.tickTimer <= 0 ){

			if ( e.getKeyCode() == KeyEvent.VK_NUMPAD8 ){

				GameplayState.getPlayer().upDirection = true;
			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_NUMPAD2 ){

				GameplayState.getPlayer().downDirection = true;
			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_NUMPAD4 ){

				GameplayState.getPlayer().leftDirection = true;
			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_NUMPAD6 ){

				GameplayState.getPlayer().rightDirection = true;
			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_NUMPAD9 ){

				GameplayState.getPlayer().upRightDirection = true;
			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_NUMPAD3 ){

				GameplayState.getPlayer().downRightDirection = true;
			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_NUMPAD1 ){

				GameplayState.getPlayer().downLeftDirection = true;
			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_NUMPAD7 ){

				GameplayState.getPlayer().upLeftDirection = true;
			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_NUMPAD5 ){

				GameplayState.getPlayer().wait = true;
			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_S ){

				if ( GameplayState.getPlayer() instanceof trl.entity.player.Barbarian ){

					GameplayState.getPlayer().shout = true;
				}else{
					// Nothing
				}
			}else{
				// Nothing
			}

			if ( e.getKeyCode() == KeyEvent.VK_B ){

				if ( GameplayState.getPlayer() instanceof trl.entity.player.Wizard ){

					GameplayState.getPlayer().blink = true;
				}else{
					// Nothing
				}
			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_E ){

				if ( GameplayState.getPlayer() instanceof trl.entity.player.Wizard ){

					GameplayState.getPlayer().explode = true;
				}else{
					// Nothing
				}
			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_C ){

				GameplayState.getPlayer().close = true;
			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_Q ){

				if ( GameplayState.getPlayer() instanceof trl.entity.player.Wizard ){

					GameplayState.getPlayer().quicken = true;
				}else{
					// Nothing
				}
			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_F ){
				if ( GameplayState.getPlayer() instanceof trl.entity.player.Ranger ){

					/*
					 * If F pressed when neither getTargets nor targetEnemy are
					 * true, get do getTargets() and enable targetEnemy().
					 * getTargets will become false.
					 */
					if ( !( (Ranger) ( GameplayState.getPlayer() ) ).gotTargets ){

						GameplayState.getPlayer().getTargets = true;
					}else{
						// Nothing
					}
					/*
					 * If F pressed when getTargets is false and targetEnemy is
					 * true, do fireArrow, using the currently selected target.
					 */
					if ( ( (Ranger) ( GameplayState.getPlayer() ) ).gotTargets ){

						GameplayState.getPlayer().gotTargets = false;
						GameplayState.getPlayer().targetEnemy = false;
						System.out.println( "Setting fireArrow to true." );
						GameplayState.getPlayer().fireArrow = true;
					}else{
						// Nothing
					}
				}else{
					// Nothing
				}

			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_ESCAPE ){

				GameplayState.getPlayer().cancel = true;
			}else{
				// Nothing
			}

			if ( e.getKeyCode() == KeyEvent.VK_LEFT ){

				GameplayState.getPlayer().previousTarget = true;
			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_RIGHT ){

				GameplayState.getPlayer().nextTarget = true;
			}else{
				// Nothing
			}
		}else{
			// Nothing
		}

		if ( this.gsm.getGameState() == 0 ){

			if ( e.getKeyCode() == KeyEvent.VK_UP ){

				MenuState.up = false;
			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_DOWN ){

				MenuState.down = false;
			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_ENTER ){

				MenuState.enter = true;
			}else{
				// Nothing
			}
		}else{
			// Nothing
		}
		if ( this.gsm.getGameState() == 2 ){

			if ( e.getKeyCode() == KeyEvent.VK_UP ){

				LoseGameState.up = false;
			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_DOWN ){

				LoseGameState.down = false;
			}else{
				// Nothing
			}
			if ( e.getKeyCode() == KeyEvent.VK_ENTER ){

				LoseGameState.enter = true;
			}else{
				// Nothing
			}
		}else{
			// Nothing
		}
	}

	public void keyTyped ( KeyEvent e ){
		
		// Nothing to do
	}
}
