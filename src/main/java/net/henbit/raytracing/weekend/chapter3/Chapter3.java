package net.henbit.raytracing.weekend.chapter3;

import net.henbit.raytracing.weekend.Vector3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import static net.henbit.raytracing.weekend.RTWeekend.IMAGE_PATH;

@SuppressWarnings("DuplicatedCode")
public class Chapter3
{

    public static void main(String[] args) throws IOException
    {
        Chapter3 chapter3 = new Chapter3();
        chapter3.run();
    }

    public void run() throws IOException
    {
        // Image
        int imageWidth = 256;
        int imageHeight = 256;

        // Render
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(IMAGE_PATH + "weekend-" + this.getClass().getSimpleName().toLowerCase(Locale.ROOT) + ".ppm"));
        bufferedWriter.write("P3" + System.lineSeparator());
        bufferedWriter.write(imageWidth + " " + imageHeight + System.lineSeparator());
        bufferedWriter.write("255" + System.lineSeparator());

        for (int j = imageHeight - 1; j >= 0; --j)
        {
            System.err.println("ScanLines remaining: " + j + "");
            for (int i = 0; i < imageWidth; ++i)
            {
                Vector3 pixelColor = new Vector3((double) i / (imageWidth - 1), (double) j / (imageHeight - 1), 0.25);
                Color.writeColor(bufferedWriter, pixelColor);
            }
        }
        bufferedWriter.close();
        System.err.println("Done");
    }

}
