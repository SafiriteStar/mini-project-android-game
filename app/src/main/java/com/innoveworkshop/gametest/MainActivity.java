package com.innoveworkshop.gametest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    protected TextView onGoingScoreTextView;
    protected Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findControls();
        gameOverTextView = (TextView) findViewById(R.id.game_over_text);
        gameOverTextView.setVisibility(View.INVISIBLE);
        onGoingScoreTextView = (TextView) findViewById(R.id.on_going_score);

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
        private int scoreViewTime = 50;
        private int scoreViewTimer = 0;
        private float score = 0;
        private boolean wonTheGame;

        @Override
        public void onStart(GameSurface surface) {
            super.onStart(surface);

            GenerateObstacles(surface);

            circle = new Circle(surface.getWidth() / 2, surface.getHeight() * (9 / 10) + 200, 70, Color.GREEN);
            // Give this circle a rigidbody
            circle.rigidbody = new Rigidbody(circle);
            // Attach a controller to this game object
            playerController = new PlayerController(circle, GetControlButtons());
            surface.addGameObject(circle);
        }

        @Override
        public void onFixedUpdate() {
            super.onFixedUpdate();
            if (!playerController.isDead() && !wonTheGame) {
                score += 0.2;
                playerController.onFixedUpdate();
                // If we have reached the end
                if (obstacleSpawner.levelActions.isEmpty()) {
                    wonTheGame = true;
                }
            }
            else {
                scoreViewTimer += 1;
            }
        }

        @Override
        public void onDraw(Canvas canvas) {
            if (playerController.isDead() || wonTheGame) {
                onGoingScoreTextView.setVisibility(View.INVISIBLE);
                gameOverTextView.setVisibility(View.VISIBLE);
                CharSequence scoreText = "Max Depth: " + (int) score + "m";
                gameOverTextView.setText(scoreText);

                if (scoreViewTime < scoreViewTimer) {
                    recreate();
                }
            }
            else {
                CharSequence scoreText = "Depth: " + (int) score + "m";
                onGoingScoreTextView.setText(scoreText);
            }
        }

        private void GenerateObstacles(GameSurface surface) {
            // Here we want to make obstacles that the player will have to deal with as they play the level
            // Instantiate the levelActions
            Queue<LevelAction> levelActions = new LinkedList<>();

            // First make the player wait 2 seconds
            levelActions.add(new WaitAction(1));

            // Starter Pair
            levelActions.add(IncrementalSpawnCircleWave(surface,
                    40,
                    4,
                    surface.getWidth() * 7/8,
                    -surface.getWidth() * 2/8,
                    surface.getHeight() + 50,
                    0,
                    50,
                    360,
                    0,
                    -50,
                    0,
                    0.75f));
            levelActions.add(IncrementalSpawnCircleWave(surface,
                    40,
                    3,
                    surface.getWidth() * 6/8,
                    -surface.getWidth() * 2/8,
                    -50,
                    0,
                    50,
                    360,
                    0,
                    50,
                    0,
                    -0.75f));

            // New Pair
            levelActions.add(IncrementalSpawnCircleWave(surface,
                    20,
                    5,
                    surface.getWidth() + 60,
                    0,
                    surface.getHeight() * 1/8,
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
                    -60,
                    0,
                    surface.getHeight() * 1/8,
                    surface.getHeight() * 1/8,
                    50,
                    60,
                    30,
                    0,
                    -1,
                    0.2f));

            // Pair
            levelActions.add(IncrementalSpawnCircleWave(surface,
                    20,
                    7,
                    -60,
                    0,
                    surface.getHeight() * 7/8,
                    surface.getHeight() * -1/8,
                    50,
                    120,
                    40,
                    0,
                    -0.8f,
                    0.2f));
            levelActions.add(IncrementalSpawnCircleWave(surface,
                    20,
                    7,
                    surface.getWidth() + 60,
                    0,
                    surface.getHeight() * 7/8,
                    surface.getHeight() * -1/8,
                    50,
                    120,
                    -40,
                    0,
                    0.8f,
                    0.2f));

            // Divider
            levelActions.add(SimpleCircleWave(surface,
                    10,
                    5,
                    surface.getWidth() / 2,
                    -100,
                    75,
                    180,
                    0,
                    1,
                    0,
                    0.5f));

            // Pair
            levelActions.add(IncrementalSpawnCircleWave(surface,
                    20,
                    7,
                    surface.getWidth() + 60,
                    0,
                    surface.getHeight() * 8/8,
                    surface.getHeight() * -1/8,
                    50,
                    120,
                    -50,
                    -1,
                    0.8f,
                    -0.05f));
            levelActions.add(IncrementalSpawnCircleWave(surface,
                    20,
                    7,
                    -60,
                    0,
                    surface.getHeight() * 8/8,
                    surface.getHeight() * -1/8,
                    50,
                    120,
                    50,
                    -1,
                    -0.8f,
                    -0.05f));

            // Certain Death
            levelActions.add(AngleSpawnWave(surface,
                    0,
                    8,
                    surface.getWidth() * 4/8,
                    surface.getHeight() * 9/8,
                    50,
                    180,
                    40,
                    -0.5f,
                    -20f,
                    -20f));
            levelActions.add(new WaitAction(20));
            levelActions.add(AngleSpawnWave(surface,
                    0,
                    8,
                    surface.getWidth() * 4/8,
                    surface.getHeight() * -1/8,
                    50,
                    180,
                    -40,
                    0.5f,
                    -20f,
                    -20f));
            levelActions.add(new WaitAction(20));
            levelActions.add(AngleSpawnWave(surface,
                    5,
                    9,
                    surface.getWidth() * -1/8,
                    surface.getHeight() * 4/8,
                    50,
                    180,
                    -10,
                    -0.5f,
                    90,
                    20f));
            levelActions.add(new WaitAction(20));
            levelActions.add(AngleSpawnWave(surface,
                    5,
                    9,
                    surface.getWidth() * 9/8,
                    surface.getHeight() * 4/8,
                    50,
                    180,
                    10,
                    0.5f,
                    90,
                    20f));

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

        public ObstacleWave AngleSpawnWave(GameSurface gameSurface, int timeBetweenObstacles, int numberOfCircles, float spawnX, float spawnY, int size, float lifeTime, float velocity, float acceleration, float startAngle, float angleIncrement) {
            ObstacleWave obstacleWave = new ObstacleWave(new LinkedList<>(), gameSurface, timeBetweenObstacles);
            float angle = startAngle;
            for (int i = 0; i < numberOfCircles; i++) {
                GameObject circleTest = new CircleObstacle(spawnX, spawnY, size, Color.RED, lifeTime);
                double sumAngle = Math.toRadians(angle) + (i * Math.toRadians(angleIncrement));
                double xFactor = Math.cos(sumAngle);
                double yFactor = Math.sin(sumAngle);
                circleTest.rigidbody.velocity.x = (float) (velocity * xFactor);
                circleTest.rigidbody.velocity.y = (float) (velocity * yFactor);
                circleTest.rigidbody.acceleration.x = (float) (acceleration * xFactor);
                circleTest.rigidbody.acceleration.y = (float) (acceleration * yFactor);
                // Add them to the queue
                obstacleWave.AddObstacle(circleTest);
            }
            return obstacleWave;
        }
    }
}