# Workspace App View Implementation

This document describes the implementation of the new workspace layout that separates the workspace into two distinct areas:

1. **Upper Area (App View)**: Reserved for displaying launched applications
2. **Bottom Area (Widget Area)**: Reserved exclusively for widgets

## Overview

The workspace now provides a unified view where users can:
- Launch applications that display in the upper area of the workspace
- Place widgets in the bottom area only
- View both running apps and widgets at the same time ("at glance" functionality)

## Architecture

### Key Components

1. **WorkspaceScreenWithAppView**: New layout container that manages the two-area workspace
   - Inherits from LinearLayout with vertical orientation
   - Contains FrameLayout for app view and CellLayout for widgets
   - Handles app launching in the upper area

2. **Modified Workspace**: Updated to use the new layout
   - Creates WorkspaceScreenWithAppView instead of plain CellLayout
   - Provides methods to access the app view functionality
   - Maintains backward compatibility

3. **Updated ItemClickHandler**: Modified to launch apps in workspace instead of separate activities
   - Intercepts AppInfo clicks and launches them in the workspace app view
   - Falls back to original behavior for other item types

### Layout Structure

```
WorkspaceScreenWithAppView (LinearLayout - vertical)
├── FrameLayout (app_view_container) - weight=1
│   ├── TextView (placeholder) - "Tap an app to launch it here"
│   └── [Launched app content goes here]
└── CellLayout (widget_area) - wrap_content, minHeight=120dp
    └── [Widgets only]
```

## Usage

### For App Launches
- Apps clicked from the All Apps screen now launch in the workspace app view area
- The upper area displays the running application
- Only one app can be displayed at a time (current implementation)

### For Widgets
- Widgets can only be placed in the bottom area
- The widget area has a minimum height to ensure visibility
- All existing widget functionality is preserved

## Implementation Details

### Files Modified
- `src/com/android/launcher3/Workspace.java`: Updated screen creation logic
- `src/com/android/launcher3/touch/ItemClickHandler.java`: Modified app launch behavior
- `res/layout/workspace_with_app_view.xml`: New layout file
- `src/com/android/launcher3/WorkspaceScreenWithAppView.java`: New component class

### Key Methods
- `WorkspaceScreenWithAppView.launchAppInView(AppInfo)`: Launch app in workspace
- `Workspace.launchAppInWorkspace(AppInfo)`: Workspace-level app launch method
- `Workspace.getWorkspaceScreenWithAppView()`: Access the new layout

## Future Enhancements

1. **Activity Embedding**: Currently shows a placeholder. Future versions could use Android's Activity Embedding API to actually embed running applications.

2. **Multiple App Support**: Could be extended to support multiple apps using tabs or split view.

3. **Gesture Support**: Could add gestures to switch between apps or close apps.

4. **App State Management**: Could preserve app state when switching between different apps.

## Testing

Tests are included to verify:
- Proper layout creation and structure
- App launch functionality
- Widget area functionality
- Component accessibility

See `tests/src/com/android/launcher3/WorkspaceScreenWithAppViewTest.java` for test cases.