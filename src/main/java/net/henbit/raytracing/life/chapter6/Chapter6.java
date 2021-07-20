package net.henbit.raytracing.life.chapter6;

import net.henbit.raytracing.life.*;
import net.henbit.raytracing.life.hittable.*;
import net.henbit.raytracing.life.material.DiffuseLight;
import net.henbit.raytracing.life.material.Lambertian;
import net.henbit.raytracing.life.material.Material;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static net.henbit.raytracing.life.RTLife.*;

public class Chapter6
{

    public static void main(String[] args) throws IOException
    {
        Chapter6 chapter = new Chapter6();
        chapter.run();
    }

    private Vector3 rayColor(final Ray r, final Vector3 background, Hittable world, int depth)
    {
        HitRecord hitRecord = new HitRecord();

        if (depth <= 0)
            return new Vector3(0, 0, 0);

        if (!world.hit(r, 0.001, INFINITY, hitRecord))
        {
            return background;
        }

        Ray scattered = new Ray();
        Vector3 emitted = hitRecord.material.emitted(hitRecord.u, hitRecord.v, hitRecord.point);
        DoubleWrapper pdf = new DoubleWrapper();
        Vector3 albedo = new Vector3();

        if (!hitRecord.material.scatter(r, hitRecord, albedo, scattered, pdf))
            return emitted;

        return emitted.add(albedo)
                .multiply(hitRecord.material.scatteringPDF(r, hitRecord, scattered))
                .multiply(rayColor(scattered, background, world, depth - 1));
    }

    public void run() throws IOException
    {
        // Image
        double aspectRatio = 1.0;
        int imageWidth = 600;
        int imageHeight = (int) (imageWidth / aspectRatio);
        int samplesPerPixel = 100;
        final int maxDepth = 50;

        // World
        HittableList world = cornellBox();
        Vector3 background = new Vector3(0, 0, 0);

        // Camera
        Vector3 lookFrom = new Vector3(278, 278, -800);
        Vector3 lookAt = new Vector3(278, 278, 0);
        Vector3 up = new Vector3(0, 1, 0);
        double aperture = 0.0;
        double distToFocus = 10.0;
        double vfov = 40.0;
        double time0 = 0.0;
        double time1 = 1.0;

        Camera camera = new Camera(lookFrom, lookAt, up, vfov, aspectRatio, aperture, distToFocus, time0, time1);

        // Render
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(IMAGE_PATH + "life-" + this.getClass().getSimpleName().toLowerCase(Locale.ROOT) + ".ppm"));
        bufferedWriter.write("P3" + System.lineSeparator());
        bufferedWriter.write(imageWidth + " " + imageHeight + System.lineSeparator());
        bufferedWriter.write("255" + System.lineSeparator());

        Vector3[] pixelColors = new Vector3[imageWidth * imageHeight];
        AtomicInteger count = new AtomicInteger(imageHeight);

        IntStream.range(0, imageHeight).parallel().forEach(j ->
        {
            System.err.println("ScanLines remaining: " + count.decrementAndGet() + "");
            int rjw = (imageHeight - j - 1) * imageWidth;
            for (int i = 0; i < imageWidth; ++i)
            {
                Vector3 pixelColor = new Vector3(0, 0, 0);
                for (int s = 0; s < samplesPerPixel; ++s)
                {
                    double u = (i + randomDouble()) / (imageWidth - 1);
                    double v = (j + randomDouble()) / (imageHeight - 1);
                    Ray ray = camera.getRay(u, v);
                    pixelColor = pixelColor.add(rayColor(ray, background, world, maxDepth));
                }
                pixelColors[rjw + i] = pixelColor;
            }
        });

        for (Vector3 pixelColor : pixelColors)
        {
            Color.writeColor(bufferedWriter, pixelColor, samplesPerPixel);
        }

        bufferedWriter.close();
        System.err.println("Done");
    }

    private HittableList cornellBox()
    {
        HittableList objects = new HittableList();

        Material red = new Lambertian(new Vector3(0.65, 0.05, 0.05));
        Material white = new Lambertian(new Vector3(0.73, 0.73, 0.73));
        Material green = new Lambertian(new Vector3(0.12, 0.45, 0.15));
        Material light = new DiffuseLight(new Vector3(15, 15, 15));

        objects.add(new YZRect(0, 555, 0, 555, 555, green));
        objects.add(new YZRect(0, 555, 0, 555, 0, red));
        objects.add(new XZRect(213, 343, 227, 332, 554, light));
        objects.add(new XZRect(0, 555, 0, 555, 0, white));
        objects.add(new XZRect(0, 555, 0, 555, 555, white));
        objects.add(new XYRect(0, 555, 0, 555, 555, white));

        Hittable box1 = new Box(new Vector3(0, 0, 0), new Vector3(165, 330, 165), white);
        box1 = new RotateY(box1, 15);
        box1 = new Translate(box1, new Vector3(265, 0, 295));
        objects.add(box1);

        Hittable box2 = new Box(new Vector3(0, 0, 0), new Vector3(165, 165, 165), white);
        box2 = new RotateY(box2, -18);
        box2 = new Translate(box2, new Vector3(130, 0, 65));
        objects.add(box2);

        return objects;
    }

}
