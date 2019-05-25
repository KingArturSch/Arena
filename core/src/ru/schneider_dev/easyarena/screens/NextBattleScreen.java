package ru.schneider_dev.easyarena.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import ru.schneider_dev.easyarena.Assets;
import ru.schneider_dev.easyarena.GameSession;
import ru.schneider_dev.easyarena.ScreenManager;

import static ru.schneider_dev.easyarena.screens.BattleScreen.score;

/**
 * Created by User on 05.04.2018.
 */

public class NextBattleScreen implements Screen {
    private Texture backgroundTexture;
    private BitmapFont font96;
    private BitmapFont font36;
    private BitmapFont font63;
    private SpriteBatch batch;
    public static int nextBattleLevel;
    private int totalScore;

    private Stage stage;
    private Skin skin;
    private float time;

    public NextBattleScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        backgroundTexture = Assets.getInstance().getAssetManager().get("gameMenuBackground.png", Texture.class);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("zorque.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 96;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetX = -3;
        parameter.shadowOffsetY = 3;
        parameter.color = Color.WHITE;
        font96 = generator.generateFont(parameter);
        parameter.size = 63;
        font63 = generator.generateFont(parameter);
        parameter.size = 36;
        font36 = generator.generateFont(parameter);
        generator.dispose();

        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Assets.getInstance().getAtlas());
        skin.add("font36", font36);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("menuBtn");
        textButtonStyle.down = skin.getDrawable("menuBtnPressed");
        textButtonStyle.font = font36;

        skin.add("tbs", textButtonStyle);
        Button btnContinueGame = new TextButton("CONTINUE LEVEL", skin, "tbs");
        Button btnNextGame = new TextButton("NEXT LEVEL", skin, "tbs");
        Button btnExitGame = new TextButton("EXIT GAME", skin, "tbs");
        btnNextGame.setPosition(640 - 211, 220);
        btnContinueGame.setPosition(640 - 211, 340);
        btnExitGame.setPosition(640 - 211, 100);
        stage.addActor(btnNextGame);
        stage.addActor(btnContinueGame);
        stage.addActor(btnExitGame);
        btnContinueGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameSession.getInstance().makeAiArmy(1);
                ScreenManager.getInstance().switchScreen(ScreenManager.ScreenType.BATTLE);
            }
        });
        btnNextGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                nextBattleLevel += 5;
//                totalScore +=score;
                GameSession.getInstance().makeAiArmy(nextBattleLevel);
                ScreenManager.getInstance().switchScreen(ScreenManager.ScreenType.BATTLE);
            }
        });
        btnExitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0);
        font96.draw(batch, "Select level", 0, 600 + 20.0f * (float) Math.sin(time), 1280, 1, false);
        font63.draw(batch, "Score:" + score, 300, 50, 1280, 1, false);
        batch.end();
        stage.draw();
    }

    public void update(float dt) {
        time += dt;
        stage.act(dt);
    }

    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().onResize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
//        music.dispose();
        font96.dispose();
        font36.dispose();
        backgroundTexture.dispose();
    }
}
