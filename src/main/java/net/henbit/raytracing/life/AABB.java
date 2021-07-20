package net.henbit.raytracing.life;

public class AABB
{

    private Vector3 minimum;
    private Vector3 maximum;

    public AABB()
    {
    }

    public AABB(Vector3 minimum, Vector3 maximum)
    {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public static AABB surroundingBox(AABB box0, AABB box1)
    {
        Vector3 small = new Vector3(
                Math.min(box0.min().x(), box1.min().x()),
                Math.min(box0.min().y(), box1.min().y()),
                Math.min(box0.min().z(), box1.min().z()));

        Vector3 big = new Vector3(
                Math.max(box0.max().x(), box1.max().x()),
                Math.max(box0.max().y(), box1.max().y()),
                Math.max(box0.max().z(), box1.max().z()));

        return new AABB(small, big);
    }

    public boolean hit(final Ray ray, double tMin, double tMax)
    {
        for (int a = 0; a < 3; a++)
        {
            double invD = 1.0f / ray.direction().get(a);
            double t0 = (min().get(a) - ray.origin().get(a)) * invD;
            double t1 = (max().get(a) - ray.origin().get(a)) * invD;
            if (invD < 0.0f) // swap
            {
                double temp = t0;
                t0 = t1;
                t1 = temp;
            }
            tMin = Math.max(t0, tMin);
            tMax = Math.min(t1, tMax);
            if (tMax <= tMin)
                return false;
        }

        return true;
    }

    public Vector3 max()
    {
        return maximum;
    }

    public Vector3 min()
    {
        return minimum;
    }

    public void copy(AABB aabb)
    {
        this.maximum = aabb.maximum;
        this.minimum = aabb.minimum;
    }

}
