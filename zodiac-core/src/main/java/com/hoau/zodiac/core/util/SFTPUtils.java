package com.hoau.zodiac.core.util;

import com.jcraft.jsch.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.Serializable;
import java.util.Properties;

/**
 * @author 于金谷
 * @date 2017/11/29 15:37
 * @description SFTP文件上传
 */
public class SFTPUtils implements Serializable {
    private static final Logger log = Logger.getLogger(SFTPUtils.class.getName());
    public static final String SFTP_UPLOAD_DIR = "/opt/data/gylftp07/jv/bills/";
    public static final String SFTP_UPLOAD_BACKUP_DIR = "/opt/data/gylftp07/jv/backup/";

    public static final String SFTP_REQ_HOST = "114.141.133.237";
    public static final Integer SFTP_REQ_PORT = 22;
    public static final String SFTP_REQ_USERNAME = "gylftp07";
    public static final String SFTP_REQ_PASSWORD = "gylftp07";
    public static final String SFTP_REQ_LOC = "location";
    public static final Integer SFTP_TIMEOUT = 300000;

    Session session = null;
    Channel channel = null;

    public ChannelSftp getChannel() throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(SFTP_REQ_USERNAME, SFTP_REQ_HOST, SFTP_REQ_PORT);
        session.setPassword(SFTP_REQ_PASSWORD);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config); // 为Session对象设置properties
        session.setTimeout(SFTP_TIMEOUT); // 设置timeout时间
        session.connect(); // 通过Session建立链接
        channel = session.openChannel("sftp"); // 打开SFTP通道
        channel.connect(); // 建立SFTP通道的连接
        log.debug("sftp connected.");
        return (ChannelSftp) channel;
    }

    public void closeChannel() throws Exception {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
        log.debug("sftp closed.");
    }

    /**
     * sftp附件上传工具类
     * @Param attDirOrFile 上传文件目录或者文件,目前支持一级目录
     * @Param dirCode 目录编号 0 上传到 /jv/bills下 1 上传到/jv/backup下
     * @throws Exception
     */
    public static final void uploadAttachment(String attDirOrFile, String dirCode) {
        SFTPUtils sftpUtils = new SFTPUtils();
        ChannelSftp sftp = null;
        try{
            sftp = sftpUtils.getChannel();
            File file = new File(attDirOrFile);
            if (file.isDirectory()){
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    File srcFile = files[i];
                    if(srcFile.isFile()){
                        uploadFile(sftp, srcFile, dirCode);
                    }
                }
            } else if(file.isFile()) {
                uploadFile(sftp, file, dirCode);
            } else {
                log.error("上传文件路径不存在：" + attDirOrFile);
            }
            sftp.quit();
            sftpUtils.closeChannel();
        } catch (Exception e) {
            e.printStackTrace();
            sftp.quit();
            try {
                sftpUtils.closeChannel();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }


    /**
     * sftp附件上传
     * @Param sftp
     * @Param file 上传文件
     * @Param dirCode 上传文件目录Code
     * @throws Exception
     */
    private static final void uploadFile(ChannelSftp sftp, File file, String dirCode) throws SftpException {
        String src = file.getPath();
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String dist = SFTP_UPLOAD_DIR + fileName.replace(suffix, "tmp");
        if("1".equals(dirCode)){
            dist = SFTP_UPLOAD_BACKUP_DIR + fileName.replace(suffix, "tmp");
        }
        sftp.put(src, dist);
        String distNewName = SFTP_UPLOAD_DIR + fileName;
        if("1".equals(dirCode)){
            distNewName = SFTP_UPLOAD_BACKUP_DIR + fileName;
        }
        sftp.rename(dist, distNewName);
    }
}
