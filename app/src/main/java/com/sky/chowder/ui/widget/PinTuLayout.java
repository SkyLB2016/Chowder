package com.sky.chowder.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sky.chowder.R;
import com.sky.chowder.model.ImagePiece;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author sky QQ:1136096189
 * @Description: TODO
 * @date 15/12/24 上午10:58
 */
public class PinTuLayout extends RelativeLayout implements View.OnClickListener {
    public PinTuLayout(Context context) {
        this(context, null);
    }

    public PinTuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinTuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private ImageView[] imageViews;
    private List<ImagePiece> imagePieces;
    private int margin = 2;
    private int piece = 3;

    private void initView() {
        margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin, getResources().getDisplayMetrics());
    }

    private boolean once = true;
    private int pieceWidth;
    private int width;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = Math.min(getMeasuredHeight(), getMeasuredWidth());

        setMeasuredDimension(width, width);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (once) {
            setView();


            once = false;
        }
    }

    private void setView() {
        imageViews = new ImageView[piece * piece];
        imagePieces = jigsaw(
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_banner), piece);
        //随机打乱数组排序
        Collections.sort(imagePieces, new Comparator<ImagePiece>() {
            @Override
            public int compare(ImagePiece lhs, ImagePiece rhs) {
                return Math.random() > 0.5 ? 1 : -1;
            }
        });
        pieceWidth = (width - margin * (piece + 1)) / piece;
        for (int i = 0; i < imagePieces.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageBitmap(imagePieces.get(i).getBitmap());
            imageView.setOnClickListener(this);
            imageView.setId(i + 1);
            imageView.setTag(i + "," + imagePieces.get(i).getNumber());
            imageViews[i] = imageView;
            LayoutParams lp = new LayoutParams(pieceWidth, pieceWidth);
            int leftMargin = i % piece * pieceWidth + (i % piece + 1) * margin;
            int topMargin = i / piece * pieceWidth + (i / piece + 1) * margin;
            lp.setMargins(leftMargin, topMargin, 0, 0);
            addView(imageView, lp);
        }
    }
    public static List<ImagePiece> jigsaw(Bitmap bitmap, int piece) {
        List<ImagePiece> imagePieces = new ArrayList<>();

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pieceWidth=Math.min(width, height)/piece;
//        int pieceWidth = width / piece;
//        int pieceHeight = height / piece;
//        int index = 0;
        for (int i = 0; i < piece; i++) {//行
            for (int j = 0; j < piece; j++) {//列
                ImagePiece image = new ImagePiece();
//                image.setNumber(index);
                image.setNumber(i * piece + j);
                int x = j * pieceWidth;
                int y = i * pieceWidth;
                image.setBitmap(Bitmap.createBitmap(bitmap, x, y, pieceWidth, pieceWidth));
                imagePieces.add(image);
//                index++;
            }
        }
        return imagePieces;
    }
    private ImageView firstImg;
    private ImageView secondImg;
    private boolean isAniming = false;

    @Override
    public void onClick(View v) {
        if (isAniming)
            return;
        if (firstImg == v) {
            firstImg.setColorFilter(null);
            firstImg = null;
            return;

        }
        if (firstImg == null) {
            firstImg = (ImageView) v;
            firstImg.setColorFilter(Color.parseColor("#66ff0000"));

        } else {
            isAniming = true;
            secondImg = (ImageView) v;
            firstImg.setVisibility(View.INVISIBLE);
            secondImg.setVisibility(View.INVISIBLE);
            final Bitmap firBit = imagePieces.get(Integer.parseInt(getViewIdByTag(firstImg))).getBitmap();
            final Bitmap secBit = imagePieces.get(Integer.parseInt(getViewIdByTag(secondImg))).getBitmap();

            final RelativeLayout aniLayout = new RelativeLayout(getContext());
            addView(aniLayout);


            LayoutParams firstlp = (LayoutParams) firstImg.getLayoutParams();
            int firstLeft = firstlp.leftMargin;
            int firstTop = firstlp.topMargin;
            LayoutParams secLP = (LayoutParams) secondImg.getLayoutParams();
            int secLeft = secLP.leftMargin;
            int secTop = secLP.topMargin;
            ImageView aniFirst = new ImageView(getContext());
            ImageView aniSec = new ImageView(getContext());
            aniFirst.setLayoutParams(firstlp);
            aniSec.setLayoutParams(secLP);
            aniFirst.setImageBitmap(firBit);
            aniSec.setImageBitmap(secBit);

            aniLayout.addView(aniFirst);
            aniLayout.addView(aniSec);
//            firstImg.setLayoutParams(secLP);
//            secondImg.setLayoutParams(firstlp);
            ObjectAnimator firstX = ObjectAnimator.ofFloat(aniFirst, "translationX", 0, secLeft - firstLeft);
            ObjectAnimator firstY = ObjectAnimator.ofFloat(aniFirst, "translationY", 0, secTop - firstTop);
            ObjectAnimator secX = ObjectAnimator.ofFloat(aniSec, "translationX", 0, firstLeft - secLeft);
            ObjectAnimator secY = ObjectAnimator.ofFloat(aniSec, "translationY", 0, firstTop - secTop);

            AnimatorSet set = new AnimatorSet();
            set.setInterpolator(new BounceInterpolator());
            set.playTogether(firstX, firstY, secX, secY);
            set.setDuration(1000);
            set.start();
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    String firTag = getViewTag(firstImg);
                    String secTag = getViewTag(secondImg);

                    getViewTag(firstImg);
                    firstImg.setImageBitmap(secBit);
                    secondImg.setImageBitmap(firBit);
                    firstImg.setTag(secTag);
                    secondImg.setTag(firTag);
                    firstImg.setVisibility(View.VISIBLE);
                    secondImg.setVisibility(View.VISIBLE);

//                    secondImg
                    aniLayout.removeAllViews();
                    firstImg.setColorFilter(null);
                    firstImg = secondImg = null;
                    isAniming = false;
                    checkSuccess();

                }
            });
        }
    }

    private String getViewTag(ImageView view) {
        return (String) view.getTag();
    }

    private String getViewIdByTag(ImageView view) {
        return getViewTag(view).split(",")[0];

    }

    private String getViewIndexByTag(ImageView view) {
        return getViewTag(view).split(",")[1];
    }

    private void checkSuccess() {
        boolean isSuccess = true;
        for (int i = 0; i < imagePieces.size(); i++) {
            if (!getViewIndexByTag(imageViews[i]).equals("" + i)) {
                isSuccess = false;
            }
        }
        if (isSuccess) {
            Toast.makeText(getContext(), "success", Toast.LENGTH_LONG).show();
            removeAllViews();
            piece++;
            setView();
        }
    }
}