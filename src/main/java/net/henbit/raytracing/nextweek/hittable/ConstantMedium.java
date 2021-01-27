package net.henbit.raytracing.nextweek.hittable;

import net.henbit.raytracing.nextweek.AABB;
import net.henbit.raytracing.nextweek.HitRecord;
import net.henbit.raytracing.nextweek.Ray;
import net.henbit.raytracing.nextweek.Vector3;
import net.henbit.raytracing.nextweek.material.Isotropic;
import net.henbit.raytracing.nextweek.material.Material;
import net.henbit.raytracing.nextweek.texture.Texture;

import static java.lang.Math.log;
import static net.henbit.raytracing.nextweek.RTNextWeek.INFINITY;
import static net.henbit.raytracing.nextweek.RTNextWeek.randomDouble;

public class ConstantMedium extends Hittable
{

    private final Hittable boundary;
    private final Material phaseFunction;
    private final double negInvDensity;

    public ConstantMedium(Hittable boundary, double d, Texture a)
    {
        this.boundary = boundary;
        this.phaseFunction = new Isotropic(a);
        this.negInvDensity = -1.0 / d;
    }

    public ConstantMedium(Hittable boundary, double d, Vector3 c)
    {
        this.boundary = boundary;
        this.phaseFunction = new Isotropic(c);
        this.negInvDensity = -1.0 / d;
    }

    @Override
    public boolean hit(Ray ray, double tMin, double tMax, HitRecord hitRecord)
    {
        HitRecord rec1 = new HitRecord();
        HitRecord rec2 = new HitRecord();

        if (!boundary.hit(ray, -INFINITY, INFINITY, rec1))
            return false;

        if (!boundary.hit(ray, rec1.t + 0.0001, INFINITY, rec2))
            return false;

        if (rec1.t < tMin) rec1.t = tMin;
        if (rec2.t > tMax) rec2.t = tMax;

        if (rec1.t >= rec2.t)
            return false;

        if (rec1.t < 0)
            rec1.t = 0;

        final double rayLength = ray.direction().length();
        final double distanceInsideBoundary = (rec2.t - rec1.t) * rayLength;
        final double hitDistance = negInvDensity * log(randomDouble());

        if (hitDistance > distanceInsideBoundary)
            return false;

        hitRecord.t = rec1.t + hitDistance / rayLength;
        hitRecord.point = ray.at(hitRecord.t);

        hitRecord.normal = new Vector3(1, 0, 0);  // arbitrary
        hitRecord.frontFace = true;     // also arbitrary
        hitRecord.material = phaseFunction;

        return true;
    }

    @Override
    public boolean boundingBox(double time0, double time1, AABB outputBox)
    {
        return boundary.boundingBox(time0, time1, outputBox);
    }
}
