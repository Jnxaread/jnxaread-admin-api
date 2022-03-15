package com.jnxaread.timer;

import com.jnxaread.service.DataBackupService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author LiSong-ux
 * @create 2022-03-10 14:46
 */
@Component
public class DataBackupTimerTask {
    @Resource
    private DataBackupService dataBackupService;

    // 执行时间：每周三凌晨三点
    // 这里有坑，经过实际测试，spring不支持cron的周字段用数字表示，只能用字符串
    @Scheduled(cron = " 0 0 3 ? * WED ")
    public void dataBackup() {
        dataBackupService.dataBackup();
    }
}