package net.henbit.raytracing.nextweek.material;

import net.henbit.raytracing.nextweek.HitRecord;
import net.henbit.raytracing.nextweek.Ray;
import net.henbit.raytracing.nextweek.Vector3;
import net.henbit.raytracing.nextweek.texture.SolidColor;
import net.henbit.raytracing.nextweek.texture.Texture;

import static net.henbit.raytracing.nextweek.Vector3.randomInUnitSphere;

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
