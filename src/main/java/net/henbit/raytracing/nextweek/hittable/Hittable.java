package net.henbit.raytracing.nextweek.hittable;

import net.henbit.raytracing.nextweek.AABB;
import net.henbit.raytracing.nextweek.HitRecord;
import net.henbit.raytracing.nextweek.Ray;

public abstract class Hittable
{

    public abstract boolean hit(final Ray ray, double tMin, double tMax, HitRecord hitRecord);
    public abstract boolean boundingBox(double time0, double time1, AABB outputBox);

}
