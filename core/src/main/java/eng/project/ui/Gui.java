package eng.project.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eng.project.game.GameScreen;
import eng.project.game.GameState;

import static com.badlogic.gdx.Input.Keys.ESCAPE;

/** Entry point for screen-space UI; add HUD elements here as the game expands. */
public class Gui {
    private final Texture buttonTexture = new Texture("pause.png");
    private final SpriteBatch buttonBatch = new SpriteBatch();
    public Gui(Pause pauseRunner, Play playRunner){
        pauseButton(pauseRunner, playRunner);
    }
    public void render(){
        // Draw ui elements here
        buttonBatch.begin();
        buttonBatch.draw(buttonTexture, 100, 100, 50, 50);
        buttonBatch.end();
    }
    private final Stage stage = new Stage();

    public void TutorialScreen() {
        Gdx.input.setInputProcessor(stage);

        Label labelWASD = new Label("Use WASD to move", new Label.LabelStyle(null, Color.WHITE));
        Label labelScroll = new Label("Use scroll wheel on mouse or keyboard numbers to select an item", new Label.LabelStyle(null, Color.WHITE));
        Label labelLClick = new Label("Left click to use item", new Label.LabelStyle(null, Color.WHITE));
        Label labelPause = new Label("Press the pause button or ESC to pause", new Label.LabelStyle(null, Color.WHITE));
        Label labelPlay = new Label("Press again to play", new Label.LabelStyle(null, Color.WHITE));
        Label labelInstruct = new Label("Find the bus pass and get to the bus before time runs out!", new Label.LabelStyle(null, Color.WHITE));

        Container labelContainer = new Container(labelWASD);
        labelContainer.bottom();
        stage.addActor(labelWASD);

        if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.A) ||
            Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            labelWASD.setVisible(false);
            Container containerScroll = new Container(labelScroll);
            containerScroll.bottom();
            stage.addActor(labelScroll);
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                labelScroll.setVisible(false);
                Container containerLClick = new Container(labelLClick);
                containerLClick.bottom();
                stage.addActor(labelLClick);
                if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    labelLClick.setVisible(false);
                    Container containerPause = new Container(labelPause);
                    containerPause.bottom();
                    stage.addActor(labelPause);
                    if (Gdx.input.isKeyJustPressed(ESCAPE)) {
                        labelPause.setVisible(false);
                        Container containerPlay = new Container(labelPlay);
                        containerPlay.bottom();
                        stage.addActor(labelPlay);
                        if (Gdx.input.isKeyJustPressed(ESCAPE)) {
                            labelPlay.setVisible(false);
                            Container containerInstruct = new Container(labelInstruct);
                            containerInstruct.bottom();
                            stage.addActor(labelInstruct);

                        }
                    }
                }
            }
        }
    }

    public void pauseButton(Runnable onPause, Runnable onPlay) {
        Gdx.input.setInputProcessor(stage);

        Texture buttonTexture = new Texture("pause.png");
        Sprite buttonSprite = new Sprite(buttonTexture);
        ImageButton pauseButton = new ImageButton(new SpriteDrawable(buttonSprite));
        pauseButton.setPosition(100, 100);
        pauseButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (GameScreen.state == GameState.Pause){
                    onPlay.run();
                }
                else{
                    onPause.run();
                }
            }
        });
        stage.addActor(pauseButton);
    }

    public void playButton(Runnable onPlay) {
        Gdx.input.setInputProcessor(stage);

        Texture buttonTexture = new Texture("play.png");
        Sprite buttonSprite = new Sprite(buttonTexture);
        ImageButton playButton = new ImageButton(new SpriteDrawable(buttonSprite));
        playButton.setPosition(100, 100);
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onPlay.run();
            }
        });
        stage.addActor(playButton);
    }

    public void update(float delta) {
        stage.act(delta);
        stage.draw();
    }

}
