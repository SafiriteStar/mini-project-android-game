package com.innoveworkshop.gametest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    protected TextView gameOverTextView;
    protected Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findControls();
        gameOverTextView = (TextView) findViewById(R.id.game_over_text);
        gameOverTextView.setVisibility(View.INVISIBLE);

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

    public void DisplayGameOver() {
        gameOverTextView.setVisibility(View.VISIBLE);
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
        }

        @Override
        public void onFixedUpdate() {
            super.onFixedUpdate();

            playerController.onFixedUpdate();
        }

        @Override
        public void onDraw(Canvas canvas) {
            if (playerController.isDead()) {
                gameOverTextView.setVisibility(View.VISIBLE);
            }
        }

        private void GenerateObstacles(GameSurface surface) {
            // Here we want to make obstacles that the player will have to deal with as they play the level
            // Instantiate the levelActions
            Queue<LevelAction> levelActions = new LinkedList<>();

            // First make the player wait 2 seconds
            levelActions.add(new WaitAction(1));

            // Start making 3 balls that will appear from the right, move left at first before turning back right
            levelActions.add(IncrementalSpawnCircleWave(surface,
                    20,
                    5,
                    surface.getWidth() + 60,
                    0,
                    0,
                    surface.getHeight() * 1/8,
                    50,
                    60,
                    -30,
                    0,
                    1,
                    0.2f));

            levelActions.add(IncrementalSpawnCircleWave(surface,
                    20,
                    5,
                    -100,
                    0,
                    surface.getHeight() * 5/8,
                    surface.getHeight() * -1/8,
                    50,
                    120,
                    50,
                    0,
                    -1,
                    0.2f));

            // Big to top down
            levelActions.add(SimpleCircleWave(surface,
                    50,
                    5,
                    surface.getWidth() / 2,
                    -100,
                    75,
                    60,
                    0,
                    30,
                    0,
                    1f));

            obstacleSpawner = new ObstacleSpawner(levelActions);
            surface.addGameObject(obstacleSpawner);
        }

        public ObstacleWave SimpleCircleWave(GameSurface gameSurface, int timeBetweenObstacles, int numberOfCircles, float spawnX, float spawnY, int size, float lifeTime, float velocityX, float velocityY, float accelerationX, float accelerationY) {
            ObstacleWave obstacleWave = new ObstacleWave(new LinkedList<>(), gameSurface, timeBetweenObstacles);
            obstacleWave.GenerateCircleWave(numberOfCircles, spawnX, spawnY, size, lifeTime, velocityX, velocityY, accelerationX, accelerationY);
            return obstacleWave;
        }

        public ObstacleWave IncrementalSpawnCircleWave(GameSurface gameSurface, int timeBetweenObstacles, int numberOfCircles, float spawnX, float spawnXIncrement, float spawnY, float spawnYIncrement, int size, float lifeTime, float velocityX, float velocityY, float accelerationX, float accelerationY) {
            ObstacleWave obstacleWave = new ObstacleWave(new LinkedList<>(), gameSurface, timeBetweenObstacles);
            for (int i = 0; i < numberOfCircles; i++) {
                GameObject circleTest = new CircleObstacle(spawnX + (spawnXIncrement * i), spawnY + (spawnYIncrement * i), size, Color.RED, lifeTime);
                circleTest.rigidbody.velocity.x = velocityX;
                circleTest.rigidbody.velocity.y = velocityY;
                circleTest.rigidbody.acceleration.x = accelerationX;
                circleTest.rigidbody.acceleration.y = accelerationY;
                // Add them to the queue
                obstacleWave.AddObstacle(circleTest);
            }
            return obstacleWave;
        }
    }
}