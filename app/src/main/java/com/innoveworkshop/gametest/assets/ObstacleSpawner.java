package com.innoveworkshop.gametest.assets;

import com.innoveworkshop.gametest.engine.GameObject;
import com.innoveworkshop.gametest.engine.GameSurface;

import java.util.Queue;

public class ObstacleSpawner extends GameObject {
    Queue<LevelAction> levelActions;
    LevelAction currentLevelAction;

    public ObstacleSpawner(Queue<LevelAction> levelActions) {
        this.levelActions = levelActions;
    }

    @Override
    public void onStart(GameSurface gameSurface) {
        super.onStart(gameSurface);
        // Set our first level action
        currentLevelAction = levelActions.remove();
        currentLevelAction.OnStart();
    }

    @Override
    public void onFixedUpdate() {
        super.onFixedUpdate();
        if (currentLevelAction.GetFinishedExecuting()) {
            // We finished doing our current action
            if (!levelActions.isEmpty()) {
                // Start the next one
                currentLevelAction = levelActions.remove();
                currentLevelAction.OnStart();
            }
            else {
                destroy();
            }
        }
        else {
            currentLevelAction.OnFixedUpdate();
        }
    }
}
