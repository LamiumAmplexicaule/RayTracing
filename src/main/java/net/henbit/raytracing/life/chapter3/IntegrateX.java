package net.henbit.raytracing.life.chapter3;

import static net.henbit.raytracing.life.RTLife.randomDouble;

public class IntegrateX
{

    public static void main(String[] args)
    {
        int N = 1;
        double sum = 0.0;

        for (int i = 0; i < N; i++)
        {
            double x = Math.pow(randomDouble(0, 8), 1. / 3.);
            sum += x * x / pdf(x);
        }

        System.out.printf("I = %.12f%n", sum / N);
    }

    static double pdf(double x)
    {
        return 3 * x * x / 8;
    }

}
