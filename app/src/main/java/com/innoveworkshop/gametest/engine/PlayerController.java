package com.innoveworkshop.gametest.engine;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.innoveworkshop.gametest.R;
public class PlayerController {
    protected Button upButton;
    protected Button downButton;
    protected Button leftButton;
    protected Button rightButton;
    protected GameObject targetGameobject;
    private boolean holdingUp, holdingDown, holdingLeft, holdingRight;
    private Vector acceleration;
    private Vector velocity;
    public PlayerController(GameObject gameObject, Button[] buttonArray) {
        this.acceleration = new Vector(1, 1);
        this.velocity = new Vector(0, 0);

        targetGameobject = gameObject;
        this.upButton = buttonArray[0];
        this.downButton = buttonArray[1];
        this.leftButton = buttonArray[2];
        this.rightButton = buttonArray[3];

        this.upButton.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    holdingUp = true;

                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    holdingUp = false;
                }
                return false;
            }
        });

        this.downButton.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    holdingDown = true;

                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    holdingDown = false;
                }
                return false;
            }
        });

        this.leftButton.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    holdingLeft = true;

                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    holdingLeft = false;
                }
                return false;
            }
        });

        this.rightButton.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    holdingRight = true;

                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    holdingRight = false;
                }
                return false;
            }
        });
    }

    public void onFixedUpdate() {
        if (holdingUp) {
            velocity.y += acceleration.y * -1;
        }
        else if (holdingDown) {
            velocity.y += acceleration.y;
        }
        else {
            // If we aren't pressing a direction, slow down anyway
            velocity.y -= Integer.signum((int) velocity.y);
        }

        if (holdingLeft) {
            velocity.x += acceleration.x * -1;
        }
        else if (holdingRight) {
            velocity.x += acceleration.x * 1;
        }
        else {
            velocity.x -= Integer.signum((int) velocity.x);
        }

        targetGameobject.position.x += velocity.x;
        targetGameobject.position.y += velocity.y;
    }
}
