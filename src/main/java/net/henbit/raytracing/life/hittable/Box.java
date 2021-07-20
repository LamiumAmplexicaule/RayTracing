package net.henbit.raytracing.life.hittable;

import net.henbit.raytracing.life.AABB;
import net.henbit.raytracing.life.HitRecord;
import net.henbit.raytracing.life.Ray;
import net.henbit.raytracing.life.Vector3;
import net.henbit.raytracing.life.material.Material;

public class Box extends Hittable
{

    private final Vector3 boxMin;
    private final Vector3 boxMax;
    private final HittableList sides = new HittableList();

    public Box(final Vector3 boxMin, final Vector3 boxMax, final Material material)
    {
        this.boxMin = boxMin;
        this.boxMax = boxMax;
        sides.add(new XYRect(boxMin.x(), boxMax.x(), boxMin.y(), boxMax.y(), boxMax.z(), material));
        sides.add(new XYRect(boxMin.x(), boxMax.x(), boxMin.y(), boxMax.y(), boxMin.z(), material));

        sides.add(new XZRect(boxMin.x(), boxMax.x(), boxMin.z(), boxMax.z(), boxMax.y(), material));
        sides.add(new XZRect(boxMin.x(), boxMax.x(), boxMin.z(), boxMax.z(), boxMin.y(), material));

        sides.add(new YZRect(boxMin.y(), boxMax.y(), boxMin.z(), boxMax.z(), boxMax.x(), material));
        sides.add(new YZRect(boxMin.y(), boxMax.y(), boxMin.z(), boxMax.z(), boxMin.x(), material));
    }

    @Override
    public boolean hit(final Ray ray, final double tMin, final double tMax, final HitRecord hitRecord)
    {
        return sides.hit(ray, tMin, tMax, hitRecord);
    }

    @Override
    public boolean boundingBox(final double time0, final double time1, final AABB outputBox)
    {
        outputBox.copy(new AABB(boxMin, boxMax));
        return true;
    }
}
