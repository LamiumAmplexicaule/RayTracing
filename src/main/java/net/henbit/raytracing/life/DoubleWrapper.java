package net.henbit.raytracing.life;

public class DoubleWrapper
{

    private double value;

    public DoubleWrapper()
    {
        value = 0;
    }

    public void copy(double value)
    {
        this.value = value;
    }

    public double getValue()
    {
        return value;
    }

}
