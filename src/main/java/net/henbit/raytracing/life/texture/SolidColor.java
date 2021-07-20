package net.henbit.raytracing.life.texture;

import net.henbit.raytracing.life.Vector3;

public class SolidColor extends Texture
{

    private final Vector3 colorValue;

    public SolidColor(Vector3 colorValue)
    {
        this.colorValue = colorValue;
    }

    public SolidColor(double red, double green, double blue)
    {
        this(new Vector3(red, green, blue));
    }

    @Override
    public Vector3 value(double u, double v, Vector3 point)
    {
        return colorValue;
    }

}
