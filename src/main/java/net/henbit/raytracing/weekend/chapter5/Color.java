package net.henbit.raytracing.weekend.chapter5;

import net.henbit.raytracing.weekend.Vector3;

import java.io.BufferedWriter;
import java.io.IOException;

@SuppressWarnings("DuplicatedCode")
public class Color
{

    static void writeColor(BufferedWriter bufferedWriter, Vector3 pixelColor) throws IOException
    {
        bufferedWriter.write((int) (255.999 * pixelColor.x()) + " "
                + (int) (255.999 * pixelColor.y()) + " "
                + (int) (255.999 * pixelColor.z()) + System.lineSeparator());
    }

}
