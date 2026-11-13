package eng.project.ui;

import eng.project.game.GameScreen;
import eng.project.game.GameState;

public class Pause implements Runnable{
    @Override
    public void run(){
        GameScreen.pauseGame();
    }
}
