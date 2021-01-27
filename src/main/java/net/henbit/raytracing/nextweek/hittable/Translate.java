package net.henbit.raytracing.nextweek.hittable;

import net.henbit.raytracing.nextweek.AABB;
import net.henbit.raytracing.nextweek.HitRecord;
import net.henbit.raytracing.nextweek.Ray;
import net.henbit.raytracing.nextweek.Vector3;

public class Translate extends Hittable
{

    private final Hittable object;
    private final Vector3 offset;

    public Translate(final Hittable object, final Vector3 displacement)
    {
        this.object = object;
        this.offset = displacement;
    }

    @Override
    public boolean hit(final Ray ray, final double tMin, final double tMax, final HitRecord hitRecord)
    {
        Ray movedRay = new Ray(ray.origin().subtract(offset), ray.direction(), ray.time());
        if (!object.hit(movedRay, tMin, tMax, hitRecord))
            return false;

        hitRecord.point = hitRecord.point.add(offset);
        hitRecord.setFaceNormal(movedRay, hitRecord.normal);

        return true;
    }

    @Override
    public boolean boundingBox(final double time0, final double time1, final AABB outputBox)
    {
        if (!object.boundingBox(time0, time1, outputBox))
            return false;

        outputBox.copy(new AABB(
                outputBox.min().add(offset),
                outputBox.max().add(offset)));
        return true;
    }
}
