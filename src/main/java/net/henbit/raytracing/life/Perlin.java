package net.henbit.raytracing.life;

import static java.lang.Math.abs;
import static java.lang.Math.floor;
import static net.henbit.raytracing.life.RTLife.randomInt;
import static net.henbit.raytracing.life.Vector3.*;

public class Perlin
{

    private static final int POINT_COUNT = 256;
    private final Vector3[] randomVectors;
    private final int[] permX;
    private final int[] permY;
    private final int[] permZ;

    public Perlin()
    {
        this.randomVectors = new Vector3[POINT_COUNT];
        for (int i = 0; i < POINT_COUNT; ++i)
        {
            this.randomVectors[i] = unitVector(random(-1, 1));
        }

        this.permX = perlinGeneratePerm();
        this.permY = perlinGeneratePerm();
        this.permZ = perlinGeneratePerm();
    }

    private static double triLinearInterpolation(double[][][] c, double u, double v, double w)
    {
        double accum = 0.0;
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                for (int k = 0; k < 2; k++)
                    accum += (i * u + (1 - i) * (1 - u)) *
                            (j * v + (1 - j) * (1 - v)) *
                            (k * w + (1 - k) * (1 - w)) * c[i][j][k];

        return accum;
    }

    private static double perlinInterpolation(Vector3[][][] c, double u, double v, double w)
    {
        double uu = u * u * (3 - 2 * u);
        double vv = v * v * (3 - 2 * v);
        double ww = w * w * (3 - 2 * w);
        double accum = 0.0;

        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                for (int k = 0; k < 2; k++)
                {
                    Vector3 weightV = new Vector3(u - i, v - j, w - k);
                    accum += (i * uu + (1 - i) * (1 - uu))
                            * (j * vv + (1 - j) * (1 - vv))
                            * (k * ww + (1 - k) * (1 - ww))
                            * dot(c[i][j][k], weightV);
                }

        return accum;
    }

    public double turb(final Vector3 point)
    {
        return turb(point, 7);
    }

    public double turb(final Vector3 point, final int depth)
    {
        double accum = 0.0;
        Vector3 temp = new Vector3();
        temp.copy(point);
        double weight = 1.0;

        for (int i = 0; i < depth; i++)
        {
            accum += weight * noise(temp);
            weight *= 0.5;
            temp = temp.multiply(2);
        }

        return abs(accum);
    }

    public double noise(final Vector3 point)
    {
        double u = point.x() - floor(point.x());
        double v = point.y() - floor(point.y());
        double w = point.z() - floor(point.z());
        u = u * u * (3 - 2 * u);
        v = v * v * (3 - 2 * v);
        w = w * w * (3 - 2 * w);

        int i = (int) (floor(point.x()));
        int j = (int) (floor(point.y()));
        int k = (int) (floor(point.z()));
        Vector3[][][] c = new Vector3[2][2][2];

        for (int di = 0; di < 2; di++)
            for (int dj = 0; dj < 2; dj++)
                for (int dk = 0; dk < 2; dk++)
                    c[di][dj][dk] = randomVectors[
                            permX[(i + di) & 255] ^ permY[(j + dj) & 255] ^ permZ[(k + dk) & 255]
                            ];

        return perlinInterpolation(c, u, v, w);
    }

    private int[] perlinGeneratePerm()
    {
        int[] p = new int[POINT_COUNT];

        for (int i = 0; i < POINT_COUNT; ++i)
            p[i] = i;

        permute(p, POINT_COUNT);

        return p;
    }

    private void permute(int[] p, int n)
    {
        for (int i = n - 1; i > 0; i--)
        {
            int target = randomInt(0, i);
            int tmp = p[i];
            p[i] = p[target];
            p[target] = tmp;
        }
    }

}
