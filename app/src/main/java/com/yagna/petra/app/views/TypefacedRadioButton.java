package com.yagna.petra.app.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.TypefaceLoader;


public class TypefacedRadioButton extends android.support.v7.widget.AppCompatRadioButton {

	private Context context;
	private String font = "Proxima-Regular.ttf";

	public TypefacedRadioButton(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public TypefacedRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(context, attrs);
	}

	public TypefacedRadioButton(Context context, AttributeSet attrs, int defStyle) {
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
			TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TypefacedRadioButton);
			int index = styledAttrs.getInt(R.styleable.TypefacedRadioButton_customTypeface, 1);
			setFont(index);
			styledAttrs.recycle();
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
