import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/*
* Build an application that downloads part of a file from a web server, in chunks.
*/

public class Main {

    private static long CHUNK_SIZE = 1049000;  //1Mib == 1,049,000 bytes
    private static int TOTAL_SIZE = 4;

    public static void main(String[] args) {
        try {
            // Use the current source code directory as a default output directory if a user doesn't specify one
            File currentDirectory = new File(new File(".").getAbsolutePath());
            String s = currentDirectory.getCanonicalPath();
            if (args.length > 0 ) {
                if (args.length == 1) {
                    URL url = new URL(args[0]);
                    String targetFileName = s + "/"+FilenameUtils.getName(url.getPath());
                    customDownload(args[0], targetFileName);
                } else  if (args.length >= 2) {
                    customDownload(args[0], args[1]);
                }
            } else {
                System.out.println("Enter a remote source url, please");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Returns an string array that can then be used in the http header ranges
     * The chunkSize argument and totalSize must specify an positive integer.
     * <p>
     * This method always returns immediately
     * @param  chunkSize  the size of each chunk
     * @param  totalSize total hunks
     * @return      the string array of header ranges
     */
    private static String[] calculateHeaderRanges(long chunkSize, int totalSize){
        List<String> ranges = new ArrayList<>();
        for(int i = 0; i<totalSize*chunkSize; i += chunkSize){
            if (i == 0) {
                ranges.add("bytes= " + 0 + "-" + (i + chunkSize));
            }
            else {
                ranges.add("bytes= " + (i + 1) + "-" + (i + chunkSize));
            }
        }
        return ranges.toArray(new String[ranges.size()]);
    }

    /**
     * Used to download a remote file based on header ranges
     * The downloadUrl argument needs to be specify and output file name is optional.
     * <p>
     * This method is executed synchronously
     * @param  downloadUrl  the source url
     * @param  outputFileName the file path to download a file to
     */
    private static void customDownload(String downloadUrl, String outputFileName) {
        File file = new File(outputFileName);
        FileOutputStream out;

        String[] rangesArray = calculateHeaderRanges(CHUNK_SIZE, TOTAL_SIZE);

        try {
            out = new FileOutputStream(file);
            HttpClient client = HttpClients.custom().build();
            HttpUriRequest request;

            for(int i= 0; i < TOTAL_SIZE; i++) {
                System.out.println(rangesArray[i]);
                request = RequestBuilder.get()
                        .setUri(downloadUrl)
                        .setHeader(HttpHeaders.RANGE, rangesArray[i])
                        .build();
                HttpResponse response;
                try {
                    response  = client.execute(request);
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream in = entity.getContent();

                        int read = -1;
                        byte[] buffer = new byte[4096];
                        while ((read = in.read(buffer)) != -1) {
                            out.write(buffer, 0, read);
                        }
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            out.close();
            System.out.println("Download finished");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
