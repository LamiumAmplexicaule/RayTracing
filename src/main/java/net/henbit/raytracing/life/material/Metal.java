package net.henbit.raytracing.life.material;

import net.henbit.raytracing.life.HitRecord;
import net.henbit.raytracing.life.Ray;
import net.henbit.raytracing.life.ScatterRecord;
import net.henbit.raytracing.life.Vector3;

import static net.henbit.raytracing.life.Vector3.*;

public class Metal extends Material
{

    private final Vector3 albedo;
    private final double fuzz;

    public Metal(final Vector3 albedo, double fuzz)
    {
        this.albedo = albedo;
        this.fuzz = fuzz < 1 ? fuzz : 1;
    }

    @Override
    public boolean scatter(final Ray ray, final HitRecord hitRecord, ScatterRecord scatterRecord)
    {
        Vector3 reflected = reflect(unitVector(ray.direction()), hitRecord.normal);
        scatterRecord.specularRay = new Ray(hitRecord.point, reflected.add(randomInUnitSphere().multiply(fuzz)));
        scatterRecord.attenuation = albedo;
        scatterRecord.isSpecular = true;
        scatterRecord.pdf = null;
        return true;
    }

    @Override
    public boolean scatter(final Ray ray, final HitRecord hitRecord, Vector3 attenuation, Ray scattered)
    {
        Vector3 reflected = reflect(unitVector(ray.direction()), hitRecord.normal);
        scattered.copy(new Ray(hitRecord.point, reflected.add(randomInUnitSphere().multiply(fuzz)), ray.time()));
        attenuation.copy(albedo);

        return dot(scattered.direction(), hitRecord.normal) > 0;
    }
}
