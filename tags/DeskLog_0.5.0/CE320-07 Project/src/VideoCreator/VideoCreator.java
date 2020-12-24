package VideoCreator;

import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jcodec.common.io.FileChannelWrapper;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Rational;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/*class called in the main*/
public class VideoCreator {
    private final String VIDEO_OUTPUT_FILENAME;
    private final String SCREENSHOT_PATH;
    private final String IMAGE_EXTENSION;

    public VideoCreator(String videoOutputFilename, String screenshotPath, String imageExtension)
    {
        VIDEO_OUTPUT_FILENAME = videoOutputFilename;
        SCREENSHOT_PATH = screenshotPath;
        IMAGE_EXTENSION = imageExtension;

    }

    public void generateVideoBySequenceImages() {
        try {
            FileChannelWrapper outputLocation = NIOUtils.writableFileChannel(VIDEO_OUTPUT_FILENAME);

            AWTSequenceEncoder encoder = new AWTSequenceEncoder(outputLocation, Rational.R(25, 30));

            Path directoryPath = Paths.get(new File(SCREENSHOT_PATH).toURI());

            if (Files.isDirectory(directoryPath)) {
                DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath, "*." + IMAGE_EXTENSION);

                List<File> filesList = new ArrayList<File>();
                for (Path path : stream) {
                    filesList.add(path.toFile());
                }
                File[] files = new File[filesList.size()];
                filesList.toArray(files);

                sortByNumber(files);

                for (File img : files) {
                    System.out.println("Encoding image " + img.getName());
                    BufferedImage image = ImageIO.read(img);
                    // Encode the image
                    encoder.encodeImage(image);

                }
            }
           encoder.finish();
            System.out.println("Encoding complete");
        } catch (Exception e)
        {
            System.out.println("Could not export video!!");
        }
    }

    private static void sortByNumber(File[] files) {
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                int n1 = extractNumber(o1.getName());
                int n2 = extractNumber(o2.getName());
                return n1 - n2;
            }
            private int extractNumber(String name) {
                int i;
                try {
                    int s = name.lastIndexOf('_')+1;
                    int e = name.lastIndexOf('.');
                    String number = name.substring(s, e);
                    i = Integer.parseInt(number);
                } catch(Exception e) {
                    i = 0;
                }
                return i;
            }
        });

    }
}
