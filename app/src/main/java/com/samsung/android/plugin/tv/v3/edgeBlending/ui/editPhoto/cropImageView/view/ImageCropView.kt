package com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.cropImageView.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.ViewConfiguration
import android.widget.ImageView
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.cropImageView.model.CropInfo
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.cropImageView.util.BitmapLoadUtils.decode
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.cropImageView.util.dpToPx
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.cropImageView.view.graphics.FastBitmapDrawable
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.util.FileUtils
import it.sephiroth.android.library.easing.Cubic
import it.sephiroth.android.library.easing.Easing
import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@SuppressLint("AppCompatCustomView")
@SuppressWarnings("WeakerAccess", "unused")
open class ImageCropView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    ImageView(context, attrs, defStyle) {
    open var mEasing: Easing = Cubic()
    open var mBaseMatrix = Matrix()
    open var mSuppMatrix = Matrix()
    open val mDisplayMatrix = Matrix()
    open var mHandler = Handler(Looper.getMainLooper())
    open var mLayoutRunnable: Runnable? = null
    open var mUserScaled = false
    private var mMaxZoom = ZOOM_INVALID
    private var mMinZoom = ZOOM_INVALID

    // true when min and max zoom are explicitly defined
    private var mMaxZoomDefined = false
    private var mMinZoomDefined = false

    open val mMatrixValues = FloatArray(9)
    private var mThisWidth = -1
    private var mThisHeight = -1
    open val center = PointF()
    private var mBitmapChanged = false
    private var mRestoreRequest = false
    protected val DEFAULT_ANIMATION_DURATION = 200
    open var mBitmapRect = RectF()
    open var mCenterRect = RectF()
    open var mScrollRect = RectF()
    open var mCropRect = RectF()
    private var mOutsideLayerPaint: Paint? = null
    private var mOutsideLayerPaintWhenMoving: Paint? = null
    private var mAspectRatioWidth = DEFAULT_ASPECT_RATIO_WIDTH
    private var mAspectRatioHeight = DEFAULT_ASPECT_RATIO_HEIGHT
    private var mTargetAspectRatio = (mAspectRatioHeight / mAspectRatioWidth).toFloat()
    private var mPts = floatArrayOf()
    private val GRID_ROW_COUNT = 3
    private val GRID_COLUMN_COUNT = 4
    private var mGridInnerLinePaint: Paint? = null
    private var mGridOuterLinePaint: Paint? = null
    private var gridInnerMode = 0
    private var gridOuterMode = 0
    private var gridLeftRightMargin = 0F
    private var gridTopBottomMargin = 0F
    private var imageFilePath: String? = null
    open var mScaleDetector: ScaleGestureDetector? = null
    open var mGestureDetector: GestureDetector? = null
    open var mTouchSlop = 0
    open var mScaleFactor = 0f
    open var mDoubleTapDirection = 0

    open var mGestureListener: GestureDetector.OnGestureListener? = null
    var isGesturing = false
        private set

    open var mScaleListener: OnScaleGestureListener? = null
    var doubleTapEnabled = false
    protected var mScaleEnabled = true
    protected var mScrollEnabled = true
    private var mDoubleTapListener: OnImageViewTouchDoubleTapListener? = null
    private var mSingleTapListener: OnImageViewTouchSingleTapListener? = null
    var isChangingScale = false
        private set
    private val suppMatrixValues = FloatArray(9)

    private fun init(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ImageCropView)

        val outsideLayerColor = a.getColor(R.styleable.ImageCropView_outsideLayerColor, Color.parseColor(DEFAULT_OUTSIDE_LAYER_COLOR_ID))
        mOutsideLayerPaint = Paint()
        mOutsideLayerPaint!!.color = outsideLayerColor

        val outsideLayerColorWhenMoving =
            a.getColor(R.styleable.ImageCropView_outsideLayerColorWhenMoving, Color.parseColor(DEFAULT_OUTSIDE_MOVING_LAYER_COLOR_ID))
        mOutsideLayerPaintWhenMoving = Paint()
        mOutsideLayerPaintWhenMoving!!.color = outsideLayerColorWhenMoving

        scaleType = ScaleType.MATRIX
        mGridInnerLinePaint = Paint()
        val gridInnerStrokeWidth = a.getDimension(R.styleable.ImageCropView_gridInnerStroke, 1f)
        mGridInnerLinePaint!!.strokeWidth = gridInnerStrokeWidth
        val gridInnerColor = a.getColor(R.styleable.ImageCropView_gridInnerColor, Color.WHITE)
        mGridInnerLinePaint!!.color = gridInnerColor
        mGridOuterLinePaint = Paint()
        val gridOuterStrokeWidth = a.getDimension(R.styleable.ImageCropView_gridOuterStroke, 1f)
        mGridOuterLinePaint!!.strokeWidth = gridOuterStrokeWidth
        val gridOuterColor = a.getColor(R.styleable.ImageCropView_gridOuterColor, Color.WHITE)
        mGridOuterLinePaint!!.color = gridOuterColor
        mGridOuterLinePaint!!.style = Paint.Style.STROKE
        gridInnerMode = a.getInt(R.styleable.ImageCropView_setInnerGridMode, GRID_OFF)
        gridOuterMode = a.getInt(R.styleable.ImageCropView_setOuterGridMode, GRID_OFF)
        gridLeftRightMargin = a.getDimension(R.styleable.ImageCropView_gridLeftRightMargin, 0f)
        gridTopBottomMargin = a.getDimension(R.styleable.ImageCropView_gridTopBottomMargin, 0f)
        val rowLineCount = (GRID_ROW_COUNT - 1) * 4
        val columnLineCount = (GRID_COLUMN_COUNT - 1) * 5
        mPts = FloatArray(rowLineCount + columnLineCount)
        a.recycle()
        mTouchSlop = ViewConfiguration.get(getContext()).scaledTouchSlop
        mGestureListener = GestureListener()

        mScaleListener = ScaleListener()
        mScaleDetector = ScaleGestureDetector(getContext(), mScaleListener)
        mGestureDetector = GestureDetector(getContext(), mGestureListener, null, true)
        mDoubleTapDirection = 1
        mBitmapChanged = false
        mRestoreRequest = false
    }

    override fun setScaleType(scaleType: ScaleType) {
        if (scaleType == ScaleType.MATRIX) {
            super.setScaleType(scaleType)
        } else {
            throw IllegalArgumentException("Unsupported scaleType. Only ScaleType.MATRIX can be used")
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        logD("onDraw", "Entry")
        drawTransparentLayer(canvas)
        drawGrid(canvas)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        logD("onLayout", "changed: $changed, bitmapChanged: $mBitmapChanged")
        super.onLayout(changed, left, top, right, bottom)
        var deltaX = 0
        var deltaY = 0
        if (changed) {
            val oldW = mThisWidth
            val oldH = mThisHeight
            mThisWidth = right - left
            mThisHeight = bottom - top
            deltaX = mThisWidth - oldW
            deltaY = mThisHeight - oldH

            // update center point
            center.x = mThisWidth / 2f
            center.y = mThisHeight / 2f
        }
        var height = (mThisWidth * mTargetAspectRatio).toInt()
        if (height > mThisHeight) {
            val width = ((mThisHeight - gridTopBottomMargin * 2) / mTargetAspectRatio).toInt()
            val halfDiff = (mThisWidth - width) / 2
            mCropRect[(left + halfDiff).toFloat(), top + gridTopBottomMargin, (right - halfDiff).toFloat()] = bottom - gridTopBottomMargin
        } else {
            height = ((mThisWidth - gridLeftRightMargin * 2) * mTargetAspectRatio).toInt()
            val halfDiff = (mThisHeight - height) / 2
            mCropRect[left + gridLeftRightMargin, (halfDiff - top).toFloat(), right - gridLeftRightMargin] = (height + halfDiff).toFloat()
        }
        val r = mLayoutRunnable
        if (r != null) {
            mLayoutRunnable = null
            r.run()
        }
        val drawable = drawable
        if (drawable != null) {
            if (changed || mBitmapChanged) {
                if (mBitmapChanged) {
                    mBaseMatrix.reset()
                    if (!mMinZoomDefined) mMinZoom = ZOOM_INVALID
                    if (!mMaxZoomDefined) mMaxZoom = ZOOM_INVALID
                }
                var scale = 1f

                // retrieve the old values
                val oldMatrixScale = getScale(mBaseMatrix)
                val oldScale = scale
                val oldMinScale = min(1f, 1f / oldMatrixScale)
                getProperBaseMatrix(drawable, mBaseMatrix)
                val newMatrixScale = getScale(mBaseMatrix)
                logD("onLayout", "old matrix scale: $oldMatrixScale")
                logD("onLayout", "new matrix scale: $newMatrixScale")
                logD("onLayout", "old min scale: $oldMinScale")
                logD("onLayout", "old scale: $oldScale")

                // 1. bitmap changed or scaleType changed
                if (mBitmapChanged) {
                    imageMatrix = imageViewMatrix
                } else if (changed) {

                    // 2. layout size changed
                    if (!mMinZoomDefined) mMinZoom = ZOOM_INVALID
                    if (!mMaxZoomDefined) mMaxZoom = ZOOM_INVALID
                    imageMatrix = imageViewMatrix
                    postTranslate(-deltaX.toFloat(), -deltaY.toFloat())
                    if (!mUserScaled) {
                        zoomTo(scale)
                    } else {
                        if (abs(oldScale - oldMinScale) > 0.001) {
                            scale = oldMatrixScale / newMatrixScale * oldScale
                        }
                        logV("onLayout", "userScaled. scale=$scale")
                        zoomTo(scale)
                    }
                    logD("onLayout", "old scale: $oldScale")
                    logD("onLayout", "new scale: $scale")
                }
                mUserScaled = false
                if (scale > maxScale || scale < minScale) {
                    // if current scale if outside the min/max bounds
                    // then restore the correct scale
                    zoomTo(scale)
                }
                if (!mRestoreRequest) {
                    center(true, true)
                }
                if (mBitmapChanged) mBitmapChanged = false
                if (mRestoreRequest) mRestoreRequest = false
                logD("onLayout", "new scale: $scale")
            }
        } else {
            if (mBitmapChanged) mBitmapChanged = false
            if (mRestoreRequest) mRestoreRequest = false
        }
    }

    fun resetDisplay() {
        mBitmapChanged = true
        resetMatrix()
        requestLayout()
    }

    fun resetMatrix() {
        logI("resetMatrix", "resetMatrix")
        mSuppMatrix = Matrix()
        imageMatrix = imageViewMatrix
        zoomTo(1f)
        postInvalidate()
    }

    private fun drawTransparentLayer(canvas: Canvas) {
        //draw 30% when gesture, 100% when finger up.
        /*-
          -------------------------------------
          |                top                |
          -------------------------------------
          |      |                    |       |
          |      |                    |       |
          | left |      mCropRect     | right |
          |      |                    |       |
          |      |                    |       |
          -------------------------------------
          |              bottom               |
          -------------------------------------
         */
        val paint = if (isGesturing) mOutsideLayerPaintWhenMoving else mOutsideLayerPaint
        val r = Rect()
        getLocalVisibleRect(r)
        canvas.drawRect(r.left.toFloat(), r.top.toFloat(), r.right.toFloat(), mCropRect.top, paint!!) // top
        canvas.drawRect(r.left.toFloat(), mCropRect.bottom, r.right.toFloat(), r.bottom.toFloat(), paint) // bottom
        canvas.drawRect(r.left.toFloat(), mCropRect.top, mCropRect.left, mCropRect.bottom, paint) // left
        canvas.drawRect(mCropRect.right, mCropRect.top, r.right.toFloat(), mCropRect.bottom, paint) // right
    }

    private fun drawGrid(canvas: Canvas) {
        var index = 0
        for (i in 0 until GRID_ROW_COUNT - 1) {
            mPts[index++] = mCropRect.left //start Xi
            mPts[index++] = mCropRect.height() * ((i.toFloat() + 1.0f) / GRID_ROW_COUNT.toFloat()) + mCropRect.top //start Yi
            mPts[index++] = mCropRect.right //stop  Xi
            mPts[index++] = mCropRect.height() * ((i.toFloat() + 1.0f) / GRID_ROW_COUNT.toFloat()) + mCropRect.top //stop  Yi
        }
        for (i in 0 until GRID_COLUMN_COUNT - 1) {
            mPts[index++] = mCropRect.width() * ((i.toFloat() + 1.0f) / GRID_COLUMN_COUNT.toFloat()) + mCropRect.left //start Xi
            mPts[index++] = mCropRect.top //start Yi
            mPts[index++] = mCropRect.width() * ((i.toFloat() + 1.0f) / GRID_COLUMN_COUNT.toFloat()) + mCropRect.left //stop  Xi
            mPts[index++] = mCropRect.bottom //stop  Yi
        }
        if (gridInnerMode == GRID_ON) {
            canvas.drawLines(mPts, mGridInnerLinePaint!!)
        }
        if (gridOuterMode == GRID_ON) {
            val halfLineWidth = mGridOuterLinePaint!!.strokeWidth * 0.5f
            canvas.drawRect(
                mCropRect.left + halfLineWidth,
                mCropRect.top + halfLineWidth,
                mCropRect.right - halfLineWidth,
                mCropRect.bottom - halfLineWidth,
                mGridOuterLinePaint!!
            )
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun setImageResource(resId: Int) {
        setImageDrawable(context.resources.getDrawable(resId))
    }

    fun setAspectRatio(aspectRatioWidth: Int, aspectRatioHeight: Int) {
        require(!(aspectRatioWidth <= 0 || aspectRatioHeight <= 0)) { "Cannot set aspect ratio value to a number less than or equal to 0." }
        mAspectRatioWidth = aspectRatioWidth
        mAspectRatioHeight = aspectRatioHeight
        mTargetAspectRatio = mAspectRatioHeight.toFloat() / mAspectRatioWidth.toFloat()
        resetDisplay()
    }

    fun setImageUri(imageUri: Uri) {
        val imageFilePath = FileUtils.getPath(context, imageUri)
        if (imageFilePath != null) setImageFilePath(imageFilePath)
    }

    fun setImageFilePath(imageFilePath: String) {
        val imageFile = File(imageFilePath)
        require(imageFile.exists()) { "Image file does not exist" }
        this.imageFilePath = imageFilePath
        val reqSize = 1000
        val bitmap = decode(imageFilePath, reqSize, reqSize, true)
        setImageBitmap(bitmap!!)
    }

    override fun setImageBitmap(bitmap: Bitmap) {
        val minScale = 1f
        val maxScale = 8f
        setImageBitmap(bitmap, minScale, maxScale)
    }

    fun setImageBitmap(bitmap: Bitmap?, min_zoom: Float, max_zoom: Float) {
        val viewWidth = width
        if (viewWidth <= 0) {
            mLayoutRunnable = Runnable { setImageBitmap(bitmap, min_zoom, max_zoom) }
            return
        }
        if (bitmap != null) {
            setImageDrawable(FastBitmapDrawable(bitmap), min_zoom, max_zoom)
        } else {
            setImageDrawable(null, min_zoom, max_zoom)
        }
    }

    override fun setImageDrawable(drawable: Drawable?) {
        val minScale = 1f
        val maxScale = 8f
        setImageDrawable(drawable, minScale, maxScale)
    }

    fun setImageDrawable(drawable: Drawable?, min_zoom: Float, max_zoom: Float) {
        val viewWidth = width
        if (viewWidth <= 0) {
            mLayoutRunnable = Runnable { setImageDrawable(drawable, min_zoom, max_zoom) }
            return
        }
        _setImageDrawable(drawable, min_zoom, max_zoom)
    }

    protected fun _setImageDrawable(drawable: Drawable?, min_zoom: Float, max_zoom: Float) {
        var minZoom = min_zoom
        var maxZoom = max_zoom
        logI("_setImageDrawable", "Entry")
        mBaseMatrix.reset()
        if (drawable != null) {
            logD("_setImageDrawable", "size: " + drawable.intrinsicWidth + "x" + drawable.intrinsicHeight)
            super.setImageDrawable(drawable)
        } else {
            super.setImageDrawable(null)
        }
        if (minZoom != ZOOM_INVALID && maxZoom != ZOOM_INVALID) {
            minZoom = min(minZoom, maxZoom)
            maxZoom = max(minZoom, maxZoom)
            mMinZoom = minZoom
            mMaxZoom = maxZoom
            mMinZoomDefined = true
            mMaxZoomDefined = true
        } else {
            mMinZoom = ZOOM_INVALID
            mMaxZoom = ZOOM_INVALID
            mMinZoomDefined = false
            mMaxZoomDefined = false
        }
        logV("_setImageDrawable", "mMinZoom: $mMinZoom, mMaxZoom: $mMaxZoom")
        mBitmapChanged = true
        mScaleFactor = maxScale / 3
        requestLayout()
    }

    private fun computeMaxZoom(): Float {
        logI("computeMaxZoom", "Entry")
        if (drawable == null) {
            return 1f
        }
        val fw = drawable.intrinsicWidth.toFloat() / mThisWidth.toFloat()
        val fh = drawable.intrinsicHeight.toFloat() / mThisHeight.toFloat()
        val scale = max(fw, fh) * 8
        logI("computeMaxZoom", "scale: $scale")
        return scale
    }

    open fun computeMinZoom(): Float {
        logI("computeMinZoom", "Entry")
        drawable ?: return 1f
        var scale = getScale(mBaseMatrix)
        scale = min(1f, 1f / scale)
        logI("computeMinZoom", "scale: $scale")
        return scale
    }

    val maxScale: Float
        get() {
            if (mMaxZoom == ZOOM_INVALID) mMaxZoom = computeMaxZoom()
            return mMaxZoom
        }
    val minScale: Float
        get() {
            logI("getMinScale", "Entry, MinZoom: $mMinZoom")
            if (mMinZoom == ZOOM_INVALID) mMinZoom = computeMinZoom()
            logV("getMinScale", "mMinZoom: $mMinZoom")
            return mMinZoom
        }
    val imageViewMatrix: Matrix
        get() = getImageViewMatrix(mSuppMatrix)

    fun getImageViewMatrix(supportMatrix: Matrix?): Matrix {
        mDisplayMatrix.set(mBaseMatrix)
        mDisplayMatrix.postConcat(supportMatrix)
        return mDisplayMatrix
    }

    private var baseScale = 1f
    protected fun getProperBaseMatrix(drawable: Drawable, matrix: Matrix) {
        val viewWidth = mCropRect.width()
        val viewHeight = mCropRect.height()
        logD("getProperBaseMatrix", "view: " + viewWidth + "x" + viewHeight)
        val w = drawable.intrinsicWidth.toFloat()
        val h = drawable.intrinsicHeight.toFloat()
        val widthScale: Float
        val heightScale: Float
        matrix.reset()
        if (w > viewWidth || h > viewHeight) {
            widthScale = viewWidth / w
            heightScale = viewHeight / h
            baseScale = max(widthScale, heightScale)
            matrix.postScale(baseScale, baseScale)
            val tw = (viewWidth - w * baseScale) / 2.0f
            val th = (viewHeight - h * baseScale) / 2.0f
            matrix.postTranslate(tw, th)
        } else {
            widthScale = viewWidth / w
            heightScale = viewHeight / h
            baseScale = max(widthScale, heightScale)
            matrix.postScale(baseScale, baseScale)
            val tw = (viewWidth - w * baseScale) / 2.0f
            val th = (viewHeight - h * baseScale) / 2.0f
            matrix.postTranslate(tw, th)
        }
        printMatrix(matrix)
    }

    protected fun getValue(matrix: Matrix, whichValue: Int): Float {
        matrix.getValues(mMatrixValues)
        return mMatrixValues[whichValue]
    }

    fun printMatrix(matrix: Matrix) {
        val scaleX = getValue(matrix, Matrix.MSCALE_X)
        val scaleY = getValue(matrix, Matrix.MSCALE_Y)
        val tx = getValue(matrix, Matrix.MTRANS_X)
        val ty = getValue(matrix, Matrix.MTRANS_Y)
        logD("printMatrix", "matrix: { x: $tx, y: $ty, scaleX: $scaleX, scaleY: $scaleY }")
    }

    val bitmapRect: RectF?
        get() = getBitmapRect(mSuppMatrix)

    open fun getBitmapRect(supportMatrix: Matrix?): RectF? {
        val drawable = drawable ?: return null
        val m = getImageViewMatrix(supportMatrix)
        mBitmapRect[0f, 0f, drawable.intrinsicWidth.toFloat()] = drawable.intrinsicHeight.toFloat()
        m.mapRect(mBitmapRect)
        return mBitmapRect
    }

    open fun getScale(matrix: Matrix): Float {
        return getValue(matrix, Matrix.MSCALE_X)
    }

    @SuppressLint("Override")
    override fun getRotation(): Float {
        return 0F
    }

    val scale: Float
        get() = getScale(mSuppMatrix)

    fun getBaseScale(): Float {
        return getScale(mBaseMatrix)
    }

    protected fun center(horizontal: Boolean, vertical: Boolean) {
        logD("center", "___________________________")
        if (drawable == null) return
        val rect = getCenter(mSuppMatrix, horizontal, vertical)
        if (rect.left != 0f || rect.top != 0f) {
            logI("center", "center")
            postTranslate(rect.left, rect.top)
        }
    }

    open fun getCenter(supportMatrix: Matrix?, horizontal: Boolean, vertical: Boolean): RectF {
        if (drawable == null) return RectF(0F, 0F, 0F, 0F)
        mCenterRect[0f, 0f, 0f] = 0f
        val rect = getBitmapRect(supportMatrix)
        val height = rect!!.height()
        val width = rect.width()
        var deltaX = 0f
        var deltaY = 0f
        if (vertical) {
            val viewHeight = mThisHeight
            when {
                height < viewHeight -> {
                    deltaY = (viewHeight - height) / 2 - rect.top
                }
                rect.top > 0 -> {
                    deltaY = -rect.top
                }
                rect.bottom < viewHeight -> {
                    deltaY = mThisHeight - rect.bottom
                }
            }
        }
        if (horizontal) {
            val viewWidth = mThisWidth
            if (width < viewWidth) {
                deltaX = (viewWidth - width) / 2 - rect.left
            } else if (rect.left > 0) {
                deltaX = -rect.left
            } else if (rect.right < viewWidth) {
                deltaX = viewWidth - rect.right
            }
        }
        mCenterRect[deltaX, deltaY, 0f] = 0f
        return mCenterRect
    }

    open fun postTranslate(deltaX: Float, deltaY: Float) {
        if (deltaX != 0f || deltaY != 0f) {
            logI("postTranslate", "deltaX: $deltaX, deltaY: $deltaY")
            mSuppMatrix.postTranslate(deltaX, deltaY)
            imageMatrix = imageViewMatrix
        }
    }

    fun postScale(scale: Float, centerX: Float, centerY: Float) {
        logI("postScale", "scale: $scale, centerX: ${centerX}, centerY: $centerY")
        mSuppMatrix.postScale(scale, scale, centerX, centerY)
        imageMatrix = imageViewMatrix
    }

    open fun zoomTo(scaleVal: Float) {
        var scale = scaleVal
        logI("zoomTo", "Entry zoomTo: $scale")
        if (scale > maxScale) scale = maxScale
        if (scale < minScale) scale = minScale
        logD("zoomTo", "final scale: $scale")
//        val center = center
        zoomTo(scale, center.x, center.y)
    }

    fun zoomTo(scale: Float, durationMs: Float) {
//        val center = center
        zoomTo(scale, center.x, center.y, durationMs)
    }

    protected fun zoomTo(scaleVal: Float, centerX: Float, centerY: Float) {
        val deltaScale = (if (scaleVal > maxScale) maxScale else scaleVal) / scale
        logD("zoomTo", "deltaScale: $deltaScale")
        postScale(deltaScale, centerX, centerY)
        center(true, true)
    }

    fun onZoomAnimationCompleted(scale: Float) {
        logD("onZoomAnimationCompleted", "scale: $scale, minZoom: $minScale")
        if (scale < minScale) zoomTo(minScale, 50f)
    }

    fun scrollBy(x: Float, y: Float) {
        logD("scrollBy", "__________")
        panBy(x.toDouble(), y.toDouble())
    }

    protected fun panBy(dx: Double, dy: Double) {
        logD("panBy", "__________")
        mScrollRect[dx.toFloat(), dy.toFloat(), 0f] = 0f
        postTranslate(mScrollRect.left, mScrollRect.top)
        adjustCropAreaImage()
    }

    private fun adjustCropAreaImage() {
        logD("adjustCropAreaImage", "__________")
        if (drawable == null) return
        val rect = getAdjust(mSuppMatrix)
        if (rect.left != 0f || rect.top != 0f) {
            logI("scrollBy", "center")
            postTranslate(rect.left, rect.top)
        }
    }

    private fun getAdjust(supportMatrix: Matrix): RectF {
        if (drawable == null) return RectF(0F, 0F, 0F, 0F)
        mCenterRect[0f, 0f, 0f] = 0f
        val rect = getBitmapRect(supportMatrix)
        var deltaX = 0f
        var deltaY = 0f

        //Y
        if (rect!!.top > mCropRect.top) {
            deltaY = mCropRect.top - rect.top
        } else if (rect.bottom < mCropRect.bottom) {
            deltaY = mCropRect.bottom - rect.bottom
        }

        //X
        if (rect.left > mCropRect.left) {
            deltaX = mCropRect.left - rect.left
        } else if (rect.right < mCropRect.right) {
            deltaX = mCropRect.right - rect.right
        }
        mCenterRect[deltaX, deltaY, 0f] = 0f
        return mCenterRect
    }

    open fun scrollBy(distanceX: Float, distanceY: Float, durationMs: Double) {
        val dx = distanceX.toDouble()
        val dy = distanceY.toDouble()
        val startTime = System.currentTimeMillis()
        mHandler.post(
            object : Runnable {
                var old_x = 0.0
                var old_y = 0.0
                override fun run() {
                    val now = System.currentTimeMillis()
                    val currentMs = min(durationMs, (now - startTime).toDouble())
                    val x = mEasing.easeOut(currentMs, 0.0, dx, durationMs)
                    val y = mEasing.easeOut(currentMs, 0.0, dy, durationMs)
                    panBy(x - old_x, y - old_y)
                    old_x = x
                    old_y = y
                    if (currentMs < durationMs) {
                        mHandler.post(this)
                    }
                }
            }
        )
    }

    protected fun zoomTo(scaleVal: Float, centerX: Float, centerY: Float, durationMs: Float) {
        var scale = scaleVal
        if (scale > maxScale) scale = maxScale
        val startTime = System.currentTimeMillis()
        val oldScale = scale
        val deltaScale = scale - oldScale
        val m = Matrix(mSuppMatrix)
        m.postScale(scale, scale, centerX, centerY)
        val rect = getCenter(m, horizontal = true, vertical = true)
        val destX = centerX + rect.left * scale
        val destY = centerY + rect.top * scale
        mHandler.post(
            object : Runnable {
                override fun run() {
                    val now = System.currentTimeMillis()
                    val currentMs = min(durationMs, (now - startTime).toFloat())
                    val newScale = mEasing.easeInOut(currentMs.toDouble(), 0.0, deltaScale.toDouble(), durationMs.toDouble()).toFloat()
                    zoomTo(oldScale + newScale, destX, destY)
                    if (currentMs < durationMs) mHandler.post(this)
                    else {
                        onZoomAnimationCompleted(scale)
                        center(horizontal = true, vertical = true)
                    }
                }
            }
        )
    }

    fun getCroppedImage(): Bitmap? {
        val cropInfo = getCropInfo() ?: return null
        return if (imageFilePath != null) cropInfo.getCroppedImage(imageFilePath)
        else viewBitmap?.let { viewBitmap -> cropInfo.getCroppedImage(viewBitmap) }
    }

    fun getCropInfo(): CropInfo? {
        val viewBitmap = viewBitmap ?: return null
        val scale = baseScale * scale
        val viewImageRect = bitmapRect
        return CropInfo(scale, viewBitmap.width.toFloat(), viewImageRect!!.top, viewImageRect.left, mCropRect.top, mCropRect.left, mCropRect.width(), mCropRect.height())
    }

    val viewBitmap: Bitmap?
        get() {
            val draw = drawable
            return if (draw != null) (draw as FastBitmapDrawable).bitmap else null.also { logE("getViewBitmap", "drawable is null") }
        }

    fun setGridInnerMode(gridInnerMode: Int) {
        this.gridInnerMode = gridInnerMode
        invalidate()
    }

    fun setGridOuterMode(gridOuterMode: Int) {
        this.gridOuterMode = gridOuterMode
        invalidate()
    }

    fun setCropViewMargin(vertical: Int = 0, horizontal: Int = 0) {
        gridTopBottomMargin = dpToPx(vertical).toFloat()
        gridLeftRightMargin = dpToPx(horizontal).toFloat()
    }

    fun setGridLeftRightMargin(marginDP: Int) {
        gridLeftRightMargin = dpToPx(marginDP).toFloat()
        requestLayout()
    }

    fun setGridTopBottomMargin(marginDP: Int) {
        gridTopBottomMargin = dpToPx(marginDP).toFloat()
        requestLayout()
    }

    fun setDoubleTapListener(listener: OnImageViewTouchDoubleTapListener?) {
        mDoubleTapListener = listener
    }

    fun setSingleTapListener(listener: OnImageViewTouchSingleTapListener?) {
        mSingleTapListener = listener
    }

    fun setScaleEnabled(value: Boolean) {
        mScaleEnabled = value
    }

    fun setScrollEnabled(value: Boolean) {
        mScrollEnabled = value
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mBitmapChanged) return false
        isGesturing = true
        mScaleDetector!!.onTouchEvent(event)
        if (!mScaleDetector!!.isInProgress) mGestureDetector!!.onTouchEvent(event)
        val action = event.action
        when (action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_UP -> return onUp(event)
        }
        return true
    }

    protected fun onDoubleTapPost(scale: Float, maxZoom: Float): Float {
        return if (mDoubleTapDirection == 1) {
            if (scale + mScaleFactor * 2 <= maxZoom) {
                scale + mScaleFactor
            } else {
                mDoubleTapDirection = -1
                maxZoom
            }
        } else {
            mDoubleTapDirection = 1
            1f
        }
    }


    fun onSingleTapConfirmed(event: MotionEvent?): Boolean {
        return true
    }

    fun onScroll(event1: MotionEvent?, event2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        mUserScaled = true
        scrollBy(-distanceX, -distanceY)
        invalidate()
        return true
    }

    fun onFling(event1: MotionEvent, event2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        val diffX = event2.x - event1.x
        val diffY = event2.y - event1.y
        if (abs(velocityX) > 800 || abs(velocityY) > 800) {
            mUserScaled = true
            scrollBy(diffX / 2, diffY / 2, 300.0)
            invalidate()
            return true
        }
        return false
    }

    fun onDown(event: MotionEvent?) = !mBitmapChanged

    fun onUp(event: MotionEvent?): Boolean {
        logD("onUp", "______")
        if (mBitmapChanged) return false
        if (scale < minScale) zoomTo(minScale, 50f)
        isGesturing = false
        invalidate()
        return true
    }

    fun onSingleTapUp(event: MotionEvent?) = !mBitmapChanged

    inner class GestureListener : SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            if (null != mSingleTapListener) {
                mSingleTapListener!!.onSingleTapConfirmed()
            }
            return this@ImageCropView.onSingleTapConfirmed(e)
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            logD("onDoubleTap", "Double tap enabled? $doubleTapEnabled")
            if (doubleTapEnabled) {
                mUserScaled = true
                val scale = scale
                var targetScale = onDoubleTapPost(scale, maxScale)
                targetScale = min(maxScale, max(targetScale, minScale))
                zoomTo(targetScale, e.x, e.y, DEFAULT_ANIMATION_DURATION.toFloat())
                invalidate()
            }
            if (null != mDoubleTapListener) {
                mDoubleTapListener!!.onDoubleTap()
            }
            return super.onDoubleTap(e)
        }

        override fun onLongPress(e: MotionEvent) {
            if (isLongClickable) {
                if (!mScaleDetector!!.isInProgress) {
                    isPressed = true
                    performLongClick()
                }
            }
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            if (!mScrollEnabled) return false
            if (e1 == null || e2 == null) return false
            if (e1.pointerCount > 1 || e2.pointerCount > 1) return false
            return if (mScaleDetector!!.isInProgress) false else this@ImageCropView.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            if (!mScrollEnabled) return false
            if (e1.pointerCount > 1 || e2.pointerCount > 1) return false
            return if (mScaleDetector!!.isInProgress) false else this@ImageCropView.onFling(e1, e2, velocityX, velocityY)
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            return this@ImageCropView.onSingleTapUp(e)
        }

        override fun onDown(e: MotionEvent): Boolean {
            return this@ImageCropView.onDown(e)
        }
    }

    inner class ScaleListener : SimpleOnScaleGestureListener() {
        var mScaled = false
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            isChangingScale = true
            return super.onScaleBegin(detector)
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            logD("onScale", "Entry")
            val span = detector.currentSpan - detector.previousSpan
            var targetScale = scale * detector.scaleFactor
            if (mScaleEnabled) {
                if (mScaled && span != 0f) {
                    mUserScaled = true
                    targetScale = min(maxScale, max(targetScale, minScale - 0.1f))
                    zoomTo(targetScale, detector.focusX, detector.focusY)
                    mDoubleTapDirection = 1
                    invalidate()
                    return true
                }

                // This is to prevent a glitch the first time
                // image is scaled.
                if (!mScaled) mScaled = true
            }
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            logD("onScale", "End")
            isChangingScale = false
            super.onScaleEnd(detector)
        }
    }

    interface OnImageViewTouchDoubleTapListener {
        fun onDoubleTap()
    }

    interface OnImageViewTouchSingleTapListener {
        fun onSingleTapConfirmed()
    }

    fun getPositionInfo(): FloatArray {
        val vals = FloatArray(9)
        mSuppMatrix.getValues(vals)
        return vals
    }

    fun setPositionInfo(values: FloatArray) {
        mBitmapChanged = true
        mRestoreRequest = true
        applyValues(values)
        requestLayout()
    }

    private fun applyValues(values: FloatArray) {
        logI("applyValues", "Matrix updated based on previous position info")
        mSuppMatrix = Matrix()
        mSuppMatrix.setValues(values)
        imageMatrix = imageViewMatrix
        postInvalidate()
    }

    companion object {
        protected const val LOG_ENABLED = false
        const val ZOOM_INVALID = -1f
        const val DEFAULT_ASPECT_RATIO_WIDTH = 1
        const val DEFAULT_ASPECT_RATIO_HEIGHT = 1
        const val GRID_OFF = 0
        const val GRID_ON = 1
        private const val DEFAULT_OUTSIDE_LAYER_COLOR_ID = "#f2f2f2"
        private const val DEFAULT_OUTSIDE_MOVING_LAYER_COLOR_ID = "#4DF2F2F2"
    }

    init {
        init(context, attrs)
    }

    private fun logV(functionName: String = "", message: String) = run { if (LOG_ENABLED) Log.v(this::class.java.simpleName, "$functionName() - $message") }
    private fun logD(functionName: String = "", message: String) = run { if (LOG_ENABLED) Log.d(this::class.java.simpleName, "$functionName() - $message") }
    private fun logI(functionName: String = "", message: String) = run { if (LOG_ENABLED) Log.i(this::class.java.simpleName, "$functionName() - $message") }
    private fun logE(functionName: String = "", message: String) = run { if (LOG_ENABLED) Log.e(this::class.java.simpleName, "$functionName() - $message") }

}