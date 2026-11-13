package eng.project.ui;

import eng.project.game.GameScreen;

public class Pause implements Runnable{
    @Override
    public void run(){
        GameScreen.pauseGame();
    }
}
