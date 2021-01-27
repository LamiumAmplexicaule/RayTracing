package net.henbit.raytracing.nextweek.texture;

import net.henbit.raytracing.nextweek.Vector3;

public abstract class Texture
{

    public abstract Vector3 value(double u, double v, final Vector3 point);

}
