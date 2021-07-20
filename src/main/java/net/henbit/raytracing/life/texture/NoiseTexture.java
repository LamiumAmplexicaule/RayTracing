package net.henbit.raytracing.life.texture;

import net.henbit.raytracing.life.Perlin;
import net.henbit.raytracing.life.Vector3;

import static java.lang.Math.sin;

public class NoiseTexture extends Texture
{

    private final Perlin noise;
    private final double scale;

    public NoiseTexture()
    {
        this.noise = new Perlin();
        this.scale = 1;
    }

    public NoiseTexture(double scale)
    {
        this.noise = new Perlin();
        this.scale = scale;
    }

    @Override
    public Vector3 value(double u, double v, Vector3 point)
    {
        return new Vector3(1, 1, 1).multiply(0.5).multiply(1 + sin(scale * point.z() + 10 * noise.turb(point)));
    }

}
