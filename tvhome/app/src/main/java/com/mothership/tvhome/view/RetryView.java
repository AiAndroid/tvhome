/**
 *   Copyright(c) 2013 DuoKan TV Group
 *    
 *   RetryLoadingView.java
 *
 *   @author xuanmingliu(liuxuanming@duokan.com)
 *
 *   2013-4-15
 */

package com.mothership.tvhome.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.mothership.tvhome.*;


/**
 *@author xuanmingliu
 *
 */

public class RetryView extends FrameLayout{

	public OnRetryLoadListener onRetryLoadListener;
	private TextView mTitle;
	public static int STYLE_NORMAL = 0;
	public static int STYLE_LIGHT = 1;
	private int style = STYLE_NORMAL;
	
	public interface OnRetryLoadListener {
		public void OnRetryLoad(View vClicked);
	}

	public RetryView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public RetryView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

    public RetryView(Context context, int style) {
        super(context);
        this.style = style;
        init();
    }

	public RetryView(Context context) {
		this(context, null, 0);
	}
	
	private void init() {
		View vContent = null;
		vContent = View.inflate(getContext(), R.layout.reload_view_dark, null);
		mTitle = (TextView) vContent.findViewById(R.id.retry_view_title);
		this.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if( onRetryLoadListener != null) {
					onRetryLoadListener.OnRetryLoad(v);
				}
			}
		});
		
		LayoutParams ltParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		ltParams.gravity = Gravity.CENTER;
		vContent.setLayoutParams(ltParams);
		addView(vContent);
	}	
	
	public void setTitle(int res){
		if(mTitle != null){
			mTitle.setText(res);
		}
	}

	public void setTitle(String res){
		if(mTitle != null){
			mTitle.setText(res);
		}
	}
	
    public void setOnRetryLoadListener(OnRetryLoadListener onRetryLoadListener) {
    	this.onRetryLoadListener = onRetryLoadListener;
    }
}


