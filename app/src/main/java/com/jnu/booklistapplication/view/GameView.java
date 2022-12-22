package com.jnu.booklistapplication.view;

import static com.jnu.booklistapplication.GameActivity.btnExit;
import static com.jnu.booklistapplication.GameActivity.btnRestart;
import static com.jnu.booklistapplication.GameActivity.btnShoot;
import static com.jnu.booklistapplication.GameActivity.layoutGameOver;
import static com.jnu.booklistapplication.GameActivity.rockerView;

import com.jnu.booklistapplication.GameActivity;
import com.jnu.booklistapplication.R;
import com.jnu.booklistapplication.data.Bullet;
import com.jnu.booklistapplication.data.Enemy;
import com.jnu.booklistapplication.data.Player;
import com.jnu.booklistapplication.utils.BitmapUtil;
import com.kongqw.rockerlibrary.view.RockerView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;
    private DrawThread drawThread;
    private Player player;
    private float touchX = -1, touchY = -1;
    private boolean isTouched;
    private float maxWidth, maxHeight;
    private int hitCount = 0;
    public long gameTime = 10;
    private boolean isStop = false;

    public GameView(Context context) {

        super(context);
        initView();
    }

    public GameView(Context context, AttributeSet attrs) {

        super(context, attrs);
        initView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        initView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    public void initView() {

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        isTouched = false;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

        maxHeight = GameView.this.getHeight();
        maxWidth = GameView.this.getWidth();
        drawThread = new DrawThread();
        drawThread.start();
        rockerView.setOnAngleChangeListener(new RockerView.OnAngleChangeListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void angle(double angle) {

                player.setDirection(angle); // 改变玩家飞机方向
                player.move();
            }

            @Override
            public void onFinish() {

            }
        });

        btnShoot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                isTouched = true;
            }
        });

        btnRestart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                layoutGameOver.setVisibility(GONE);
                isStop = false;
                drawThread = new DrawThread(); //要放在这里，不能放在构造函数或者别的地方
                drawThread.start();
            }
        });

        btnExit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                ((Activity) getContext()).finish();
            }
        });

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        //drawThread.myStop();
    }

    private class DrawThread extends Thread {

        //private ArrayList<CircleSprite> sprites = new ArrayList<>();
        private ArrayList<Bullet> bullets = new ArrayList<>();
        private ArrayList<Enemy> enemies = new ArrayList<>();

        private Runnable runnable = new Runnable() {

            @Override
            public void run() {

                while (true) {
                    try {
                        enemies.add(new Enemy(GameView.this.getWidth(), GameView.this.getHeight()));
                        sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        };

        private Runnable gameTimeRunnable = new Runnable() {

            @Override
            public void run() {

                while (gameTime > 0) {
                    try {
                        gameTime--;
                        sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                myStop();
            }
        };

        public DrawThread() {

            player = new Player(GameView.this.getWidth(), GameView.this.getHeight());
//            enemies.add(new Enemy(GameView.this.getWidth(), GameView.this.getHeight()));
//            sprites.add(new CircleSprite(100,100,100,GameView.this.getWidth(),GameView.this.getHeight()));
//            sprites.add(new CircleSprite(500,500,300,GameView.this.getWidth(),GameView.this.getHeight()));
        }

        public void myStop() {

            isStop = true;
            gameTime = 10;
            hitCount = 0;
            bullets.clear();
            enemies.clear();

            ((GameActivity) getContext()).runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    layoutGameOver.setVisibility(VISIBLE);
                }
            });

            while (true) {
                try {
                    this.join();
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // 判断子弹是否击中敌机
        public void isHitted(Bullet bullet) {

            for (int i = enemies.size() - 1; i >= 0; i--) {
                Enemy enemy = enemies.get(i);

                if (bullet.x <= enemy.destRect.centerX() + Enemy.imgWidth / 2
                        && bullet.x >= enemy.destRect.centerX() - Enemy.imgWidth / 2
                        && bullet.y <= enemy.destRect.centerY() + Enemy.imgHeight / 2
                        && bullet.y >= enemy.destRect.centerY() - Enemy.imgHeight / 2) {

                    enemies.remove(i);
                    hitCount++;
                }
            }
        }

        @Override
        public void run() {

            super.run();
            Canvas canvas = null;
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setAntiAlias(true);
            paint.setTextSize(60);
            new Thread(runnable).start();
            new Thread(gameTimeRunnable).start();

            while (!isStop) {
                try {
                    canvas = surfaceHolder.lockCanvas();//获得一块画布
                    Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.game_background2);

                    if (background != null) {
                        canvas.drawBitmap(background, new Rect(0, 0, (int) maxWidth, (int) maxHeight),
                                new Rect(0, 0, (int) maxWidth, (int) maxHeight), paint);
                    }

                    canvas.drawText("剩余时间: " + gameTime, 0, 100, paint);
                    canvas.drawText("击中数: " + hitCount, maxWidth / 2, 100, paint);
                    Bitmap playerImage = BitmapFactory.decodeResource(getResources(), player.getImage());
                    Bitmap enemyImage = BitmapFactory.decodeResource(getResources(), R.drawable.gmae_enemy_plane);
                    enemyImage = new BitmapUtil().zoomImg(enemyImage, Enemy.imgWidth, Enemy.imgHeight);
                    playerImage = new BitmapUtil().zoomImg(playerImage, Player.imgWidth, Player.imgHeight);

                    if (playerImage != null) {
                        canvas.drawBitmap(playerImage, player.srcRect, player.destRect, paint);
                        //Log.e("Rect",player.destRect.centerX()+" "+player.destRect.centerY()+"");
                    }

                    if (isTouched) {
                        bullets.add(new Bullet((int) player.x, (int) player.y - Player.imgHeight / 2, 0, maxWidth, maxHeight));
                        isTouched = false;
                    }

                    for (int i = bullets.size() - 1; i >= 0; i--) { //因为可能要在循环中删元素，反向遍历保证不会出错
                        Bullet bullet = bullets.get(i);
                        if (0 == bullet.type)
                            if (!bullet.moveUp())
                                bullets.remove(i);
                            else {
                                canvas.drawCircle(bullet.x, bullet.y, bullet.radius, bullet.paint);
                                isHitted(bullet); //击中判定
                            }
                        else if (!bullets.get(i).moveDown())
                            bullets.remove(i);
                        else
                            canvas.drawCircle(bullet.x, bullet.y, bullet.radius, bullet.paint);
                    }

                    for (int i = enemies.size() - 1; i >= 0; i--) {
                        Enemy enemy = enemies.get(i);

                        if (!enemy.move())
                            enemies.remove(i);
                        else if (enemyImage != null) {
                            canvas.drawBitmap(enemyImage, enemy.srcRect, enemy.destRect, paint);
                            //Log.e("Rect",player.destRect.centerX()+" "+player.destRect.centerY()+"");
                        }
                    }
                } catch (Exception e) {
                    Log.e("异常", e.getMessage());
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);//推出去刷新
                    }
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}