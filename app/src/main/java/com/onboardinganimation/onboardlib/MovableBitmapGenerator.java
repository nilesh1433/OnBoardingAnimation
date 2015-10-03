package com.onboardinganimation.onboardlib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

/**
 * Created by Nilesh Kumar Tiwari on 8/17/2015.
 */
public class MovableBitmapGenerator {

    private int desiredWidth;
    private int expectedHeight;
    private int desiredHeight;
    private int expectedWidth;
    private float ratio;
    private int paddingY;
    private Bitmap paddedBitmap;
    private int paddingX;
    private Context mContext;

    /**
     * creates bitmap which has drawable center aligned.
     * Need to check why desiredHeight and desiredWidth respectively is disabled.
     *
     * @param context
     * @param bitmap
     * @param devLayoutWidth  - layout defined in devLayoutWidth attr
     * @param devLayoutHeight - layout defined in devLayoutHeight attr
     * @param imageWidth      - actual width of image
     * @param imageHeight     - actual height of image
     * @param desiredWidth    - width desired by view
     * @param desiredHeight   - height desired by view
     * @param drawable
     * @return bitmap which contains the image center_aligned
     */
    public Bitmap createDevBitmap(Context context, Bitmap bitmap, int devLayoutWidth, int devLayoutHeight,
                                  int imageWidth, int imageHeight, int desiredWidth, int desiredHeight, Drawable drawable) {

        ratio = (float) (drawable.getIntrinsicWidth() / (drawable.getIntrinsicHeight() * 1.0));
        mContext = context;

        if (devLayoutWidth == 0) {
            desiredWidth = imageWidth;
        } else {
            desiredWidth = devLayoutWidth;
        }

        if (devLayoutHeight == 0) {
            desiredHeight = imageHeight;
        } else {
            desiredHeight = devLayoutHeight;
        }

        expectedWidth = desiredWidth;
        expectedHeight = desiredHeight;

        if (ratio > 1) {
            //make width for view as default
            //expectedWidth = desiredWidth;
            expectedHeight = (int) (expectedWidth / ratio);
            if (expectedHeight > drawable.getIntrinsicHeight()) {
                expectedHeight = imageHeight;
            }else {
                paddingY = desiredHeight - expectedHeight;
            }
        } else if(ratio < 1){
            //make height for view as default
            //expectedHeight = desiredHeight;
            expectedWidth = (int) (expectedHeight * ratio);
            if (expectedWidth > drawable.getIntrinsicWidth()) {
                expectedWidth = imageWidth;
            }else {
                paddingX = desiredWidth - expectedWidth;
            }
        }

        paddedBitmap = Bitmap.createBitmap(
                (int) convertPxToDp(imageWidth) + paddingX,
                (int) convertPxToDp(imageHeight) + paddingY,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(paddedBitmap);
        canvas.drawARGB(0x00000000, 0x00000000, 0x00000000, 0x00000000); // this represents transparent color

        /*canvas.drawBitmap(
                Bitmap.createScaledBitmap(bitmap, expectedWidth, expectedHeight, false),
                paddingX / 2,
                paddingY / 2,
                new Paint(Paint.FILTER_BITMAP_FLAG));*/
        canvas.drawBitmap(
                Bitmap.createScaledBitmap(bitmap, expectedWidth, expectedHeight, false),
                paddingX / 2,
                paddingY / 2,
                new Paint(Paint.FILTER_BITMAP_FLAG));
        return paddedBitmap;
    }

    /**
     * creates bitmap which has drawable center aligned.
     *
     * @param context
     * @param bitmap
     * @param devLayoutWidth  - layout defined in devLayoutWidth attr
     * @param devLayoutHeight - layout defined in devLayoutHeight attr
     * @param imageWidth      - actual width of image
     * @param imageHeight     - actual height of image
     * @param desiredWidth    - width desired by view
     * @param desiredHeight   - height desired by view
     * @param drawable
     * @return bitmap which contains the image center_aligned
     */

    public Bitmap createBitmap(Context context, Bitmap bitmap, int devLayoutWidth, int devLayoutHeight,
                               int imageWidth, int imageHeight, int desiredWidth, int desiredHeight, Drawable drawable) {

        ratio = (float) (drawable.getIntrinsicWidth() / (drawable.getIntrinsicHeight() * 1.0));
        mContext = context;

        expectedWidth = imageWidth;
        expectedHeight = imageHeight;

        if (ratio > 1) {
            //make width for view as default
            expectedWidth = desiredWidth;
            expectedHeight = (int) (expectedWidth / ratio);
            if (expectedHeight > imageHeight) {
                expectedHeight = imageHeight;
            }else {
                paddingY = desiredHeight - expectedHeight;
            }
        } else {
            //make height for view as default
            expectedHeight = desiredHeight;
            expectedWidth = (int) (expectedHeight * ratio);
            if (expectedWidth > imageWidth) {
                expectedWidth = imageWidth;
            }else {
                paddingX = desiredWidth - expectedWidth;
            }
        }

        paddedBitmap = Bitmap.createBitmap(
                expectedWidth + paddingX,
                expectedHeight + paddingY,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(paddedBitmap);
        canvas.drawARGB(0x00000000, 0x00000000, 0x00000000, 0x00000000); // this represents transparent color

        canvas.drawBitmap(
                Bitmap.createScaledBitmap(bitmap, expectedWidth, expectedHeight, false),
                paddingX / 2,
                paddingY / 2,
                new Paint(Paint.FILTER_BITMAP_FLAG));
        return paddedBitmap;
    }

    /**
     * converts dp to px
     *
     * @param value
     * @return
     */
    private float convertPxToDp(int value) {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        float dp = value / (metrics.densityDpi / 160f);
        return dp;
    }
}
