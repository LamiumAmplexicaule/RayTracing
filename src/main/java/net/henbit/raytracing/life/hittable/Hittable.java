package net.henbit.raytracing.life.hittable;

import net.henbit.raytracing.life.AABB;
import net.henbit.raytracing.life.HitRecord;
import net.henbit.raytracing.life.Ray;
import net.henbit.raytracing.life.Vector3;

public abstract class Hittable
{

    public abstract boolean hit(final Ray ray, double tMin, double tMax, HitRecord hitRecord);

    public abstract boolean boundingBox(double time0, double time1, AABB outputBox);

    public double pdfValue(final Vector3 origin, final Vector3 v)
    {
        return 0.0;
    }

    public Vector3 random(final Vector3 origin)
    {
        return new Vector3(1, 0, 0);
    }

}
