package net.henbit.raytracing.weekend.chapter9;

import net.henbit.raytracing.weekend.Ray;
import net.henbit.raytracing.weekend.Vector3;

public class Camera
{

    private final Vector3 origin;
    private final Vector3 lowerLeftCorner;
    private final Vector3 horizontal;
    private final Vector3 vertical;

    public Camera()
    {
        double aspect_ratio = 16.0 / 9.0;
        double viewportHeight = 2.0;
        double viewportWidth = aspect_ratio * viewportHeight;
        double focalLength = 1.0;

        origin = new Vector3(0, 0, 0);
        horizontal = new Vector3(viewportWidth, 0, 0);
        vertical = new Vector3(0, viewportHeight, 0);
        lowerLeftCorner = origin.subtract(horizontal.divide(2)).subtract(vertical.divide(2)).subtract(new Vector3(0, 0, focalLength));
    }

    public Ray getRay(double u, double v)
    {
        return new Ray(origin, lowerLeftCorner.add(horizontal.multiply(u)).add(vertical.multiply(v)).subtract(origin));
    }

}
