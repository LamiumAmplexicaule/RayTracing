package net.henbit.raytracing.nextweek.chapter8;

import net.henbit.raytracing.nextweek.*;
import net.henbit.raytracing.nextweek.hittable.*;
import net.henbit.raytracing.nextweek.material.*;
import net.henbit.raytracing.nextweek.texture.CheckerTexture;
import net.henbit.raytracing.nextweek.texture.ImageTexture;
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

@SuppressWarnings({"DuplicatedCode", "ConstantConditions"})
public class Chapter8
{

    public static void main(String[] args) throws IOException
    {
        Chapter8 chapter = new Chapter8();
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
        Vector3 attenuation = new Vector3();
        Vector3 emitted = hitRecord.material.emitted(hitRecord.u, hitRecord.v, hitRecord.point);

        if (!hitRecord.material.scatter(r, hitRecord, attenuation, scattered))
            return emitted;

        return emitted.add(attenuation).multiply(rayColor(scattered, background, world, depth - 1));
    }


    public void run() throws IOException
    {
        // Image
        double aspectRatio = 16.0 / 9.0;
        int imageWidth = 400;
        int samplesPerPixel = 100;
        final int maxDepth = 50;

        // World
        HittableList world;

        Vector3 lookFrom;
        Vector3 lookAt;
        double vfov;
        double aperture = 0.0;
        Vector3 background;

        switch (0)
        {
            case 1:
                world = randomScene();
                background = new Vector3(0.70, 0.80, 1.00);
                lookFrom = new Vector3(13, 2, 3);
                lookAt = new Vector3(0, 0, 0);
                vfov = 20.0;
                aperture = 0.1;
                break;
            case 2:
                world = twoSphere();
                background = new Vector3(0.70, 0.80, 1.00);
                lookFrom = new Vector3(13, 2, 3);
                lookAt = new Vector3(0, 0, 0);
                vfov = 20.0;
                break;
            case 3:
                world = twoPerlinSphere();
                background = new Vector3(0.70, 0.80, 1.00);
                lookFrom = new Vector3(13, 2, 3);
                lookAt = new Vector3(0, 0, 0);
                vfov = 20.0;
                break;
            case 4:
                world = earth();
                background = new Vector3(0.70, 0.80, 1.00);
                lookFrom = new Vector3(13, 2, 3);
                lookAt = new Vector3(0, 0, 0);
                vfov = 20.0;
                break;
            case 5:
                world = simpleLight();
                samplesPerPixel = 400;
                background = new Vector3(0, 0, 0);
                lookFrom = new Vector3(26, 3, 6);
                lookAt = new Vector3(0, 2, 0);
                vfov = 20.0;
                break;
            default:
            case 6:
                world = cornellBox();
                aspectRatio = 1.0;
                imageWidth = 600;
                samplesPerPixel = 400;
                background = new Vector3(0, 0, 0);
                lookFrom = new Vector3(278, 278, -800);
                lookAt = new Vector3(278, 278, 0);
                vfov = 40.0;
                break;
        }

        // Camera
        Vector3 up = new Vector3(0, 1, 0);
        double distToFocus = 10.0;
        int imageHeight = (int) (imageWidth / aspectRatio);

        Camera camera = new Camera(lookFrom, lookAt, up, vfov, aspectRatio, aperture, distToFocus, 0.0, 1.0);

        // Render
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(IMAGE_PATH + "nextweek-" + this.getClass().getSimpleName().toLowerCase(Locale.ROOT) + ".ppm"));
        bufferedWriter.write("P3" + System.lineSeparator());
        bufferedWriter.write(imageWidth + " " + imageHeight + System.lineSeparator());
        bufferedWriter.write("255" + System.lineSeparator());

        Vector3[] pixelColors = new Vector3[imageWidth * imageHeight];
        AtomicInteger count = new AtomicInteger(imageHeight);

        int finalImageWidth = imageWidth;
        int finalSamplesPerPixel = samplesPerPixel;
        IntStream.range(0, imageHeight).parallel().forEach(j ->
        {
            System.err.println("ScanLines remaining: " + count.decrementAndGet() + "");
            for (int i = 0; i < finalImageWidth; ++i)
            {
                Vector3 pixelColor = new Vector3(0, 0, 0);
                for (int s = 0; s < finalSamplesPerPixel; ++s)
                {
                    double u = (i + randomDouble()) / (finalImageWidth - 1);
                    double v = (j + randomDouble()) / (imageHeight - 1);
                    Ray ray = camera.getRay(u, v);
                    pixelColor = pixelColor.add(rayColor(ray, background, world, maxDepth));
                }
                pixelColors[(imageHeight - j - 1) * finalImageWidth + i] = pixelColor;
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

    private HittableList simpleLight()
    {
        HittableList objects = new HittableList();

        Texture pertext = new NoiseTexture(4);
        objects.add(new Sphere(new Vector3(0, -1000, 0), 1000, new Lambertian(pertext)));
        objects.add(new Sphere(new Vector3(0, 2, 0), 2, new Lambertian(pertext)));

        Material diffuseLight = new DiffuseLight(new Vector3(4, 4, 4));
        objects.add(new XYRect(3, 5, 1, 3, -2, diffuseLight));

        return objects;
    }

    private HittableList earth()
    {
        ImageTexture earthTexture = new ImageTexture("earthmap.jpg");
        Lambertian earthSurface = new Lambertian(earthTexture);
        Hittable globe = new Sphere(new Vector3(0, 0, 0), 2, earthSurface);

        return new HittableList(globe);
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

