package net.henbit.raytracing.life;

import net.henbit.raytracing.life.pdf.PDF;

public class ScatterRecord
{

    public Ray specularRay;
    public boolean isSpecular;
    public Vector3 attenuation;
    public PDF pdf;

    public ScatterRecord()
    {
        specularRay = new Ray();
        isSpecular = false;
        attenuation = new Vector3();
        pdf = null;
    }

}
