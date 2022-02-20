package com.zs.aidata.core.tools;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;

import java.io.*;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Date;

/**
 * FTP工具类
 *
 * @author 张顺
 * @since 2021/12/2
 */
@Slf4j
public class FtpUtil {

    /**
     * 上传文件到FTP服务器
     */
    public static void uploadFile() throws IOException {
        FTPClient ftpClient = new FTPClient();

        // 控制命令超时时间
        ftpClient.setDefaultTimeout(10 * 1000);
        // 获取连接时的超时时间
        ftpClient.setConnectTimeout(10 * 1000);
        // 数据传输时的超时时间
        ftpClient.setDataTimeout(10 * 1000);


        ftpClient.setControlEncoding("UTF-8");
        ftpClient.addProtocolCommandListener(
                new PrintCommandListener(
                        new PrintWriter(new OutputStreamWriter(System.out, "UTF-8")), true));
        ftpClient.connect("127.0.0.1", 21);
        ftpClient.login("ftp_user", "zs69894050481");
        int replyCode = ftpClient.getReplyCode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);//设置为二进制文件

        if (!FTPReply.isPositiveCompletion(replyCode)) {
            ftpClient.disconnect();
            System.out.println("FTP连接失败");
        } else {
            System.out.println("FTP连接成功");
        }
        //本地文件流
//        InputStream in = new FileInputStream("D:/迅雷下载/霸王之卵.720p.rmvb");
//        InputStream in = new FileInputStream("D:/版本控制.rar");
        InputStream in = new FileInputStream("D:/英雄联盟安装包/LOL_V3.1.9.7__FULL.7z.003");
        //指定写入目录,注意:假如指定的目录不存在,会直接上传到根目录,假如存在，就上传到指定路径
        ftpClient.changeWorkingDirectory("/");
        //远程文件名
        String removePath = "/bbbb";
        // 每个多少秒做心跳连接(单位s)
        ftpClient.setControlKeepAliveTimeout(60 * 60);
        // 这个心跳连接的最大值，超过了就算超时报错了(单位ms)
        ftpClient.setControlKeepAliveReplyTimeout(60 * 1000);
        ftpClient.setKeepAlive(true);
        // 被动
        ftpClient.enterLocalPassiveMode();
        ftpClient.setCopyStreamListener(new MyCSL(ftpClient, 5000,
                ftpClient.getControlKeepAliveReplyTimeout()));
        // 622
        // 二进制
//        ftpClient.setBufferSize(10 * 1024);
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        //上传
        log.info(new Date().toLocaleString());
        try {
            if (ftpClient.storeFile(removePath, in)) {
                System.out.println("文件上传成功");
            } else {
                System.out.println("文件上传失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info(new Date().toLocaleString());
        //关闭文件流
        in.close();
        //关闭连接
        if (ftpClient != null) {
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }


    /*public static void main(String[] args) throws IOException {
        FtpUtil.uploadFile();
    }*/


    private static class MyCSL implements CopyStreamListener {

        private final FTPClient parent;
        private final long idle;
        private final int currentSoTimeout;

        private long time = System.currentTimeMillis();
        private int notAcked;

        MyCSL(FTPClient parent, long idleTime, int maxWait) throws SocketException {
            this.idle = idleTime;
            this.parent = parent;
            this.currentSoTimeout = parent.getSoTimeout();
            parent.setSoTimeout(maxWait);
        }

        @Override
        public void bytesTransferred(CopyStreamEvent event) {
            bytesTransferred(event.getTotalBytesTransferred(), event.getBytesTransferred(), event.getStreamSize());
        }

        @Override
        public void bytesTransferred(long totalBytesTransferred,
                                     int bytesTransferred, long streamSize) {
            long now = System.currentTimeMillis();
            if (now - time > idle) {
                try {
                    parent.noop();
                } catch (SocketTimeoutException e) {
                    notAcked++;
                } catch (IOException e) {
                    // Ignored
                }
                time = now;
            }
        }

        void cleanUp() throws IOException {
            try {
                while (notAcked-- > 0) {
                    parent.getReply();
                }
            } finally {
                parent.setSoTimeout(currentSoTimeout);
            }
        }

    }
}
