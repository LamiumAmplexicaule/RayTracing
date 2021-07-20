package net.henbit.raytracing.life.hittable;

import net.henbit.raytracing.life.AABB;
import net.henbit.raytracing.life.HitRecord;
import net.henbit.raytracing.life.Ray;
import net.henbit.raytracing.life.Vector3;

import java.util.ArrayList;
import java.util.List;

import static net.henbit.raytracing.life.AABB.surroundingBox;
import static net.henbit.raytracing.life.RTLife.randomInt;

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
            } else
            {
                outputBox.copy(surroundingBox(outputBox, tempBox));
            }
            firstBox = false;
        }

        return true;
    }

    @Override
    public double pdfValue(Vector3 origin, Vector3 v)
    {
        double weight = 1.0 / objects.size();
        double sum = 0.0;

        for (Hittable object : objects)
            sum += weight * object.pdfValue(origin, v);

        return sum;
    }

    @Override
    public Vector3 random(Vector3 origin)
    {
        int size = objects.size();
        return objects.get(randomInt(0, size - 1)).random(origin);
    }

}
