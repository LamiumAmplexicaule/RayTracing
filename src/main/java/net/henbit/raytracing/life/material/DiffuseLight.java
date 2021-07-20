package net.henbit.raytracing.life.material;

import net.henbit.raytracing.life.HitRecord;
import net.henbit.raytracing.life.Ray;
import net.henbit.raytracing.life.Vector3;
import net.henbit.raytracing.life.texture.SolidColor;
import net.henbit.raytracing.life.texture.Texture;

public class DiffuseLight extends Material
{

    private final Texture emit;

    public DiffuseLight(Texture emit)
    {
        this.emit = emit;
    }

    public DiffuseLight(Vector3 color)
    {
        this.emit = new SolidColor(color);
    }

    @Override
    public boolean scatter(Ray ray, HitRecord hitRecord, Vector3 attenuation, Ray scattered)
    {
        return false;
    }

    @Override
    public Vector3 emitted(double u, double v, Vector3 point)
    {
        return emit.value(u, v, point);
    }

    @Override
    public Vector3 emitted(final Ray ray, final HitRecord hitRecord, double u, double v, Vector3 point)
    {
        if (!hitRecord.frontFace)
            return new Vector3(0, 0, 0);
        return emit.value(u, v, point);
    }
}
