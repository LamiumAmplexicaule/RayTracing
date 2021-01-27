package net.henbit.raytracing.nextweek.hittable;

import net.henbit.raytracing.nextweek.AABB;
import net.henbit.raytracing.nextweek.HitRecord;
import net.henbit.raytracing.nextweek.Ray;
import net.henbit.raytracing.nextweek.Vector3;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static net.henbit.raytracing.nextweek.RTNextWeek.INFINITY;
import static net.henbit.raytracing.nextweek.RTNextWeek.degreesToRadians;

public class RotateY extends Hittable
{

    private final Hittable object;
    private final double sinTheta;
    private final double cosTheta;
    private final boolean hasBox;
    private final AABB bbox = new AABB();

    public RotateY(final Hittable object, final double angle)
    {
        this.object = object;
        double radians = degreesToRadians(angle);
        this.sinTheta = sin(radians);
        this.cosTheta = cos(radians);
        this.hasBox = object.boundingBox(0, 1, bbox);

        Vector3 min = new Vector3(INFINITY, INFINITY, INFINITY);
        Vector3 max = new Vector3(-INFINITY, -INFINITY, -INFINITY);

        for (int i = 0; i < 2; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                for (int k = 0; k < 2; k++)
                {
                    double x = i * bbox.max().x() + (1 - i) * bbox.min().x();
                    double y = j * bbox.max().y() + (1 - j) * bbox.min().y();
                    double z = k * bbox.max().z() + (1 - k) * bbox.min().z();

                    double newx = cosTheta * x + sinTheta * z;
                    double newz = -sinTheta * x + cosTheta * z;

                    Vector3 tester = new Vector3(newx, y, newz);

                    for (int c = 0; c < 3; c++)
                    {
                        min.e[c] = Math.min(min.get(c), tester.get(c));
                        max.e[c] = Math.max(max.get(c), tester.get(c));
                    }
                }
            }
        }

        bbox.copy(new AABB(min, max));
    }

    @Override
    public boolean hit(final Ray ray, final double tMin, final double tMax, final HitRecord hitRecord)
    {
        Vector3 origin = new Vector3();
        Vector3 direction = new Vector3();
        origin.copy(ray.origin());
        direction.copy(ray.direction());

        origin.e[0] = cosTheta * ray.origin().get(0) - sinTheta * ray.origin().get(2);
        origin.e[2] = sinTheta * ray.origin().get(0) + cosTheta * ray.origin().get(2);

        direction.e[0] = cosTheta * ray.direction().get(0) - sinTheta * ray.direction().get(2);
        direction.e[2] = sinTheta * ray.direction().get(0) + cosTheta * ray.direction().get(2);

        Ray rotatedR = new Ray(origin, direction, ray.time());

        if (!object.hit(rotatedR, tMin, tMax, hitRecord))
            return false;

        Vector3 point = new Vector3();
        Vector3 normal = new Vector3();
        point.copy(hitRecord.point);
        normal.copy(hitRecord.normal);

        point.e[0] = cosTheta * hitRecord.point.get(0) + sinTheta * hitRecord.point.get(2);
        point.e[2] = -sinTheta * hitRecord.point.get(0) + cosTheta * hitRecord.point.get(2);

        normal.e[0] = cosTheta * hitRecord.normal.get(0) + sinTheta * hitRecord.normal.get(2);
        normal.e[2] = -sinTheta * hitRecord.normal.get(0) + cosTheta * hitRecord.normal.get(2);

        hitRecord.point = point;
        hitRecord.setFaceNormal(rotatedR, normal);

        return true;
    }

    @Override
    public boolean boundingBox(double time0, double time1, AABB outputBox)
    {
        outputBox.copy(bbox);
        return hasBox;
    }
}
