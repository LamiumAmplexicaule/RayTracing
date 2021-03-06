package net.henbit.raytracing.nextweek.chapter5;

import net.henbit.raytracing.nextweek.*;
import net.henbit.raytracing.nextweek.hittable.Hittable;
import net.henbit.raytracing.nextweek.hittable.HittableList;
import net.henbit.raytracing.nextweek.hittable.MovingSphere;
import net.henbit.raytracing.nextweek.hittable.Sphere;
import net.henbit.raytracing.nextweek.material.Dielectric;
import net.henbit.raytracing.nextweek.material.Lambertian;
import net.henbit.raytracing.nextweek.material.Material;
import net.henbit.raytracing.nextweek.material.Metal;
import net.henbit.raytracing.nextweek.texture.CheckerTexture;
import net.henbit.raytracing.nextweek.texture.NoiseTexture;
import net.henbit.raytracing.nextweek.texture.Texture;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static net.henbit.raytracing.nextweek.RTNextWeek.*;
import static net.henbit.raytracing.nextweek.Vector3.random;
import static net.henbit.raytracing.nextweek.Vector3.unitVector;

@SuppressWarnings({"DuplicatedCode", "ConstantConditions"})
public class Chapter5
{

    public static void main(String[] args) throws IOException
    {
        Chapter5 chapter5 = new Chapter5();
        chapter5.run();
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
        final int samplesPerPixel = 100;
        final int maxDepth = 50;

        // World
        HittableList world;

        Vector3 lookFrom;
        Vector3 lookAt;
        double vfov;
        double aperture = 0.0;

        switch (0)
        {
            case 1:
                world = randomScene();
                lookFrom = new Vector3(13, 2, 3);
                lookAt = new Vector3(0, 0, 0);
                vfov = 20.0;
                aperture = 0.1;
                break;
            case 2:
                world = twoSphere();
                lookFrom = new Vector3(13, 2, 3);
                lookAt = new Vector3(0, 0, 0);
                vfov = 20.0;
                break;
            default:
            case 3:
                world = twoPerlinSphere();
                lookFrom = new Vector3(13, 2, 3);
                lookAt = new Vector3(0, 0, 0);
                vfov = 20.0;
                break;
        }

        // Camera
        Vector3 up = new Vector3(0, 1, 0);
        double distToFocus = 10.0;
        final int imageHeight = (int) (imageWidth / aspectRatio);

        Camera camera = new Camera(lookFrom, lookAt, up, vfov, aspectRatio, aperture, distToFocus, 0.0, 1.0);

        // Render
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(IMAGE_PATH + "nextweek-" + this.getClass().getSimpleName().toLowerCase(Locale.ROOT) + ".ppm"));
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

    private HittableList twoPerlinSphere()
    {
        HittableList world = new HittableList();

        Texture pertext = new NoiseTexture(4);
        world.add(new Sphere(new Vector3(0, -1000, 0), 1000, new Lambertian(pertext)));
        world.add(new Sphere(new Vector3(0, 2, 0), 2, new Lambertian(pertext)));

        return world;
    }

    private HittableList twoSphere()
    {
        HittableList world = new HittableList();

        Texture checker = new CheckerTexture(new Vector3(0.2, 0.3, 0.1), new Vector3(0.9, 0.9, 0.9));
        world.add(new Sphere(new Vector3(0, -10, 0), 10, new Lambertian(checker)));
        world.add(new Sphere(new Vector3(0, 10, 0), 10, new Lambertian(checker)));

        return world;
    }

    private HittableList randomScene()
    {
        HittableList world = new HittableList();

        Texture checker = new CheckerTexture(new Vector3(0.2, 0.3, 0.1), new Vector3(0.9, 0.9, 0.9));
        world.add(new Sphere(new Vector3(0, -1000, 0), 1000, new Lambertian(checker)));

        for (int a = -11; a < 11; a++)
        {
            for (int b = -11; b < 11; b++)
            {
                double chooseMat = randomDouble();
                Vector3 center = new Vector3(a + 0.9 * randomDouble(), 0.2, b + 0.9 * randomDouble());

                if ((center.subtract(new Vector3(4, 0.2, 0))).length() > 0.9)
                {
                    Material sphereMaterial;

                    if (chooseMat < 0.8)
                    {
                        // diffuse
                        Vector3 albedo = random().multiply(random());
                        sphereMaterial = new Lambertian(albedo);
                        Vector3 center2 = center.add(new Vector3(0, randomDouble(0, 0.5), 0));
                        world.add(new MovingSphere(center, center2, 0.0, 1.0, 0.2, sphereMaterial));
                    }
                    else if (chooseMat < 0.95)
                    {
                        // metal
                        Vector3 albedo = random(0.5, 1);
                        double fuzz = randomDouble(0, 0.5);
                        sphereMaterial = new Metal(albedo, fuzz);
                        world.add(new Sphere(center, 0.2, sphereMaterial));
                    }
                    else
                    {
                        // glass
                        sphereMaterial = new Dielectric(1.5);
                        world.add(new Sphere(center, 0.2, sphereMaterial));
                    }
                }
            }
        }

        Material material1 = new Dielectric(1.5);
        world.add(new Sphere(new Vector3(0, 1, 0), 1.0, material1));

        Material material2 = new Lambertian(new Vector3(0.4, 0.2, 0.1));
        world.add(new Sphere(new Vector3(-4, 1, 0), 1.0, material2));

        Material material3 = new Metal(new Vector3(0.7, 0.6, 0.5), 0.0);
        world.add(new Sphere(new Vector3(4, 1, 0), 1.0, material3));

        return world;
    }

}
