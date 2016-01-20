package org.n3r.sandbox.upload;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MultipartFileUploader {
    String boundary;
    HttpURLConnection httpConn;
    OutputStream out;

    public MultipartFileUploader(String requestURL) throws IOException {
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();

        boundary = "---" + System.currentTimeMillis() + "---"; // 定义数据分隔线

        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        httpConn.setUseCaches(false);
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        out = new DataOutputStream(httpConn.getOutputStream());
    }

    /**
     * Adds a header field to the request.
     *
     * @param name  - name of the header field
     * @param value - value of the header field
     */
    public void addHeader(String name, String value) throws IOException {
        out.write((name + ": " + value + "\r\n").getBytes());
    }

    public void addFilePart(File file) throws IOException {
        addFilePart(file.getName(), new FileInputStream(file));
    }

    public void addFilePart(String fileName, InputStream inputStream) throws IOException {
        String preparePart = preparePart(fileName);
        out.write(preparePart.getBytes());

        copyStream(inputStream);

        out.write("\r\n".getBytes()); //多个文件时，二个文件之间加入这个
    }

    private String preparePart(String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("\r\n");

        sb.append("Content-Disposition: form-data;name=\"").append(fileName)
                .append("\";filename=\"").append(fileName).append("\"\r\n");
        String contentType = URLConnection.guessContentTypeFromName(fileName);
        //sb.append("Content-Type:application/octet-stream\r\n\r\n");
        sb.append("Content-Type:").append(contentType).append("\r\n\r\n");
        return sb.toString();
    }

    private void copyStream(InputStream is) throws IOException {
        DataInputStream in = new DataInputStream(is);

        try {
            int bytes;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
        } finally {
            in.close();
            is.close();
        }
    }

    public String finish() throws IOException {
        byte[] end_data = ("\r\n--" + boundary + "--\r\n").getBytes();// 定义最后数据分隔线
        out.write(end_data);
        out.flush();
        out.close();

        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = httpConn.getInputStream();
            String response = readInputStreamToString(inputStream);
            httpConn.disconnect();

            return response.toString();
        }

        throw new IOException("Server returned non-OK status: " + status);
    }

    private String readInputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder response = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } finally {
            if (reader != null) reader.close();
        }

        return response.toString();
    }

    public static void main(String[] args) throws IOException {
        MultipartFileUploader multipartFileUploader
                = new MultipartFileUploader("http://a.b.d:8001/upload?maxSize=10M&suffix=jpg|gif&path=images");
        multipartFileUploader.addHeader("User-Agent", "CodeJava");
        multipartFileUploader.addFilePart(new File("/Users/bingoohuang/Desktop/3.jpg"));
        multipartFileUploader.addFilePart(new File("/Users/bingoohuang/Desktop/4.jpg"));

        String finish = multipartFileUploader.finish();
        System.out.println(finish);
    }

}