package org.maplibre.navigation.android.navigation.v5.navigation;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.maplibre.navigation.android.navigation.v5.models.DirectionsRoute;
import org.maplibre.navigation.android.navigation.v5.milestone.Milestone;
import org.maplibre.navigation.android.navigation.v5.milestone.MilestoneEventListener;
import org.maplibre.navigation.android.navigation.v5.offroute.OffRouteListener;
import org.maplibre.navigation.android.navigation.v5.route.FasterRouteListener;
import org.maplibre.navigation.android.navigation.v5.routeprogress.ProgressChangeListener;
import org.maplibre.navigation.android.navigation.v5.routeprogress.RouteProgress;
import org.maplibre.navigation.android.navigation.v5.utils.RouteUtils;

import java.util.concurrent.CopyOnWriteArrayList;

import timber.log.Timber;

class NavigationEventDispatcher {

  private CopyOnWriteArrayList<NavigationEventListener> navigationEventListeners;
  private CopyOnWriteArrayList<MilestoneEventListener> milestoneEventListeners;
  private CopyOnWriteArrayList<ProgressChangeListener> progressChangeListeners;
  private CopyOnWriteArrayList<OffRouteListener> offRouteListeners;
  private CopyOnWriteArrayList<FasterRouteListener> fasterRouteListeners;
  private RouteUtils routeUtils;

  NavigationEventDispatcher() {
    this(new RouteUtils());
  }

  NavigationEventDispatcher(RouteUtils routeUtils) {
    navigationEventListeners = new CopyOnWriteArrayList<>();
    milestoneEventListeners = new CopyOnWriteArrayList<>();
    progressChangeListeners = new CopyOnWriteArrayList<>();
    offRouteListeners = new CopyOnWriteArrayList<>();
    fasterRouteListeners = new CopyOnWriteArrayList<>();
    this.routeUtils = routeUtils;
  }

  void addMilestoneEventListener(@NonNull MilestoneEventListener milestoneEventListener) {
    if (milestoneEventListeners.contains(milestoneEventListener)) {
      Timber.w("The specified MilestoneEventListener has already been added to the stack.");
      return;
    }
    milestoneEventListeners.add(milestoneEventListener);
  }

  void removeMilestoneEventListener(@Nullable MilestoneEventListener milestoneEventListener) {
    if (milestoneEventListener == null) {
      milestoneEventListeners.clear();
    } else if (!milestoneEventListeners.contains(milestoneEventListener)) {
      Timber.w("The specified MilestoneEventListener isn't found in stack, therefore, cannot be removed.");
    } else {
      milestoneEventListeners.remove(milestoneEventListener);
    }
  }

  void addProgressChangeListener(@NonNull ProgressChangeListener progressChangeListener) {
    if (progressChangeListeners.contains(progressChangeListener)) {
      Timber.w("The specified ProgressChangeListener has already been added to the stack.");
      return;
    }
    progressChangeListeners.add(progressChangeListener);
  }

  void removeProgressChangeListener(@Nullable ProgressChangeListener progressChangeListener) {
    if (progressChangeListener == null) {
      progressChangeListeners.clear();
    } else if (!progressChangeListeners.contains(progressChangeListener)) {
      Timber.w("The specified ProgressChangeListener isn't found in stack, therefore, cannot be removed.");
    } else {
      progressChangeListeners.remove(progressChangeListener);
    }
  }

  void addOffRouteListener(@NonNull OffRouteListener offRouteListener) {
    if (offRouteListeners.contains(offRouteListener)) {
      Timber.w("The specified OffRouteListener has already been added to the stack.");
      return;
    }
    offRouteListeners.add(offRouteListener);
  }

  void removeOffRouteListener(@Nullable OffRouteListener offRouteListener) {
    if (offRouteListener == null) {
      offRouteListeners.clear();
    } else if (!offRouteListeners.contains(offRouteListener)) {
      Timber.w("The specified OffRouteListener isn't found in stack, therefore, cannot be removed.");
    } else {
      offRouteListeners.remove(offRouteListener);
    }
  }

  void addNavigationEventListener(@NonNull NavigationEventListener navigationEventListener) {
    if (navigationEventListeners.contains(navigationEventListener)) {
      Timber.w("The specified NavigationEventListener has already been added to the stack.");
      return;
    }
    this.navigationEventListeners.add(navigationEventListener);
  }

  void removeNavigationEventListener(@Nullable NavigationEventListener navigationEventListener) {
    if (navigationEventListener == null) {
      navigationEventListeners.clear();
    } else if (!navigationEventListeners.contains(navigationEventListener)) {
      Timber.w("The specified NavigationEventListener isn't found in stack, therefore, cannot be removed.");
    } else {
      navigationEventListeners.remove(navigationEventListener);
    }
  }

  void addFasterRouteListener(@NonNull FasterRouteListener fasterRouteListener) {
    if (fasterRouteListeners.contains(fasterRouteListener)) {
      Timber.w("The specified FasterRouteListener has already been added to the stack.");
      return;
    }
    fasterRouteListeners.add(fasterRouteListener);
  }

  void removeFasterRouteListener(@Nullable FasterRouteListener fasterRouteListener) {
    if (fasterRouteListener == null) {
      fasterRouteListeners.clear();
    } else if (!fasterRouteListeners.contains(fasterRouteListener)) {
      Timber.w("The specified FasterRouteListener isn't found in stack, therefore, cannot be removed.");
    } else {
      fasterRouteListeners.remove(fasterRouteListener);
    }
  }

  void onMilestoneEvent(RouteProgress routeProgress, String instruction, Milestone milestone) {
    for (MilestoneEventListener milestoneEventListener : milestoneEventListeners) {
      milestoneEventListener.onMilestoneEvent(routeProgress, instruction, milestone);
    }
  }

  void onProgressChange(Location location, RouteProgress routeProgress) {
    for (ProgressChangeListener progressChangeListener : progressChangeListeners) {
      progressChangeListener.onProgressChange(location, routeProgress);
    }
  }

  void onUserOffRoute(Location location) {
    for (OffRouteListener offRouteListener : offRouteListeners) {
      offRouteListener.userOffRoute(location);
    }
  }

  void onNavigationEvent(boolean isRunning) {
    for (NavigationEventListener navigationEventListener : navigationEventListeners) {
      navigationEventListener.onRunning(isRunning);
    }
  }

  void onFasterRouteEvent(DirectionsRoute directionsRoute) {
    for (FasterRouteListener fasterRouteListener : fasterRouteListeners) {
      fasterRouteListener.fasterRouteFound(directionsRoute);
    }
  }
}
