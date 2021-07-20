package net.henbit.raytracing.life.chapter12;

import net.henbit.raytracing.life.*;
import net.henbit.raytracing.life.hittable.*;
import net.henbit.raytracing.life.material.*;
import net.henbit.raytracing.life.pdf.HittablePDF;
import net.henbit.raytracing.life.pdf.MixturePDF;
import net.henbit.raytracing.life.pdf.PDF;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static net.henbit.raytracing.life.RTLife.*;

public class Chapter12
{

    public static void main(String[] args) throws IOException
    {
        Chapter12 chapter = new Chapter12();
        chapter.run();
    }

    private Vector3 rayColor(final Ray r, final Vector3 background, Hittable world, Hittable lights, int depth)
    {
        HitRecord hitRecord = new HitRecord();

        if (depth <= 0)
            return new Vector3(0, 0, 0);

        if (!world.hit(r, 0.001, INFINITY, hitRecord))
        {
            return background;
        }

        ScatterRecord scatterRecord = new ScatterRecord();
        Vector3 emitted = hitRecord.material.emitted(r, hitRecord, hitRecord.u, hitRecord.v, hitRecord.point);
        if (!hitRecord.material.scatter(r, hitRecord, scatterRecord))
            return emitted;

        if (scatterRecord.isSpecular)
        {
            return scatterRecord.attenuation
                    .multiply(rayColor(scatterRecord.specularRay, background, world, lights, depth - 1));
        }

        PDF light_ptr = new HittablePDF(lights, hitRecord.point);
        PDF p = new MixturePDF(light_ptr, scatterRecord.pdf);

        Ray scattered = new Ray(hitRecord.point, p.generate(), r.time());
        double pdfValue = p.value(scattered.direction());

        return emitted
                .add(scatterRecord.attenuation)
                .multiply(hitRecord.material.scatteringPDF(r, hitRecord, scattered))
                .multiply(rayColor(scattered, background, world, lights, depth - 1))
                .divide(pdfValue);
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
        HittableList lights = new HittableList();
        lights.add(new XZRect(213, 343, 227, 332, 554, new Material()
        {
        }));
        lights.add(new Sphere(new Vector3(190, 90, 190), 90, new Material()
        {
        }));
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
                    pixelColor = pixelColor.add(rayColor(ray, background, world, lights, maxDepth));
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
        objects.add(new FlipFace(new XZRect(213, 343, 227, 332, 554, light)));
        objects.add(new XZRect(0, 555, 0, 555, 0, white));
        objects.add(new XZRect(0, 555, 0, 555, 555, white));
        objects.add(new XYRect(0, 555, 0, 555, 555, white));

        Material aluminum = new Metal(new Vector3(0.8, 0.85, 0.88), 0.0);
        Hittable box1 = new Box(new Vector3(0, 0, 0), new Vector3(165, 330, 165), aluminum);
        box1 = new RotateY(box1, 15);
        box1 = new Translate(box1, new Vector3(265, 0, 295));
        objects.add(box1);

        Material glass = new Dielectric(1.5);
        objects.add(new Sphere(new Vector3(190, 90, 190), 90, glass));

        return objects;
    }

}
