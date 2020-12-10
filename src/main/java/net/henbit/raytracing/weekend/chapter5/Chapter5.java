package net.henbit.raytracing.weekend.chapter5;

import net.henbit.raytracing.weekend.Ray;
import net.henbit.raytracing.weekend.Vector3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static net.henbit.raytracing.weekend.RTWeekend.IMAGE_PATH;
import static net.henbit.raytracing.weekend.Vector3.dot;

@SuppressWarnings("DuplicatedCode")
public class Chapter5
{

    public static void main(String[] args) throws IOException
    {
        Chapter5 chapter5 = new Chapter5();
        chapter5.run();
    }

    private boolean hitSphere(final Vector3 center, final double radius, final Ray ray)
    {
        Vector3 oc = ray.origin().subtract(center);
        double a = dot(ray.direction(), ray.direction());
        double b = 2.0 * dot(oc, ray.direction());
        double c = dot(oc, oc) - radius * radius;
        double discriminant = b * b - 4 * a * c;
        return discriminant > 0;
    }

    private Vector3 rayColor(final Ray ray)
    {
        if (hitSphere(new Vector3(0, 0, -1), 0.5, ray))
            return new Vector3(1, 0, 0);

        Vector3 unitDirection = Vector3.unitVector(ray.direction());
        double t = 0.5 * (unitDirection.y() + 1.0);
        return new Vector3(1.0, 1.0, 1.0).multiply(1.0 - t).add(new Vector3(0.5, 0.7, 1.0).multiply(t));
    }

    public void run() throws IOException
    {
        // Image
        final double aspectRatio = 16.0 / 9.0;
        final int imageWidth = 400;
        final int imageHeight = (int) (imageWidth / aspectRatio);

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

        Vector3[] pixelColors = new Vector3[imageWidth * imageHeight];
        AtomicInteger count = new AtomicInteger(imageHeight);

        IntStream.range(0, imageHeight).parallel().forEach(j ->
        {
            System.err.println("ScanLines remaining: " + count.decrementAndGet() + "");
            for (int i = 0; i < imageWidth; ++i)
            {
                double u = (double) i / (imageWidth - 1);
                double v = (double) j / (imageHeight - 1);
                Ray ray = new Ray(origin, lowerLeftCorner.add(horizontal.multiply(u)).add(vertical.multiply(v)).subtract(origin));
                Vector3 pixelColor = rayColor(ray);
                pixelColors[(imageHeight - j - 1) * imageWidth + i] = pixelColor;
            }
        });

        for (Vector3 pixelColor : pixelColors)
        {
            Color.writeColor(bufferedWriter, pixelColor);
        }

        bufferedWriter.close();
        System.err.println("Done");
    }

}
