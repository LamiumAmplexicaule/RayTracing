package net.henbit.raytracing.weekend;

public abstract class Hittable
{

    public abstract boolean hit(final Ray ray, double tMin, double tMax, HitRecord hitRecord);

}
