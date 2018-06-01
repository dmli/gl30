package com.yc.myegl3demo01.utils;

import android.util.Log;

import java.util.Arrays;

public class YCPointMgr {
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private int mLength = 300;
    private int mIndex = 0;
    private float[] mData = null;
    private float mLastX, mLastY;

    public YCPointMgr() {
        mData = new float[mLength];
    }

    public int getLength() {
        return mIndex;
    }

    public int getPointLength() {
        return mIndex / 2;
    }

    public boolean isMax() {
        return mIndex == mLength;
    }

    public void reset() {
        mIndex = 0;
        put(mLastX, mLastY, false);
    }

    public void put(float px, float py) {
        put(px, py, true);
    }

    public void putStartPoint(float px, float py) {
        put(px, py, false);
    }

    private void put(float px, float py, boolean continuously) {
//        ensureExplicitCapacity(mIndex + 4);
        if (continuously && mIndex > 0) {
            mData[mIndex] = mData[mIndex - 2];
            mData[mIndex + 1] = mData[mIndex - 1];
            mData[mIndex + 2] = px;
            mData[mIndex + 3] = py;
        } else {
            mData[mIndex] = px;
            mData[mIndex + 1] = py;
            mData[mIndex + 2] = px;
            mData[mIndex + 3] = py;
        }
        mIndex += 4;
        mLastX = px;
        mLastY = py;
    }

    public float[] getData() {
        return mData;
    }

    private void ensureExplicitCapacity(int minCapacity) {
        // overflow-conscious code
        if (minCapacity - mData.length > 0)
            grow(minCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
    }

    private void grow(int minCapacity) {
        int newCapacity = mData.length + mLength;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        Log.e("cccccccccc", "newCapacity = " + newCapacity);
        mData = Arrays.copyOf(mData, newCapacity);
    }


}
