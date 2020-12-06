package net.henbit.raytracing.weekend;

import static net.henbit.raytracing.weekend.Vector3.dot;

public class HitRecord
{

    public Vector3 point;
    public Vector3 normal;
    public Material material;
    public double t;
    public boolean frontFace;

    public void setFaceNormal(final Ray ray, final Vector3 outwardNormal)
    {
        frontFace = dot(ray.direction(), outwardNormal) < 0;
        normal = frontFace ? outwardNormal : outwardNormal.minus();
    }

    public void copy(final HitRecord hitRecord)
    {
        this.point = hitRecord.point;
        this.normal = hitRecord.normal;
        this.material = hitRecord.material;
        this.t = hitRecord.t;
        this.frontFace = hitRecord.frontFace;
    }

}
