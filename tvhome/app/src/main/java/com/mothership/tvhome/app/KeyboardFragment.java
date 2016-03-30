package com.mothership.tvhome.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mothership.tvhome.R;


public class KeyboardFragment extends Fragment implements View.OnClickListener {

    private TextView mInput;
    private ViewGroup mLetterKeyboard;
    private ViewGroup mNumberKeyboard;
    private String mText = "";
    private OnTextChangedListener mChangedListener;

    public interface OnTextChangedListener {
        public void onTextChanged(String text);
    }

    public void setOnTextChangedListener(OnTextChangedListener listener) {
        mChangedListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keyboard, container, false);
        mLetterKeyboard = (ViewGroup) view.findViewById(R.id.keyBoard);
        mNumberKeyboard = (ViewGroup) view.findViewById(R.id.keyBoardNumber);
        setKeyListeners(mLetterKeyboard);
        setKeyListeners(mNumberKeyboard);
        view.findViewById(R.id.backspaceBtn).setOnLongClickListener(mBackspaceLongClickListener);
        view.findViewById(R.id.backspaceBtnInNumber).setOnLongClickListener(mBackspaceLongClickListener);
        mInput = (TextView) view.findViewById(R.id.inputBox);
        return view;
    }

    private void setKeyListeners(ViewGroup keyboard) {
        int keyCount = keyboard.getChildCount();
        for (int i = 0; i < keyCount; i++) {
            View key = keyboard.getChildAt(i);
            key.setOnClickListener(this);
        }
    }

    private View.OnLongClickListener mBackspaceLongClickListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View arg0) {
            updateText("");
            return true;
        }
    };

    @Override
    public void onClick(View view) {
        View btn = view;
        if (btn.getId() == R.id.backspaceBtn || btn.getId() == R.id.backspaceBtnInNumber) {
            if (mText.length() > 0) {
                updateText(mText.substring(0, mText.length() - 1));
            }
        } else if (btn.getId() == R.id.switchLetterBtn) {
            switchToLetter();
        } else if (btn.getId() == R.id.switchNumberBtn) {
            switchToNumber();
        } else {
            if (mText.length() < 12) {
                updateText(mText + ((Button) btn).getText().toString());
            }
        }
    }

    private void switchToLetter() {
        mLetterKeyboard.setVisibility(View.VISIBLE);
        mNumberKeyboard.setVisibility(View.GONE);
    }

    private void switchToNumber() {
        mNumberKeyboard.setVisibility(View.VISIBLE);
        mLetterKeyboard.setVisibility(View.GONE);
    }

    private void updateText(String text) {
        if (mText.equals(text)) {
            return;
        }
        mText = text;
        mInput.setText(mText);
        if (mChangedListener != null) {
            mChangedListener.onTextChanged(mText);
        }
    }

}
