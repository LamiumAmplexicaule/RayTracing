package net.henbit.raytracing.life.pdf;

import net.henbit.raytracing.life.Vector3;
import net.henbit.raytracing.life.hittable.Hittable;

public class HittablePDF extends PDF
{

    private final Vector3 origin;
    private final Hittable ptr;

    public HittablePDF(final Hittable ptr, final Vector3 origin)
    {
        this.origin = origin;
        this.ptr = ptr;
    }

    @Override
    public double value(Vector3 direction)
    {
        return ptr.pdfValue(origin, direction);
    }

    @Override
    public Vector3 generate()
    {
        return ptr.random(origin);
    }

}
