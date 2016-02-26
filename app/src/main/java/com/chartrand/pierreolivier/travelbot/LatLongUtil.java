package com.chartrand.pierreolivier.travelbot;

import android.graphics.Point;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Pierre-Olivier on 2016-01-26.
 */
public final class LatLongUtil {
    private LatLongUtil()
    {

    }

    /**
     * the length of one degree of latitude (and one degree of longitude at equator) in meters.
     */
    private static final int DEGREE_DISTANCE_AT_EQUATOR = 111329;
    /**
     * the radius of the earth in meters.
     */
    private static final double EARTH_RADIUS = 6378137; //meters
    /**
     * the length of one minute of latitude in meters, i.e. one nautical mile in meters.
     */
    private static final double MINUTES_TO_METERS = 1852d;
    /**
     * the amount of minutes in one degree.
     */
    private static final double DEGREE_TO_MINUTES = 60d;

    /**
     * This method extrapolates the endpoint of a movement with a given length from a given starting point using a given
     * course.
     *
     * @param startPointLat the latitude of the starting point in degrees, must not be {@link Double#NaN}.
     * @param startPointLon the longitude of the starting point in degrees, must not be {@link Double#NaN}.
     * @param course        the course to be used for extrapolation in degrees, must not be {@link Double#NaN}.
     * @param distance      the distance to be extrapolated in meters, must not be {@link Double#NaN}.
     *
     * @return the extrapolated point.
     */
    public static LatLng extrapolate(final double startPointLat, final double startPointLon, final double course,
                                    final double distance) {
        //
        //lat =asin(sin(lat1)*cos(d)+cos(lat1)*sin(d)*cos(tc))
        //dlon=atan2(sin(tc)*sin(d)*cos(lat1),cos(d)-sin(lat1)*sin(lat))
        //lon=mod( lon1+dlon +pi,2*pi )-pi
        //
        // where:
        // lat1,lon1  -start pointi n radians
        // d          - distance in radians Deg2Rad(nm/60)
        // tc         - course in radians

        final double crs = Math.toRadians(course);
        final double d12 = Math.toRadians(distance / MINUTES_TO_METERS / DEGREE_TO_MINUTES);

        final double lat1 = Math.toRadians(startPointLat);
        final double lon1 = Math.toRadians(startPointLon);

        final double lat = Math.asin(Math.sin(lat1) * Math.cos(d12)
                + Math.cos(lat1) * Math.sin(d12) * Math.cos(crs));
        final double dlon = Math.atan2(Math.sin(crs) * Math.sin(d12) * Math.cos(lat1),
                Math.cos(d12) - Math.sin(lat1) * Math.sin(lat));
        final double lon = (lon1 + dlon + Math.PI) % (2 * Math.PI) - Math.PI;

        return new LatLng(Math.toDegrees(lat), Math.toDegrees(lon));
    }
}
