package net.henbit.raytracing.nextweek;

import net.henbit.raytracing.nextweek.material.Material;

import static net.henbit.raytracing.nextweek.Vector3.dot;

public class HitRecord
{

    public Vector3 point;
    public Vector3 normal;
    public Material material;
    public double t;
    public double u;
    public double v;
    public boolean frontFace;

    public void setFaceNormal(final Ray ray, final Vector3 outwardNormal)
    {
        this.frontFace = dot(ray.direction(), outwardNormal) < 0;
        this.normal = frontFace ? outwardNormal : outwardNormal.minus();
    }

    public void copy(final HitRecord hitRecord)
    {
        this.point = hitRecord.point;
        this.normal = hitRecord.normal;
        this.material = hitRecord.material;
        this.t = hitRecord.t;
        this.u = hitRecord.u;
        this.v = hitRecord.v;
        this.frontFace = hitRecord.frontFace;
    }

}
