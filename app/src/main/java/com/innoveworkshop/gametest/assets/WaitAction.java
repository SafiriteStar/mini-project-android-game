package com.innoveworkshop.gametest.assets;

public class WaitAction implements LevelAction {
    private int waitTime;
    private long waitTimer;
    private boolean finishedExecuting = false;

    public WaitAction(int waitTime) {
        this.waitTime = waitTime;
    }

    @Override
    public void OnStart() {
        waitTimer = 0;
    }

    @Override
    public void OnFixedUpdate() {
        if (waitTimer >= waitTime) {
            finishedExecuting = true;
        }
        else {
            waitTimer += 1;
        }
    }

    @Override
    public boolean GetFinishedExecuting() {
        return finishedExecuting;
    }
}
