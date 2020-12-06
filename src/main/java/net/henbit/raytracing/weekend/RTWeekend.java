package net.henbit.raytracing.weekend;

import java.util.Random;

public class RTWeekend
{

    public static final double INFINITY = Double.POSITIVE_INFINITY;
    public static final double PI = Math.PI;

    public static final String IMAGE_PATH = "image/";

    public static double degreesToRadians(double degrees)
    {
        return degrees * PI / 180.0;
    }

    public static double randomDouble()
    {
        Random random = new Random();
        return random.nextDouble();
    }

    public static double randomDouble(double min, double max)
    {
        return min + (max - min) * randomDouble();
    }

    public static double clamp(double x, double min, double max)
    {
        if (x < min) return min;
        return Math.min(x, max);
    }

}
