package com.mobilitychina.common.component.tab;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

import com.mobilitychina.hr.R;
import com.mobilitychina.hr.app.BaseActivity;


/**
 * This demonstrates how you can implement switching between the tabs of a
 * TabHost through fragments. It uses a trick (see the code below) to allow the
 * tabs to switch between fragments instead of simple views.
 */
public class TabViewActivity extends BaseActivity {

	protected TabHost mTabHost;
	protected TabManager mTabManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setOnContentView();
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);

		setTabWidgetBackground(0);
	
	}

	protected void setOnContentView() {
		setContentView(R.layout.fragment_tabs_bottom);
	}

	public void addTab(String title, Class<?> clss, Bundle args) {
		addTab(title, 0, clss, args);
	}

	public void addTab(String title, int indicatorView, Class<?> clss,
			Bundle args) {
		if (title == null) {
			throw new IllegalArgumentException("title cann't be null!");
		}

		mTabManager.addTab(
				mTabHost.newTabSpec(title).setIndicator(
						new LabelIndicatorStrategy(this, title, indicatorView)
								.createIndicatorView(mTabHost)), clss, args);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
	}

	/**
	 * This is a helper class that implements a generic mechanism for
	 * associating fragments with the tabs in a tab host. It relies on a trick.
	 * Normally a tab host has a simple API for supplying a View or Intent that
	 * each tab will show. This is not sufficient for switching between
	 * fragments. So instead we make the content part of the tab host 0dp high
	 * (it is not shown) and the TabManager supplies its own dummy view to show
	 * as the tab content. It listens to changes in tabs, and takes care of
	 * switch to the correct fragment shown in a separate content area whenever
	 * the selected tab changes.
	 */
	public static class TabManager implements TabHost.OnTabChangeListener {
		private final TabViewActivity mActivity;
		private final TabHost mTabHost;
		private final int mContainerId;
		private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
		TabInfo mLastTab;

		static final class TabInfo {
			private final String tag;
			private final Class<?> clss;
			private final Bundle args;
			private Fragment fragment;

			TabInfo(String _tag, Class<?> _class, Bundle _args) {
				tag = _tag;
				clss = _class;
				args = _args;
			}
		}

		static class DummyTabFactory implements TabHost.TabContentFactory {
			private final Context mContext;

			public DummyTabFactory(Context context) {
				mContext = context;
			}

			@Override
			public View createTabContent(String tag) {
				View v = new View(mContext);
				v.setMinimumWidth(0);
				v.setMinimumHeight(0);
				return v;
			}
		}

		public TabManager(TabViewActivity activity, TabHost tabHost,
				int containerId) {
			mActivity = activity;
			mTabHost = tabHost;
			mContainerId = containerId;
			mTabHost.setOnTabChangedListener(this);
		}

		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
			tabSpec.setContent(new DummyTabFactory(mActivity));
			String tag = tabSpec.getTag();

			TabInfo info = new TabInfo(tag, clss, args);

			// Check to see if we already have a fragment for this tab, probably
			// from a previously saved state. If so, deactivate it, because our
			// initial state is that a tab isn't shown.
			info.fragment = mActivity.getFragmentManager()
					.findFragmentByTag(tag);
			if (info.fragment != null && !info.fragment.isHidden()) {
				FragmentTransaction ft = mActivity.getFragmentManager()
						.beginTransaction();
				ft.hide(info.fragment);
				ft.commit();
			}

			mTabs.put(tag, info);
			mTabHost.addTab(tabSpec);
		}

		@Override
		public void onTabChanged(String tabId) {
//			String	tabId = tabIds ;
//			if(tabIds.equals("安装设备")){
//				tabId = "点位登记";
//				Geocoding.getInstance().setPage(true);
//			}else{
//				Geocoding.getInstance().setPage(false);
//			}
			TabInfo  newTab = mTabs.get(tabId);
			if (mLastTab != newTab) {
				FragmentTransaction ft = mActivity.getFragmentManager()
						.beginTransaction();
				if (mLastTab != null) {
					if (mLastTab.fragment != null) {
						ft.hide(mLastTab.fragment);
//						ft.remove(mLastTab.fragment);
//						ft.add(mLastTab.fragment, mLastTab.tag);
					}
				}
				if (newTab != null) {
					
					if (newTab.fragment == null) {
						newTab.fragment = Fragment.instantiate(mActivity,
								newTab.clss.getName(), newTab.args);
						ft.add(mContainerId, newTab.fragment, newTab.tag);
//						System.out.println( "onTabChanged with tabId:" + tabId
//								+ ", newTab.fragment is null, newTab.tag is "
//								+ newTab.tag);
					} else {
						ft.show(newTab.fragment);
//						System.out.println( "onTabChanged with tabId:" + tabId
//								+ ", show fragment success");
					}
				} else {
					
//					System.out.println( "onTabChanged with tabId:" + tabId
//							+ ", newTab is null");
				}
				mLastTab = newTab;
				ft.commit();
				mActivity.getFragmentManager()
						.executePendingTransactions();
			}
			mActivity.onTabChanged(tabId);
		}
	}

	public void onTabChanged(String tabId) {

	}

	protected void setTabWidgetBackground(int drawableId) {
		if (drawableId > 0) {
			mTabHost.getTabWidget().setBackgroundResource(drawableId);
		}
	}

}
