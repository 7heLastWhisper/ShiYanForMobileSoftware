package com.jnu.booklistapplication.data;

import static java.lang.Math.random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class CircleSprite {
    private float x, y, radius;
    double direction;
    float maxWidth, maxHeight;

    public CircleSprite(float x, float y, float radius, float maxWidth, float maxHeight) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.direction = Math.random();
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        canvas.drawCircle(x, y, radius, paint);

    }

    public void move() {
        this.x += (float) (20 * Math.cos(direction)); //要递增才有动画效果
        this.y += (float) (20 * Math.sin(direction));
        if (this.x < 0) this.x += maxWidth;
        if (this.y < 0) this.y += maxHeight;
        if (this.x > maxWidth) this.x -= maxWidth;
        if (this.y > maxHeight) this.y -= maxHeight;
    }

    public boolean isShot(float touchX, float touchY) {
        double distance = (touchX - this.x) * (touchX - this.x) + (touchY - this.y) * (touchY - this.y);
        return Math.abs(distance) < radius * radius;
    }
}