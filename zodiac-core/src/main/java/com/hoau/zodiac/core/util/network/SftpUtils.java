package com.hoau.zodiac.core.util.network;

import com.jcraft.jsch.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Properties;

/**
 * @author DINGYONG
 * @version 1.0
 * @title SftpUtils
 * @package com.hoau.scorpio.util
 * @description
 * @date 2017/11/18
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SftpUtils {

    private Channel channel = null;
    private Session sshSession = null;

    /**
     * 连接sftp服务器
     *
     * @param host     主机
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public ChannelSftp connect(String host, int port, String username,
                               String password) {
        ChannelSftp sftp = null;

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sftp;
    }

    /**
     * 上传文件
     *
     * @param directory  上传的目录
     * @param uploadFile 要上传的文件
     * @param sftp
     */
    public void upload(String directory, File uploadFile, String fileName, ChannelSftp sftp) {
        try {
            createDir(directory, sftp);
            sftp.put(new FileInputStream(uploadFile), fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     *
     * @param directory   上传的目录
     * @param inputStream 要上传的文件
     * @param sftp
     */
    public void uploadByInputStream(String directory, InputStream inputStream, String fileName, ChannelSftp sftp) {
        try {
            createDir(directory, sftp);
            sftp.put(inputStream, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断目录是否存在
     */
    public boolean isDirExist(String directory, ChannelSftp sftp) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }

    /**
     * 创建目录
     *
     * @param createpath
     * @param sftp
     */
    public void createDir(String createpath, ChannelSftp sftp) {
        try {
            if (isDirExist(createpath, sftp)) {
                sftp.cd(createpath);
            }
            String pathArry[] = createpath.split("/");
            StringBuffer filePath = new StringBuffer("/");
            for (String path : pathArry) {
                if (path.equals("")) {
                    continue;
                }
                filePath.append(path + "/");
                if (isDirExist(filePath.toString(), sftp)) {
                    sftp.cd(filePath.toString());
                } else {
                    // 建立目录
                    sftp.mkdir(filePath.toString());
                    // 进入并设置为当前目录
                    sftp.cd(filePath.toString());
                }
            }
            sftp.cd(createpath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     * @param sftp
     */
    public void download(String directory, String downloadFile, String saveFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(saveFile);
            sftp.get(downloadFile, new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取指定文件,返回输入流
     *
     * @param directory
     * @param downloadFile
     * @param sftp
     * @return
     * @author DINGYONG
     */
    public InputStream downloadForStream(String directory, String downloadFile, ChannelSftp sftp)
            throws SftpException, IOException {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            sftp.cd(directory);
            sftp.get(downloadFile, outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取指定文件,返回字节
     *
     * @param directory
     * @param downloadFile
     * @param sftp
     * @return
     * @author DINGYONG
     */
    public byte[] downloadForBytes(String directory, String downloadFile, ChannelSftp sftp)
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
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     * @param sftp
     */
    public void delete(String directory, String deleteFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     * @param sftp
     * @return
     * @throws SftpException
     */
    public List<ChannelSftp.LsEntry> listFiles(String directory, ChannelSftp sftp) throws SftpException {
        return sftp.ls(directory);
    }

    /**
     * 关闭资源
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
