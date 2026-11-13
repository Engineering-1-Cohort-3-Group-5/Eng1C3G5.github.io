package eng.project.ui;

import eng.project.game.GameScreen;

public class Play implements Runnable{
    @Override
    public void run(){
        GameScreen.resumeGame();
    }
}
