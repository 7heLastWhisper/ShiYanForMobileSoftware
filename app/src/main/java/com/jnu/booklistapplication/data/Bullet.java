package com.jnu.booklistapplication.data;

import android.graphics.Color;
import android.graphics.Paint;

// 子弹类
public class Bullet {

    public int x, y, radius;
    public Paint paint;
    private float maxWidth, maxHeight;
    public int type; // 0表示己方子弹，1表示敌方子弹

    public Bullet(int x, int y, int type, float maxWidth, float maxHeight) {

        this.x = x;
        this.y = y;
        this.radius = 10;
        this.type = type;

        paint = new Paint();
        paint.setColor(Color.YELLOW);
    }

    //返回false时表示已超出屏幕边界
    public boolean moveUp() {

        if (y - 40 > 1e-6) {
            y -= 40; // 向上移动

            return true;
        }

        return false;
    }

    public boolean moveDown() {

        if (y + 40 < maxHeight) {
            y += 40;

            return true;
        }

        return false;
    }
}