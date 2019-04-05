package com.xsy.superassociation.fragment;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xsy.superassociation.action.R;
import com.xsy.superassociation.fragment.base.BasePager;
import com.xsy.superassociation.fragment.base.DiscoverPager;
import com.xsy.superassociation.fragment.base.FriendsPager;
import com.xsy.superassociation.fragment.base.SheTuanPager;
import com.xsy.superassociation.fragment.base.ToolsPager;
import com.xsy.superassociation.view.NoScrollingViewPager;

public class ContentFragment extends BaseFragment{
	@ViewInject(R.id.tag_radiogroup)
	private RadioGroup bottom_tab_bg;
	@ViewInject(R.id.content_viewpage)
	private NoScrollingViewPager content_viewpage;
	private ArrayList<BasePager> list;
	@Override
	public View initView() {
		View view = View.inflate(myActivity, R.layout.content_fregment_layout, null);
		ViewUtils.inject(this, view);
		return view;
	}
	
	@Override
	public void initData() {
		bottom_tab_bg.check(R.id.radio0);
		list = new ArrayList<BasePager>();
		list.add(new SheTuanPager(getActivity()));
		list.add(new FriendsPager(getActivity()));
		list.add(new DiscoverPager(getActivity()));
		list.add(new ToolsPager(getActivity()));
		content_viewpage.setAdapter(new myAdapter());
		bottom_tab_bg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio0:
					content_viewpage.setCurrentItem(0,false);
					break;
				case R.id.radio1:
					content_viewpage.setCurrentItem(1,false);
					break;
				case R.id.radio2:
					content_viewpage.setCurrentItem(2,false);
					break;
				case R.id.radio3:
					content_viewpage.setCurrentItem(3,false);
					break;
				default:
					break;
				}
			}
		});
		content_viewpage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				list.get(position).initData();
			}
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}
			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
		list.get(0).initData();
	}
	class myAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return bottom_tab_bg.getChildCount();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View myView = list.get(position).myView;
			container.addView(myView);
			return myView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
	}
	public SheTuanPager getSheTuanPager(){
		return (SheTuanPager) list.get(0);
	}
	public FriendsPager getFriendsPager(){
		return (FriendsPager) list.get(1);
	}
	public DiscoverPager getDiscoverPager() {
		return (DiscoverPager) list.get(2);
	}
	public ToolsPager getToolsPager() {
		return (ToolsPager) list.get(3);
	}
}
