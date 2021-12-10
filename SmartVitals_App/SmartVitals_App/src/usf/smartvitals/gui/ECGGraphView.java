package usf.smartvitals.gui;

import usf.smartvitals.sensor.BioHarnessECGPacket;
import usf.smartvitals.sensor.BioHarnessGeneralPacket;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

public class ECGGraphView extends View
{
    private Bitmap  mBitmap;
    private Paint   mPaint = new Paint();
    private Canvas  mCanvas = new Canvas();
    private Path    mPath = new Path();
    private RectF   mRect = new RectF();
    private float   mLastValues[] = new float[3*2];
    private int     mColors[] = new int[3*2];
    private float   mLastX;
    private float   mScale[] = new float[1];
    private float   mYOffset;
    private float   mMaxX;
    private float   mSpeed = 0.3f;
    public ECGGraphView(Context context) {
        super(context);
        mColors[0] = Color.RED;
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
        mScale[0] = - (h * 0.5f * (1.0f / (1000)));
        mMaxX = w;
        mLastX = w;
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
                    
                    cavas.drawLine(0,300,maxx, 300,paint); //fixed lines
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
                final float v = mYOffset + packet.getHeartRate() * mScale[0] + 75;
                paint.setColor(mColors[0]);
                
                if (mLastX == 0){
                	canvas.drawLine(mLastX, mLastValues[0], newX, v, paint);}
                else{canvas.drawLine(mLastX, mLastValues[0], newX, v, paint);}
                
                mLastValues[0] = v;
                
                mLastX += mSpeed;
            }
            invalidate();
        }
    }
    
    public void updateGraph(BioHarnessECGPacket packet) {
    	int[] ecgValues = packet.getSamples();
    	for (int i : ecgValues) {
    		updateGraph(i);
    	}
    	/*synchronized (this) {
            if (mBitmap != null) {
                final Canvas canvas = mCanvas;
                final Paint paint = mPaint;
                float deltaX = mSpeed;
                float newX = mLastX + deltaX;
                
                int[] ecgValues = packet.getSamples();
                
                for (int i : ecgValues) {
	                final float v = mYOffset + i * mScale[0] + 75;
	                paint.setColor(mColors[0]);
	                
	                if (mLastX == 0){
	                	canvas.drawLine(mLastX, mLastValues[0], newX, v, paint);}
	                else{canvas.drawLine(mLastX, mLastValues[0], newX, v, paint);}
	                
	                mLastValues[0] = v;
	                
	                mLastX += mSpeed;
                }
            }
            invalidate();
        }*/
    }
    	
    private void updateGraph(int val) {
       	synchronized (this) {
               if (mBitmap != null) {
                   final Canvas canvas = mCanvas;
                   final Paint paint = mPaint;
                   float deltaX = mSpeed;
                   float newX = mLastX + deltaX;
                   
   
	               final float v = mYOffset + val * mScale[0] + 25;
	               paint.setColor(mColors[0]);
	               
	               if (mLastX == 0){
	               	canvas.drawLine(mLastX, mLastValues[0], newX, v, paint);}
	               else{canvas.drawLine(mLastX, mLastValues[0], newX, v, paint);}
	               
	               mLastValues[0] = v;
	              
	               mLastX += mSpeed;
                }
               invalidate();
            }
    	}
}
