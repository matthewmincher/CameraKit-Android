package com.wonderkiln.camerakit;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.nio.ByteBuffer;

public class BitmapOperator {

    private ByteBuffer handler;

    private BitmapOperator() {
    }

    public BitmapOperator(final Bitmap bitmap) {
        storeBitmap(bitmap);

    }

    private void storeBitmap(final Bitmap bitmap) {
        if (handler != null) freeBitmap();
        handler = jniStoreBitmapData(bitmap);
    }

    public void rotateBitmap(int degrees) {
        if (handler == null) return;
        if (degrees == 90){
            jniRotateBitmapCw90(handler);
        } else if (degrees == 180){
            jniRotateBitmap180(handler);
        } else if (degrees == 270){
            jniRotateBitmapCcw90(handler);
        }
    }

    public void cropBitmap(final int left, final int top, final int right, final int bottom) {
        if (handler == null) return;
        jniCropBitmap(handler, left, top, right, bottom);
    }

    public void flipBitmapHorizontal() {
        if (handler == null) return;
        jniFlipBitmapHorizontal(handler);
    }

    public void flipBitmapVertical() {
        if (handler == null) return;
        jniFlipBitmapVertical(handler);
    }

    public byte[] getJpeg(int quality) {
        if (handler == null) return null;
        return jniGetJpegData(handler, quality);
    }

    public byte[] getJpegAndFree(int quality) {
        final byte[] jpeg = getJpeg(quality);
        freeBitmap();
        return jpeg;
    }

    public int getWidth(){
        return jniGetBitmapWidth(handler);
    }
    public int getHeight(){
        return jniGetBitmapHeight(handler);
    }

    public Bitmap getBitmap() {
        if (handler == null) return null;
        return jniGetBitmapFromStoredBitmapData(handler);
    }

    public Bitmap getBitmapAndFree() {
        final Bitmap bitmap = getBitmap();
        freeBitmap();
        return bitmap;
    }

    private void freeBitmap() {
        if (handler == null) return;
        jniFreeBitmapData(handler);
        handler = null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (handler == null) return;
        freeBitmap();
    }

    static {
        System.loadLibrary("JniYuvOperator");
        System.loadLibrary("jpgd");
        System.loadLibrary("jpge");
        System.loadLibrary("JniBitmapOperator");
    }

    private native ByteBuffer jniStoreBitmapData(Bitmap bitmap);

    private native Bitmap jniGetBitmapFromStoredBitmapData(ByteBuffer handler);

    private native byte[] jniGetJpegData(ByteBuffer handler, int quality);

    private native void jniFreeBitmapData(ByteBuffer handler);

    private native void jniRotateBitmapCcw90(ByteBuffer handler);

    private native void jniRotateBitmapCw90(ByteBuffer handler);

    private native void jniRotateBitmap180(ByteBuffer handler);

    private native void jniCropBitmap(ByteBuffer handler, final int left, final int top, final int right, final int bottom);

    private native void jniFlipBitmapHorizontal(ByteBuffer handler);

    private native void jniFlipBitmapVertical(ByteBuffer handler);

    private native int jniGetBitmapWidth(ByteBuffer handler);

    private native int jniGetBitmapHeight(ByteBuffer handler);

}
