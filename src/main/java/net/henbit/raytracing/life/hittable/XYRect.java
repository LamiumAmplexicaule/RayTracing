package net.henbit.raytracing.life.hittable;

import net.henbit.raytracing.life.AABB;
import net.henbit.raytracing.life.HitRecord;
import net.henbit.raytracing.life.Ray;
import net.henbit.raytracing.life.Vector3;
import net.henbit.raytracing.life.material.Material;

public class XYRect extends Hittable
{

    private final Material material;
    private final double x0;
    private final double x1;
    private final double y0;
    private final double y1;
    private final double k;

    public XYRect(final double x0, final double x1, final double y0, final double y1, final double k, final Material material)
    {
        this.material = material;
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
        this.k = k;
    }

    @Override
    public boolean hit(final Ray ray, final double tMin, final double tMax, final HitRecord hitRecord)
    {
        double t = (k - ray.origin().z()) / ray.direction().z();
        if (t < tMin || t > tMax)
            return false;
        double x = ray.origin().x() + t * ray.direction().x();
        double y = ray.origin().y() + t * ray.direction().y();
        if (x < x0 || x > x1 || y < y0 || y > y1)
            return false;
        hitRecord.u = (x - x0) / (x1 - x0);
        hitRecord.v = (y - y0) / (y1 - y0);
        hitRecord.t = t;
        Vector3 outwardNormal = new Vector3(0, 0, 1);
        hitRecord.setFaceNormal(ray, outwardNormal);
        hitRecord.material = material;
        hitRecord.point = ray.at(t);
        return true;
    }

    @Override
    public boolean boundingBox(final double time0, final double time1, final AABB outputBox)
    {
        outputBox.copy(new AABB(new Vector3(x0, y0, k - 0.0001), new Vector3(x1, y1, k + 0.0001)));
        return true;
    }

}
