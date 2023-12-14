package com.innoveworkshop.gametest.assets;

import android.graphics.Color;

import com.innoveworkshop.gametest.assets.LevelAction;
import com.innoveworkshop.gametest.engine.CircleObstacle;
import com.innoveworkshop.gametest.engine.GameObject;
import com.innoveworkshop.gametest.engine.GameSurface;

import java.util.List;
import java.util.Queue;

public class ObstacleWave implements LevelAction {
    private boolean finishedExecuting = false;
    private int timeBetweenObstacles = 0;
    private int obstacleSpawnTimer = 0;
    private Queue<GameObject> obstacles;
    private final GameSurface gameSurface;

    public ObstacleWave(Queue<GameObject> obstacles, GameSurface gameSurface, int timeBetweenObstacles) {
        this.obstacles = obstacles;
        this.gameSurface = gameSurface;
        this.timeBetweenObstacles = timeBetweenObstacles;
    }

    public void GenerateCircleWave(int numberOfCircles, float spawnX, float spawnY, int size, float lifeTime, float velocityX, float velocityY, float accelerationX, float accelerationY) {
        for (int i = 0; i < numberOfCircles; i++) {
            GameObject circleTest = new CircleObstacle(spawnX, spawnY, size, Color.RED, lifeTime);
            circleTest.rigidbody.velocity.x = velocityX;
            circleTest.rigidbody.velocity.y = velocityY;
            circleTest.rigidbody.acceleration.x = accelerationX;
            circleTest.rigidbody.acceleration.y = accelerationY;
            // Add them to the queue
            AddObstacle(circleTest);
        }
    }

    public void AddObstacle(GameObject obstacle) {
        obstacles.add(obstacle);
    }

    @Override
    public void OnStart() {
        obstacleSpawnTimer = 0;
        finishedExecuting = false;
    }
    @Override
    public void OnFixedUpdate() {
        if (timeBetweenObstacles <= obstacleSpawnTimer) {
            obstacleSpawnTimer = 0;

            // Do we have obstacles to spawn?
            if (obstacles.size() > 0) {
                // Yes
                // "Spawn" obstacle
                gameSurface.addGameObject(obstacles.remove());
            }
            else {
                finishedExecuting = true;
            }
        }
        else {
            // Increment the timer
            obstacleSpawnTimer += 1;
        }
    }

    @Override
    public boolean GetFinishedExecuting() {
        return finishedExecuting;
    }
}
