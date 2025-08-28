/*
 * Copyright (C) 2024 Lawnchair
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.launcher3;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.launcher3.model.data.AppInfo;
import com.android.launcher3.model.data.ItemInfo;
import com.android.launcher3.model.data.LauncherAppWidgetInfo;

/**
 * A specialized workspace screen that separates app view area from widget area.
 * The upper area is reserved for displaying launched applications, while the
 * bottom area is exclusively for widgets.
 */
public class WorkspaceScreenWithAppView extends LinearLayout {
    
    private FrameLayout mAppViewContainer;
    private CellLayout mWidgetArea;
    private TextView mAppViewPlaceholder;
    private Launcher mLauncher;
    
    public WorkspaceScreenWithAppView(Context context) {
        this(context, null);
    }
    
    public WorkspaceScreenWithAppView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public WorkspaceScreenWithAppView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    
    private void init(Context context) {
        mLauncher = Launcher.getLauncher(context);
        LayoutInflater.from(context).inflate(R.layout.workspace_with_app_view, this, true);
        
        mAppViewContainer = findViewById(R.id.app_view_container);
        mWidgetArea = findViewById(R.id.widget_area);
        mAppViewPlaceholder = findViewById(R.id.app_view_placeholder);
        
        setupWidgetArea();
    }
    
    private void setupWidgetArea() {
        if (mWidgetArea != null) {
            // Configure the widget area to only accept widgets
            mWidgetArea.setCellLayoutContainer(new WidgetOnlyCellLayoutContainer());
        }
    }
    
    /**
     * Launch an application in the app view area
     */
    public void launchAppInView(AppInfo appInfo) {
        if (mAppViewContainer == null) return;
        
        try {
            // Hide placeholder
            mAppViewPlaceholder.setVisibility(View.GONE);
            
            // Clear any existing app view
            mAppViewContainer.removeAllViews();
            
            // Create intent to launch the app
            Intent intent = appInfo.intent;
            if (intent != null) {
                // For now, we'll just show a simple text view with the app name
                // In a full implementation, this would involve activity embedding
                TextView appView = new TextView(getContext());
                appView.setText("Running: " + appInfo.title);
                appView.setTextSize(18);
                appView.setGravity(android.view.Gravity.CENTER);
                appView.setBackground(getContext().getDrawable(android.R.color.white));
                appView.setPadding(16, 16, 16, 16);
                
                mAppViewContainer.addView(appView);
                
                // TODO: Implement actual app activity embedding here
                // This would require using ActivityView or similar mechanism
            }
        } catch (Exception e) {
            // Handle any launch errors
            showAppViewPlaceholder();
        }
    }
    
    /**
     * Clear the app view and show placeholder
     */
    public void clearAppView() {
        if (mAppViewContainer != null) {
            mAppViewContainer.removeAllViews();
            showAppViewPlaceholder();
        }
    }
    
    private void showAppViewPlaceholder() {
        if (mAppViewPlaceholder != null) {
            mAppViewPlaceholder.setVisibility(View.VISIBLE);
        }
    }
    
    /**
     * Get the widget area CellLayout
     */
    public CellLayout getWidgetArea() {
        return mWidgetArea;
    }
    
    /**
     * Get the app view container
     */
    public FrameLayout getAppViewContainer() {
        return mAppViewContainer;
    }
    
    /**
     * Custom CellLayoutContainer that only accepts widgets
     */
    private class WidgetOnlyCellLayoutContainer implements CellLayoutContainer {
        
        @Override
        public boolean acceptsWidget() {
            return true; // Only widgets are allowed
        }
        
        @Override
        public int getCellLayoutId(CellLayout cellLayout) {
            return 0; // Widget area screen ID
        }
        
        @Override
        public CellLayout getCellLayoutForScreenId(int screenId) {
            return mWidgetArea;
        }
        
        @Override
        public void beginDragShared(View child, DragSource source, DragOptions options) {
            // Only allow dragging widgets
            if (child.getTag() instanceof LauncherAppWidgetInfo) {
                // Delegate to workspace
                if (mLauncher.getWorkspace() != null) {
                    mLauncher.getWorkspace().beginDragShared(child, source, options);
                }
            }
        }
    }
}