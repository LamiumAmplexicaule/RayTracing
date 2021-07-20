package net.henbit.raytracing.life.texture;

import net.henbit.raytracing.life.Vector3;

public abstract class Texture
{

    public abstract Vector3 value(double u, double v, final Vector3 point);

}
