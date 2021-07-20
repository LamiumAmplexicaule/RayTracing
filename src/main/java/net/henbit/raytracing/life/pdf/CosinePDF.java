package net.henbit.raytracing.life.pdf;

import net.henbit.raytracing.life.ONB;
import net.henbit.raytracing.life.Vector3;

import static java.lang.Math.*;
import static net.henbit.raytracing.life.RTLife.PI;
import static net.henbit.raytracing.life.RTLife.randomDouble;
import static net.henbit.raytracing.life.Vector3.dot;
import static net.henbit.raytracing.life.Vector3.unitVector;

public class CosinePDF extends PDF
{

    private final ONB uvw = new ONB();

    public CosinePDF(Vector3 w)
    {
        uvw.buildFromW(w);
    }

    @Override
    public double value(Vector3 direction)
    {
        double cosine = dot(unitVector(direction), uvw.w());
        return (cosine <= 0) ? 0 : cosine / PI;
    }

    @Override
    public Vector3 generate()
    {
        return uvw.local(randomCosineDirection());
    }

    static Vector3 randomCosineDirection()
    {
        double r1 = randomDouble();
        double r2 = randomDouble();
        double z = sqrt(1 - r2);

        double phi = 2 * PI * r1;
        double x = cos(phi) * sqrt(r2);
        double y = sin(phi) * sqrt(r2);

        return new Vector3(x, y, z);
    }

}
