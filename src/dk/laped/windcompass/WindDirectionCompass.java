package dk.laped.windcompass;

import java.util.ArrayList;
import java.util.EnumSet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class WindDirectionCompass extends View {

	private static final String TAG = WindDirectionCompass.class
			.getSimpleName();

	private ArrayList<Float> directionAngles;
	private float sweepAnglePadding = 22.5f;

	// drawing tools
	private RectF rimRect;
	private Paint rimPaint;
	private Paint backgroundPaint;

	private RectF scaleRect;
	private Paint scalePaint;
	private Paint textScalePaint;

	private Paint arcPaint;

	private Paint arrowPaint;
	private Path arrowPath;
	private float arrowAngle;
	private Boolean isPerfectWindDirection = false;
	// end drawing tools

	private Bitmap background; // holds the cached static part

	// scale configuration
	private static final int nickStartDegree = 45;
	private static final int nickDegreeSpace = 90;

	public WindDirectionCompass(Context context) {
		super(context);
		initDrawingTools();
	}

	public WindDirectionCompass(Context context, AttributeSet attrs) {
		super(context, attrs);
		initDrawingTools();
	}

	public WindDirectionCompass(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initDrawingTools();
	}

	private void initDrawingTools() {
		directionAngles = new ArrayList<Float>();

		scaleRect = new RectF(0.11f, 0.11f, 0.89f, 0.89f);

		float rimPosition = 0.04f;
		rimRect = new RectF();
		rimRect.set(scaleRect.left + rimPosition, scaleRect.top + rimPosition,
				scaleRect.right - rimPosition, scaleRect.bottom - rimPosition);

		rimPaint = new Paint();
		rimPaint.setAntiAlias(true);
		rimPaint.setStyle(Paint.Style.STROKE);
		rimPaint.setColor(Color.rgb(83, 83, 83));
		rimPaint.setStrokeWidth(0.03f);

		backgroundPaint = new Paint();
		backgroundPaint.setFilterBitmap(true);

		scalePaint = new Paint();
		scalePaint.setStyle(Paint.Style.STROKE);
		scalePaint.setColor(Color.rgb(83, 83, 83));
		scalePaint.setStrokeWidth(0.03f);
		scalePaint.setAntiAlias(true);

		textScalePaint = new Paint();
		textScalePaint.setAntiAlias(true);
		textScalePaint.setColor(Color.rgb(83, 83, 83));
		textScalePaint.setTextSize(0.15f);
		textScalePaint.setTypeface(Typeface.DEFAULT);
		textScalePaint.setTextAlign(Paint.Align.CENTER);

		arcPaint = new Paint();
		arcPaint.setAntiAlias(true);
		arcPaint.setColor(Color.rgb(175, 175, 175));

		arrowPaint = new Paint();
		arrowPaint.setAntiAlias(true);
		arrowPaint.setColor(Color.parseColor("#dd0d0b"));
		arrowPaint.setStyle(Paint.Style.FILL);

		arrowPath = new Path();
		arrowPath.moveTo(0.50f, 0.34f);
		arrowPath.lineTo(0.46f, 0.34f);
		arrowPath.lineTo(0.46f, 0.52f);
		arrowPath.lineTo(0.41f, 0.52f);
		arrowPath.lineTo(0.50f, 0.64f);
		arrowPath.lineTo(0.60f, 0.52f);
		arrowPath.lineTo(0.54f, 0.52f);
		arrowPath.lineTo(0.54f, 0.34f);
		arrowPath.close();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.d(TAG, "Width spec: " + MeasureSpec.toString(widthMeasureSpec));
		Log.d(TAG, "Height spec: " + MeasureSpec.toString(heightMeasureSpec));

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);

		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int chosenWidth = chooseDimension(widthMode, widthSize);
		int chosenHeight = chooseDimension(heightMode, heightSize);

		int chosenDimension = Math.min(chosenWidth, chosenHeight);

		setMeasuredDimension(chosenDimension, chosenDimension);
	}

	private int chooseDimension(int mode, int size) {
		if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
			return size;
		} else {
			return getPreferredSize();
		}
	}

	// in case there is no size specified
	private int getPreferredSize() {
		return 80;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawBackground(canvas);

		float scale = (float) getWidth();
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.scale(scale, scale);

		drawArrow(canvas);

		canvas.restore();
	}

	private void drawBackground(Canvas canvas) {
		if (background == null) {
			Log.w(TAG, "Background not created");
		} else {
			canvas.drawBitmap(background, 0, 0, backgroundPaint);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		Log.d(TAG, "Size changed to " + w + "x" + h);

		regenerateBackground();
	}

	private void regenerateBackground() {
		// free the old bitmap
		if (background != null) {
			background.recycle();
		}

		background = Bitmap.createBitmap(getWidth(), getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas backgroundCanvas = new Canvas(background);
		float scale = (float) getWidth();
		backgroundCanvas.scale(scale, scale);

		drawScale(backgroundCanvas);
		drawWindDirection(backgroundCanvas);
		drawRim(backgroundCanvas);
	}

	private void drawRim(Canvas canvas) {
		canvas.drawOval(rimRect, rimPaint);
	}

	private void drawScale(Canvas canvas) {
		// canvas.drawOval(scaleRect, scalePaint);
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		float y1 = rimRect.top;
		float y2 = y1 - 0.048f;

		// First nick
		canvas.rotate(nickStartDegree, 0.5f, 0.5f);
		canvas.drawLine(0.5f, y1, 0.5f, y2, scalePaint);

		// Second nick
		canvas.rotate(nickDegreeSpace, 0.5f, 0.5f);
		canvas.drawLine(0.5f, y1, 0.5f, y2, scalePaint);

		// Third nick
		canvas.rotate(nickDegreeSpace, 0.5f, 0.5f);
		canvas.drawLine(0.5f, y1, 0.5f, y2, scalePaint);

		// Fourth nick
		canvas.rotate(nickDegreeSpace, 0.5f, 0.5f);
		canvas.drawLine(0.5f, y1, 0.5f, y2, scalePaint);

		canvas.restore();

		// North char
		canvas.drawText("N", 0.5f, scaleRect.top, textScalePaint);

		// East char
		canvas.drawText("Ø", scaleRect.right + 0.045f, 0.552f, textScalePaint);

		// South char
		canvas.drawText("S", 0.5f, scaleRect.bottom + 0.1f, textScalePaint);

		// West char
		canvas.drawText("V", scaleRect.left - 0.045f, 0.552f, textScalePaint);

	}

	private void drawWindDirection(Canvas canvas) {
		Log.d(TAG, "drawWindDirection=" + directionAngles);
		int index = 0;
		for (Float startAngle : directionAngles) {
			canvas.drawArc(rimRect, startAngle - 1f, 46f, true, arcPaint);
			index++;
		}
	}

	private void drawArrow(Canvas canvas) {
		if (isPerfectWindDirection)
			arrowPaint.setColor(Color.parseColor("#34860a"));

		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.rotate(arrowAngle, 0.5f, 0.5f);
		canvas.drawPath(arrowPath, arrowPaint);
		canvas.restore();
	}

	public void setWindDirections(Integer windDirections) {

		if (windDirections != null) {

			EnumSet<WindDirectionType> windSet = EnumUtils.decode(
					windDirections, WindDirectionType.class);

			Log.d(TAG, "setWindDirections= " + windDirections + " winSet="
					+ windSet);

			directionAngles.clear();
			for (WindDirectionType windDirectionType : windSet) {
				switch (windDirectionType) {
				case North:
					directionAngles.add(270 - sweepAnglePadding);
					break;
				case NorthEast:
					directionAngles.add(315 - sweepAnglePadding);
					break;
				case East:
					directionAngles.add(360 - sweepAnglePadding);
					break;
				case SouthEast:
					directionAngles.add(45 - sweepAnglePadding);
					break;
				case South:
					directionAngles.add(90 - sweepAnglePadding);
					break;
				case SouthWest:
					directionAngles.add(135 - sweepAnglePadding);
					break;
				case West:
					directionAngles.add(180 - sweepAnglePadding);
					break;
				case NorthWest:
					directionAngles.add(225 - sweepAnglePadding);
					break;
				}
			}
		}
		invalidate();
	}

	public void setCurrentWindDegree(double windDirectionDegree) {
		arrowAngle = (float) windDirectionDegree;
		invalidate();
	}

	public void setIsPerfectWindDirection(Boolean isPerfect) {
		this.isPerfectWindDirection = isPerfect;
		invalidate();
	}

}
