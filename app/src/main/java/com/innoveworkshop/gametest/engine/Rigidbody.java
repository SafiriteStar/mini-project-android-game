package com.innoveworkshop.gametest.engine;

public class Rigidbody {
    public Vector acceleration;
    public Vector velocity;
    public int drag = 0;
    public GameObject parentTransform;

    public Rigidbody(GameObject parentTransform) {
        this.parentTransform = parentTransform;
        acceleration = new Vector(0, 0);
        velocity = new Vector(0, 0);
    }

    public void OnFixedUpdate() {
        // Our acceleration
        velocity.x += acceleration.x;
        velocity.y += acceleration.y;
        // Our drag force
        velocity.x -= Integer.signum((int) velocity.x) * drag;
        velocity.y -= Integer.signum((int) velocity.y) * drag;

        if (parentTransform instanceof Caged) {
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

        parentTransform.position.x += velocity.x;
        parentTransform.position.y += velocity.y;
    }
}
