package com.innoveworkshop.gametest.engine;

import com.innoveworkshop.gametest.engine.Circle;

public class CircleObstacle extends Circle implements Collidable {
    private float lifeTime;
    private float lifeTimer;
    public CircleObstacle(float x, float y, float radius, int color, float lifeTime) {
        super(x, y, radius, color);
        hasCollider = true;
        rigidbody = new Rigidbody(this);
        this.lifeTime = lifeTime;
    }

    @Override
    public void onFixedUpdate() {
        super.onFixedUpdate();
        if (lifeTimer > lifeTime) {
            destroy();
        }
        else {
            lifeTimer += 1;
        }
    }

    @Override
    public boolean CollisionWithCircle(float x, float y, float r) {
        return Math.sqrt(Math.pow(x - position.x, 2) + Math.pow(y - position.y, 2)) <= radius + r;
    }
}
