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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.android.launcher3.model.data.AppInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for the WorkspaceScreenWithAppView implementation
 */
@RunWith(AndroidJUnit4.class)
public class WorkspaceScreenWithAppViewTest {

    private Context mContext;
    private WorkspaceScreenWithAppView mWorkspaceScreen;

    @Before
    public void setUp() {
        mContext = InstrumentationRegistry.getContext();
        mWorkspaceScreen = new WorkspaceScreenWithAppView(mContext);
    }

    @Test
    public void testWorkspaceScreenHasAppViewContainer() {
        FrameLayout appViewContainer = mWorkspaceScreen.getAppViewContainer();
        assertNotNull("App view container should not be null", appViewContainer);
    }

    @Test
    public void testWorkspaceScreenHasWidgetArea() {
        CellLayout widgetArea = mWorkspaceScreen.getWidgetArea();
        assertNotNull("Widget area should not be null", widgetArea);
        assertTrue("Widget area should accept widgets", widgetArea.acceptsWidget());
    }

    @Test
    public void testLaunchAppInView() {
        // Create a dummy AppInfo for testing
        AppInfo testApp = new AppInfo();
        testApp.title = "Test App";

        // Launch the app in the view
        mWorkspaceScreen.launchAppInView(testApp);

        // Check that the app view container has content now
        FrameLayout appViewContainer = mWorkspaceScreen.getAppViewContainer();
        assertTrue("App view container should have children after launching app", 
                   appViewContainer.getChildCount() > 0);
    }

    @Test
    public void testClearAppView() {
        // Create a dummy AppInfo and launch it
        AppInfo testApp = new AppInfo();
        testApp.title = "Test App";
        mWorkspaceScreen.launchAppInView(testApp);

        // Clear the app view
        mWorkspaceScreen.clearAppView();

        // Check that the placeholder is visible again
        View placeholder = mWorkspaceScreen.findViewById(R.id.app_view_placeholder);
        assertNotNull("Placeholder should exist", placeholder);
        assertTrue("Placeholder should be visible after clearing app view", 
                   placeholder.getVisibility() == View.VISIBLE);
    }
}