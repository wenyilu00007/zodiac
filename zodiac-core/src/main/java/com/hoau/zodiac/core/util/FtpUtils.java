package com.hoau.zodiac.core.util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.util.ArrayList;

/**
 * @author 邓寅
 * @version V1.0
 * @Title: FtpUtils
 * @Package com.hoau.zodiac.core.util
 * @Description: 操作ftp服务器工具类
 * @date 2017/9/21 10:35
 */
public class FtpUtils {

    protected static final Logger logger = LoggerFactory.getLogger(FtpUtils.class);

    /**
     * 连接ftp进行操作的客户端
     */
    private FTPClient ftpClient;

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
     * 构造工具类，需要进行操作前的必要参数
     * @param host
     * @param port
     * @param username
     * @param password
     * @author 陈宇霖
     * @date 2017年09月21日10:53:35
     */
    public FtpUtils(String host, int port, String username, String password) {
        if (null == ftpClient) {
            ftpClient = new FTPClient();
        }
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * 连接（配置通用连接属性）至服务器
     *
     * @param remotePath 当前访问目录
     * @return <b>true</b>：连接成功 <br/>
     * <b>false</b>：连接失败
     */
    public boolean connectToTheServer(String remotePath) {
        // 定义返回值
        boolean result = false;
        try {
            // 连接至服务器，端口默认为21时，可直接通过URL连接
            ftpClient.connect(this.host, this.port);
            // 登录服务器
            ftpClient.login(this.username, this.password);
            // 判断返回码是否合法
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                // 不合法时断开连接
                ftpClient.disconnect();
                // 结束程序
                return result;
            }
            //设置文件传输模式
            //被动模式
            //ftpClient.enterLocalPassiveMode();
            //创建目录
            ftpClient.makeDirectory(remotePath);
            // 设置文件操作目录
            ftpClient.changeWorkingDirectory(remotePath);
            // 设置文件类型，二进制
            result = ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 设置缓冲区大小
            ftpClient.setBufferSize(3072);
            // 设置字符编码
            ftpClient.setControlEncoding("UTF-8");
        } catch (IOException e) {
            logger.error("FtpUtil 连接FTP服务器异常", e);
            throw new RuntimeException("FtpUtil 连接FTP服务器异常", e);
        }
        return result;
    }

    /**
     * 上传文件至FTP服务器
     *
     * @param remotePath 上传文件存储路径
     * @param fileName  上传文件存储名称
     * @param is        上传文件输入流
     * @return <b>true</b>：上传成功 <br/>
     * <b>false</b>：上传失败
     */
    public boolean uploadFile(String remotePath, String fileName, InputStream is) {
        boolean result = false;
        try {
            // 连接至服务器
            result = connectToTheServer(remotePath);
            // 判断服务器是否连接成功
            if (result) {
                // 上传文件
                result = ftpClient.storeFile(fileName, is);
            }
            // 关闭输入流
            is.close();
        } catch (IOException e) {
            logger.error("FtpUtil 上传文件至FTP异常 " + e.getMessage());
        } finally {
            // 判断输入流是否存在
            if (null != is) {
                try {
                    // 关闭输入流
                    is.close();
                } catch (IOException e) {
                    logger.error("FtpUtil 上传文件至FTP 关闭连接时异常" + e.getMessage());
                    throw new RuntimeException("FtpUtil 上传文件至FTP 关闭连接时异常", e);
                }
            }
            // 登出服务器并断开连接
            logout();
        }
        return result;
    }

    /**
     * 下载FTP服务器文件至本地<br/>
     * 操作完成后需调用logout方法与服务器断开连接
     *
     * @param remotePath 下载文件存储路径
     * @param fileName   下载文件存储名称
     * @return <b>InputStream</b>：文件输入流
     */
    public byte[] retrieveFile(String remotePath, String fileName) {
        try {
            // 判断服务器是否连接成功
            if (connectToTheServer(remotePath)) {
                // 获取文件输入流
                return StreamUtils.copyToByteArray(ftpClient.retrieveFileStream(fileName));
            }
        } catch (IOException e) {
            logger.error("FtpUtil 从FTP下载文件到本地异常" + e.getMessage());
            throw new RuntimeException("FtpUtil 从FTP下载文件到本地异常", e);
        }
        return null;
    }

    /**
     * Description: 从FTP服务器下载文件
     *
     * @param remotePath FTP服务器上的相对路径
     * @param fileName   要下载的文件名
     * @param localPath  下载后保存到本地的路径
     * @return
     */
    public boolean downloadFile(String remotePath, String fileName, String localPath) {
        // 初始表示下载失败
        boolean success = false;
        //表示是否连接成功
        File file = new File(localPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            // 连接至服务器
            if (connectToTheServer(remotePath)) {
                // 列出该目录下所有文件
                FTPFile[] fs = ftpClient.listFiles();
                // 遍历所有文件，找到指定的文件
                for (FTPFile ff : fs) {
                    if (ff.getName().equals(fileName)) {
                        // 根据绝对路径初始化文件
                        File localFile = new File(localPath + "/" + ff.getName());
                        //输出流
                        OutputStream is = new FileOutputStream(localFile);
                        //下载文件
                        success = ftpClient.retrieveFile(ff.getName(), is);
                        is.close();
                    }
                }
            }
        } catch (IOException e) {
            logger.error("FtpUtil 从FTP服务器下载文件异常", e);
        } finally {
            // 登出服务器并断开连接
            logout();
        }
        return success;
    }


    /**
     * 删除FTP服务器文件
     *
     * @param remotePath 当前访问目录
     * @param fileName   文件存储名称
     * @return <b>true</b>：删除成功 <br/>
     * <b>false</b>：删除失败
     */
    public boolean deleteFile(String remotePath, String fileName) {
        boolean result = false;
        // 判断服务器是否连接成功
        if (connectToTheServer(remotePath)) {
            try {
                // 删除文件
                result = ftpClient.deleteFile(fileName);
            } catch (IOException e) {
                logger.error("FtpUtil 删除FTP服务器上的 文件异常" + e.getMessage());
                throw new RuntimeException("FtpUtil 删除FTP服务器上的 文件异常", e);
            } finally {
                // 登出服务器并断开连接
                logout();
            }
        }
        return result;
    }

    /**
     * 检测FTP服务器文件是否存在
     *
     * @param remotePath 检测文件存储路径
     * @param fileName   检测文件存储名称
     * @return <b>true</b>：文件存在 <br/>
     * <b>false</b>：文件不存在
     */
    public boolean checkFile(String remotePath, String fileName) {
        boolean result = false;
        try {
            // 判断服务器是否连接成功
            if (connectToTheServer(remotePath)) {
                // 获取文件操作目录下所有文件名称
                String[] remoteNames = ftpClient.listNames();
                // 循环比对文件名称，判断是否含有当前要下载的文件名
                for (String remoteName : remoteNames) {
                    if (fileName.equals(remoteName)) {
                        result = true;
                    }
                }
            }
        } catch (IOException e) {
            logger.error("FtpUtil 检查FTP文件是否存在异常" + e.getMessage());
            throw new RuntimeException("FtpUtil 检查FTP文件是否存在异常", e);
        } finally {
            // 登出服务器并断开连接
            logout();
        }
        return result;
    }

    /**
     * 登出服务器并断开连接
     *
     * @return <b>true</b>：操作成功 <br/>
     * <b>false</b>：操作失败
     */
    public boolean logout() {
        boolean result = false;
        if (null != ftpClient) {
            try {
                // 退出服务器
                result = ftpClient.logout();
            } catch (IOException e) {
                logger.error("FtpUtil 退出FTP服务器异常" + e.getMessage());
                throw new RuntimeException("FtpUtil 退出FTP服务器异常", e);
            } finally {
                // 判断连接是否存在
                if (ftpClient.isConnected()) {
                    try {
                        // 断开连接
                        ftpClient.disconnect();
                    } catch (IOException e) {
                        logger.error("FtpUtil 关闭FTP服务器异常" + e.getMessage());
                        throw new RuntimeException("FtpUtil 关闭FTP服务器异常", e);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 读取本地TXT
     *
     * @param filepath txt文件目录即文件名
     */
    public ArrayList<String> readTxt(String filepath) {
        ArrayList<String> readList = new ArrayList<String>();
        try {
            String temp = null;
            File f = new File(filepath);
            // 指定读取编码用于读取中文
            InputStreamReader read = new InputStreamReader(new FileInputStream(f), "UTF-8");
            BufferedReader reader = new BufferedReader(read);
            do {
                temp = reader.readLine();
                if (temp != null) {
                    readList.add(temp);
                }
            } while (temp != null);
            read.close();
        } catch (Exception e) {
            logger.error("读取本地txt异常" + e.getMessage());
            throw new RuntimeException("读取本地txt异常", e);
        }
        return readList;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
