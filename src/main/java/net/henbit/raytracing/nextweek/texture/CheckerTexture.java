package net.henbit.raytracing.nextweek.texture;

import net.henbit.raytracing.nextweek.Vector3;

import static java.lang.Math.sin;

public class CheckerTexture extends Texture
{

    private final Texture odd;
    private final Texture even;

    public CheckerTexture(final Texture odd, final Texture even)
    {
        this.odd = odd;
        this.even = even;
    }

    public CheckerTexture(final Vector3 odd, final Vector3 even)
    {
        this.odd = new SolidColor(odd);
        this.even = new SolidColor(even);
    }

    @Override
    public Vector3 value(double u, double v, Vector3 point)
    {
        double sines = sin(10 * point.x()) * sin(10 * point.y()) * sin(10 * point.z());
        if (sines < 0)
            return odd.value(u, v, point);
        else
            return even.value(u, v, point);
    }

}
