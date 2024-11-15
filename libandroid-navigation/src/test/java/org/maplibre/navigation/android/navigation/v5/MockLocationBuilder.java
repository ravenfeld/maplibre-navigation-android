package org.maplibre.navigation.android.navigation.v5;

import static org.maplibre.navigation.android.navigation.v5.utils.Constants.PRECISION_6;

import android.location.Location;
import androidx.annotation.NonNull;

import org.maplibre.navigation.android.navigation.v5.models.LegStep;
import org.maplibre.geojson.LineString;
import org.maplibre.geojson.Point;
import org.maplibre.navigation.android.navigation.v5.routeprogress.RouteProgress;
import org.maplibre.turf.TurfConstants;
import org.maplibre.turf.TurfMeasurement;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MockLocationBuilder {

  Location buildDefaultMockLocationUpdate(double lng, double lat) {
    return buildMockLocationUpdate(lng, lat, 30f, 10f, System.currentTimeMillis());
  }

  @NonNull
  Point buildPointAwayFromLocation(Location location, double distanceAway) {
    Point fromLocation = Point.fromLngLat(
      location.getLongitude(), location.getLatitude());
    return TurfMeasurement.destination(fromLocation, distanceAway, 90, TurfConstants.UNIT_METERS);
  }

  @NonNull
  Point buildPointAwayFromPoint(Point point, double distanceAway, double bearing) {
    return TurfMeasurement.destination(point, distanceAway, bearing, TurfConstants.UNIT_METERS);
  }

  @NonNull
  List<Point> createCoordinatesFromCurrentStep(RouteProgress progress) {
    LegStep currentStep = progress.currentLegProgress().currentStep();
    LineString lineString = LineString.fromPolyline(currentStep.geometry(), PRECISION_6);
    return lineString.coordinates();
  }

  private Location buildMockLocationUpdate(double lng, double lat, float speed, float horizontalAccuracy, long time) {
    Location location = mock(Location.class);
    when(location.getLongitude()).thenReturn(lng);
    when(location.getLatitude()).thenReturn(lat);
    when(location.getSpeed()).thenReturn(speed);
    when(location.getAccuracy()).thenReturn(horizontalAccuracy);
    when(location.getTime()).thenReturn(time);
    return location;
  }
}
