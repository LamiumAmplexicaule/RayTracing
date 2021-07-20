package net.henbit.raytracing.life.chapter7;

import static java.lang.Math.*;
import static net.henbit.raytracing.life.RTLife.PI;
import static net.henbit.raytracing.life.RTLife.randomDouble;

public class CosCubed
{

    public static void main(String[] args)
    {
        int N = 1000000;
        double sum = 0.0;
        for (int i = 0; i < N; i++)
        {
            double r1 = randomDouble();
            double r2 = randomDouble();
            double x = cos(2 * PI * r1) * 2 * sqrt(r2 * (1 - r2));
            double y = sin(2 * PI * r1) * 2 * sqrt(r2 * (1 - r2));
            double z = 1 - r2;
            sum += z * z * z / (1.0 / (2.0 * PI));
        }

        System.out.printf("Pi/2 = %.12f%n", PI / 2);
        System.out.printf("Estimate = %.12f%n", sum / N);
    }

}
