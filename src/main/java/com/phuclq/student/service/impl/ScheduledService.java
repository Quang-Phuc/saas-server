package com.phuclq.student.service.impl;

//import com.aspose.words.Document;

import com.phuclq.student.domain.Attachment;
import com.phuclq.student.domain.Blog;
import com.phuclq.student.domain.File;
import com.phuclq.student.dto.RequestFileDTO;
import com.phuclq.student.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.phuclq.student.utils.StringUtils.getSearchableStringUrl;
import static com.phuclq.student.utils.StringUtils.getSearchableStringUrlExit;

@Service
@Transactional
public class ScheduledService {

    private static List<RequestFileDTO> dtos;
    private final Logger log = LoggerFactory.getLogger(ScheduledService.class);
    @Autowired
    RateRepository rateRepository;
    @Autowired
    UserCoinBackupRepository userCoinBackupRepository;
    @Autowired
    UserCoinRepository userCoinRepository;
    @Autowired
    WarningRepository warningRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${database.backup.path}")
    private String path;
    @Value("${database.backup.schema}")
    private String schema;

    @Scheduled(fixedRate = 480 * 600 * 1000)
    @Transactional
    public void jobCompareFile() {
        List<Attachment> allByCodeFileNotNull = attachmentRepository.findDuplicateFiles();
        log.info("total file {}", allByCodeFileNotNull.size());
        allByCodeFileNotNull.forEach(x -> {
            Optional<Attachment> first = allByCodeFileNotNull.stream()
                    .filter(i -> i.getCodeFile().equals(x.getCodeFile()) && !x.getId().equals(i.getId()))
                    .findFirst();
            if (first.isPresent()) {
                log.info("file is duplicate is {}", first.get().getId());
                Optional<File> files = fileRepository.findById(x.getRequestId());
                if (files.isPresent()) {
                    File file = files.get();
                    file.setIsDuplicate(true);
                    file.setFileDuplicate(first.get().getRequestId());
                    file.setCodeFile(first.get().getCodeFile());
                    fileRepository.save(file);
                }
                x.setCheckDuplicate(true);
                attachmentRepository.save(x);

            }
        });
    }
//
//  @Scheduled(fixedRate = 50 * 600 * 1000)
//  @Transactional
//  public void jobCompareCoin() {
//    List<UserCoinBackup> userCoinBackups = userCoinBackupRepository.findAll();
//    List<UserCoin> userCoin = userCoinRepository.findAll();
//    if(userCoinBackups.size()>userCoin.size()||userCoinBackups.size()<userCoin.size()){
//      List<Integer>  userCoinIdBackup = userCoinBackups.stream().map(UserCoinBackup::getUserId).collect(
//          Collectors.toList());
//
//      List<Integer>  userCoinId = userCoin.stream().map(UserCoin::getUserId).collect(
//          Collectors.toList());
//
//      List<Integer> list = new ArrayList<>(CollectionUtils.disjunction(userCoinIdBackup, userCoinId));
//
//      Warning warning = new Warning();
//      warning.setType(WarningType.COIN_COMPARE_USER.getName());
//      warning.setDetail(list.toString());
//      warningRepository.save(warning);
//    }
//    userCoin.forEach(x->{
//      List<UserCoinBackup> userCoinBackupList = userCoinBackups.stream()
//          .filter(y -> y.getUserId().equals(x.getUserId())).collect(Collectors.toList());
//      if (userCoinBackupList.size() > 0) {
//        UserCoinBackup userCoinBackup = userCoinBackupList.get(0);
//        if (!Objects.equals(userCoinBackup.getTotalCoin(), x.getTotalCoin())) {
//          userCoinBackup.setTotalCompare(userCoinBackup.getTotalCoin() - x.getTotalCoin());
//          userCoinBackupRepository.save(userCoinBackup);
//          userCoinRepository.save(x);
//          x.setTotalCompare(userCoinBackup.getTotalCoin() - x.getTotalCoin());
//          userCoinRepository.save(x);
//        }
//      } else {
//        x.setTotalCompare(0 - x.getTotalCoin());
//        userCoinRepository.save(x);
//      }
//
//    });
//  }

    @Scheduled(fixedRate = 50 * 600 * 1000)
    @Transactional
    public void jobCheckMoneyTop() {
        List<File> byMoneyTopIsNotNull = fileRepository.findByMoneyTopIsNotNull();
        byMoneyTopIsNotNull.stream().limit(100).forEach(byId -> {
            if (LocalDateTime.now().isAfter(byId.getEndMoneyTop())) {
                byId.setMoneyTop(null);
                byId.setStartMoneyTop(null);
                byId.setEndMoneyTop(null);
                fileRepository.save(byId);
            }
        });
    }

    @Scheduled(fixedRate = 10 * 60 * 1000)
    @Transactional
    public void blogUrl() {

        List<Blog> allByIdUrlIsNull = blogRepository.findAllByIdUrlIsNullOrIdUrl("");
        allByIdUrlIsNull.stream().limit(100).forEach(byId -> {
            log.info("JOB BLOG {}",byId.getId());
            byId.setIdUrl(getSearchableStringUrl(byId.getTitle(), blogRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(byId.getTitle())).size()));
            blogRepository.saveAndFlush(byId);
        });


    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public  void backupDataWithMysqldump() {
        String executeCmd = "mysqldump -u " + username + " -p" + password + " " + schema + " -r " + path;

        try {
            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            if (processComplete == 0) {
                System.out.println("Backup created successfully");
            } else {
                System.out.println("Could not create the backup");
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

}
