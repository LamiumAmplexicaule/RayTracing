package net.henbit.raytracing.life.texture;

import net.henbit.raytracing.life.Vector3;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static net.henbit.raytracing.life.RTLife.clamp;

public class ImageTexture extends Texture
{

    public static final int BYTES_BY_PIXEL = 3;
    private final int width;
    private final int height;
    private final int bytesPerScanline;
    private BufferedImage data;

    public ImageTexture(String filename)
    {
        try
        {
            this.data = ImageIO.read(new File(filename));
        } catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        this.width = data.getWidth();
        this.height = data.getHeight();
        this.bytesPerScanline = BYTES_BY_PIXEL * this.width;
    }

    public ImageTexture()
    {
        this.data = null;
        this.width = 0;
        this.height = 0;
        this.bytesPerScanline = 0;
    }

    @Override
    public Vector3 value(double u, double v, Vector3 point)
    {
        // If we have no texture data, then return solid cyan as a debugging aid.
        if (data == null)
            return new Vector3(0, 1, 1);

        // Clamp input texture coordinates to [0,1] x [1,0]
        u = clamp(u, 0.0, 1.0);
        v = 1.0 - clamp(v, 0.0, 1.0);  // Flip V to image coordinates

        int i = (int) (u * width);
        int j = (int) (v * height);

        // Clamp integer mapping, since actual coordinates should be less than 1.0
        if (i >= width) i = width - 1;
        if (j >= height) j = height - 1;

        final double colorScale = 1.0 / 255.0;
        int rgb = data.getRGB(i, j);
        int[] pixel = {(rgb >> 16) & (0xFF), (rgb >> 8) & (0xFF), (rgb) & (0xFF)};

        return new Vector3(colorScale * pixel[0], colorScale * pixel[1], colorScale * pixel[2]);
    }
}
