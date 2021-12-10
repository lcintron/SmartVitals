package usf.smartvitals.gui;

import usf.smartvitals.sensor.BioHarnessAccelerometerPacket;
import usf.smartvitals.sensor.BioHarnessGeneralPacket;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

	
public class AccGraphview extends View
{
    private Bitmap  mBitmap;
    private Paint   mPaint = new Paint();
    private Canvas  mCanvas = new Canvas();
    private Path    mPath = new Path();
    private RectF   mRect = new RectF();
    private float   mLastValues[] = new float[3*2];
    private int     mColors[] = new int[3*2];
    private float   mLastX;
    private float   mScale[] = new float[3];
    private float   mYOffset;
    private float   mMaxX;
    private float   mSpeed = 0.5f;
    public AccGraphview(Context context) {
        super(context);
        mColors[0] = Color.GREEN;
        mColors[1] = Color.BLUE;
        mColors[2] = Color.BLACK;

        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mRect.set(-0.5f, -0.5f, 0.5f, 0.5f);
        mPath.arcTo(mRect, 0, 180);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        mCanvas.setBitmap(mBitmap);
        mCanvas.drawColor(0xFFFFFFFF);
        mYOffset = h * 0.5f;
        mScale[0] = - (h * 0.5f * (1.0f / (100)));
        mScale[1] = - (h * 0.5f * (1.0f / (100)));
        mScale[2] = - (h * 0.5f * (1.0f / (100)));

        mMaxX = w;        
        mLastX = mMaxX ;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        synchronized (this) {
            if (mBitmap != null) {
                final Paint paint = mPaint;
                if (mLastX >= mMaxX) {
                    mLastX = 0;
                    final Canvas cavas = mCanvas;
                    final float maxx = mMaxX;
                    paint.setColor(0xFFAAAAAA);
                    cavas.drawColor(0xFFFFFFFF);
                    
                    cavas.drawLine(0, 300,      maxx, 300,      paint); //fixed lines
                }
                canvas.drawBitmap(mBitmap, 0, 0, null);

             }
        }
    }
 
    public void updateGraph(BioHarnessGeneralPacket packet) {
    	synchronized (this) {
            if (mBitmap != null) {
                final Canvas canvas = mCanvas;
                final Paint paint = mPaint;
                float deltaX = mSpeed;
                float newX = mLastX + deltaX;
                int initialxValue = -150;
                
                final float u = (float) (mYOffset + packet.getLateralAxePeak()/10.0 * mScale[0] + 0) ;
                paint.setColor(mColors[0]);
                if (mLastX == 0){
                	canvas.drawLine(initialxValue, mLastValues[0] , newX, u, paint);}
                else{canvas.drawLine(mLastX, mLastValues[0] , newX, u, paint);}
                mLastValues[0] = u;
                                 
                final float w = (float) (mYOffset + packet.getVerticalAxePeak()/10.0 * mScale[1] + 30);
                paint.setColor(mColors[1]);
                if (mLastX == 0){
                	canvas.drawLine(initialxValue, mLastValues[1] , newX , w, paint);}
                else{canvas.drawLine(mLastX, mLastValues[1] , newX , w, paint);}
                mLastValues[1] = w;
                
                final float t = (float) (mYOffset + packet.getSagitalAxePeak()/10.0 * mScale[2] + 60);
                paint.setColor(mColors[2]);
                if (mLastX == 0){
                	canvas.drawLine(initialxValue, mLastValues[2] , newX , t, paint);}
                else{canvas.drawLine(mLastX, mLastValues[2] , newX , t, paint);}
                mLastValues[2] = t;
                
                mLastX += mSpeed;
            }
            invalidate();
        }
    }
    
    public void updateGraph(BioHarnessAccelerometerPacket packet) {
    	int[] accX = packet.getX();
    	int[] accY = packet.getY();
    	int[] accZ = packet.getZ();
    	
    	for (int i = 0; i < accX.length-1; i++) {
    		updateGraph(accX[i]/10,accY[i]/10,accZ[i]/10);
    	}
    }
    
    private void updateGraph(int x, int y, int z) {
    	synchronized (this) {
            if (mBitmap != null) {
                final Canvas canvas = mCanvas;
                final Paint paint = mPaint;
                float deltaX = mSpeed;
                float newX = mLastX + deltaX;
                int initialxValue = -150;
                
                final float u = (float) (mYOffset + x * mScale[0] + 0) ;
                paint.setColor(mColors[0]);
                if (mLastX == 0){
                	canvas.drawLine(initialxValue, mLastValues[0] , newX, u, paint);}
                else{canvas.drawLine(mLastX, mLastValues[0] , newX, u, paint);}
                mLastValues[0] = u;
                                 
                final float w = (float) (mYOffset + y * mScale[1] + 30);
                paint.setColor(mColors[1]);
                if (mLastX == 0){
                	canvas.drawLine(initialxValue, mLastValues[1] , newX , w, paint);}
                else{canvas.drawLine(mLastX, mLastValues[1] , newX , w, paint);}
                mLastValues[1] = w;
                
                final float t = (float) (mYOffset + z * mScale[2] + 60);
                paint.setColor(mColors[2]);
                if (mLastX == 0){
                	canvas.drawLine(initialxValue, mLastValues[2] , newX , t, paint);}
                else{canvas.drawLine(mLastX, mLastValues[2] , newX , t, paint);}
                mLastValues[2] = t;
                
                mLastX += mSpeed;
            }
            invalidate();
        }
    }
}
