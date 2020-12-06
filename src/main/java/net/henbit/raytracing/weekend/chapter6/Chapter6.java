package net.henbit.raytracing.weekend.chapter6;

import net.henbit.raytracing.weekend.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import static net.henbit.raytracing.weekend.RTWeekend.IMAGE_PATH;
import static net.henbit.raytracing.weekend.RTWeekend.INFINITY;
import static net.henbit.raytracing.weekend.Vector3.unitVector;

@SuppressWarnings("DuplicatedCode")
public class Chapter6
{

    public static void main(String[] args) throws IOException
    {
        Chapter6 chapter6 = new Chapter6();
        chapter6.run();
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

        // World
        HittableList world = new HittableList();
        world.add(new Sphere(new Vector3(0, 0, -1), 0.5));
        world.add(new Sphere(new Vector3(0, -100.5, -1), 100));

        // Camera
        double viewportHeight = 2.0;
        double viewportWidth = aspectRatio * viewportHeight;
        double focalLength = 1.0;

        Vector3 origin = new Vector3(0, 0, 0);
        Vector3 horizontal = new Vector3(viewportWidth, 0, 0);
        Vector3 vertical = new Vector3(0, viewportHeight, 0);
        Vector3 lowerLeftCorner = origin.subtract(horizontal.divide(2)).subtract(vertical.divide(2)).subtract(new Vector3(0, 0, focalLength));

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
                double u = (double) i / (imageWidth - 1);
                double v = (double) j / (imageHeight - 1);
                Ray ray = new Ray(origin, lowerLeftCorner.add(horizontal.multiply(u)).add(vertical.multiply(v)).subtract(origin));
                Vector3 pixelColor = rayColor(ray, world);
                Color.writeColor(bufferedWriter, pixelColor);
            }
        }
        bufferedWriter.close();
        System.err.println("Done");
    }

}
