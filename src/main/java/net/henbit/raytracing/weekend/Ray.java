package net.henbit.raytracing.weekend;

public class Ray
{

    private Vector3 origin;
    private Vector3 direction;

    public Ray()
    {
    }

    public Ray(final Vector3 origin, final Vector3 direction)
    {
        this.origin = origin;
        this.direction = direction;
    }

    public Vector3 origin()
    {
        return origin;
    }

    public Vector3 direction()
    {
        return direction;
    }

    public Vector3 at(double t)
    {
        return origin.add(direction.multiply(t));
    }

    public void copy(Ray ray)
    {
        this.origin = ray.origin;
        this.direction = ray.direction;
    }
}
