package net.henbit.raytracing.life.material;

import net.henbit.raytracing.life.*;
import net.henbit.raytracing.life.pdf.CosinePDF;
import net.henbit.raytracing.life.texture.SolidColor;
import net.henbit.raytracing.life.texture.Texture;

import static net.henbit.raytracing.life.RTLife.PI;
import static net.henbit.raytracing.life.Vector3.*;

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
    public boolean scatter(final Ray ray, final HitRecord hitRecord, ScatterRecord scatterRecord)
    {
        scatterRecord.isSpecular = false;
        scatterRecord.attenuation = albedo.value(hitRecord.u, hitRecord.v, hitRecord.point);
        scatterRecord.pdf = new CosinePDF(hitRecord.normal);
        return true;
    }

    @Override
    public boolean scatter(final Ray ray, final HitRecord hitRecord, Vector3 albedo, Ray scattered, DoubleWrapper pdf)
    {
        Vector3 scatterDirection = hitRecord.normal.add(randomUnitVector());

        if (scatterDirection.nearZero())
            scatterDirection = hitRecord.normal;

        scattered.copy(new Ray(hitRecord.point, scatterDirection, ray.time()));
        albedo.copy(this.albedo.value(hitRecord.u, hitRecord.v, hitRecord.point));
        pdf.copy(dot(hitRecord.normal, scattered.direction()) / PI);
        return true;
    }

    @Override
    public double scatteringPDF(Ray ray, HitRecord hitRecord, Ray scattered)
    {
        double cosine = dot(hitRecord.normal, unitVector(scattered.direction()));
        return cosine < 0 ? 0 : cosine / PI;
    }
}
