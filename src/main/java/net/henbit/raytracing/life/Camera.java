package net.henbit.raytracing.life;

import static java.lang.Math.tan;
import static net.henbit.raytracing.life.RTLife.degreesToRadians;
import static net.henbit.raytracing.life.RTLife.randomDouble;
import static net.henbit.raytracing.life.Vector3.*;

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

    private final double time0;
    private final double time1;

    public Camera(final Vector3 lookFrom, final Vector3 lookAt, final Vector3 up, final double vfov,
                  final double aspect_ratio, double aperture, final double focusDist)
    {
        double theta = degreesToRadians(vfov);
        double h = tan(theta / 2);
        double viewportHeight = 2.0 * h;
        double viewportWidth = aspect_ratio * viewportHeight;

        this.w = unitVector(lookFrom.subtract(lookAt));
        this.u = unitVector(cross(up, w));
        this.v = cross(w, u);

        this.origin = lookFrom;
        this.horizontal = u.multiply(viewportWidth * focusDist);
        this.vertical = v.multiply(viewportHeight * focusDist);
        this.lowerLeftCorner = origin.subtract(horizontal.divide(2)).subtract(vertical.divide(2)).subtract(w.multiply(focusDist));
        this.lensRadius = aperture / 2;
        this.time0 = 0.0;
        this.time1 = 0.0;
    }

    public Camera(final Vector3 lookFrom, final Vector3 lookAt, final Vector3 up, final double vfov,
                  final double aspect_ratio, double aperture, final double focusDist, final double open,
                  final double close)
    {
        double theta = degreesToRadians(vfov);
        double h = tan(theta / 2);
        double viewportHeight = 2.0 * h;
        double viewportWidth = aspect_ratio * viewportHeight;

        this.w = unitVector(lookFrom.subtract(lookAt));
        this.u = unitVector(cross(up, w));
        this.v = cross(w, u);

        this.origin = lookFrom;
        this.horizontal = u.multiply(viewportWidth * focusDist);
        this.vertical = v.multiply(viewportHeight * focusDist);
        this.lowerLeftCorner = origin.subtract(horizontal.divide(2)).subtract(vertical.divide(2)).subtract(w.multiply(focusDist));
        this.lensRadius = aperture / 2;
        this.time0 = open;
        this.time1 = close;
    }

    public Ray getRay(double s, double t)
    {
        Vector3 rd = randomInUnitDisk().multiply(lensRadius);
        Vector3 offset = u.multiply(rd.x()).add(v.multiply(rd.y()));

        return new Ray(origin.add(offset), lowerLeftCorner.add(horizontal.multiply(s))
                .add(vertical.multiply(t)).subtract(origin).subtract(offset), randomDouble(time0, time1));
    }

}
