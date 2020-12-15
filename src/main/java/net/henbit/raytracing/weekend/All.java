package net.henbit.raytracing.weekend;

import net.henbit.raytracing.Utils;
import net.henbit.raytracing.weekend.chapter10.Chapter10;
import net.henbit.raytracing.weekend.chapter11.Chapter11;
import net.henbit.raytracing.weekend.chapter12.Chapter12;
import net.henbit.raytracing.weekend.chapter13.Chapter13;
import net.henbit.raytracing.weekend.chapter2.Chapter2;
import net.henbit.raytracing.weekend.chapter3.Chapter3;
import net.henbit.raytracing.weekend.chapter4.Chapter4;
import net.henbit.raytracing.weekend.chapter5.Chapter5;
import net.henbit.raytracing.weekend.chapter6.Chapter6;
import net.henbit.raytracing.weekend.chapter7.Chapter7;
import net.henbit.raytracing.weekend.chapter8.Chapter8;
import net.henbit.raytracing.weekend.chapter9.Chapter9;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static net.henbit.raytracing.weekend.RTWeekend.IMAGE_PATH;

public class All
{

    public static void main(String[] args) throws IOException
    {
        final Chapter2 chapter2 = new Chapter2();
        final Chapter3 chapter3 = new Chapter3();
        final Chapter4 chapter4 = new Chapter4();
        final Chapter5 chapter5 = new Chapter5();
        final Chapter6 chapter6 = new Chapter6();
        final Chapter7 chapter7 = new Chapter7();
        final Chapter8 chapter8 = new Chapter8();
        final Chapter9 chapter9 = new Chapter9();
        final Chapter10 chapter10 = new Chapter10();
        final Chapter11 chapter11 = new Chapter11();
        final Chapter12 chapter12 = new Chapter12();
        final Chapter13 chapter13 = new Chapter13();
        System.out.println("Chapter2");
        chapter2.run();
        System.out.println("Chapter3");
        chapter3.run();
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
        System.out.println("Chapter11");
        chapter11.run();
        System.out.println("Chapter12");
        chapter12.run();
        System.out.println("Chapter13");
        chapter13.run();

        ppm2png();
    }

    private static void ppm2png() throws IOException
    {
        for (int i = 2; i < 14; i++)
        {
            BufferedImage ppm = Utils.loadPPM(IMAGE_PATH + "weekend-chapter" + i + ".ppm");
            ImageIO.write(ppm, "png", new File(IMAGE_PATH + "weekend-chapter" + i + ".png"));
        }
    }

}
