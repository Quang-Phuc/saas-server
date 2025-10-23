package com.phuclq.student.service;


import com.phuclq.student.domain.RequestHistory;
import com.phuclq.student.repository.RequestHistoryRepository;
import com.phuclq.student.types.RequestHistoryStatus;
import com.phuclq.student.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@Slf4j
public class RequestHistorySubService implements InitializingBean {

    @Autowired
    private RequestHistoryRepository requestHistoryRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;


    private TransactionTemplate transactionTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    @Async
    public void saveLog(String url, String requestContent, String responseContent, int responseCode,
                        String requestType, long startTimeCallWs, Integer requestId) {
        RequestHistory history = new RequestHistory();
        history.setRequestId(requestId);
        history.setUrl(url);
        history.setType(requestType);
        history.setRequestContent(requestContent);
        history.setResponseContent(responseContent);
        try {
            if (responseCode == 200) {
                history.setStatus(RequestHistoryStatus.SUCCESS.name());
                history.setResponseCode(String.valueOf(responseCode));
                log.info("Create policy {}", "a");
                log.info("Successfully to call url={}", url);
            } else {
                history.setStatus(RequestHistoryStatus.FAIL.name());

                log.error("Fail to call url={}", url);
            }

        } catch (Exception e) {
            log.error("Fail to call url=" + url, e);

            history.setStatus(RequestHistoryStatus.FAIL.name());
        } finally {
            long duration = System.currentTimeMillis() - startTimeCallWs;
            history.setDuration(duration);
            history.setCreatedTime(DateTimeUtils.getCurrentTime());
            requestHistoryRepository.save(history);
        }

    }
}
