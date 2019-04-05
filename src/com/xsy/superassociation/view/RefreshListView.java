package com.xsy.superassociation.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xsy.superassociation.action.R;

public class RefreshListView extends ListView implements OnScrollListener,OnItemClickListener{
	private ImageView refresh_hander_arraw;
	private ProgressBar refresh_hander_progressBar;
	private TextView refresh_hander_title;
	private TextView refresh_hander_date;
	private int myHanderViewHeight;
	private int startY = -1;
	private View handerView;
	
	private static final int PULL_REFRESH = 1;
	private static final int REFRESHING = 2;
	private static final int RELEASE_REFRESH = 3;
	
	private int currentState = PULL_REFRESH;
	private RotateAnimation upAnimation;
	private RotateAnimation downAnimation;

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHanderView();
		initFooterView();
	}
	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHanderView();
		initFooterView();
	}
	public RefreshListView(Context context) {
		super(context);
		initHanderView();
		initFooterView();
	}
	private void initFooterView(){
		footerView = View.inflate(getContext(), R.layout.listview_footerview, null);
		addFooterView(footerView);
		footerView.measure(0, 0);
		myFooterHeight = footerView.getMeasuredHeight();
		footerView.setPadding(0, -myFooterHeight, 0, 0);
		setOnScrollListener(this);
	}
	private void initHanderView() {
		handerView = View.inflate(getContext(), R.layout.refresh_listview_hander, null);
		refresh_hander_arraw = (ImageView) handerView.findViewById(R.id.refresh_hander_arraw);
		refresh_hander_progressBar = (ProgressBar) handerView.findViewById(R.id.refresh_hander_progressBar);
		refresh_hander_title = (TextView) handerView.findViewById(R.id.refresh_hander_title);
		refresh_hander_date = (TextView) handerView.findViewById(R.id.refresh_hander_date);
		addHeaderView(handerView);
		handerView.measure(0, 0);
		myHanderViewHeight = handerView.getMeasuredHeight();
		handerView.setPadding(0, -myHanderViewHeight, 0, 0);
		initAnimation();
		refresh_hander_date.setText("最后刷新:" + getCurrentTime());
	}
	private void initAnimation() {
		upAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		upAnimation.setDuration(600);
		upAnimation.setFillAfter(true);
		downAnimation = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		downAnimation.setDuration(600);
		downAnimation.setFillAfter(true);
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (startY == -1) {
				startY = (int) ev.getRawY();
			}
			if (currentState == REFRESHING) {
				break;
			}
			int endY = (int) ev.getRawY();
			int dy = endY - startY;
			if (dy > 0 && getFirstVisiblePosition() == 0) {
				int padding = dy-myHanderViewHeight;
				handerView.setPadding(0, padding, 0, 0);
				if (padding > 0 && currentState != RELEASE_REFRESH) {
					currentState = RELEASE_REFRESH;
					updateState();
				} else if (padding <= 0 && currentState != PULL_REFRESH) {
					currentState = PULL_REFRESH;
					updateState();
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			startY = -1;
			if (currentState == PULL_REFRESH) {
				handerView.setPadding(0, -myHanderViewHeight, 0, 0);
			} else if(currentState == RELEASE_REFRESH){
				currentState = REFRESHING;
				handerView.setPadding(0, 0, 0, 0);
				updateState();
				if (myListener != null) {
					myListener.refreshData();
				}
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	private void updateState() {
		switch (currentState) {
		case PULL_REFRESH:
			refresh_hander_title.setText("下拉刷新");
			refresh_hander_progressBar.setVisibility(View.INVISIBLE);
			refresh_hander_arraw.setVisibility(View.VISIBLE);
			refresh_hander_arraw.startAnimation(downAnimation);
			break;
		case RELEASE_REFRESH:
			refresh_hander_title.setText("松开刷新");
			refresh_hander_progressBar.setVisibility(View.INVISIBLE);
			refresh_hander_arraw.setVisibility(View.VISIBLE);
			refresh_hander_arraw.startAnimation(upAnimation);
			break;
		case REFRESHING:
			refresh_hander_title.setText("正在刷新...");
			refresh_hander_arraw.clearAnimation();
			refresh_hander_progressBar.setVisibility(View.VISIBLE);
			refresh_hander_arraw.setVisibility(View.INVISIBLE);
			break;

		default:
			break;
		}
	}
	private RefreshListViewListener myListener;
	private View footerView;
	private int myFooterHeight;
	public void setMyListener(RefreshListViewListener myListener) {
		this.myListener = myListener;
	}
	public interface RefreshListViewListener{
		public void refreshData();
		public void londMoreData();
	}
	boolean isLondMone = false;
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
			if (getLastVisiblePosition() == getCount() - 1 && !isLondMone) {
				footerView.setPadding(0, 0, 0, 0);
				setSelection(getCount() - 1);
				isLondMone = true;
				if (myListener != null) {
					myListener.londMoreData();
				}
			}
		}
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}
	public void recoverPullRefresh(boolean isupdateTime){
		refresh_hander_title.setText("下拉刷新");
		refresh_hander_progressBar.setVisibility(View.INVISIBLE);
		refresh_hander_arraw.setVisibility(View.VISIBLE);
		refresh_hander_arraw.clearAnimation();
		currentState = PULL_REFRESH;
		handerView.setPadding(0, -myHanderViewHeight, 0, 0);
		if (isupdateTime) {
			refresh_hander_date.setText("最后刷新:" + getCurrentTime());
		}
	}
	public void recoverFooter(){
		footerView.setPadding(0, -myFooterHeight, 0, 0);
		isLondMone = false;
	}
	private String getCurrentTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	OnItemClickListener myOnItemClickListener;
	@Override
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener listener) {
		super.setOnItemClickListener(this);
		myOnItemClickListener = listener;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (myOnItemClickListener != null) {
			myOnItemClickListener.onItemClick(parent, view, position - getHeaderViewsCount(), id);
		}
	}
}
