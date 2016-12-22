package com.mmtou.tool.compress;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 压缩组件
 * 
 * @author mmtou
 *
 */
@SuppressWarnings("restriction")
public class CompressUtils {

  private static final String COMPRESS_PATH = "/Users/mmtou/";

  /**
   * 压缩图片
   * 
   * @param sourceFilePath 源文件路径
   * @param quality 压缩质量（0<=quality<=1）
   * @param width 压缩后的宽
   * @param height 压缩后的高
   * @throws IOException
   * @throws ParamErrorException
   */
  public static void compressImg(String sourceFilePath, Float quality, Integer width,
      Integer height) throws IOException, ParamErrorException {
    // 参数校验
    if (sourceFilePath == null || sourceFilePath.isEmpty()) {
      throw new ParamErrorException("sourceFilePath为null");
    }

    compressImg(new File(sourceFilePath), quality, width, height);
  }

  /**
   * 压缩图片
   * 
   * @param sourceFile 源文件
   * @param quality 压缩质量（0<=quality<=1）
   * @param width 压缩后的宽
   * @param height 压缩后的高
   * @throws IOException
   * @throws ParamErrorException
   */
  public static void compressImg(File sourceFile, Float quality, Integer width, Integer height)
      throws IOException, ParamErrorException {
    // 参数校验
    if (sourceFile == null) {
      throw new ParamErrorException("sourceFile为null");
    }

    // 文件后缀名
    String[] tempFilePaths = sourceFile.getName().split("\\.");
    String formatName = tempFilePaths[tempFilePaths.length - 1];

    compressImg(new FileInputStream(sourceFile), formatName, quality, width, height);
  }

  /**
   * 压缩图片
   * 
   * @param srouceInput 源文件流
   * @param formatName
   * @param quality 压缩质量（0<=quality<=1）
   * @param width 压缩后的宽
   * @param height 压缩后的高
   * @throws IOException
   * @throws ParamErrorException
   */
  public static void compressImg(InputStream srouceInput, String formatName, Float quality,
      Integer width, Integer height) throws IOException, ParamErrorException {
    // 参数校验
    if (srouceInput == null) {
      throw new ParamErrorException("input为null");
    }
    if (formatName == null || formatName.isEmpty()) {
      throw new ParamErrorException("formatName为null");
    }
    if (quality != null && (quality < 0 || quality > 1)) {
      throw new ParamErrorException("quality必须>=0 && <=1");
    }

    // 源文件
    BufferedImage sourceImage = ImageIO.read(srouceInput);

    // 压缩后的所在路径及名称
    File compressedImageFile =
        new File(COMPRESS_PATH + System.currentTimeMillis() + "." + formatName);

    FileOutputStream out = null;

    try {
      // 缩放
      if (width != null && height != null) {

        boolean preserveAlpha = true;

        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage newImg = new BufferedImage(width, height, imageType);
        Graphics2D g = newImg.createGraphics();
        if (preserveAlpha) {
          g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(sourceImage, 0, 0, width, height, null);
        g.dispose();

        out = new FileOutputStream(compressedImageFile);
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(newImg);

        // 把缩放后的newImg赋给sourceImg
        sourceImage = newImg;
      }

      // 压缩
      if (quality != null) {
        ImageOutputStream ios = null;
        ImageWriter writer = null;

        try {
          Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(formatName);
          writer = (ImageWriter) writers.next();

          ios = ImageIO.createImageOutputStream(out);
          writer.setOutput(ios);

          ImageWriteParam param = writer.getDefaultWriteParam();

          param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
          param.setCompressionQuality(quality);
          writer.write(null, new IIOImage(sourceImage, null, null), param);
        } finally {
          // close
          if (ios != null) {
            ios.close();
          }
          if (writer != null) {
            writer.dispose();
          }
        }
      }
    } finally {
      if (out != null) {
        out.close();
      }
    }
  }

  public static void main(String[] args) {
    try {
      // compressImg("/Users/mmtou/Pictures/rice-fields-nature-wallpaper.jpg", 0.1f, 100, 100);

      // compressImg(new File("/Users/mmtou/Pictures/rice-fields-nature-wallpaper.jpg"), 0.1f, 100,
      // 100);

      FileInputStream sourceInput =
          new FileInputStream(new File("/Users/mmtou/Pictures/rice-fields-nature-wallpaper.jpg"));
      compressImg(sourceInput, "jpg", 0.1f, 100, 100);
      sourceInput.close();

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ParamErrorException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
