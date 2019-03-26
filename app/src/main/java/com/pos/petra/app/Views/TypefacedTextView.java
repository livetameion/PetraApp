package com.pos.petra.app.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.pos.petra.app.R;
import com.pos.petra.app.Util.TypefaceLoader;


public class TypefacedTextView extends android.support.v7.widget.AppCompatTextView {

	private Context context;
	private String font = "Proxima-Regular.ttf";

	public TypefacedTextView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public TypefacedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(context, attrs);
	}

	public TypefacedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init(context, attrs);
	}

	private void init() {
		if (!isInEditMode()) {
			Typeface typeface = TypefaceLoader.get(context, "fonts/" + font);
			setTypeface(typeface);
		}
	}

	private void init(Context context, AttributeSet attrs) {
		if (!isInEditMode()) {
			TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TypefacedTextView);
			int index = styledAttrs.getInt(R.styleable.TypefacedTextView_customTypeface, 1);
			setFont(index);
			styledAttrs.recycle();

			/*font = "Roboto-Regular.ttf";
			switch (index) {
				case 2:
					font = "Roboto-Thin.ttf";
					break;
				case 3:
					font = "Roboto-Light.ttf";
					break;
				case 4:
					font = "Roboto-Medium.ttf";
					break;
				case 5:
					font = "Roboto-Bold.ttf";
					break;
				case 6:
					font = "Roboto-Italic.ttf";
					break;
				case 7:
					font = "Roboto-ThinItalic.ttf";
					break;
				case 8:
					font = "Roboto-LightItalic.ttf";
					break;
				case 9:
					font = "Roboto-MediumItalic.ttf";
					break;
				case 10:
					font = "Roboto-BoldItalic.ttf";
					break;
			}
			styledAttrs.recycle();
			Typeface typeface = TypefaceLoader.get(context, "fonts/" + font);
			setTypeface(typeface);*/
		}
	}

	private void setFont(int index) {
		font = "Roboto-Regular.ttf";
		switch (index) {
			case 2:
				font = "Roboto-Thin.ttf";
				break;
			case 3:
				font = "Roboto-Light.ttf";
				break;
			case 4:
				font = "Roboto-Medium.ttf";
				break;
			case 5:
				font = "Roboto-Bold.ttf";
				break;
			case 6:
				font = "Roboto-Italic.ttf";
				break;
			case 7:
				font = "Roboto-ThinItalic.ttf";
				break;
			case 8:
				font = "Roboto-LightItalic.ttf";
				break;
			case 9:
				font = "Roboto-MediumItalic.ttf";
				break;
			case 10:
				font = "Roboto-BoldItalic.ttf";
				break;
		}
		Typeface typeface = TypefaceLoader.get(context, "fonts/" + font);
		setTypeface(typeface);
	}

	public void setCustomTypeface(int index) {
		setFont(index);
		invalidate();
	}
}
