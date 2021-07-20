package net.henbit.raytracing.life.pdf;

import net.henbit.raytracing.life.Vector3;

import static net.henbit.raytracing.life.RTLife.randomDouble;

public class MixturePDF extends PDF
{

    private final PDF[] p = new PDF[2];

    public MixturePDF(final PDF p0, final PDF p1)
    {
        p[0] = p0;
        p[1] = p1;
    }

    @Override
    public double value(Vector3 direction)
    {
        return 0.5 * p[0].value(direction) + 0.5 * p[1].value(direction);
    }

    @Override
    public Vector3 generate()
    {
        if (randomDouble() < 0.5)
            return p[0].generate();
        else
            return p[1].generate();
    }

}
