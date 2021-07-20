package net.henbit.raytracing.life.pdf;

import net.henbit.raytracing.life.Vector3;

import static java.lang.Math.*;
import static net.henbit.raytracing.life.RTLife.PI;
import static net.henbit.raytracing.life.RTLife.randomDouble;

public abstract class PDF
{

    public abstract double value(final Vector3 direction);

    public abstract Vector3 generate();

    public static Vector3 randomToSphere(double radius, double distanceSquared)
    {
        double r1 = randomDouble();
        double r2 = randomDouble();
        double z = 1 + r2 * (sqrt(1 - radius * radius / distanceSquared) - 1);

        double phi = 2 * PI * r1;
        double x = cos(phi) * sqrt(1 - z * z);
        double y = sin(phi) * sqrt(1 - z * z);

        return new Vector3(x, y, z);
    }

}
