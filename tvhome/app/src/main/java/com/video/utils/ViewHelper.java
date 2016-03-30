package com.video.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

public class ViewHelper {

    public static void setVisibility(View view, boolean visible) {
        if (view != null) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    public static void setVisibilityDelayed(final View view, boolean visible, long delayMillis) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                setVisibility(view, true);
            }
        }, delayMillis);
    }


    public static boolean isVisible(View view) {
        return view != null && view.getVisibility() == View.VISIBLE;
    }

    @SuppressWarnings("deprecation")
    public static void setBackground(View view, Drawable background) {
        if (view != null && background != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(background);
            } else {
                view.setBackgroundDrawable(background);
            }
        }
    }

    public static void getBackgroudn(View view) {
        view.getBackground();
    }



    public static void setTextWithBackground(final Context context, final TextView textView, final String text, String bgUrl) {
        if (TextUtils.isEmpty(bgUrl)) {
            return; // 以bgUrl为准, 不存在即停止后续逻辑
        }

        Glide.with(context).load(bgUrl).asBitmap().centerCrop().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                textView.setBackground(new BitmapDrawable(context.getResources(), resource));
                ViewHelper.setText(textView, text);
            }
        });
    }

    public static void setText(TextView textView, String text) {
        if (textView != null && !TextUtils.isEmpty(text)) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }
    }

    public static void setText(TextView textView, boolean visible, String text) {
        if (textView != null) {
            if (visible && !TextUtils.isEmpty(text)) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(text);
            } else {
                textView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 延迟请求控件的焦点
     * @param view
     * @param delayMillis
     */
    public static void requestFocusDelayed(final View view, long delayMillis) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.requestFocus();
            }
        }, delayMillis);
    }

    /**
     * release all image resource bind on view
     * @param view
     */
    @SuppressWarnings("deprecation")
    public static void unbindDrawables(View view) {
        if (null == view) {
            return;
        }
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
            if (Build.VERSION.SDK_INT >= 16)
                view.setBackground(null);
            else
                view.setBackgroundDrawable(null);
        }
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (imageView.getDrawable() != null) {
                imageView.getDrawable().setCallback(null);
                imageView.setImageDrawable(null);
            }
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            if (!(view instanceof AdapterView<?>)) {
                ((ViewGroup) view).removeAllViews();
            }
        }
    }

    public static FrameLayout addLayout(ViewGroup group, int id) {
        FrameLayout layout = new FrameLayout(group.getContext());
        layout.setId(id);
        group.addView(layout, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        return layout;
    }


    public static void setEditTextCursorEnd(EditText editText) {
        editText.setSelection(editText.getText().length());
    }

    /**
     * 获取ListView的高度，然后设置ViewPager的高度
     *
     * @param listView
     * @return
     */
    public static int setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }

        int totalHeight = 0;
        for (int i = 0, count = listAdapter.getCount(); i < count; i++) {
            View item = listAdapter.getView(i, null, listView);
            item.measure(0, 0);
            totalHeight += item.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        return params.height;
    }

    ////////////////////////////////// FIND VIEW /////////////////////////////////////
    public static View findParentView(View view, int parentId) {
        if (view == null)
            return null;
        View parent = (View) view.getParent();
        while (parent != null && parent.getId() != parentId) {
            parent = (View) parent.getParent();
        }
        return parent;
    }

    public static <T extends View> T findParentView(View view, Class<T> c) {
        if (view == null || c == null)
            return null;
        View parent = (View) view.getParent();
        while (parent != null && !parent.getClass().equals(c)) {
            parent = (View) parent.getParent();
        }
        return (T) parent;
    }

    public static View findLastChildView(ViewGroup group) {
        if (group != null && group.getChildCount() > 0)
            return group.getChildAt(group.getChildCount() - 1);
        return null;
    }

}
