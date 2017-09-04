package com.sky.demo.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.sky.demo.R;

/**
 * @author sky QQ:1136096189
 * @Description: TODO
 * @date 16/4/15 上午11:11
 */
public class EditTextDel extends EditText {
    private Drawable image;

    public EditTextDel(Context context) {
        super(context, null);
    }

    public EditTextDel(Context context, AttributeSet attrs) {
        this(context, attrs,R.attr.editTextStyle);
    }

    public EditTextDel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        image = context.getResources().getDrawable(R.mipmap.common_input_box_clear);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (length() > 0)
                    setCompoundDrawablesWithIntrinsicBounds(null, null, image, null);
                else setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
//                int x = (int) getX();
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();
                Rect rect = new Rect();
                getGlobalVisibleRect(rect);
                int height = rect.bottom - rect.top;
                rect.left = rect.right - height;
                if (image != null && rect.contains(x, y)) setText("");
                break;
        }
        return super.onTouchEvent(event);
    }
}
