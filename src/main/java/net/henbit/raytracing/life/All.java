package net.henbit.raytracing.life;

import net.henbit.raytracing.Utils;
import net.henbit.raytracing.life.chapter10.Chapter10;
import net.henbit.raytracing.life.chapter12.Chapter12;
import net.henbit.raytracing.life.chapter6.Chapter6;
import net.henbit.raytracing.life.chapter9.Chapter9;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static net.henbit.raytracing.life.RTLife.IMAGE_PATH;

public class All
{

    public static void main(String[] args) throws IOException
    {
        final Chapter6 chapter6 = new Chapter6();
        final Chapter9 chapter9 = new Chapter9();
        final Chapter10 chapter10 = new Chapter10();
        final Chapter12 chapter12 = new Chapter12();
        System.out.println("Chapter6");
        chapter6.run();
        System.out.println("Chapter9");
        chapter9.run();
        System.out.println("Chapter10");
        chapter10.run();
        System.out.println("Chapter12");
        chapter12.run();

        ppm2png();
    }

    private static void ppm2png() throws IOException
    {
        int[] chapters = {6, 9, 10, 12};
        for (int chapter : chapters)
        {
            BufferedImage ppm = Utils.loadPPM(IMAGE_PATH + "life-chapter" + chapter + ".ppm");
            ImageIO.write(ppm, "png", new File(IMAGE_PATH + "life-chapter" + chapter + ".png"));
        }
    }

}
