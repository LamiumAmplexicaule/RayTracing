package net.henbit.raytracing.weekend;

import static net.henbit.raytracing.weekend.Vector3.dot;

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
        hitRecord.material = material;

        return true;
    }

}
