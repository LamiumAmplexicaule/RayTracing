package net.henbit.raytracing.life;

import static java.lang.Math.*;
import static net.henbit.raytracing.life.RTLife.randomDouble;

public class Vector3
{

    public final double[] e;

    public Vector3()
    {
        this(0, 0, 0);
    }

    public Vector3(final double e0, final double e1, final double e2)
    {
        this.e = new double[]{e0, e1, e2};
    }

    public static Vector3 add(final Vector3 u, final Vector3 v)
    {
        return new Vector3(u.e[0] + v.e[0], u.e[1] + v.e[1], u.e[2] + v.e[2]);
    }

    public static Vector3 subtract(final Vector3 u, final Vector3 v)
    {
        return new Vector3(u.e[0] - v.e[0], u.e[1] - v.e[1], u.e[2] - v.e[2]);
    }

    public static double dot(final Vector3 u, final Vector3 v)
    {
        return u.e[0] * v.e[0] + u.e[1] * v.e[1] + u.e[2] * v.e[2];
    }

    public static Vector3 cross(final Vector3 u, final Vector3 v)
    {
        return new Vector3
                (
                        u.e[1] * v.e[2] - u.e[2] * v.e[1],
                        u.e[2] * v.e[0] - u.e[0] * v.e[2],
                        u.e[0] * v.e[1] - u.e[1] * v.e[0]
                );
    }

    public static Vector3 unitVector(final Vector3 v)
    {
        return v.divide(v.length());
    }

    public static Vector3 random()
    {
        return new Vector3(randomDouble(), randomDouble(), randomDouble());
    }

    public static Vector3 random(double min, double max)
    {
        return new Vector3(randomDouble(min, max), randomDouble(min, max), randomDouble(min, max));
    }

    public static Vector3 randomInUnitSphere()
    {
        while (true)
        {
            Vector3 p = random(-1, 1);
            if (p.lengthSquared() >= 1) continue;
            return p;
        }
    }

    public static Vector3 randomUnitVector()
    {
        return unitVector(randomInUnitSphere());
    }

    public static Vector3 randomInHemisphere(final Vector3 normal)
    {
        Vector3 inUnitSphere = randomInUnitSphere();
        if (dot(inUnitSphere, normal) > 0.0)
            return inUnitSphere;
        else
            return inUnitSphere.minus();
    }

    public static Vector3 randomInUnitDisk()
    {
        while (true)
        {
            Vector3 p = new Vector3(randomDouble(-1, 1), randomDouble(-1, 1), 0);
            if (p.lengthSquared() >= 1) continue;
            return p;
        }
    }

    public static Vector3 reflect(final Vector3 v, final Vector3 n)
    {
        return v.subtract(n.multiply(dot(v, n) * 2));
    }

    public static Vector3 refract(final Vector3 uv, final Vector3 n, final double etaiOverEtat)
    {
        double cos_theta = min(dot(uv.minus(), n), 1.0);
        Vector3 r_out_perp = uv.add(n.multiply(cos_theta)).multiply(etaiOverEtat);
        Vector3 r_out_parallel = n.multiply(-sqrt(abs(1.0 - r_out_perp.lengthSquared())));
        return r_out_perp.add(r_out_parallel);
    }

    @Override
    public String toString()
    {
        return e[0] + " " + e[1] + " " + e[2];
    }

    public Vector3 minus()
    {
        return new Vector3(-e[0], -e[1], -e[2]);
    }

    public Vector3 add(final Vector3 v)
    {
        return new Vector3(e[0] + v.e[0], e[1] + v.e[1], e[2] + v.e[2]);
    }

    public Vector3 subtract(final Vector3 v)
    {
        return new Vector3(e[0] - v.e[0], e[1] - v.e[1], e[2] - v.e[2]);
    }

    public Vector3 multiply(final double t)
    {
        return new Vector3(e[0] * t, e[1] * t, e[2] * t);
    }

    public Vector3 multiply(final Vector3 v)
    {
        return new Vector3(e[0] * v.e[0], e[1] * v.e[1], e[2] * v.e[2]);
    }

    public Vector3 divide(final double t)
    {
        return new Vector3(e[0] / t, e[1] / t, e[2] / t);
    }

    public double length()
    {
        return sqrt(lengthSquared());
    }

    public double lengthSquared()
    {
        return e[0] * e[0] + e[1] * e[1] + e[2] * e[2];
    }

    public double x()
    {
        return e[0];
    }

    public double y()
    {
        return e[1];
    }

    public double z()
    {
        return e[2];
    }

    public double get(final int index)
    {
        return e[index];
    }

    public boolean nearZero()
    {
        final double s = 1e-8;
        return (abs(e[0]) < s) && (abs(e[1]) < s) && (abs(e[2]) < s);
    }

    public void copy(final Vector3 vector)
    {
        this.e[0] = vector.x();
        this.e[1] = vector.y();
        this.e[2] = vector.z();
    }
}