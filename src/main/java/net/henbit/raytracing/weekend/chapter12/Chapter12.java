package net.henbit.raytracing.weekend.chapter12;

import net.henbit.raytracing.weekend.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import static net.henbit.raytracing.weekend.RTWeekend.*;
import static net.henbit.raytracing.weekend.Vector3.unitVector;

@SuppressWarnings("DuplicatedCode")
public class Chapter12
{

    public static void main(String[] args) throws IOException
    {
        Chapter12 chapter12 = new Chapter12();
        chapter12.run();
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
        Material center = new Lambertian(new Vector3(0.1, 0.2, 0.5));
        Material left = new Dielectric(1.5);
        Material right = new Metal(new Vector3(0.8, 0.6, 0.2), 0);

        world.add(new Sphere(new Vector3(0.0, -100.5, -1.0), 100.0, ground));
        world.add(new Sphere(new Vector3(0.0, 0.0, -1.0), 0.5, center));
        world.add(new Sphere(new Vector3(-1.0, 0.0, -1.0), 0.5, left));
        world.add(new Sphere(new Vector3(-1.0, 0.0, -1.0), -0.4, left));
        world.add(new Sphere(new Vector3(1.0, 0.0, -1.0), 0.5, right));

        // Camera
        Vector3 lookFrom = new Vector3(3, 3, 2);
        Vector3 lookAt = new Vector3(0, 0, -1);
        Vector3 up = new Vector3(0, 1, 0);
        double distToFocus = (lookFrom.subtract(lookAt)).length();
        double aperture = 2.0;

        Camera camera = new Camera(lookFrom, lookAt, up, 20, aspectRatio, aperture, distToFocus);

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
                Vector3 pixelColor = new Vector3(0, 0, 0);
                for (int s = 0; s < samplesPerPixel; ++s)
                {
                    double u = (i + randomDouble()) / (imageWidth - 1);
                    double v = (j + randomDouble()) / (imageHeight - 1);
                    Ray ray = camera.getRay(u, v);
                    pixelColor = pixelColor.add(rayColor(ray, world, maxDepth));
                }
                Color.writeColor(bufferedWriter, pixelColor, samplesPerPixel);
            }
        }

        bufferedWriter.close();
        System.err.println("Done");
    }

}

