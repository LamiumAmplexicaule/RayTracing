package net.henbit.raytracing.weekend;

public abstract class Material
{

    public abstract boolean scatter(final Ray ray, final HitRecord hitRecord, Vector3 attenuation, Ray scattered);

}
