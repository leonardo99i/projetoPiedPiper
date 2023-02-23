package compressor;

import java.io.*;
import java.net.*;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class servidor {

    public static void main(String[] args) {
        int porta = 5000;
        ServerSocket serverSocket;
        Socket socket;
        byte[] buffer = new byte[1024];

        try {
            serverSocket = new ServerSocket(porta);
            System.out.println("Servidor iniciado na porta " + porta);
            while (true) {
                socket = serverSocket.accept();
                System.out.println("Conexão estabelecida com " + socket.getInetAddress().getHostAddress());

                InputStream inputStream = socket.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }

                byte[] fileData = byteArrayOutputStream.toByteArray();
                byte[] compressedData = compressData(fileData);
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(compressedData);

                byteArrayOutputStream.close();
                outputStream.close();
                inputStream.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] compressData(byte[] data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, deflater);
        deflaterOutputStream.write(data);
        deflaterOutputStream.finish();
        deflaterOutputStream.flush();
        deflaterOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
