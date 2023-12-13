package com.innoveworkshop.gametest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.innoveworkshop.gametest.assets.DroppingRectangle;
import com.innoveworkshop.gametest.engine.Circle;
import com.innoveworkshop.gametest.engine.GameObject;
import com.innoveworkshop.gametest.engine.GameSurface;
import com.innoveworkshop.gametest.engine.Rectangle;
import com.innoveworkshop.gametest.engine.Vector;
import com.innoveworkshop.gametest.engine.PlayerController;

public class MainActivity extends AppCompatActivity {
    protected GameSurface gameSurface;
    protected Button upButton;
    protected Button downButton;
    protected Button leftButton;
    protected Button rightButton;
    protected Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findControls();

        gameSurface = (GameSurface) findViewById(R.id.gameSurface);
        game = new Game();
        gameSurface.setRootGameObject(game);
    }

    private void findControls() {
        upButton = (Button) findViewById(R.id.up_button);
        downButton = (Button) findViewById(R.id.down_button);
        leftButton = (Button) findViewById(R.id.left_button);
        rightButton = (Button) findViewById(R.id.right_button);
    }

    public Button[] GetControlButtons() {
        return new Button[] {upButton, downButton, leftButton, rightButton};
    }

    class Game extends GameObject {
        protected PlayerController playerController;
        public Circle circle;

        @Override
        public void onStart(GameSurface surface) {
            super.onStart(surface);

            circle = new Circle(surface.getWidth() / 2, surface.getHeight() / 2, 100, Color.RED);
            // Attach a controller to this game object
            playerController = new PlayerController(circle, GetControlButtons());
            surface.addGameObject(circle);

            surface.addGameObject(new Rectangle(new Vector(surface.getWidth() / 3, surface.getHeight() / 3),
                    200, 100, Color.GREEN));

            surface.addGameObject(new DroppingRectangle(new Vector(surface.getWidth() / 3, surface.getHeight() / 3),
                    100, 100, 10, Color.rgb(128, 14, 80)));
        }

        @Override
        public void onFixedUpdate() {
            super.onFixedUpdate();

            playerController.onFixedUpdate();
        }
    }
}