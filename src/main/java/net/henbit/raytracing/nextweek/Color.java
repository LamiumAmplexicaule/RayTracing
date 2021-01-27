package net.henbit.raytracing.nextweek;

import java.io.BufferedWriter;
import java.io.IOException;

import static java.lang.Math.sqrt;
import static net.henbit.raytracing.nextweek.RTNextWeek.clamp;

@SuppressWarnings("DuplicatedCode")
public class Color
{

    public static void writeColor(BufferedWriter bufferedWriter, Vector3 pixelColor, int samplePerPixel) throws IOException
    {
        double r = pixelColor.x();
        double g = pixelColor.y();
        double b = pixelColor.z();
        double scale = 1.0 / samplePerPixel;
        r = sqrt(scale * r);
        g = sqrt(scale * g);
        b = sqrt(scale * b);
        bufferedWriter.write((int) (256 * clamp(r, 0.0, 0.999)) + " "
                + (int) (256 * clamp(g, 0.0, 0.999)) + " "
                + (int) (256 * clamp(b, 0.0, 0.999)) + System.lineSeparator());
    }

}
