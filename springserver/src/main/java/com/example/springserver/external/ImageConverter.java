package com.example.springserver.external;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ImageConverter {

    public static File convertToWebP(File inputFile) throws IOException {
        BufferedImage image = ImageIO.read(inputFile);
        File outputFile = new File(inputFile.getParent(), "converted.webp");

        return outputFile;
    }
}
