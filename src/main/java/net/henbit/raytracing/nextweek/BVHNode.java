package net.henbit.raytracing.nextweek;

import net.henbit.raytracing.nextweek.hittable.Hittable;
import net.henbit.raytracing.nextweek.hittable.HittableList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static net.henbit.raytracing.nextweek.AABB.surroundingBox;
import static net.henbit.raytracing.nextweek.RTNextWeek.randomInt;

public class BVHNode extends Hittable
{
    Comparator<Hittable> boxXCompare = (o1, o2) -> comparator(o1, o2, 0) ? 0 : 1;
    Comparator<Hittable> boxYCompare = (o1, o2) -> comparator(o1, o2, 1) ? 0 : 1;
    Comparator<Hittable> boxZCompare = (o1, o2) -> comparator(o1, o2, 2) ? 0 : 1;

    private Hittable left;
    private Hittable right;
    private AABB box;

    public BVHNode()
    {
    }

    public BVHNode(final HittableList list, double time0, double time1)
    {
        this(list.objects, 0, list.objects.size(), time0, time1);
    }

    public BVHNode(final List<Hittable> srcObjects, int start, int end, double time0, double time1)
    {
        List<Hittable> objects = new ArrayList<>(srcObjects);
        int axis = randomInt(0, 2);
        Comparator<Hittable> comparator = (axis == 0) ? boxXCompare
                : (axis == 1) ? boxYCompare
                : boxZCompare;

        int srcObjectSpan = end - start;

        if (srcObjectSpan == 1)
        {
            left = right = objects.get(start);
        }
        else if (srcObjectSpan == 2)
        {
            if (comparator.compare(objects.get(start), objects.get(start + 1)) == 0)
            {
                left = objects.get(start);
                right = objects.get(start + 1);
            }
            else
            {
                left = objects.get(start + 1);
                right = objects.get(start);
            }
        }
        else
        {
            objects.subList(start, end).sort(comparator);

            int mid = start + srcObjectSpan / 2;
            left = new BVHNode(objects, start, mid, time0, time1);
            right = new BVHNode(objects, mid, end, time0, time1);
        }

        AABB boxLeft = new AABB();
        AABB boxRight = new AABB();

        if (!left.boundingBox(time0, time1, boxLeft) || !right.boundingBox(time0, time1, boxRight))
            System.err.println("No bounding box in BVHNode constructor.");

        box = surroundingBox(boxLeft, boxRight);
    }

    @Override
    public boolean hit(Ray ray, double tMin, double tMax, HitRecord hitRecord)
    {
        if (!box.hit(ray, tMin, tMax))
            return false;

        boolean hitLeft = left.hit(ray, tMin, tMax, hitRecord);
        boolean hitRight = right.hit(ray, tMin, hitLeft ? hitRecord.t : tMax, hitRecord);

        return hitLeft || hitRight;
    }

    @Override
    public boolean boundingBox(double time0, double time1, AABB outputBox)
    {
        outputBox.copy(box);
        return true;
    }

    public boolean comparator(Hittable a, Hittable b, int axis)
    {
        AABB boxA = new AABB();
        AABB boxB = new AABB();

        if (!a.boundingBox(0, 0, boxA) || !b.boundingBox(0, 0, boxB))
            System.err.println("No bounding box in bvh_node constructor.");

        return boxA.min().e[axis] < boxB.min().e[axis];
    }

}
