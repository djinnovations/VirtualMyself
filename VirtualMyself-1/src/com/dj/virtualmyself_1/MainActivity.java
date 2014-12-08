package com.dj.virtualmyself_1;

import java.util.ArrayList;

import com.dj.fragments.AlterFragment;
import com.dj.fragments.ExtraFragment;
import com.dj.fragments.HomeFragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	
	
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTitle = mDrawerTitle = getTitle();
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navDrawerItems = new ArrayList<NavDrawerItem>();
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        
        navMenuIcons.recycle();
        
        adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
        mDrawerList.setAdapter(adapter);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.drawable.ic_drawer,R.string.app_name,R.string.app_name){
        	
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }
 
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        
        if (savedInstanceState == null) {
            displayView(0);
        }
        
       
 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
        case R.id.about_app: Toast.makeText(getBaseContext(), "Under Construction", Toast.LENGTH_LONG).show();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.about_app).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
	

	 @Override
	 public void setTitle(CharSequence title) {
	     mTitle = title;
	     getActionBar().setTitle(mTitle);
	 }
	
	 @Override
	 protected void onPostCreate(Bundle savedInstanceState) {
	     super.onPostCreate(savedInstanceState);
	     mDrawerToggle.syncState();
	 }
	 
	 @Override
	 public void onConfigurationChanged(Configuration newConfig) {
	     super.onConfigurationChanged(newConfig);
	     mDrawerToggle.onConfigurationChanged(newConfig);
	 }
	
	
	 private class SlideMenuClickListener implements ListView.OnItemClickListener {
		 
		 @Override
		 public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			 displayView(position);
		 }
		 
	 }
	 
	 private void displayView(int position) {
	        
	        Fragment fragment = null;
	        switch (position) {
	        case 0:
	            fragment = new HomeFragment();
	           
	            break;
	        case 1:
	            fragment = new AlterFragment();
	            break;
	            
	        case 2:
	            fragment = new ExtraFragment();
	            break;
	       
	 
	        default:
	            break;
	        }
	 
	        if (fragment != null) {
	            FragmentManager fragmentManager = getFragmentManager();
	            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
	 
	            
	            mDrawerList.setItemChecked(position, true);
	            mDrawerList.setSelection(position);
	            setTitle(navMenuTitles[position]);
	            mDrawerLayout.closeDrawer(mDrawerList);
	        }
	        else {
	            
	        }
	    }
	 
	
	
	
	
	

}
