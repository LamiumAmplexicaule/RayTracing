package net.henbit.raytracing.life.hittable;

import net.henbit.raytracing.life.AABB;
import net.henbit.raytracing.life.HitRecord;
import net.henbit.raytracing.life.Ray;

public class FlipFace extends Hittable
{

    private final Hittable ptr;

    public FlipFace(Hittable ptr)
    {
        this.ptr = ptr;
    }

    @Override
    public boolean hit(Ray ray, double tMin, double tMax, HitRecord hitRecord)
    {
        if (!ptr.hit(ray, tMin, tMax, hitRecord))
            return false;

        hitRecord.frontFace = !hitRecord.frontFace;
        return true;
    }

    @Override
    public boolean boundingBox(double time0, double time1, AABB outputBox)
    {
        return ptr.boundingBox(time0, time1, outputBox);
    }

}
