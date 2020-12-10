package net.henbit.raytracing.weekend.chapter2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

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

        int[][] pixelColors = new int[3][imageWidth * imageHeight];
        AtomicInteger count = new AtomicInteger(imageHeight);

        IntStream.range(0, imageHeight).parallel().forEach(j ->
        {
            System.err.println("ScanLines remaining: " + count.decrementAndGet() + "");
            for (int i = 0; i < imageWidth; ++i)
            {
                double r = (double) i / (imageWidth - 1);
                double g = (double) j / (imageHeight - 1);
                double b = 0.25;

                int ir = (int) (255.999 * r);
                int ig = (int) (255.999 * g);
                int ib = (int) (255.999 * b);
                final int k = (imageHeight - j - 1) * imageWidth + i;
                pixelColors[0][k] = ir;
                pixelColors[1][k] = ig;
                pixelColors[2][k] = ib;
            }
        });

        for (int i = 0; i < pixelColors[0].length; ++i)
        {
            bufferedWriter.write(pixelColors[0][i] + " " + pixelColors[1][i] + " " + pixelColors[2][i] + System.lineSeparator());
        }

        bufferedWriter.close();
        System.err.println("Done");
    }

}
