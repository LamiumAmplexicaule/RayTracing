package net.henbit.raytracing.life.material;

import net.henbit.raytracing.life.HitRecord;
import net.henbit.raytracing.life.Ray;
import net.henbit.raytracing.life.ScatterRecord;
import net.henbit.raytracing.life.Vector3;

import static java.lang.Math.*;
import static net.henbit.raytracing.life.RTLife.randomDouble;
import static net.henbit.raytracing.life.Vector3.*;

@SuppressWarnings("DuplicatedCode")
public class Dielectric extends Material
{

    private final double indexOfRefraction;

    public Dielectric(double indexOfRefraction)
    {
        this.indexOfRefraction = indexOfRefraction;
    }

    static double reflectance(double cosine, double ref_idx)
    {
        // Use Schlick's approximation for reflectance.
        double r0 = (1 - ref_idx) / (1 + ref_idx);
        r0 = r0 * r0;
        return r0 + (1 - r0) * pow((1 - cosine), 5);
    }

    @Override
    public boolean scatter(final Ray ray, final HitRecord hitRecord, ScatterRecord scatterRecord)
    {
        scatterRecord.isSpecular = true;
        scatterRecord.pdf = null;
        scatterRecord.attenuation = new Vector3(1.0, 1.0, 1.0);
        double refractionRatio = hitRecord.frontFace ? (1.0 / indexOfRefraction) : indexOfRefraction;

        Vector3 unitDirection = unitVector(ray.direction());

        double cosTheta = min(dot(unitDirection.minus(), hitRecord.normal), 1.0);
        double sinTheta = sqrt(1.0 - cosTheta * cosTheta);

        boolean cannotRefract = refractionRatio * sinTheta > 1.0;
        Vector3 direction;

        if (cannotRefract || reflectance(cosTheta, refractionRatio) > randomDouble())
            direction = reflect(unitDirection, hitRecord.normal);
        else
            direction = refract(unitDirection, hitRecord.normal, refractionRatio);
        scatterRecord.specularRay = new Ray(hitRecord.point, direction, ray.time());
        return true;
    }

    @Override
    public boolean scatter(final Ray ray, final HitRecord hitRecord, Vector3 attenuation, Ray scattered)
    {
        attenuation.copy(new Vector3(1.0, 1.0, 1.0));
        double refractionRatio = hitRecord.frontFace ? (1.0 / indexOfRefraction) : indexOfRefraction;

        Vector3 unitDirection = unitVector(ray.direction());

        double cosTheta = min(dot(unitDirection.minus(), hitRecord.normal), 1.0);
        double sinTheta = sqrt(1.0 - cosTheta * cosTheta);

        boolean cannotRefract = refractionRatio * sinTheta > 1.0;
        Vector3 direction;

        if (cannotRefract || reflectance(cosTheta, refractionRatio) > randomDouble())
            direction = reflect(unitDirection, hitRecord.normal);
        else
            direction = refract(unitDirection, hitRecord.normal, refractionRatio);

        scattered.copy(new Ray(hitRecord.point, direction, ray.time()));
        return true;
    }
}
