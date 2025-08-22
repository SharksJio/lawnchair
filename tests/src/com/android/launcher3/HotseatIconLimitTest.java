/*
 * Copyright (C) 2023 The Android Open Source Project
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

import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.filters.SmallTest;

import org.junit.Test;

import java.util.List;

/**
 * Tests to ensure that hotseat can have up to 6 icons maximum.
 */
@SmallTest
public class HotseatIconLimitTest {

    @Test
    public void testHotseatIconLimit_maxSixIcons() {
        Context context = ApplicationProvider.getApplicationContext();
        List<InvariantDeviceProfile.GridOption> gridOptions = 
                InvariantDeviceProfile.parseAllGridOptions(context);

        // Ensure we have some grid options to test
        assertTrue("No grid options found", !gridOptions.isEmpty());

        for (InvariantDeviceProfile.GridOption option : gridOptions) {
            // Verify that numDatabaseHotseatIcons does not exceed 6
            assertTrue("Grid " + option.name + " has numDatabaseHotseatIcons=" + 
                    option.numDatabaseHotseatIcons + " which exceeds the maximum of 6",
                    option.numDatabaseHotseatIcons <= 6);
            
            // Verify that numShownHotseatIcons does not exceed 6
            assertTrue("Grid " + option.name + " has numShownHotseatIcons=" + 
                    option.numShownHotseatIcons + " which exceeds the maximum of 6",
                    option.numShownHotseatIcons <= 6);
        }
    }
}