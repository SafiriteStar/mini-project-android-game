package com.innoveworkshop.gametest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.innoveworkshop.gametest.assets.DroppingRectangle;
import com.innoveworkshop.gametest.assets.LevelAction;
import com.innoveworkshop.gametest.assets.ObstacleSpawner;
import com.innoveworkshop.gametest.assets.ObstacleWave;
import com.innoveworkshop.gametest.assets.WaitAction;
import com.innoveworkshop.gametest.engine.Circle;
import com.innoveworkshop.gametest.engine.GameObject;
import com.innoveworkshop.gametest.engine.GameSurface;
import com.innoveworkshop.gametest.engine.Rectangle;
import com.innoveworkshop.gametest.engine.Vector;
import com.innoveworkshop.gametest.engine.PlayerController;
import com.innoveworkshop.gametest.engine.Rigidbody;
import com.innoveworkshop.gametest.engine.CircleObstacle;

import java.util.LinkedList;
import java.util.Queue;

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
        private ObstacleSpawner obstacleSpawner;
        public Circle circle;

        @Override
        public void onStart(GameSurface surface) {
            super.onStart(surface);

            GenerateObstacles(surface);

            circle = new Circle(surface.getWidth() / 2, surface.getHeight() * (9 / 10) + 200, 100, Color.GREEN);
            // Give this circle a rigidbody
            circle.rigidbody = new Rigidbody(circle);
            // Attach a controller to this game object
            playerController = new PlayerController(circle, GetControlButtons());
            surface.addGameObject(circle);

            surface.addGameObject(new DroppingRectangle(new Vector(surface.getWidth() / 3, surface.getHeight() / 3),
                    100, 100, 10, Color.rgb(128, 14, 80)));

            GameObject circleTest = new CircleObstacle(surface.getWidth() + 100, surface.getHeight() / 3, 50, Color.RED, 60);
            circleTest.rigidbody.velocity.x = -30;
            circleTest.rigidbody.acceleration.x = 1f;
            circleTest.rigidbody.acceleration.y = 0.2f;
            surface.addGameObject(circleTest);
        }

        @Override
        public void onFixedUpdate() {
            super.onFixedUpdate();

            playerController.onFixedUpdate();
        }

        private void GenerateObstacles(GameSurface surface) {
            // Here we want to make obstacles that the player will have to deal with as they play the level
            // Instantiate the levelActions
            Queue<LevelAction> levelActions = new LinkedList<>();

            // First make the player wait 2 seconds
            levelActions.add(new WaitAction(100));

            // Start making 3 balls that will appear from the right, move left at first before turning back right
            ObstacleWave testWave = new ObstacleWave(new LinkedList<>(), surface, 50);

            // Create 3 balls that will spawn
            for (int i = 0; i < 3; i++) {
                GameObject circleTest = new CircleObstacle(surface.getWidth() + 100, surface.getHeight() / 3, 50, Color.RED, 60);
                circleTest.rigidbody.velocity.x = -30;
                circleTest.rigidbody.acceleration.x = 1f;
                circleTest.rigidbody.acceleration.y = 0.2f;
                // Add them to the queue
                testWave.AddObstacle(circleTest);
            }
            levelActions.add(testWave);

            obstacleSpawner = new ObstacleSpawner(levelActions);
            surface.addGameObject(obstacleSpawner);

            GameObject circleRotationPoint = new CircleObstacle(surface.getWidth() / 2, surface.getHeight() / 2, 50, Color.BLACK, 9000);
            circleRotationPoint.rigidbody.velocity.y = -5;
            surface.addGameObject(circleRotationPoint);
        }
    }
}