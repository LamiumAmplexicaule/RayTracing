package net.henbit.raytracing.life.chapter7;

import static java.lang.Math.*;
import static net.henbit.raytracing.life.RTLife.PI;
import static net.henbit.raytracing.life.RTLife.randomDouble;

public class SpherePlot
{

    public static void main(String[] args)
    {
        for (int i = 0; i < 200; i++)
        {
            double r1 = randomDouble();
            double r2 = randomDouble();
            double x = cos(2 * PI * r1) * 2 * sqrt(r2 * (1 - r2));
            double y = sin(2 * PI * r1) * 2 * sqrt(r2 * (1 - r2));
            double z = 1 - 2 * r2;
            System.out.println(x + " " + y + "" + z);
        }
    }

}
