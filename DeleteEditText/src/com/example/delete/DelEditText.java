package com.example.delete;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * 自己封装的带删除按钮的输入框
 * <p>
 * 
 * 2016-04-05
 * </p>
 * 
 * @author WuMeng
 * @version 1.0
 * 
 */
public class DelEditText extends EditText {

	/** 删除按钮的资源ID - 实际项目中修改此资源ID即可 */
	private final int DALETE_DRAWABLE_ID = android.R.drawable.ic_delete;

	/** 删除按钮 */
	private Drawable daleteDrawable;

	/** 增加deleteIsShowing判断，避免重复setCompoundDrawables */
	private boolean deleteIsShowing;

	private final String TAG = "DelEditText";

	public DelEditText(Context context) {
		super(context);
		init(context);
	}

	public DelEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public DelEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	@SuppressLint("NewApi")
	public DelEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context);
	}

	private void init(Context context) {
		// 监听输入
		addTextChangedListener(mTextWatcher);
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		// 若获取焦点且包含文字，则显示
		isShowDelete(focused && null != getText() && null != getText().toString()
				&& getText().toString().length() > 0);
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}

	/**
	 * 文本输入的监听
	 */
	private TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (null != s && s.toString().length() > 0) {
				isShowDelete(true);
			} else {
				isShowDelete(false);
			}
		}
	};

	/**
	 * 显示/隐藏删除按钮
	 * 
	 * @param isShow
	 */
	private void isShowDelete(boolean isShow) {
		// 增加deleteIsShowing判断，避免重复setCompoundDrawables
		if (isShow != deleteIsShowing && isShow) {
			if (null == daleteDrawable) {
				// 初始化按钮
				daleteDrawable = getDrawable(DALETE_DRAWABLE_ID);
				// 设置边界
				daleteDrawable.setBounds(0, 0, daleteDrawable.getMinimumWidth(),
						daleteDrawable.getMinimumHeight());
			}
			// 画在右边
			setCompoundDrawables(null, null, daleteDrawable, null);
			Log.d(TAG, "set show delete ");
		} else if (isShow != deleteIsShowing && !isShow) {
			setCompoundDrawables(null, null, null, null);
			Log.d(TAG, "set hide delete ");
		}
		deleteIsShowing = isShow;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (daleteDrawable != null && event.getAction() == MotionEvent.ACTION_UP) {
			int eventX = (int) event.getRawX();
			int eventY = (int) event.getRawY();
			Rect rect = new Rect();
			getGlobalVisibleRect(rect);
			rect.left = rect.right - 50;
			if (rect.contains(eventX, eventY)) {
				setText("");
				isShowDelete(false);
			}
		} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// 重写performClick完全是为了消除黄色警告
			performClick();
		}
		return super.onTouchEvent(event);
	}

	@Override
	public boolean performClick() {
		// 重写performClick完全是为了消除黄色警告
		return super.performClick();
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public Drawable getDrawable(int resID) {
		// 根据SDK版本判断调用方法
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			return getResources().getDrawable(resID, null);
		} else {
			return getResources().getDrawable(resID);
		}
	}
}
