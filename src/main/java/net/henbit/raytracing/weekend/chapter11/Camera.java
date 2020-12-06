package net.henbit.raytracing.weekend.chapter11;

import net.henbit.raytracing.weekend.Ray;
import net.henbit.raytracing.weekend.Vector3;

import static java.lang.Math.tan;
import static net.henbit.raytracing.weekend.RTWeekend.degreesToRadians;
import static net.henbit.raytracing.weekend.Vector3.cross;
import static net.henbit.raytracing.weekend.Vector3.unitVector;

public class Camera
{

    private final Vector3 origin;
    private final Vector3 lowerLeftCorner;
    private final Vector3 horizontal;
    private final Vector3 vertical;

    public Camera(Vector3 lookFrom, Vector3 lookAt, Vector3 up, double vfov, double aspectRatio)
    {
        double theta = degreesToRadians(vfov);
        double h = tan(theta / 2);
        double viewportHeight = 2.0 * h;
        double viewportWidth = aspectRatio * viewportHeight;

        Vector3 w = unitVector(lookFrom.subtract(lookAt));
        Vector3 u = unitVector(cross(up, w));
        Vector3 v = cross(w, u);

        origin = lookFrom;
        horizontal = u.multiply(viewportWidth);
        vertical = v.multiply(viewportHeight);
        lowerLeftCorner = origin.subtract(horizontal.divide(2)).subtract(vertical.divide(2)).subtract(w);
    }

    public Ray getRay(double s, double t)
    {
        return new Ray(origin, lowerLeftCorner.add(horizontal.multiply(s)).add(vertical.multiply(t)).subtract(origin));
    }

}
