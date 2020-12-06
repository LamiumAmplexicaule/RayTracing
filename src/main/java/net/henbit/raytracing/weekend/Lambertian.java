package net.henbit.raytracing.weekend;

import static net.henbit.raytracing.weekend.Vector3.randomUnitVector;

public class Lambertian extends Material
{

    private final Vector3 albedo;

    public Lambertian(final Vector3 albedo)
    {
        this.albedo = albedo;
    }

    @Override
    public boolean scatter(final Ray ray, final HitRecord hitRecord, Vector3 attenuation, Ray scattered)
    {
        Vector3 scatterDirection = hitRecord.normal.add(randomUnitVector());

        if (scatterDirection.nearZero())
            scatterDirection = hitRecord.normal;

        scattered.copy(new Ray(hitRecord.point, scatterDirection));
        attenuation.copy(albedo);
        return true;
    }

}
