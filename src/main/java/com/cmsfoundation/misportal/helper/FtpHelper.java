package com.cmsfoundation.misportal.helper;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

@Component
public class FtpHelper {
    private static final String FTP_SERVER = "31.97.234.95";
    private static final String FTP_USERNAME = "u703629182.cmsmisphotos";
    private static final String FTP_PASSWORD = "Z1z~c1]hwKXsFcJ?";
    private static final int port = 21;

    public String uploadFile(InputStream inputStream, String remoteFileName) {
        FTPClient ftp = new FTPClient();
        try {
            ftp.connect(FTP_SERVER, port);

            boolean login = ftp.login(FTP_USERNAME, FTP_PASSWORD);
            if (!login) {
                return null;
            }
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            String workingDir = ftp.printWorkingDirectory();

            String remotefilepath = "/" + remoteFileName;

            boolean done = ftp.storeFile(remotefilepath, inputStream);
            inputStream.close();

            if (done) {

                return "https://lakhpatididi.in/cmsmis/photos" + remotefilepath;
            } else {

                int replayCode = ftp.getReplyCode();

                return null;
            }
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        } finally {
            try {
                if (ftp.isConnected()) {
                    ftp.logout();
                    ftp.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}