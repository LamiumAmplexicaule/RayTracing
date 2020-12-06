package net.henbit.raytracing.weekend.chapter7;

import net.henbit.raytracing.weekend.Vector3;

import java.io.BufferedWriter;
import java.io.IOException;

import static net.henbit.raytracing.weekend.RTWeekend.clamp;

@SuppressWarnings("DuplicatedCode")
public class Color
{

    static void writeColor(BufferedWriter bufferedWriter, Vector3 pixelColor, int samplePerPixel) throws IOException
    {
        double r = pixelColor.x();
        double g = pixelColor.y();
        double b = pixelColor.z();
        double scale = 1.0 / samplePerPixel;
        r *= scale;
        g *= scale;
        b *= scale;
        bufferedWriter.write((int) (256 * clamp(r, 0.0, 0.999)) + " "
                + (int) (256 * clamp(g, 0.0, 0.999)) + " "
                + (int) (256 * clamp(b, 0.0, 0.999)) + System.lineSeparator());
    }

}
