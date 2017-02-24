package com.kris.record;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Vector;

/**
 * Created by Kris on 2017/2/24.
 */

public class SoundView extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    private SurfaceHolder mHolder;
    private boolean mIsDrawing;
    private Canvas mCanvas;
    private Vector<Float> points = new Vector<Float>();
    private int mWidth;
    private int mHeight;
    private int mTimes = 10;
    private Paint mPaint;

    public SoundView(Context context) {
        super(context);
        initView();
    }

    public SoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
        setZOrderOnTop(true);
//        mHolder.setFormat(PixelFormat.TRANSLUCENT);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(0xff0099cc);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setPathEffect(new CornerPathEffect(1));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mWidth = width;
            mHeight = height;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    public void  startDraw(){
        mIsDrawing = true;
        new Thread(this).start();
    }


    public void stopDraw(){
        mIsDrawing = false;
    }
    @Override
    public void run() {
        while(mIsDrawing){
            draw();
        }
    }

    public void addData(Float pointY){
        if (points.size()*mTimes>=mWidth){
            points.removeElementAt(0);
        }
        points.addElement(pointY);
    }

    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            resetCanvas(mCanvas);
            float tempX = 0;
            float centerY = mHeight /2f;
            float avgHeight = mHeight / 10;
            float resultY =  centerY ;
            float tempY =centerY;
            for (int i=0;i<points.size();i++){
                if (points.elementAt(i)*avgHeight<centerY){
                    resultY = centerY-points.elementAt(i)*avgHeight;
                }else{
                    resultY = points.elementAt(i)*avgHeight;
                }
                mCanvas.drawLine(tempX+mTimes*(i-1),tempY,tempX+mTimes*i,resultY,mPaint);
                tempY = resultY;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (mCanvas != null){
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    private void resetCanvas(Canvas mCanvas) {
        mCanvas.drawColor(Color.WHITE, PorterDuff.Mode.SCREEN);
    }
}
