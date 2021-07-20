package net.henbit.raytracing.life.hittable;

import net.henbit.raytracing.life.AABB;
import net.henbit.raytracing.life.HitRecord;
import net.henbit.raytracing.life.Ray;
import net.henbit.raytracing.life.Vector3;
import net.henbit.raytracing.life.material.Material;

public class YZRect extends Hittable
{

    private final Material material;
    private final double y0;
    private final double y1;
    private final double z0;
    private final double z1;
    private final double k;

    public YZRect(final double y0, final double y1, final double z0, final double z1, final double k, final Material material)
    {
        this.material = material;
        this.y0 = y0;
        this.y1 = y1;
        this.z0 = z0;
        this.z1 = z1;
        this.k = k;
    }

    @Override
    public boolean hit(final Ray ray, final double tMin, final double tMax, final HitRecord hitRecord)
    {
        double t = (k - ray.origin().x()) / ray.direction().x();
        if (t < tMin || t > tMax)
            return false;
        double y = ray.origin().y() + t * ray.direction().y();
        double z = ray.origin().z() + t * ray.direction().z();
        if (y < y0 || y > y1 || z < z0 || z > z1)
            return false;
        hitRecord.u = (y - y0) / (y1 - y0);
        hitRecord.v = (z - z0) / (z1 - z0);
        hitRecord.t = t;
        Vector3 outwardNormal = new Vector3(1, 0, 0);
        hitRecord.setFaceNormal(ray, outwardNormal);
        hitRecord.material = material;
        hitRecord.point = ray.at(t);
        return true;
    }

    @Override
    public boolean boundingBox(final double time0, final double time1, final AABB outputBox)
    {
        outputBox.copy(new AABB(new Vector3(k - 0.0001, y0, z0), new Vector3(k + 0.0001, y1, z1)));
        return true;
    }

}
