package com.onboardinganimation.onboardlib;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.onboardinganimation.R;

/**
 * Created by Nilesh Kumar Tiwari on 7/31/2015.
 */
public class MovableView extends View {

    private float endY;
    private float endX;
    private float startY;
    private float startX;
    private int img;
    private Bitmap imgBitmap;
    private Canvas canvas;
    private float currentX;
    private float currentY;
    private RectF rectF;
    private int mHeight;
    private int mWidth;
    private int imageHeight;
    private int imageWidth;
    private BitmapDrawable drwable;
    private int widthMode;
    private int widthSize;
    private int heightMode;
    private int heightSize;
    private Resources resourceObject;
    private Bitmap actualBitmapToBeDrawn;
    private boolean startXLeft;
    private boolean startYBottom;
    private boolean startXRight;
    private boolean startYTop;
    private boolean endXLeft;
    private boolean endXRight;
    private boolean endYTop;
    private boolean endYBottom;
    private Context mContext;
    private int parentHeight;
    private int parentWidth;
    private boolean startYCenterVertical;
    private boolean startXCenterHorizontal;
    private boolean endXCenterHorizontal;
    private boolean endYCenterVertical;
    private boolean isShowLayoutDev;
    private int devLayoutWidth;
    private int devLayoutHeight;
    private Mode renderingMode;
    private MovableBitmapGenerator movableBitmapGenerator;
    private boolean isSetUpXAndYDone;

    private enum Mode {NORMAL, DEV}

    public MovableView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MovableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MovableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * initializes all the attributes and bitmap to be drawn
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        this.mContext = context;
        resourceObject = getResources();

        movableBitmapGenerator = new MovableBitmapGenerator();
        setUpAttributes(attrs, defStyleAttr);
        decideMode();
        getImageToBeDrawn();
    }

    /**
     * decides which mode it is,
     * NORMAL for device rendering
     * DEV for layout rendering, to help developer to decide position of images
     */
    private void decideMode() {
        if (!isShowLayoutDev) {
            renderingMode = Mode.NORMAL;
            return;
        }
        renderingMode = Mode.DEV;
    }

    /**
     * gets the attributes defined in xml for the view
     *
     * @param attrs
     * @param defStyleAttr
     */
    private void setUpAttributes(AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.AutoOnBoardAnimation, defStyleAttr, 0);
            try {
                img = ta.getResourceId(R.styleable.AutoOnBoardAnimation_image, 0);
                startX = ta.getDimension(R.styleable.AutoOnBoardAnimation_startX, 0f);
                startY = ta.getDimension(R.styleable.AutoOnBoardAnimation_startY, 0f);
                endX = ta.getDimension(R.styleable.AutoOnBoardAnimation_endX, 0f);
                endY = ta.getDimension(R.styleable.AutoOnBoardAnimation_endY, 0f);

                startXLeft = ta.getBoolean(R.styleable.AutoOnBoardAnimation_startX_left, false);
                startXRight = ta.getBoolean(R.styleable.AutoOnBoardAnimation_startX_right, false);
                startXCenterHorizontal = ta.getBoolean(R.styleable.AutoOnBoardAnimation_startX_center_horizontal, false);
                startYTop = ta.getBoolean(R.styleable.AutoOnBoardAnimation_startY_top, false);
                startYBottom = ta.getBoolean(R.styleable.AutoOnBoardAnimation_startY_bottom, false);
                startYCenterVertical = ta.getBoolean(R.styleable.AutoOnBoardAnimation_startY_center_vertical, false);

                endXLeft = ta.getBoolean(R.styleable.AutoOnBoardAnimation_endX_left, false);
                endXRight = ta.getBoolean(R.styleable.AutoOnBoardAnimation_endX_right, false);
                endXCenterHorizontal = ta.getBoolean(R.styleable.AutoOnBoardAnimation_endX_center_horizontal, false);
                endYTop = ta.getBoolean(R.styleable.AutoOnBoardAnimation_endY_top, false);
                endYBottom = ta.getBoolean(R.styleable.AutoOnBoardAnimation_endY_bottom, false);
                endYCenterVertical = ta.getBoolean(R.styleable.AutoOnBoardAnimation_endY_center_vertical, false);

                isShowLayoutDev = ta.getBoolean(R.styleable.AutoOnBoardAnimation_show_layout_dev, false);
                devLayoutHeight = ta.getLayoutDimension(R.styleable.AutoOnBoardAnimation_dev_layout_height, 0);
                devLayoutWidth = ta.getLayoutDimension(R.styleable.AutoOnBoardAnimation_dev_layout_width, 0);

            } finally {
                ta.recycle();
            }
        }
    }

    /**
     * gets the bitmap of image and stores the actual height and width of image
     * intrinsicHeight() gives the actual width and height of image.
     * <p/>
     * get bitmap of size equivalent to width and height defined by user, if not it defaults to wrap_content
     */
    private void getImageToBeDrawn() {
        if (img != 0) {
            drwable = (BitmapDrawable) resourceObject.getDrawable(img);
            imgBitmap = drwable.getBitmap();
            imageHeight = drwable.getIntrinsicHeight();
            imageWidth = drwable.getIntrinsicWidth();

            int desiredHeight = imageHeight;
            int desiredWidth = imageWidth;
            if (devLayoutWidth != 0) {
                desiredWidth = devLayoutWidth;
            }
            if (devLayoutHeight != 0) {
                desiredHeight = devLayoutHeight;
            }

            actualBitmapToBeDrawn = padBitmap(imgBitmap, desiredWidth, desiredHeight);
        }
    }

    /**
     * finds rendering device width and height
     */
    private void getScreenWidthAndHeight() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        parentWidth = point.x;
        parentHeight = point.y;
    }

    /**
     * stores view start and end position
     * made public to get start and end co-ordinate for auto animation
     */
    public void setUpXAndY() {
        if(!isSetUpXAndYDone) {
            parentWidth = ((View) this.getParent()).getWidth();
            parentHeight = ((View) this.getParent()).getHeight();
            startX = startXRight ? (parentWidth - imageWidth) : startXLeft ? 0 : startXCenterHorizontal ? (parentWidth / 2) : startX;
            startY = startYTop ? 0 : startYBottom ? (parentHeight - imageHeight) : startYCenterVertical ? (parentHeight / 2) : startY;
            endX = endXRight ? (parentWidth - imageWidth) : endXLeft ? 0 : endXCenterHorizontal ? (parentWidth / 2 - imageWidth / 2) : endX;
            endY = endYTop ? 0 : endYBottom ? (parentHeight - imageHeight) : endYCenterVertical ? (parentHeight / 2 - imageWidth / 2) : endY;
            isSetUpXAndYDone = true;
        }
    }

    /**
     * draws view according to the mode.
     * NORMAL - device rendering.
     * DEV - layout rendering. Shows only the end position
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setUpXAndY();
        this.canvas = canvas;
        switch (renderingMode) {
            case NORMAL:
                draw();
                break;
            case DEV:
                drawDev();
                break;
        }
    }

    /**
     * NORMAL mode drawing
     */
    private void draw() {
        rectF = new RectF(0, 0, mWidth, mHeight);
        canvas.drawBitmap(actualBitmapToBeDrawn, null, rectF, null);
    }

    /**
     * DEV mode drawing
     */
    private void drawDev() {
        /*rectF = new RectF(startX, startY, startX + imageWidth, startY + imageHeight);
        canvas.drawBitmap(actualBitmapToBeDrawn, null, rectF, null);*/
        rectF = new RectF(endX, endY, endX + imageWidth, endY + imageHeight);
        canvas.drawBitmap(actualBitmapToBeDrawn, null, rectF, null);
    }

    public void reDraw(float x, float y) {
        if (canvas != null) {
            currentX = x;
            currentY = y;
            invalidate();
        }
    }

    /**
     * converts dp to px
     *
     * @param value
     * @return
     */
    private float convertPxToDp(int value) {
        DisplayMetrics metrics = resourceObject.getDisplayMetrics();
        float dp = value / (metrics.densityDpi / 160f);
        return dp;
    }

    private float convertDpToPx(int value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resourceObject.getDisplayMetrics());
    }

    /**
     * this method determines how big view needs to be.
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        widthMode = MeasureSpec.getMode(widthMeasureSpec);
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightMode = MeasureSpec.getMode(heightMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);


        devMeasure();

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            mWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            mWidth = Math.min(imageWidth, widthSize);
        } else {
            //Be whatever you want
            mWidth = imageWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            mHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            mHeight = Math.min(imageHeight, heightSize);
        } else {
            //Be whatever you want
            mHeight = imageHeight;
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * overrides the measure
     * in dev mode, it makes the view to occupy whole layout and make the view as big as defined by dev_layout_width and dev_layout_height
     * in non-dev mode it overrides the layout_width and layout_height with dev_layout_width and dev_layout_height, if value is greater than zero, otherwise wrap_content.
     */
    private void devMeasure() {
        switch (renderingMode) {
            case DEV:
                widthMode = MeasureSpec.EXACTLY;
                heightMode = MeasureSpec.EXACTLY;
                imageWidth = devLayoutWidth != 0 ? devLayoutWidth:imageWidth;
                imageHeight = devLayoutHeight != 0 ? devLayoutHeight:imageHeight;
                break;

            case NORMAL:
                imageWidth = devLayoutWidth != 0 ? devLayoutWidth:imageWidth;
                imageHeight = devLayoutHeight !=0 ? devLayoutHeight:imageHeight;
                break;
        }
    }

    /**
     * creates bitmap with padding
     * this changes the bitmap to be drawn, needs inspection.
     *
     * @param bitmap
     * @return
     */
    public Bitmap padBitmap(Bitmap bitmap, int desiredWidth, int desiredHeight) {
        switch (renderingMode) {
            case DEV:
                return movableBitmapGenerator.createDevBitmap(mContext, imgBitmap,
                        devLayoutWidth, devLayoutHeight, imageWidth, imageHeight, desiredWidth, desiredHeight, drwable);
            case NORMAL:
                return movableBitmapGenerator.createBitmap(mContext, imgBitmap,
                        devLayoutWidth, devLayoutHeight, imageWidth, imageHeight, desiredWidth, desiredHeight, drwable);
        }
        return null;
    }

    /**
     * returns end position Y co-ordinates
     *
     * @return
     */
    public float getEndY() {
        return endY;
    }

    /**
     * sets end position Y co-ordinates
     *
     * @param endY
     */
    public void setEndY(float endY) {
        this.endY = endY;
    }

    /**
     * returns end position X co-ordinates
     *
     * @return
     */
    public float getEndX() {
        return endX;
    }

    /**
     * sets end position X co-ordinates
     *
     * @param endX
     */
    public void setEndX(float endX) {
        this.endX = endX;
    }

    /**
     * returns start position Y co-ordinates
     *
     * @return
     */
    public float getStartY() {
        return startY;
    }

    /**
     * sets start position Y co-ordinates
     *
     * @param startY
     */
    public void setStartY(float startY) {
        this.startY = startY;
    }

    /**
     * returns start position X co-ordinates
     *
     * @return
     */
    public float getStartX() {
        return startX;
    }

    /**
     * sets start position X co-ordinates
     *
     * @param startX
     */
    public void setStartX(float startX) {
        this.startX = startX;
    }

    /**
     * returns if view is to be parent left aligned
     *
     * @return
     */
    public boolean isStartXLeft() {
        return startXLeft;
    }

    /**
     * sets if view is to be parent left aligned
     *
     * @param startXLeft
     */
    public void setStartXLeft(boolean startXLeft) {
        this.startXLeft = startXLeft;
    }

    /**
     * returns if view is to parent bottom aligned
     *
     * @return
     */
    public boolean isStartYBottom() {
        return startYBottom;
    }

    /**
     * sets if view is to parent bottom aligned
     *
     * @param startYBottom
     */
    public void setStartYBottom(boolean startYBottom) {
        this.startYBottom = startYBottom;
    }

    /**
     * returns if view is to parent right aligned
     *
     * @return
     */
    public boolean isStartXRight() {
        return startXRight;
    }

    /**
     * sets if view is to parent right aligned
     *
     * @param startXRight
     */
    public void setStartXRight(boolean startXRight) {
        this.startXRight = startXRight;
    }

    /**
     * returns if view is to parent top aligned
     *
     * @return
     */
    public boolean isStartYTop() {
        return startYTop;
    }

    /**
     * sets if view is to parent top aligned
     *
     * @param startYTop
     */
    public void setStartYTop(boolean startYTop) {
        this.startYTop = startYTop;
    }

    /**
     * returns if view is to parent left aligned
     *
     * @return
     */
    public boolean isEndXLeft() {
        return endXLeft;
    }

    /**
     * set if view is to parent left aligned
     *
     * @param endXLeft
     */
    public void setEndXLeft(boolean endXLeft) {
        this.endXLeft = endXLeft;
    }

    /**
     * returns if view is to parent right aligned
     *
     * @return
     */
    public boolean isEndXRight() {
        return endXRight;
    }

    /**
     * sets if view is to parent bottom aligned
     *
     * @param endXRight
     */
    public void setEndXRight(boolean endXRight) {
        this.endXRight = endXRight;
    }

    /**
     * returns if view is to parent top aligned
     *
     * @return
     */
    public boolean isEndYTop() {
        return endYTop;
    }

    /**
     * sets if view is to parent top aligned
     *
     * @param endYTop
     */
    public void setEndYTop(boolean endYTop) {
        this.endYTop = endYTop;
    }

    /**
     * returns if view is to parent bottom aligned
     *
     * @return
     */
    public boolean isEndYBottom() {
        return endYBottom;
    }

    /**
     * sets if view is to parent bottom aligned
     *
     * @param endYBottom
     */
    public void setEndYBottom(boolean endYBottom) {
        this.endYBottom = endYBottom;
    }

    /**
     * returns if view is to parent center vertical aligned
     *
     * @return
     */
    public boolean isStartYCenterVertical() {
        return startYCenterVertical;
    }

    /**
     * sets if view is to parent center vertical aligned
     *
     * @param startYCenterVertical
     */
    public void setStartYCenterVertical(boolean startYCenterVertical) {
        this.startYCenterVertical = startYCenterVertical;
    }

    /**
     * returns if view is to parent center horizontal aligned
     *
     * @return
     */
    public boolean isStartXCenterHorizontal() {
        return startXCenterHorizontal;
    }

    /**
     * sets if view is to parent center horizontal aligned
     *
     * @param startXCenterHorizontal
     */
    public void setStartXCenterHorizontal(boolean startXCenterHorizontal) {
        this.startXCenterHorizontal = startXCenterHorizontal;
    }

    /**
     * returns if view is to parent center horizontal aligned
     *
     * @return
     */
    public boolean isEndXCenterHorizontal() {
        return endXCenterHorizontal;
    }

    /**
     * sets if view is to parent center horizontal aligned
     *
     * @param endXCenterHorizontal
     */
    public void setEndXCenterHorizontal(boolean endXCenterHorizontal) {
        this.endXCenterHorizontal = endXCenterHorizontal;
    }

    /**
     * returns if view is to parent center vertical aligned
     *
     * @return
     */
    public boolean isEndYCenterVertical() {
        return endYCenterVertical;
    }

    /**
     * sets if view is to parent center vertical aligned
     *
     * @param endYCenterVertical
     */
    public void setEndYCenterVertical(boolean endYCenterVertical) {
        this.endYCenterVertical = endYCenterVertical;
    }

    /**
     * returns whether to show developer layout or not
     * @return
     */
    public boolean isShowLayoutDev() {
        return isShowLayoutDev;
    }

    /**
     * sets developer show layout
     * @param isShowLayoutDev
     */
    public void setIsShowLayoutDev(boolean isShowLayoutDev) {
        this.isShowLayoutDev = isShowLayoutDev;
    }
}
