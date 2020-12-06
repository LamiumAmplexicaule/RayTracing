package net.henbit.raytracing.weekend.chapter2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import static net.henbit.raytracing.weekend.RTWeekend.IMAGE_PATH;

@SuppressWarnings("DuplicatedCode")
public class Chapter2
{

    public static void main(String[] args) throws IOException
    {
        Chapter2 chapter2 = new Chapter2();
        chapter2.run();
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
                double r = (double) i / (imageWidth - 1);
                double g = (double) j / (imageHeight - 1);
                double b = 0.25;

                int ir = (int) (255.999 * r);
                int ig = (int) (255.999 * g);
                int ib = (int) (255.999 * b);
                bufferedWriter.write(ir + " " + ig + " " + ib + System.lineSeparator());
            }
        }

        bufferedWriter.close();
        System.err.println("Done");
    }

}
