package net.henbit.raytracing.weekend;

import java.util.ArrayList;
import java.util.List;

public class HittableList extends Hittable
{

    private final List<Hittable> objects = new ArrayList<>();

    public HittableList()
    {
    }

    public HittableList(final Hittable object)
    {
        add(object);
    }

    public void add(final Hittable object)
    {
        objects.add(object);
    }

    public void clear()
    {
        objects.clear();
    }

    @Override
    public boolean hit(final Ray ray, final double tMin, final double tMax, final HitRecord hitRecord)
    {
        HitRecord temp = new HitRecord();
        boolean hitAnything = false;
        double closestSoFar = tMax;

        for (final Hittable object : objects)
        {
            if (object.hit(ray, tMin, closestSoFar, temp))
            {
                hitAnything = true;
                closestSoFar = temp.t;
                hitRecord.copy(temp);
            }
        }

        return hitAnything;
    }
}
