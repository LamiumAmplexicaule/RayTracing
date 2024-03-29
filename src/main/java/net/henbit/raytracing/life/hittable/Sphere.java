package net.henbit.raytracing.life.hittable;

import net.henbit.raytracing.life.*;
import net.henbit.raytracing.life.material.Material;

import static java.lang.Math.*;
import static net.henbit.raytracing.life.RTLife.INFINITY;
import static net.henbit.raytracing.life.Vector3.dot;
import static net.henbit.raytracing.life.pdf.PDF.randomToSphere;

@SuppressWarnings("DuplicatedCode")
public class Sphere extends Hittable
{

    public final Vector3 center;
    public final double radius;
    public final Material material;

    public Sphere(final Vector3 center, final double radius)
    {
        this.center = center;
        this.radius = radius;
        this.material = null;
    }

    public Sphere(final Vector3 center, final double radius, final Material material)
    {
        this.center = center;
        this.radius = radius;
        this.material = material;
    }

    private static void getSphereUV(final Vector3 point, HitRecord hitRecord)
    {
        // p: a given point on the sphere of radius one, centered at the origin.
        // u: returned value [0,1] of angle around the Y axis from X=-1.
        // v: returned value [0,1] of angle from Y=-1 to Y=+1.
        //     <1 0 0> yields <0.50 0.50>       <-1  0  0> yields <0.00 0.50>
        //     <0 1 0> yields <0.50 1.00>       < 0 -1  0> yields <0.50 0.00>
        //     <0 0 1> yields <0.25 0.50>       < 0  0 -1> yields <0.75 0.50>

        double theta = acos(-point.y());
        double phi = atan2(-point.z(), point.x()) + PI;

        hitRecord.u = phi / (2 * PI);
        hitRecord.v = theta / PI;
    }

    @Override
    public boolean hit(final Ray ray, final double tMin, final double tMax, final HitRecord hitRecord)
    {
        Vector3 oc = ray.origin().subtract(center);
        double a = ray.direction().lengthSquared();
        double half_b = dot(oc, ray.direction());
        double c = oc.lengthSquared() - radius * radius;
        double discriminant = half_b * half_b - a * c;
        if (discriminant < 0) return false;

        double sqrtD = Math.sqrt(discriminant);

        double root = (-half_b - sqrtD) / a;
        if (root < tMin || tMax < root)
        {
            root = (-half_b + sqrtD) / a;
            if (root < tMin || tMax < root)
                return false;
        }

        hitRecord.t = root;
        hitRecord.point = ray.at(hitRecord.t);
        Vector3 outwardNormal = hitRecord.point.subtract(center).divide(radius);
        hitRecord.setFaceNormal(ray, outwardNormal);
        getSphereUV(outwardNormal, hitRecord);
        hitRecord.material = material;

        return true;
    }

    @Override
    public boolean boundingBox(double time0, double time1, AABB outputBox)
    {
        outputBox.copy(
                new AABB(
                        center.subtract(new Vector3(radius, radius, radius)),
                        center.add(new Vector3(radius, radius, radius))
                )
        );
        return true;
    }

    @Override
    public double pdfValue(Vector3 origin, Vector3 v)
    {
        HitRecord hitRecord = new HitRecord();
        if (!this.hit(new Ray(origin, v), 0.001, INFINITY, hitRecord))
            return 0;

        double cosThetaMax = sqrt(1 - radius * radius / (center.subtract(origin)).lengthSquared());
        double solidAngle = 2 * PI * (1 - cosThetaMax);

        return 1 / solidAngle;
    }

    @Override
    public Vector3 random(Vector3 origin)
    {
        Vector3 direction = center.subtract(origin);
        double distance_squared = direction.lengthSquared();
        ONB uvw = new ONB();
        uvw.buildFromW(direction);
        return uvw.local(randomToSphere(radius, distance_squared));
    }

}
