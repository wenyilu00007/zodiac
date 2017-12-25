package com.hoau.zodiac.core.util;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Properties;

/**
 * @author DINGYONG
 * @version 1.0
 * @title SftpClient
 * @package com.hoau.zodiac.core.util.network
 * @description
 * @date 2017/11/18
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SftpClient {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 服务器地址
     */
    private String host;

    /**
     * 服务器端口
     */
    private int port;

    /**
     * 用户登录名
     */
    private String username;

    /**
     * 用户登录密码
     */
    private String password;

    /**
     * sftp连接通道对象
     */
    private Channel channel = null;

    /**
     * session对象
     */
    private Session sshSession = null;

    /**
     * sftp操作通道对象
     */
    private ChannelSftp sftp = null;

    public SftpClient(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * 连接sftp服务器
     *
     * @return
     */
    public void connect()
            throws JSchException {
        JSch jsch = new JSch();
        jsch.getSession(username, host, port);
        sshSession = jsch.getSession(username, host, port);
        sshSession.setPassword(password);
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        sshSession.setConfig(sshConfig);
        sshSession.connect();
        channel = sshSession.openChannel("sftp");
        channel.connect();
        sftp = (ChannelSftp) channel;

    }

    /**
     * 判断目录是否存在
     *
     * @param directory 上传的目录
     */
    public boolean isDirExist(String directory) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            logger.error("sftp服务器没有这个目录,{}", e);
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }

    /**
     * 创建目录
     *
     * @param createPath
     * @author DINGYONG
     */
    public void createDir(String createPath) {
        try {
            if (isDirExist(createPath)) {
                sftp.cd(createPath);
            }
            String[] pathArray = createPath.split("/");
            StringBuilder filePath = new StringBuilder("/");
            for (String path : pathArray) {
                if ("".equals(path)) {
                    continue;
                }
                filePath.append(path).append("/");
                if (isDirExist(filePath.toString())) {
                    sftp.cd(filePath.toString());
                } else {
                    // 建立目录
                    sftp.mkdir(filePath.toString());
                    // 进入并设置为当前目录
                    sftp.cd(filePath.toString());
                }
            }
            sftp.cd(createPath);
        } catch (Exception e) {
            logger.error("sftp服务器创建目录失败,{}", e);
        }
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     * @return
     * @throws SftpException
     */
    public List<ChannelSftp.LsEntry> listFiles(String directory)
            throws SftpException {
        return sftp.ls(directory);
    }

    /**
     * 上传文件
     *
     * @param directory  上传的目录
     * @param uploadFile 要上传的文件
     */
    public void upload(String directory, File uploadFile, String fileName) {
        try {
            createDir(directory);
            sftp.put(new FileInputStream(uploadFile), fileName);
        } catch (Exception e) {
            logger.error("上传文件到sftp服务器异常,{}", e);
        }
    }

    /**
     * 上传文件流
     *
     * @param directory   上传的目录
     * @param inputStream 要上传的文件
     */
    public void uploadByInputStream(String directory, InputStream inputStream, String fileName) {
        try {
            createDir(directory);
            sftp.put(inputStream, fileName);
        } catch (Exception e) {
            logger.error("上传文件到sftp服务器异常,{}", e);
        }
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     */
    public void download(String directory, String downloadFile, String saveFile) {
        try {
            sftp.cd(directory);
            File file = new File(saveFile);
            sftp.get(downloadFile, new FileOutputStream(file));
        } catch (Exception e) {
            logger.error("从sftp服务器下载文件异常,{}", e);
        }
    }

    /**
     * 读取指定文件,返回输入流
     *
     * @param directory
     * @param downloadFile
     * @return
     * @author DINGYONG
     */
    public InputStream downloadForStream(String directory, String downloadFile)
            throws SftpException, IOException {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            sftp.cd(directory);
            sftp.get(downloadFile, outputStream);
            //注意返回接收的inputStream需要关闭
            return new ByteArrayInputStream(outputStream.toByteArray());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                logger.error("读取sftp文件内容异常,{}", e);
            }
        }
    }

    /**
     * 读取指定文件,返回字节
     *
     * @param directory
     * @param downloadFile
     * @return byte
     * @author DINGYONG
     * @throws SftpException
     * @throws IOException
     */
    public byte[] downloadForBytes(String directory, String downloadFile)
            throws SftpException, IOException {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            sftp.cd(directory);
            sftp.get(downloadFile, outputStream);
            return outputStream.toByteArray();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                logger.error("读取sftp文件内容异常,{}", e);
            }
        }
    }

    /**
     * 重命名文件
     *
     * @param oldPath
     * @param newPath
     * @throws SftpException
     * @throws IOException
     * @author DINGYONG
     */
    public void renameFileName(String oldPath,String newPath)
            throws SftpException, IOException {
        sftp.rename(oldPath,newPath);
    }

    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     */
    public void delete(String directory, String deleteFile) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            logger.error("删除sftp文件发生异常,{}", e);
        }
    }

    /**
     * 判断是否断开了sftp连接通道 false则为断开
     * @return true
     * @author DINGYONG
     */
    public boolean isClosed() {
        return channel.isConnected() || sshSession.isConnected();
    }


    /**
     * 关闭资源
     * @author DINGYONG
     */
    public void close() {
        if (channel.isConnected()) {
            channel.disconnect();

        }
        if (sshSession.isConnected()) {
            sshSession.disconnect();
        }
    }
}
