package utb.fai;

import java.net.*;
import java.io.*;

public class EmailSender {
    private Socket socket;
    private String message;
    private InputStream in;
    private OutputStream out;

    /*
     * Constructor opens Socket to host/port. If the Socket throws an exception
     * during opening,
     * the exception is not handled in the constructor.
     */
    public EmailSender(String host, int port) throws UnknownHostException, IOException {
        this.socket = new Socket(host, port);
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
    }

    /*
     * Sends email from an email address to an email address with some subject and
     * text.
     * If the Socket throws an exception during sending, the exception is not
     * handled by this method.
     */
    public void send(String from, String to, String subject, String text) throws IOException {
        final byte[] buffer = new byte[256];
        int len;
        try {
            if (in.available() > 0) {
                len = in.read(buffer, 0, 256);
                System.out.write(buffer, 0, len);
            }

            message = "MAIL FROM:<" + from + ">\r\n";
            out.write(message.getBytes());
            out.flush();

            if (in.available() > 0) {
                len = in.read(buffer, 0, 256);
                System.out.write(buffer, 0, len);
            }

            message = "RCPT TO:<" + to + ">\r\n";
            out.write(message.getBytes());
            out.flush();

            if (in.available() > 0) {
                len = in.read(buffer, 0, 256);
                System.out.write(buffer, 0, len);
            }

            message = "DATA\r\n";
            out.write(message.getBytes());
            message = "Subject: " + subject + "\r\n\r\n";
            out.write(message.getBytes());

            if (in.available() > 0) {
                len = in.read(buffer, 0, 256);
                System.out.write(buffer, 0, len);
            }

            message = text + "\r\n";
            out.write(message.getBytes());

            if (in.available() > 0) {
                len = in.read(buffer, 0, 256);
                System.out.write(buffer, 0, len);
            }

            message = ".\r\n";
            out.write(message.getBytes());
            out.flush();

            if (in.available() > 0) {
                len = in.read(buffer, 0, 256);
                System.out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /*
     * Sends QUIT and closes the socket
     */
    public void close() throws IOException {
        message = "QUIT\r\n";
        OutputStream out = socket.getOutputStream();
        out.write(message.getBytes());
        out.flush();
        socket.close();
    }
}