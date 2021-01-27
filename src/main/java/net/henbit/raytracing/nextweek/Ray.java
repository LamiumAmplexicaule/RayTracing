package net.henbit.raytracing.nextweek;

public class Ray
{

    private Vector3 origin;
    private Vector3 direction;
    private double time;

    public Ray()
    {
    }

    public Ray(final Vector3 origin, final Vector3 direction)
    {
        this.origin = origin;
        this.direction = direction;
        this.time = 0.0;
    }

    public Ray(final Vector3 origin, final Vector3 direction, final double time)
    {
        this.origin = origin;
        this.direction = direction;
        this.time = time;
    }

    public Vector3 origin()
    {
        return origin;
    }

    public Vector3 direction()
    {
        return direction;
    }

    public double time()
    {
        return time;
    }

    public Vector3 at(double t)
    {
        return origin.add(direction.multiply(t));
    }

    public void copy(Ray ray)
    {
        this.origin = ray.origin;
        this.direction = ray.direction;
        this.time = ray.time;
    }

}
