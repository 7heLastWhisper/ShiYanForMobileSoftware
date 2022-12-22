package com.jnu.booklistapplication.data;

import android.graphics.Rect;

import com.jnu.booklistapplication.R;

public class Player {
    public static int imgWidth = 260, imgHeight = 195;
    public static int moveRate = 20;
    public float x, y; //图片中心坐标
    private int image;
    private double direction;
    private float maxWidth, maxHeight;
    public Rect srcRect;
    public Rect destRect;

    public Player(float maxWidth, float maxHeight) {
        this.x = maxWidth / 2;
        this.y = maxHeight - 300;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.image = R.drawable.game_player_plane;
        this.srcRect = new Rect(0, 0, imgWidth, imgHeight);
        this.destRect = new Rect((int) x - imgWidth / 2, (int) y - imgHeight / 2, (int) x + imgWidth / 2, (int) y + imgHeight / 2);
    }

    public void move() {
        if (direction > 45 && direction < 135) { //向下
            if (isHeightAllowed(y + moveRate))
                y += moveRate;
        } else if (direction > 135 && direction < 225) {//向左
            if (isWidthAllowed(x - moveRate))
                x -= moveRate;
        } else if (direction > 225 && direction < 315) {//向上
            if (isHeightAllowed(y - moveRate))
                y -= moveRate;
        } else if ((direction > 315 && direction < 360) || (direction > 0 && direction < 45)) {//向右
            if (isWidthAllowed(x + moveRate))
                x += moveRate;
        }
        destRect.set((int) x - imgWidth / 2, (int) y - imgHeight / 2, (int) x + imgWidth / 2, (int) y + imgHeight / 2);
    }

    private boolean isWidthAllowed(float v) { //判断是否超出屏幕范围
        return v > 1e-6 + imgWidth / 2 && v < maxWidth;
    }

    private boolean isHeightAllowed(float v) {
        return v > 1e-6 + imgHeight / 2 && v < maxHeight;
    }


    public void setDirection(double direction) {
        this.direction = direction;
    }

    public int getImage() {
        return image;
    }

    public void setSrcRect(Rect srcRect) {
        this.srcRect = srcRect;
    }

    public void setDestRect(Rect destRect) {
        this.destRect = destRect;
    }

    public Rect getSrcRect() {
        return this.srcRect;
    }

    public Rect getDestRect() {
        return this.destRect;
    }

}
