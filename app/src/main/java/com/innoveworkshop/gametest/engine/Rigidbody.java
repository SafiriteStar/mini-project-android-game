package com.innoveworkshop.gametest.engine;

public class Rigidbody {
    public Vector acceleration;
    public Vector velocity;
    public int drag = 0;
    public GameObject parentTransform;
    public boolean caged = false;

    public Rigidbody(GameObject parentTransform) {
        this.parentTransform = parentTransform;
        acceleration = new Vector(0, 0);
        velocity = new Vector(0, 0);
    }

    public void OnFixedUpdate() {
        DistanceFailSafe();
        // Our acceleration
        velocity.x += acceleration.x;
        velocity.y += acceleration.y;
        // Our drag force
        velocity.x -= Integer.signum((int) velocity.x) * drag;
        velocity.y -= Integer.signum((int) velocity.y) * drag;

        if (parentTransform instanceof Caged && caged) {
            ContainWithinScreen();
        }

        parentTransform.position.x += velocity.x;
        parentTransform.position.y += velocity.y;

        // If we go too far, destroy ourselves;
    }

    // If we are too far from the screen boundaries, just destroy ourselves.
    private void DistanceFailSafe() {
        if (parentTransform.position.y >= parentTransform.gameSurface.getHeight() * 2 ||
            parentTransform.position.y <= -parentTransform.gameSurface.getHeight() ||
            parentTransform.position.x >= parentTransform.gameSurface.getWidth() * 2 ||
            parentTransform.position.x <= -parentTransform.gameSurface.getWidth()) {
            parentTransform.destroy();
        }
    }

    private void ContainWithinScreen() {
        // If we hit the floor or ceiling
        if (parentTransform.isFloored() || parentTransform.hitTopWall()) {
            // Move ourselves back inbounds
            while ((parentTransform.isFloored() || parentTransform.hitTopWall())) {
                if (parentTransform.isFloored()) {
                    parentTransform.position.y += -1;
                }
                else {
                    parentTransform.position.y += 1;
                }
            }
            // And stop
            velocity.y = 0;
        }

        // If we hit either wall
        if (parentTransform.hitLeftWall() || parentTransform.hitRightWall()) {
            // Move ourselves back inbounds
            while ((parentTransform.hitRightWall() || parentTransform.hitLeftWall())) {
                if (parentTransform.hitRightWall()) {
                    parentTransform.position.x += -1;
                }
                else {
                    parentTransform.position.x += 1;
                }
            }
            // And stop
            velocity.x = 0;
        }
    }
}
