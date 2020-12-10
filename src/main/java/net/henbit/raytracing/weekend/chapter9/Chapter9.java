package net.henbit.raytracing.weekend.chapter9;

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
public class Chapter9
{

    public static void main(String[] args) throws IOException
    {
        Chapter9 chapter9 = new Chapter9();
        chapter9.run();
    }

    private Vector3 rayColor(final Ray r, Hittable world, int depth)
    {
        HitRecord hitRecord = new HitRecord();

        if (depth <= 0)
            return new Vector3(0, 0, 0);

        if (world.hit(r, 0.001, INFINITY, hitRecord))
        {
            Ray scattered = new Ray();
            Vector3 attenuation = new Vector3();
            if (hitRecord.material.scatter(r, hitRecord, attenuation, scattered))
                return attenuation.multiply(rayColor(scattered, world, depth - 1));
            return new Vector3(0, 0, 0);
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
        final int maxDepth = 50;

        // World
        HittableList world = new HittableList();

        Material ground = new Lambertian(new Vector3(0.8, 0.8, 0.0));
        Material center = new Lambertian(new Vector3(0.7, 0.3, 0.3));
        Material left = new Metal(new Vector3(0.8, 0.8, 0.8), 0.3);
        Material right = new Metal(new Vector3(0.8, 0.6, 0.2), 1);

        world.add(new Sphere(new Vector3(0.0, -100.5, -1.0), 100.0, ground));
        world.add(new Sphere(new Vector3(0.0, 0.0, -1.0), 0.5, center));
        world.add(new Sphere(new Vector3(-1.0, 0.0, -1.0), 0.5, left));
        world.add(new Sphere(new Vector3(1.0, 0.0, -1.0), 0.5, right));

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
                    pixelColor = pixelColor.add(rayColor(ray, world, maxDepth));
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
