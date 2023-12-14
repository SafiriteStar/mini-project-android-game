package com.innoveworkshop.gametest.assets;

public interface LevelAction {
    public void OnStart();
    public void OnFixedUpdate();
    public boolean GetFinishedExecuting();
}
