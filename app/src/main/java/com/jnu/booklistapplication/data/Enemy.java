package com.jnu.booklistapplication.data;

import android.graphics.Rect;

import com.jnu.booklistapplication.R;

public class Enemy {
    public static int imgWidth = 320, imgHeight = 240;
    public static int moveRate = 30;
    private float x, y; //在屏幕上的位置
    private boolean isShot; //是否被击中
    private int image; //图像ID
    private float maxWidth, maxHeight;
    public Rect srcRect;
    public Rect destRect;

    public Enemy(float maxWidth, float maxHeight) {
        this.x = (float) Math.random() * (maxWidth - imgWidth / 2);
        this.y = imgHeight / 2;
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
        this.image = R.drawable.gmae_enemy_plane;
        this.srcRect = new Rect(0, 0, imgWidth, imgHeight);
        this.destRect = new Rect((int) x - imgWidth / 2, (int) y - imgHeight / 2, (int) x + imgWidth / 2, (int) y + imgHeight / 2);
    }

    public boolean move() {
        if (y + moveRate < maxHeight) {
            y += moveRate;
            this.destRect.set((int) x - imgWidth / 2, (int) y - imgHeight / 2, (int) x + imgWidth / 2, (int) y + imgHeight / 2);
            return true;
        }
        return false;
    }

    public int getImage() {
        return image;
    }
}
