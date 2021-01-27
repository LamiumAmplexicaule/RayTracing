package net.henbit.raytracing.nextweek.material;

import net.henbit.raytracing.nextweek.*;
import net.henbit.raytracing.nextweek.texture.SolidColor;
import net.henbit.raytracing.nextweek.texture.Texture;

import static net.henbit.raytracing.nextweek.Vector3.randomUnitVector;

public class Lambertian extends Material
{

    private final Texture albedo;

    public Lambertian(final Vector3 albedo)
    {
        this.albedo = new SolidColor(albedo);
    }

    public Lambertian(final Texture texture)
    {
        this.albedo = texture;
    }

    @Override
    public boolean scatter(final Ray ray, final HitRecord hitRecord, Vector3 attenuation, Ray scattered)
    {
        Vector3 scatterDirection = hitRecord.normal.add(randomUnitVector());

        if (scatterDirection.nearZero())
            scatterDirection = hitRecord.normal;

        scattered.copy(new Ray(hitRecord.point, scatterDirection, ray.time()));
        attenuation.copy(albedo.value(hitRecord.u, hitRecord.v, hitRecord.point));
        return true;
    }

}
