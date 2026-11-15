package eng.project.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eng.project.entity.geese.Goose;
import eng.project.entity.geese.GeeseManager;
import eng.project.entity.geese.GooseState;
import eng.project.entity.Player;

import java.util.ArrayList;

/* Every goose within range of the player gets stunned */
public class Whistle extends Item{
    //Numbers to be changed for balancing
    private final double stunDistance = 10d;
    private final float stunTime = 2f;
    private float rechargeTime = 10f;
    private ItemState state = ItemState.Usable;
    private GeeseManager manager;
    private Player player;

    public Whistle(Player player, GeeseManager manager, String name, TextureRegion icon){
        super(name, icon);
        this.manager = manager;
        this.player = player;
    }

    @Override
    public boolean Use(){
        float playerX = player.getX();
        float playerY = player.getY();
        ArrayList<Goose> geeseList = manager.getGeese();
        //Calculate distance from player
        for (int i = 0; i < geeseList.size(); i++){
            Goose currentGoose = geeseList.get(i);
            float dx = playerX - currentGoose.getX();
            float dy = playerY - currentGoose.getY();
            double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
            if (distance <= stunDistance){
                //Goose cannot leave idle state for the stun time
                currentGoose.changeState(GooseState.Idle);
                currentGoose.changeTimer(stunTime);
            }
        }
        state = ItemState.Recharging;
        return false;
    }
}
