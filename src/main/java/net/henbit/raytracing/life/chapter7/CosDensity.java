package net.henbit.raytracing.life.chapter7;

import net.henbit.raytracing.life.Vector3;

import static java.lang.Math.*;
import static net.henbit.raytracing.life.RTLife.PI;
import static net.henbit.raytracing.life.RTLife.randomDouble;

public class CosDensity
{

    public static void main(String[] args)
    {
        int N = 1000000;

        double sum = 0.0;
        for (int i = 0; i < N; i++)
        {
            Vector3 v = randomCosineDirection();
            sum += v.z() * v.z() * v.z() / (v.z() / PI);
        }

        System.out.printf("Pi/2 = %.12f%n", PI / 2);
        System.out.printf("Estimate = %.12f%n", sum / N);
    }

    static Vector3 randomCosineDirection()
    {
        double r1 = randomDouble();
        double r2 = randomDouble();
        double z = sqrt(1 - r2);

        double phi = 2 * PI * r1;
        double x = cos(phi) * sqrt(r2);
        double y = sin(phi) * sqrt(r2);

        return new Vector3(x, y, z);
    }

}
