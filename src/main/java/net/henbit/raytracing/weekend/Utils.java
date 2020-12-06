package net.henbit.raytracing.weekend;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.IOException;

public class Utils
{

    public static BufferedImage loadPPM(String path) throws IOException
    {
        PPM ppm = PPM.load(path);
        BufferedImage bufferedImage = new BufferedImage(ppm.getWidth(), ppm.getHeight(), BufferedImage.TYPE_INT_RGB);
        DataBuffer dataBuffer = bufferedImage.getRaster().getDataBuffer();

        for (int y = 0; y < ppm.getHeight(); y++)
        {
            for (int x = 0; x < ppm.getWidth(); x++)
            {
                int pixelIndex = y * ppm.getWidth() + x;
                int dataIndex = pixelIndex * 3;
                int r = ppm.getData()[dataIndex], g = ppm.getData()[dataIndex + 1], b = ppm.getData()[dataIndex + 2];
                int value = ((0xFF) << 24) |
                        ((r & 0xFF) << 16) |
                        ((g & 0xFF) << 8) |
                        ((b & 0xFF));
                dataBuffer.setElem(pixelIndex, value);
            }
        }

        return bufferedImage;
    }

}
