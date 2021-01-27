package net.henbit.raytracing.nextweek.hittable;

import net.henbit.raytracing.nextweek.AABB;
import net.henbit.raytracing.nextweek.HitRecord;
import net.henbit.raytracing.nextweek.Ray;
import net.henbit.raytracing.nextweek.Vector3;
import net.henbit.raytracing.nextweek.material.Material;

public class XZRect extends Hittable
{

    private final Material material;
    private final double x0;
    private final double x1;
    private final double z0;
    private final double z1;
    private final double k;

    public XZRect(final double x0, final double x1, final double z0, final double z1, final double k, final Material material)
    {
        this.material = material;
        this.x0 = x0;
        this.x1 = x1;
        this.z0 = z0;
        this.z1 = z1;
        this.k = k;
    }

    @Override
    public boolean hit(final Ray ray, final double tMin, final double tMax, final HitRecord hitRecord)
    {
        double t = (k - ray.origin().y()) / ray.direction().y();
        if (t < tMin || t > tMax)
            return false;
        double x = ray.origin().x() + t * ray.direction().x();
        double z = ray.origin().z() + t * ray.direction().z();
        if (x < x0 || x > x1 || z < z0 || z > z1)
            return false;
        hitRecord.u = (x - x0) / (x1 - x0);
        hitRecord.v = (z - z0) / (z1 - z0);
        hitRecord.t = t;
        Vector3 outwardNormal = new Vector3(0, 1, 0);
        hitRecord.setFaceNormal(ray, outwardNormal);
        hitRecord.material = material;
        hitRecord.point = ray.at(t);
        return true;
    }

    @Override
    public boolean boundingBox(final double time0, final double time1, final AABB outputBox)
    {
        outputBox.copy(new AABB(new Vector3(x0, k - 0.0001, z0), new Vector3(x1, k + 0.0001, z1)));
        return true;
    }

}
