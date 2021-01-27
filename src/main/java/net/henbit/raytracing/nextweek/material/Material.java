package net.henbit.raytracing.nextweek.material;

import net.henbit.raytracing.nextweek.HitRecord;
import net.henbit.raytracing.nextweek.Ray;
import net.henbit.raytracing.nextweek.Vector3;

public abstract class Material
{

    public abstract boolean scatter(final Ray ray, final HitRecord hitRecord, Vector3 attenuation, Ray scattered);

    public Vector3 emitted(double u, double v, final Vector3 point)
    {
        return new Vector3(0, 0, 0);
    }

}
