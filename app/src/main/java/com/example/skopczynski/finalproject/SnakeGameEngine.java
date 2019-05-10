package com.example.skopczynski.finalproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

// Tutorial used as starting point: http://gamecodeschool.com/android/coding-a-snake-game-for-android/

public class SnakeGameEngine extends SurfaceView implements Runnable {
    private Thread thread = null;

    private Context context;


    private int screenX;
    private int screenY;


    private int wallX[];
    private int wallY;

    private int blockSize;

    private final int NUM_BLOCKS_WIDE = 40;
    private int numBlocksHigh;

    private long nextFrameTime;

    private long FPS = 60;
    private final long MILLIS_PER_SECOND = 1000;

    private int level;

    private double snakeX;
    private double snakeY;

    private String pauseOrPlay;
    private volatile boolean isPlaying;
    private boolean firstMove;
    private Canvas canvas;
    private int spreadFactor;
    private SurfaceHolder surfaceHolder;

    private double oldSnakeX;

    private Paint paint;

    public SnakeGameEngine(Context context, Point size) {
        super(context);
        context = context;

        screenX = size.x;
        screenY = size.y;

        blockSize = screenX / NUM_BLOCKS_WIDE;
        numBlocksHigh = screenY / blockSize;

        surfaceHolder = getHolder();
        paint = new Paint();
        pauseOrPlay = "Start";
        snakeX = 0.0;
        snakeY = 0.0;
        spreadFactor = 5;
        wallX = new int[5];

        newGame();
    }
    @Override
    public void run() {

        while (isPlaying) {

            if(updateRequired()) {
                update();
                draw();
            }
            if(pauseOrPlay != "Start" && nextFrameTime < System.currentTimeMillis())
                this.pause();

        }
    }

    public void pause() {
        isPlaying = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }
    public void newGame() {
        snakeX = NUM_BLOCKS_WIDE / 2;
        snakeY = numBlocksHigh;
        spreadFactor = 5;
        spawnWall();

        level = 1;
        nextFrameTime = System.currentTimeMillis();

    }
    public void spawnWall() {
        Random random = new Random();
        wallX[0] = random.nextInt(NUM_BLOCKS_WIDE - 1) + 1;
        for(int i = 1; i < 5; i++){
            int randomNum = random.nextInt(2);
            if(randomNum == 1) {
                if(wallX[0] < (NUM_BLOCKS_WIDE -(spreadFactor + 1)))
                    wallX[i] = wallX[0] + (random.nextInt(spreadFactor) + 1);
                else {
                    wallX[i] = wallX[0] - (random.nextInt(spreadFactor) + 1);
                }
            }
            else{
                if(wallX[0] > (spreadFactor+1))
                    wallX[i] = wallX[0] - (random.nextInt(spreadFactor) + 1);
                else{
                    wallX[i] = wallX[0] + (random.nextInt(spreadFactor) + 1);
                }
            }
        }
        wallY = random.nextInt(numBlocksHigh - (numBlocksHigh/2)) + 5;
    }
    private void moveSnake(){
        if((int)snakeY == 0){
            spreadFactor+=1;
            level+=1;
            spawnWall();
            snakeY = (float) numBlocksHigh;
            snakeX = NUM_BLOCKS_WIDE / 2;

        }
        else {
            snakeY = snakeY - 0.2;

        }
    }

    public void update() {

        if (((int)snakeX == wallX[0] || (int)snakeX == wallX[0] - 1 || (int)snakeX == wallX[0] + 1) && (
                (int)snakeY == 5)) {

        }
        else if (((int)snakeX == wallX[1] || (int)snakeX == wallX[1] - 1 || (int)snakeX == wallX[1] + 1) && (
                (int)snakeY == 15)) {

        }
        else if (((int)snakeX == wallX[2] || (int)snakeX == wallX[2] - 1 || (int)snakeX == wallX[2] + 1) && (
                (int)snakeY == 25)) {

        }
        else if (((int)snakeX == wallX[3] || (int)snakeX == wallX[3] - 1 || (int)snakeX == wallX[3] + 1) && (
                (int)snakeY == 35)) {

        }
        else if (((int)snakeX == wallX[4] || (int)snakeX == wallX[4] - 1 || (int)snakeX == wallX[4] + 1) && (
                (int)snakeY == 45)) {

        }
        else if ((int)snakeY == 5 || (int)snakeY == 15 || (int)snakeY == 25 || (int)snakeY == 35 || (int)snakeY == 45) {


            newGame();
        }

        moveSnake();


    }
    public void draw() {

        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            canvas.drawColor(Color.BLACK);

            paint.setColor(Color.WHITE);

            paint.setTextSize(50);
            canvas.drawText("Level:" + level, 10, 70, paint);
            canvas.drawText(pauseOrPlay, screenX - screenX/5, 70, paint);
            paint.setColor(Color.CYAN);
            canvas.drawRect((float)snakeX * blockSize,
                    (float)(snakeY * blockSize),
                    (float)(snakeX * blockSize) + blockSize,
                    (float)(snakeY * blockSize) + blockSize,
                        paint);
            paint.setColor(Color.RED);

            for (int i = 0; i < NUM_BLOCKS_WIDE; i++) {
                for (int j = 5; j < 55; j = j + 10) {
                    if ((i == wallX[0] || i == wallX[0] - 1 || i == wallX[0] + 1 ) && j == 5) {
                        paint.setColor(Color.CYAN);
                        canvas.drawRect(i * blockSize,
                                (j * blockSize),
                                (i * blockSize) + blockSize,
                                (j * blockSize) + blockSize,
                                paint);
                        paint.setColor(Color.RED);
                    }
                    else if ((i == wallX[1] || i == wallX[1] - 1 || i == wallX[1] + 1 ) && j == 15) {
                        paint.setColor(Color.CYAN);
                        canvas.drawRect(i * blockSize,
                                (j * blockSize),
                                (i * blockSize) + blockSize,
                                (j * blockSize) + blockSize,
                                paint);
                        paint.setColor(Color.RED);
                    }
                    else if ((i == wallX[2] || i == wallX[2] - 1 || i == wallX[2] + 1 ) && j == 25) {
                        paint.setColor(Color.CYAN);
                        canvas.drawRect(i * blockSize,
                                (j * blockSize),
                                (i * blockSize) + blockSize,
                                (j * blockSize) + blockSize,
                                paint);
                        paint.setColor(Color.RED);
                    }
                    else if ((i == wallX[3] || i == wallX[3] - 1 || i == wallX[3] + 1 ) && j == 35) {
                        paint.setColor(Color.CYAN);
                        canvas.drawRect(i * blockSize,
                                (j * blockSize),
                                (i * blockSize) + blockSize,
                                (j * blockSize) + blockSize,
                                paint);
                        paint.setColor(Color.RED);
                    }
                    else if ((i == wallX[4] || i == wallX[4] - 1 || i == wallX[4] + 1 ) && j == 45) {
                        paint.setColor(Color.CYAN);
                        canvas.drawRect(i * blockSize,
                                (j * blockSize),
                                (i * blockSize) + blockSize,
                                (j * blockSize) + blockSize,
                                paint);
                        paint.setColor(Color.RED);
                    }
                    else {

                        canvas.drawRect(i * blockSize,
                                (j * blockSize),
                                (i * blockSize) + blockSize,
                                (j * blockSize) + blockSize,
                                paint);
                    }
                }
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
    public boolean updateRequired() {
        if(nextFrameTime <= System.currentTimeMillis()){
            nextFrameTime =System.currentTimeMillis() + MILLIS_PER_SECOND / FPS;
            return true;
        }

        return false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if(motionEvent.getX() >= screenX - screenX/4 && motionEvent.getY() <=70){
                    if(pauseOrPlay == "Start"){
                        this.resume();
                        pauseOrPlay = "Pause";
                    }
                    else if(isPlaying){
                        this.pause();
                        pauseOrPlay = "Play";
                    }
                    else{
                        this.resume();
                        pauseOrPlay = "Pause";
                    }
                }
            case MotionEvent.ACTION_DOWN:
                double newSnake = (motionEvent.getX() / (NUM_BLOCKS_WIDE));

                //newSnake += 20;
                snakeX -= (snakeX - newSnake);

                break;
            case MotionEvent.ACTION_MOVE:
                double newX = motionEvent.getX() / NUM_BLOCKS_WIDE;
                if(snakeX > 15)
                    snakeX -= (snakeX - newX) - ((newX - 20) + 5);
                else{
                    snakeX -= (snakeX - newX);
                }
                //oldSnakeX = snakeX;
        }
        return true;
    }
}
