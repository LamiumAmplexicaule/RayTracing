package net.henbit.raytracing.life.chapter4;

import net.henbit.raytracing.life.Vector3;

import static net.henbit.raytracing.life.RTLife.PI;

public class SphereImportance
{

    public static void main(String[] args)
    {
        int N = 1000000;
        double sum = 0.0;
        for (int i = 0; i < N; i++)
        {
            Vector3 d = Vector3.randomUnitVector();
            double cosineSquared = d.z() * d.z();
            sum += cosineSquared / pdf(d);
        }

        System.out.printf("I = %.12f%n", sum / N);
    }

    static double pdf(Vector3 p)
    {
        return 1 / (4 * PI);
    }

}
