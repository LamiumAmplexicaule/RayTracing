package net.henbit.raytracing.weekend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@SuppressWarnings("unused")
public class PPM
{

    private final String magic;
    private final int width;
    private final int height;
    private final int max;
    private final int[] data;

    public PPM(String magic, int width, int height, int max, int[] data)
    {
        this.magic = magic;
        this.width = width;
        this.height = height;
        this.max = max;
        this.data = data;
    }

    public static PPM load(String path) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        String magic = bufferedReader.readLine();
        if (!magic.equals("P3"))
            throw new IOException("Not supported.");

        String[] format = bufferedReader.readLine().split("[\\s]");
        int width = Integer.parseInt(format[0]);
        int height = Integer.parseInt(format[1]);
        int max = Integer.parseInt(bufferedReader.readLine());
        int[] data = new int[width * height * 3];

        String line;
        int index = 0;
        while ((line = bufferedReader.readLine()) != null)
        {
            String[] pixel = line.split("[\\s]");
            data[index] = Integer.parseInt(pixel[0]);
            data[index + 1] = Integer.parseInt(pixel[1]);
            data[index + 2] = Integer.parseInt(pixel[2]);
            index += 3;
        }

        return new PPM(magic, width, height, max, data);
    }

    public String getMagic()
    {
        return magic;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getMax()
    {
        return max;
    }

    public int[] getData()
    {
        return data;
    }
}