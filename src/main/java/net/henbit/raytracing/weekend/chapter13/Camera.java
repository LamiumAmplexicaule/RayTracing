package net.henbit.raytracing.weekend.chapter13;

import net.henbit.raytracing.weekend.Ray;
import net.henbit.raytracing.weekend.Vector3;

import static java.lang.Math.tan;
import static net.henbit.raytracing.weekend.RTWeekend.degreesToRadians;
import static net.henbit.raytracing.weekend.Vector3.*;

public class Camera
{

    private final Vector3 origin;
    private final Vector3 lowerLeftCorner;
    private final Vector3 horizontal;
    private final Vector3 vertical;
    private final Vector3 u;
    private final Vector3 v;
    private final Vector3 w;
    private final double lensRadius;

    public Camera(Vector3 lookFrom, Vector3 lookAt, Vector3 up, double vfov, double aspect_ratio, double aperture,
                  double focusDist)
    {
        double theta = degreesToRadians(vfov);
        double h = tan(theta / 2);
        double viewportHeight = 2.0 * h;
        double viewportWidth = aspect_ratio * viewportHeight;

        w = unitVector(lookFrom.subtract(lookAt));
        u = unitVector(cross(up, w));
        v = cross(w, u);

        origin = lookFrom;
        horizontal = u.multiply(viewportWidth * focusDist);
        vertical = v.multiply(viewportHeight * focusDist);
        lowerLeftCorner = origin.subtract(horizontal.divide(2)).subtract(vertical.divide(2)).subtract(w.multiply(focusDist));
        lensRadius = aperture / 2;
    }

    public Ray getRay(double s, double t)
    {
        Vector3 rd = randomInUnitDisk().multiply(lensRadius);
        Vector3 offset = u.multiply(rd.x()).add(v.multiply(rd.y()));

        return new Ray(origin.add(offset), lowerLeftCorner.add(horizontal.multiply(s))
                .add(vertical.multiply(t)).subtract(origin).subtract(offset));
    }

}
