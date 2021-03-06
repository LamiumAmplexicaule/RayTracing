package net.henbit.raytracing.weekend.chapter7;

import net.henbit.raytracing.weekend.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static net.henbit.raytracing.weekend.RTWeekend.*;
import static net.henbit.raytracing.weekend.Vector3.unitVector;

@SuppressWarnings("DuplicatedCode")
public class Chapter7
{

    public static void main(String[] args) throws IOException
    {
        Chapter7 chapter7 = new Chapter7();
        chapter7.run();
    }

    private Vector3 rayColor(final Ray r, Hittable world)
    {
        HitRecord hitRecord = new HitRecord();
        if (world.hit(r, 0, INFINITY, hitRecord))
        {
            return hitRecord.normal.add(new Vector3(1, 1, 1)).multiply(0.5);
        }
        Vector3 unitDirection = unitVector(r.direction());
        double t = 0.5 * (unitDirection.y() + 1.0);
        return new Vector3(1.0, 1.0, 1.0).multiply(1.0 - t).add(new Vector3(0.5, 0.7, 1.0).multiply(t));
    }

    public void run() throws IOException
    {
        // Image
        final double aspectRatio = 16.0 / 9.0;
        final int imageWidth = 400;
        final int imageHeight = (int) (imageWidth / aspectRatio);
        final int samplesPerPixel = 100;

        // World
        HittableList world = new HittableList();
        world.add(new Sphere(new Vector3(0, 0, -1), 0.5));
        world.add(new Sphere(new Vector3(0, -100.5, -1), 100));

        // Camera
        Camera camera = new Camera();

        // Render
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(IMAGE_PATH + "weekend-" + this.getClass().getSimpleName().toLowerCase(Locale.ROOT) + ".ppm"));
        bufferedWriter.write("P3" + System.lineSeparator());
        bufferedWriter.write(imageWidth + " " + imageHeight + System.lineSeparator());
        bufferedWriter.write("255" + System.lineSeparator());

        Vector3[] pixelColors = new Vector3[imageWidth * imageHeight];
        AtomicInteger count = new AtomicInteger(imageHeight);

        IntStream.range(0, imageHeight).parallel().forEach(j ->
        {
            System.err.println("ScanLines remaining: " + count.decrementAndGet() + "");
            for (int i = 0; i < imageWidth; ++i)
            {
                Vector3 pixelColor = new Vector3(0, 0, 0);
                for (int s = 0; s < samplesPerPixel; ++s)
                {
                    double u = (i + randomDouble()) / (imageWidth - 1);
                    double v = (j + randomDouble()) / (imageHeight - 1);
                    Ray ray = camera.getRay(u, v);
                    pixelColor = pixelColor.add(rayColor(ray, world));
                }
                pixelColors[(imageHeight - j - 1) * imageWidth + i] = pixelColor;
            }
        });

        for (Vector3 pixelColor : pixelColors)
        {
            Color.writeColor(bufferedWriter, pixelColor, samplesPerPixel);
        }

        bufferedWriter.close();
        System.err.println("Done");
    }

}
