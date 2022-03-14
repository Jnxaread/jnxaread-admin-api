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

    @Scheduled(cron = " 0 0 3 ? * 2 ")
    public void dataBackup() {
        dataBackupService.dataBackup();
    }
}