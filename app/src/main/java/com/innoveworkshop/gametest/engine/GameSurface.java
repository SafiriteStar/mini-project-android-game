package com.innoveworkshop.gametest.engine;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.innoveworkshop.gametest.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameSurface extends SurfaceView {
    private SurfaceHolder holder;
    private Timer timer;

    private GameObject root;
    private ArrayList<GameObject> gameObjects;

    public GameSurface(Context context) {
        this(context, null);
    }

    public GameSurface(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameSurface(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Create the GameObject list.
        gameObjects = new ArrayList<GameObject>();

        // Ensure we are on top of everything.
        setZOrderOnTop(true);

        // Set up the SurfaceHolder event handler.
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                // Ensure we get the onDraw events.
                setWillNotDraw(false);

                // Start up the root object.
                root.onStart(GameSurface.this);

                // Set up the fixed update timer.
                timer = new Timer();
                timer.scheduleAtFixedRate(new FixedUpdateTimer(), 0, 1000 / 30);
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                // TODO: Stop everything.
            }
        });
    }

    public void setRootGameObject(GameObject root) {
        this.root = root;
    }

    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
        gameObject.onStart(this);
    }

    public boolean removeGameObject(GameObject gameObject) {
        return gameObjects.remove(gameObject);
    }

    public boolean CheckCollisionFromCircle(float x, float y, float r) {
        for (GameObject gameObject : gameObjects) {
            if (gameObject.hasCollider) {
                if (gameObject.CheckCollisionWithCircle(x, y, r)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        root.onDraw(canvas);
        for (GameObject gameObject : gameObjects) {
            gameObject.onDraw(canvas);
        }
    }

    class FixedUpdateTimer extends TimerTask {
        @Override
        public void run() {
            ArrayList<GameObject> tempList = new ArrayList<GameObject>(gameObjects);
            for (GameObject gameObject : tempList) {
                gameObject.onFixedUpdate();
            }

            root.onFixedUpdate();
            invalidate();
        }
    }
}
