package net.henbit.raytracing.life.hittable;

import net.henbit.raytracing.life.AABB;
import net.henbit.raytracing.life.HitRecord;
import net.henbit.raytracing.life.Ray;
import net.henbit.raytracing.life.Vector3;
import net.henbit.raytracing.life.material.Material;

import static net.henbit.raytracing.life.AABB.surroundingBox;
import static net.henbit.raytracing.life.Vector3.dot;

@SuppressWarnings("DuplicatedCode")
public class MovingSphere extends Hittable
{

    public final Vector3 center0, center1;
    public final double time0, time1;
    public final double radius;
    public final Material material;

    public MovingSphere(final Vector3 center0, final Vector3 center1, final double time0, final double time1, final double radius, final Material material)
    {
        this.center0 = center0;
        this.center1 = center1;
        this.time0 = time0;
        this.time1 = time1;
        this.radius = radius;
        this.material = material;
    }

    @Override
    public boolean hit(final Ray ray, final double tMin, final double tMax, final HitRecord hitRecord)
    {
        Vector3 oc = ray.origin().subtract(center(ray.time()));
        double a = ray.direction().lengthSquared();
        double halfB = dot(oc, ray.direction());
        double c = oc.lengthSquared() - radius * radius;

        double discriminant = halfB * halfB - a * c;
        if (discriminant < 0) return false;
        double sqrtD = Math.sqrt(discriminant);

        double root = (-halfB - sqrtD) / a;
        if (root < tMin || tMax < root)
        {
            root = (-halfB + sqrtD) / a;
            if (root < tMin || tMax < root)
                return false;
        }

        hitRecord.t = root;
        hitRecord.point = ray.at(hitRecord.t);
        Vector3 outwardNormal = hitRecord.point.subtract(center(ray.time())).divide(radius);
        hitRecord.setFaceNormal(ray, outwardNormal);
        hitRecord.material = material;

        return true;
    }

    @Override
    public boolean boundingBox(final double time0, final double time1, final AABB outputBox)
    {
        AABB box0 = new AABB(
                center(time0).subtract(new Vector3(radius, radius, radius)),
                center(time0).add(new Vector3(radius, radius, radius)));
        AABB box1 = new AABB(
                center(time1).subtract(new Vector3(radius, radius, radius)),
                center(time1).add(new Vector3(radius, radius, radius)));
        outputBox.copy(surroundingBox(box0, box1));
        return true;
    }

    private Vector3 center(final double time)
    {
        return center0.add(center1.subtract(center0).multiply((time - time0) / (time1 - time0)));
    }

}
