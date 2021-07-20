package net.henbit.raytracing.life.material;

import net.henbit.raytracing.life.HitRecord;
import net.henbit.raytracing.life.Ray;
import net.henbit.raytracing.life.Vector3;
import net.henbit.raytracing.life.texture.SolidColor;
import net.henbit.raytracing.life.texture.Texture;

import static net.henbit.raytracing.life.Vector3.randomInUnitSphere;

public class Isotropic extends Material
{

    private final Texture albedo;

    public Isotropic(Vector3 albedo)
    {
        this.albedo = new SolidColor(albedo);
    }

    public Isotropic(Texture albedo)
    {
        this.albedo = albedo;
    }

    @Override
    public boolean scatter(Ray ray, HitRecord hitRecord, Vector3 attenuation, Ray scattered)
    {
        scattered.copy(new Ray(hitRecord.point, randomInUnitSphere(), ray.time()));
        attenuation.copy(albedo.value(hitRecord.u, hitRecord.v, hitRecord.point));
        return true;
    }

}
