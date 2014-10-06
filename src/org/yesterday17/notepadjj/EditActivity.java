package org.yesterday17.notepadjj;

import org.yesterday17.graduation.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.Settings;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends Activity {

	public boolean NightMode = false;

	public static int getScreenBrightness(Activity activity) {
		int nowBrightnessValue = 0;
		ContentResolver resolver = activity.getContentResolver();
		try {
			nowBrightnessValue = android.provider.Settings.System.getInt(
					resolver, Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nowBrightnessValue;
	}

	public static class LinedEditText extends EditText {
		private Rect mRect;
		private Paint mPaint;

		// This constructor is used by LayoutInflater
		public LinedEditText(Context context, AttributeSet attrs) {
			super(context, attrs);

			// Creates a Rect and a Paint object, and sets the style and color
			// of the Paint object.
			mRect = new Rect();
			mPaint = new Paint();
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setColor(0x800000FF);
		}

		/**
		 * This is called to draw the LinedEditText object
		 * 
		 * @param canvas
		 *            The canvas on which the background is drawn.
		 */
		@Override
		protected void onDraw(Canvas canvas) {

			// Gets the number of lines of text in the View.
			int count = getLineCount();

			// Gets the global Rect and Paint objects
			Rect r = mRect;
			Paint paint = mPaint;

			/*
			 * Draws one line in the rectangle for every line of text in the
			 * EditText
			 */
			for (int i = 0; i < count; i++) {

				// Gets the baseline coordinates for the current line of text
				int baseline = getLineBounds(i, r);

				/*
				 * Draws a line in the background from the left of the rectangle
				 * to the right, at a vertical position one dip below the
				 * baseline, using the "paint" object for details.
				 */
				canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1,
						paint);
			}

			// Finishes up by calling the parent method
			super.onDraw(canvas);
		}
	}

	public void ChangeMode() {
		LinedEditText note = (LinedEditText) findViewById(R.id.notepadjj_note);
		if (NightMode) {
			NightMode = false;
			note.setBackgroundColor(getResources().getColor(
					R.color.night_mode_back));
			note.setTextColor(getResources().getColor(R.color.night_mode_text));
		} else {
			NightMode = true;
			note.setBackgroundColor(getResources().getColor(
					R.color.day_mode_back));
			note.setTextColor(getResources().getColor(R.color.day_mode_text));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notepadjj_edit);

		if (getScreenBrightness(this) == 30)
			ChangeMode();

		// ///////Test/////////
		// 30,47,102,255
		// String x = String.valueOf(getScreenBrightness(this));
		// Log.w("jhz", x);

		Intent intent = getIntent();
		if (intent.getData() != null) {
			Toast toast = Toast.makeText(this, intent.getData().getPath(),
					Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	protected void onDestroy() {
		super.onDestroy();
	}
}
