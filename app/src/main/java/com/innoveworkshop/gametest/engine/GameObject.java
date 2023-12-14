package com.innoveworkshop.gametest.engine;

import android.graphics.Canvas;
import android.view.SurfaceView;

public abstract class GameObject implements Caged {
    public Vector position;
    public Rigidbody rigidbody;
    protected GameSurface gameSurface = null;
    protected boolean destroyed = false;

    public GameObject() {
        this(0, 0);
    }

    public GameObject(Vector position) {
        this.position = position;
    }

    public GameObject(float x, float y) {
        position = new Vector(x, y);
    }

    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
    }

    public void destroy() {
        gameSurface.removeGameObject(this);
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void onStart(GameSurface surface) {
        this.gameSurface = surface;
    }

    public void onFixedUpdate() {
        if (destroyed) {
            setPosition(-100, -100);
        }
        else {
            if (rigidbody != null) {
                rigidbody.OnFixedUpdate();
            }
        }
    }

    public void onDraw(Canvas canvas) {}

    @Override
    public boolean isFloored() {
        return false;
    }

    @Override
    public boolean hitTopWall() {
        return false;
    }

    @Override
    public boolean hitLeftWall() {
        return false;
    }

    @Override
    public boolean hitRightWall() {
        return false;
    }
}
