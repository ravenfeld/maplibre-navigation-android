package org.maplibre.navigation.android.navigation.v5.utils;

import org.maplibre.navigation.android.navigation.v5.models.LegStep;
import org.maplibre.geojson.Feature;
import org.maplibre.geojson.LineString;
import org.maplibre.geojson.Point;
import org.maplibre.turf.TurfMeasurement;
import org.maplibre.turf.TurfMisc;

import static org.maplibre.turf.TurfConstants.UNIT_METERS;

public final class MeasurementUtils {

  private MeasurementUtils() {
    throw new AssertionError("No Instance.");
  }

  /**
   * Calculates the distance between the users current raw {@link android.location.Location} object
   * to the closest {@link Point} in the {@link LegStep}.
   *
   * @param usersRawLocation {@link Point} the raw location where the user is currently located
   * @param step             {@link LegStep} to calculate the closest point on the step to our
   *                         predicted location
   * @return double in distance meters
   * @since 0.2.0
   */
  public static double userTrueDistanceFromStep(Point usersRawLocation, LegStep step) {
    // Check that the leg step contains geometry.
    if (TextUtils.isEmpty(step.geometry())) {
      return 0;
    }

    // Get the lineString from the step geometry.
    LineString lineString = LineString.fromPolyline(step.geometry(), Constants.PRECISION_6);

    // Make sure that the step coordinates isn't less than size 2. If the points equal each other,
    // the distance is obviously zero, so return 0 to avoid executing additional unnecessary code.
    if (lineString.coordinates().isEmpty()
      || usersRawLocation.equals(lineString.coordinates().get(0))) {
      return 0;
    }
    if (lineString.coordinates().size() == 1) {
      return TurfMeasurement.distance(usersRawLocation, lineString.coordinates().get(0),
        UNIT_METERS);
    }

    Feature feature = TurfMisc.nearestPointOnLine(usersRawLocation, lineString.coordinates());
    Point snappedPoint = (Point) feature.geometry();

    if (snappedPoint == null) {
      return 0;
    }
    if (Double.isInfinite(snappedPoint.latitude())
      || Double.isInfinite(snappedPoint.longitude())) {
      return TurfMeasurement.distance(usersRawLocation,
        lineString.coordinates().get(0), UNIT_METERS);
    }

    double distance = TurfMeasurement.distance(usersRawLocation, snappedPoint, UNIT_METERS);
    return Double.isNaN(distance) ? 0d : distance;
  }
}
