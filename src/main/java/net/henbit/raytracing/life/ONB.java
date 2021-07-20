package net.henbit.raytracing.life;

import static java.lang.Math.abs;
import static net.henbit.raytracing.life.Vector3.cross;
import static net.henbit.raytracing.life.Vector3.unitVector;

public class ONB
{

    private final Vector3[] axis = new Vector3[3];

    public void buildFromW(Vector3 n)
    {
        axis[2] = unitVector(n);
        Vector3 a = (abs(w().x()) > 0.9) ? new Vector3(0, 1, 0) : new Vector3(1, 0, 0);
        axis[1] = unitVector(cross(w(), a));
        axis[0] = cross(w(), v());
    }

    public Vector3 u()
    {
        return axis[0];
    }

    public Vector3 v()
    {
        return axis[1];
    }

    public Vector3 w()
    {
        return axis[2];
    }

    public Vector3 local(double a, double b, double c)
    {
        return u().multiply(a).add(v().multiply(b)).add(w().multiply(c));
    }

    public Vector3 local(Vector3 a)
    {
        return u().multiply(a.x()).add(v().multiply(a.y())).add(w().multiply(a.z()));
    }

}
