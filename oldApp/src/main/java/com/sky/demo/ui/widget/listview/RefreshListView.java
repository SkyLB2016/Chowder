package com.sky.demo.ui.widget.listview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.sky.demo.R;

/**
 * @author 彬 QQ 1136096189
 * @Description: TODO 重写的listview
 * @date 2015/8/17 15:30
 */
public class RefreshListView extends ListView implements OnScrollListener {

	private final static int SCROLL_BACK_HEADER = 0;
	private final static int SCROLL_BACK_FOOTER = 1;

	private final static int SCROLL_DURATION = 400;

	// when pull up >= 50px 下拉超过50px
	private final static int PULL_LOAD_MORE_DELTA = 50;

	// 控制头尾缩放的高
	private final static float OFFSET_RADIO = 1.8f;

	private float mLastY = -1;

	// 滑屏是返回的监听类
	private Scroller mScroller;
	// 滑动监听接口
	private OnScrollListener mScrollListener;
	// 监听是在头部，还是在尾部
	private int mScrollBack;

	// 外部调用刷新接口
	private OnRefreshListener mListener;
	// 头部主视图
	private HeaderView mHeader;
	private RelativeLayout mHeaderContent;
	private TextView mHeaderTime;
	private int mHeaderHeight;
	// 尾部主视图
	private LinearLayout mFooterLayout;
	private FooterView mFooterView;
	private boolean mIsFooterReady = false;// 确保尾部视图只加载一次

	private boolean mEnablePullRefresh = true;// 是否支持刷新
	private boolean mPullRefreshing = false;// 刷新的状态

	private boolean mEnablePullLoad = true;// 是否支持载入
	private boolean mEnableAutoLoad = false;// 自动加载
	private boolean mPullLoading = false;// 载入的状态

	// total list items, used to detect is at the bottom of ListView
	// ListView的item的总数，包含head与foot
	private int mTotalItemCount;

	public RefreshListView(Context context) {
		this(context, null);
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}

	private void initWithContext(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		super.setOnScrollListener(this);

		// init list_header view 载入head
		mHeader = new HeaderView(context);
		mHeaderContent = (RelativeLayout) mHeader
				.findViewById(R.id.header_content);
		mHeaderTime = (TextView) mHeader.findViewById(R.id.header_last_time);
		addHeaderView(mHeader);

		// init list_footer view
		mFooterView = new FooterView(context);
		mFooterLayout = new LinearLayout(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		params.gravity = Gravity.CENTER;
		mFooterLayout.addView(mFooterView, params);

		// init list_header height
		ViewTreeObserver observer = mHeader.getViewTreeObserver();
		if (null != observer) {
			// 添加视图改变监听
			observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@SuppressWarnings("deprecation")
				@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
				@Override
				public void onGlobalLayout() {
					mHeaderHeight = mHeaderContent.getHeight();
					ViewTreeObserver observer = getViewTreeObserver();

					if (null != observer) {
						if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
							observer.removeGlobalOnLayoutListener(this);
						} else {
							observer.removeOnGlobalLayoutListener(this);
						}
					}
				}
			});
		}
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		// 确保尾部视图只加载了一次
		if (!mIsFooterReady) {
			mIsFooterReady = true;
			addFooterView(mFooterLayout);
		}

		super.setAdapter(adapter);
	}

	/**
	 * 显示与隐藏header
	 *
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		mHeaderContent.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
	}

	/**
	 * 显示或隐藏foot
	 *
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;

		if (!mEnablePullLoad) {
			mFooterView.setBottomMargin(0);
			mFooterView.hide();
			mFooterView.setPadding(0, 0, 0, mFooterView.getHeight() * (-1));
			mFooterView.setOnClickListener(null);

		} else {
			mPullLoading = false;
			mFooterView.setPadding(0, 0, 0, 0);
			mFooterView.show();
			mFooterView.setState(FooterView.STATE_NORMAL);
			// 下拉或点击按钮都能加载更多.
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}

	/**
	 * Enable or disable auto load more feature when scroll to bottom.
	 * scroll滑动到底部时是否刷新
	 *
	 * @param enable
	 */
	public void setAutoLoadEnable(boolean enable) {
		mEnableAutoLoad = enable;
	}

	/**
	 * Stop refresh, reset list_header view. 结束刷新，重置header的高
	 */
	public void stopRefresh() {
		if (mPullRefreshing) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}

	/**
	 * Stop load more, reset list_footer view. 结束载入，重置foot的高
	 */
	public void stopLoadMore() {
		if (mPullLoading) {
			mPullLoading = false;
			mFooterView.setState(FooterView.STATE_NORMAL);
		}
	}

	/**
	 * Set last refresh time 设置最后的刷新时间
	 * 
	 * @param time
	 */
	public void setRefreshTime(String time) {
		mHeaderTime.setText(time);
	}

	/**
	 * Set listener. 设置外部监听
	 * 
	 * @param listener
	 */
	public void setRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	/**
	 * Auto call back refresh.
	 * 自动刷新
	 */
	public void autoRefresh() {
		mHeader.setVisibleHeight(mHeaderHeight);

		if (mEnablePullRefresh && !mPullRefreshing) {
			// image的转换状态
			if (mHeader.getVisibleHeight() > mHeaderHeight) {
				mHeader.setState(HeaderView.STATE_READY);
			} else {
				mHeader.setState(HeaderView.STATE_NORMAL);
			}
		}

		mPullRefreshing = true;
		mHeader.setState(HeaderView.STATE_REFRESHING);
		refresh();
	}

	/**
	 * 滑动监听
	 */
	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnRefreshScrollListener) {
			OnRefreshScrollListener listener = (OnRefreshScrollListener) mScrollListener;
			listener.onRefreshScrolling(this);
		}
	}

	/**
	 * 更新header的高 
	 * @param delta
	 */
	private void updateHeaderHeight(float delta) {
		mHeader.setVisibleHeight((int) delta + mHeader.getVisibleHeight());

		if (mEnablePullRefresh && !mPullRefreshing) {
			// image状态更新
			if (mHeader.getVisibleHeight() > mHeaderHeight) {
				mHeader.setState(HeaderView.STATE_READY);
			} else {
				mHeader.setState(HeaderView.STATE_NORMAL);
			}
		}
		// scroll to top each time 自动定位到listview顶部
		setSelection(0);
	}

	/**
	 * 重置header状态
	 */
	private void resetHeaderHeight() {
		int height = mHeader.getVisibleHeight();
		if (height == 0)
			return;

		// refreshing and list_header isn't shown fully. do nothing. 判断header刷新状态是否已重置
		if (mPullRefreshing && height <= mHeaderHeight)
			return;

		// default: scroll back to dismiss list_header.
		int finalHeight = 0;
		// is refreshing, just scroll back to show all the list_header.
		if (mPullRefreshing && height > mHeaderHeight) {
			finalHeight = mHeaderHeight;
		}

		mScrollBack = SCROLL_BACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height,
				SCROLL_DURATION);

		// trigger computeScroll 触发器
		invalidate();
	}

	/**
	 * 更新foot的高
	 * @param delta
	 */
	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;

		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) {
				// height enough to invoke load more.
				mFooterView.setState(FooterView.STATE_READY);
			} else {
				mFooterView.setState(FooterView.STATE_NORMAL);
			}
		}

		mFooterView.setBottomMargin(height);

		// scroll to bottom
		// setSelection(mTotalItemCount - 1);
	}

	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();

		if (bottomMargin > 0) {
			mScrollBack = SCROLL_BACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}

	private void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(FooterView.STATE_LOADING);
		loadMore();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();
			break;

		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();

			if (getFirstVisiblePosition() == 0
					&& (mHeader.getVisibleHeight() > 0 || deltaY > 0)) {
				// the first item is showing, list_header has shown or pull down.
				updateHeaderHeight(deltaY / OFFSET_RADIO);
				invokeOnScrolling();

			} else if (getLastVisiblePosition() == mTotalItemCount - 1
					&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
				// last item, already pulled up or want to pull up.
				updateFooterHeight(-deltaY / OFFSET_RADIO);
			}
			break;

		default:
			// reset
			mLastY = -1;
			if (getFirstVisiblePosition() == 0) {
				// invoke refresh
				if (mEnablePullRefresh
						&& mHeader.getVisibleHeight() > mHeaderHeight) {
					mPullRefreshing = true;
					mHeader.setState(HeaderView.STATE_REFRESHING);
					refresh();
				}
				resetHeaderHeight();

			} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
				// invoke load more.
				if (mEnablePullLoad&&!mEnableAutoLoad
						&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
					startLoadMore();
				}
				resetFooterHeight();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLL_BACK_HEADER) {
				mHeader.setVisibleHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}

			postInvalidate();
			invokeOnScrolling();
		}

		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}

		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			if (mEnableAutoLoad && getLastVisiblePosition() == getCount() - 1) {
				//滑动到底部自动刷新
				startLoadMore();
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// send to user's listener 
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}

	private void refresh() {
		if (mEnablePullRefresh && null != mListener) {
			mListener.onRefresh();
		}
	}

	private void loadMore() {
		if (mEnablePullLoad && null != mListener) {
			mListener.onLoadMore();
		}
	}

	public interface OnRefreshScrollListener extends OnScrollListener {
		public void onRefreshScrolling(View view);
	}

	public interface OnRefreshListener {
		public void onRefresh();

		public void onLoadMore();
	}
}
