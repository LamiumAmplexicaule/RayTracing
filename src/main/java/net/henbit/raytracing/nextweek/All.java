package net.henbit.raytracing.nextweek;

import net.henbit.raytracing.Utils;
import net.henbit.raytracing.nextweek.chapter10.Chapter10;
import net.henbit.raytracing.nextweek.chapter2.Chapter2;
import net.henbit.raytracing.nextweek.chapter4.Chapter4;
import net.henbit.raytracing.nextweek.chapter5.Chapter5;
import net.henbit.raytracing.nextweek.chapter6.Chapter6;
import net.henbit.raytracing.nextweek.chapter7.Chapter7;
import net.henbit.raytracing.nextweek.chapter8.Chapter8;
import net.henbit.raytracing.nextweek.chapter9.Chapter9;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static net.henbit.raytracing.nextweek.RTNextWeek.IMAGE_PATH;

public class All
{

    public static void main(String[] args) throws IOException
    {
        final Chapter2 chapter2 = new Chapter2();
        final Chapter4 chapter4 = new Chapter4();
        final Chapter5 chapter5 = new Chapter5();
        final Chapter6 chapter6 = new Chapter6();
        final Chapter7 chapter7 = new Chapter7();
        final Chapter8 chapter8 = new Chapter8();
        final Chapter9 chapter9 = new Chapter9();
        final Chapter10 chapter10 = new Chapter10();
        System.out.println("Chapter2");
        chapter2.run();
        System.out.println("Chapter4");
        chapter4.run();
        System.out.println("Chapter5");
        chapter5.run();
        System.out.println("Chapter6");
        chapter6.run();
        System.out.println("Chapter7");
        chapter7.run();
        System.out.println("Chapter8");
        chapter8.run();
        System.out.println("Chapter9");
        chapter9.run();
        System.out.println("Chapter10");
        chapter10.run();

        ppm2png();
    }

    private static void ppm2png() throws IOException
    {
        for (int i = 2; i < 11; i++)
        {
            if (i == 3) continue;
            BufferedImage ppm = Utils.loadPPM(IMAGE_PATH + "nextweek-chapter" + i + ".ppm");
            ImageIO.write(ppm, "png", new File(IMAGE_PATH + "nextweek-chapter" + i + ".png"));
        }
    }

}
