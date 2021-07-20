package net.henbit.raytracing.life;

import static net.henbit.raytracing.life.RTLife.randomDouble;

public class Pi
{

    public static void main(String[] args)
    {
        int insideCircle = 0;
        int insideCircleStratified = 0;
        int sqrtN = 10000;
        for (int i = 0; i < sqrtN; i++)
        {
            for (int j = 0; j < sqrtN; j++)
            {
                double x = randomDouble(-1, 1);
                double y = randomDouble(-1, 1);
                if (x * x + y * y < 1)
                    insideCircle++;
                x = 2 * ((i + randomDouble()) / sqrtN) - 1;
                y = 2 * ((j + randomDouble()) / sqrtN) - 1;
                if (x * x + y * y < 1)
                    insideCircleStratified++;
            }
        }
        System.out.printf(
                "Regular Estimate of Pi = %.12f%n" +
                        "Stratified Estimate of Pi = %.12f%n",
                4 * (double) (insideCircle) / (sqrtN * sqrtN),
                4 * (double) (insideCircleStratified) / (sqrtN * sqrtN));
    }

}
