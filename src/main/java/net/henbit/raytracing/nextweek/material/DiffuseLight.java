package net.henbit.raytracing.nextweek.material;

import net.henbit.raytracing.nextweek.*;
import net.henbit.raytracing.nextweek.texture.SolidColor;
import net.henbit.raytracing.nextweek.texture.Texture;

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

}
