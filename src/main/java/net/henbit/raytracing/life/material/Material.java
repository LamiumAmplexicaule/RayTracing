package net.henbit.raytracing.life.material;

import net.henbit.raytracing.life.*;

public abstract class Material
{

    public boolean scatter(final Ray ray, final HitRecord hitRecord, ScatterRecord scatterRecord)
    {
        return false;
    }

    public boolean scatter(final Ray ray, final HitRecord hitRecord, Vector3 albedo, Ray scattered)
    {
        return false;
    }

    public boolean scatter(final Ray ray, final HitRecord hitRecord, Vector3 albedo, Ray scattered, DoubleWrapper pdf)
    {
        return false;
    }

    public double scatteringPDF(final Ray ray, final HitRecord hitRecord, Ray scattered)
    {
        return 0;
    }

    public Vector3 emitted(double u, double v, final Vector3 point)
    {
        return new Vector3(0, 0, 0);
    }

    public Vector3 emitted(final Ray ray, final HitRecord hitRecord, double u, double v, Vector3 point)
    {
        return new Vector3(0, 0, 0);
    }

}
