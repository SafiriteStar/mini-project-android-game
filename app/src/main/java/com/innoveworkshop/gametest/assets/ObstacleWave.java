package com.innoveworkshop.gametest.assets;

import com.innoveworkshop.gametest.assets.LevelAction;
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
