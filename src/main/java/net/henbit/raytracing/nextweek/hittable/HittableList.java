package net.henbit.raytracing.nextweek.hittable;

import net.henbit.raytracing.nextweek.AABB;
import net.henbit.raytracing.nextweek.HitRecord;
import net.henbit.raytracing.nextweek.Ray;

import java.util.ArrayList;
import java.util.List;

import static net.henbit.raytracing.nextweek.AABB.surroundingBox;

@SuppressWarnings({"DuplicatedCode", "unused"})
public class HittableList extends Hittable
{

    public final List<Hittable> objects = new ArrayList<>();

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

    @Override
    public boolean boundingBox(double time0, double time1, AABB outputBox)
    {
        if (objects.isEmpty()) return false;

        AABB tempBox = new AABB();
        boolean firstBox = true;

        for (final Hittable object : objects)
        {
            if (!object.boundingBox(time0, time1, tempBox)) return false;
            if (firstBox)
            {
                outputBox.copy(tempBox);
            }
            else
            {
                outputBox.copy(surroundingBox(outputBox, tempBox));
            }
            firstBox = false;
        }

        return true;
    }
}
