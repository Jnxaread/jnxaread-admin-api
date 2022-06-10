package com.jnxaread.service.impl;

import com.jnxaread.service.DataBackupService;
import com.jnxaread.util.FileUtil;
import com.jnxaread.util.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author LiSong-ux
 * @create 2022-03-11 14:22
 */
@Service
public class DataBackupServiceImpl implements DataBackupService {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${jnxaread.data-backup.path}")
    private String path;
    @Resource
    private MailUtil mailUtil;
    @Value("${jnxaread.data-backup.mail}")
    private String backupMailAddress;
    private final DateTimeFormatter filePattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private final DateTimeFormatter mailPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Logger logger = LoggerFactory.getLogger("backup");

    /**
     * 获取数据库主机IP地址或域名
     *
     * @return 数据库主机地址
     */
    private String getHostAddress() {
        return url.substring(url.indexOf("mysql"), url.indexOf("3306"))
                .replace(":", "")
                .replace("//", "")
                .replace("mysql", "");
    }

    /**
     * 数据库备份期间会锁表
     */
    @Override
    public void dataBackup() {
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.toLocalDate().toString().replace("-", "");

        File saveDir = new File(path);
        if (!saveDir.exists()) {
            if (!saveDir.mkdirs()) {
                String message = "创建备份目录失败：" + path;
                logger.error(message);
                String title = nowDate + "谨下网数据库备份失败！";
                sendMail(title, message, null);
                return;
            }
        }
        // backup_20220310150159.sql
        String hostAddr = getHostAddress();

        String filename = path + File.separator + "backup_" + now.format(filePattern) + ".sql";

        /*

        mysqldump jnxaread --result-file=C:\Users\ADMIN\Desktop\jnxaread_local.sql
        --skip-extended-insert --user=root --password=123 --host=127.0.0.1 --port=3306

         */

        String command = "mysqldump jnxaread --result-file=" + filename
                + " --skip-extended-insert --user=root --password="
                + password + " --host=" + hostAddr + " --port=3306";

        logger.info("开始备份数据库：{}", command);
        long start = System.currentTimeMillis();

        String durationMessage = "";
        try {
            Process exec = Runtime.getRuntime().exec(command);
            processReader(exec.getInputStream(), true);
            processReader(exec.getErrorStream(), false);
            int result = exec.waitFor();

            // 获取数据库备份操作消耗的时间，单位：秒
            long duration = (System.currentTimeMillis() - start) / 1000;
            durationMessage = (duration / 60) + "分" + (duration % 60) + "秒";

            if (result == 0) {
                logger.info("数据库备份成功：{}，耗时：{}", filename, durationMessage);
            } else {
                String message = "数据库备份失败：Process.waitFor=" + result + "，耗时：" + durationMessage;
                logger.error(message);
                String title = nowDate + "谨下网数据库备份失败！";
                sendMail(title, message, null);
            }
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        }

        String compress;
        try {
            compress = FileUtil.zipCompress(filename);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            String message = "备份文件压缩失败：" + filename;
            logger.error(message);
            String title = nowDate + "谨下网数据库备份失败！";
            String text = message + "\n" + e;
            sendMail(title, text, null);
            return;
        }
        logger.info("备份文件压缩完成：{}", compress);
        File backup = new File(filename);
        File zip = new File(compress);

        long backupSize = backup.length();
        if (backup.delete()) {
            logger.info("删除原备份文件：{}", filename);
        } else {
            logger.error("原备份文件删除失败：{}", filename);
        }

        String subject = nowDate + "谨下网数据库备份";
        String content0 = "执行时间：" + now.format(mailPattern);
        String content1 = "执行耗时：" + durationMessage;
        String content2 = "备份大小：" + String.format("%.2f", backupSize / 1024d / 1024) + "MB";
        String content3 = "压缩大小：" + String.format("%.2f", zip.length() / 1024d / 1024) + "MB";
        String content4 = "执行命令：" + command;
        String text = content0 + "\n" + content1 + "\n" + content2 + "\n" + content3 + "\n" + content4;
        sendMail(subject, text, zip);
    }

    /**
     * 接收一个输入流并开启一个线程从输入流中读取数据并输出到日志中
     *
     * @param is    输入流
     * @param isStd 是否是标准输入
     */
    private void processReader(InputStream is, boolean isStd) {
        new Thread(() -> {
            String line;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                while ((line = reader.readLine()) != null) {
                    if (isStd) {
                        logger.info(line);
                    } else {
                        logger.error(line);
                    }
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }).start();
    }

    /**
     * 发送邮件，统一日志输出
     *
     * @param title 邮件标题
     * @param text  邮件正文
     * @param file  附件
     */
    private void sendMail(String title, String text, File file) {
        logger.info("开始发送邮件：{}，接收人：{}", title, backupMailAddress);
        try {
            if (file == null) {
                mailUtil.send(backupMailAddress, title, text);
            } else {
                mailUtil.send(backupMailAddress, title, text, file);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.error("邮件发送失败：{}，接收人：{}", title, backupMailAddress);
            return;
        }
        logger.info("邮件发送成功：{}，接收人：{}", title, backupMailAddress);
    }
}
