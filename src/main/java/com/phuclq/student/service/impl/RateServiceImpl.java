package com.phuclq.student.service.impl;

import com.phuclq.student.domain.Rate;
import com.phuclq.student.domain.School;
import com.phuclq.student.dto.rate.RateDto;
import com.phuclq.student.repository.RateRepository;
import com.phuclq.student.repository.SchoolRepository;
import com.phuclq.student.service.RateService;
import com.phuclq.student.service.UserService;
import com.phuclq.student.types.RateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.averagingDouble;

@Service
public class RateServiceImpl implements RateService {

    @Autowired
    RateRepository rateRepository;

    @Autowired
    SchoolRepository schoolRepository;
    @Autowired
    UserService userService;

    @Override
    public Rate rate(Rate rate) {

        String userId = userService.getUserLogin().getId().toString();
        List<Rate> allByRequestIdAndTypeAndCreatedBy = rateRepository.findAllByRequestIdAndTypeAndCreatedBy(rate.getRequestId(), rate.getType(), userId);
        if (Objects.nonNull(allByRequestIdAndTypeAndCreatedBy)) {
            rateRepository.deleteAll(allByRequestIdAndTypeAndCreatedBy);
        }
        rate.setCreatedDate(LocalDateTime.now());
        rate.setCreatedBy(userId);

        Rate save = rateRepository.saveAndFlush(rate);
        if (RateType.RATE_SCHOOL.getName().equals(rate.getType())) {
            List<Rate> allByRequestIdInAndType = rateRepository.findAllByRequestIdInAndType(Collections.singletonList(rate.getRequestId()), RateType.RATE_SCHOOL.getName());
            Map<String, Double> groupBy = allByRequestIdInAndType.stream().collect(Collectors.groupingBy(Rate::getRequestId, averagingDouble(Rate::getRate)));

            School school = schoolRepository.findAllByIdUrl(rate.getRequestId());
            for (Map.Entry<String, Double> entry : groupBy.entrySet()) {
                if (school.getId().toString().equals(entry.getKey())) {
                    school.setTotalRate(entry.getValue());
                    System.out.println(entry.getKey() + " " + entry.getValue());

                }
            }
            schoolRepository.save(school);
        }
        return save;
    }

    @Override
    public Map<Double, Long> rateByIdAndType(String idUrl, String type) {
        RateDto rateDto = new RateDto();
        List<Rate> allByRequestIdAndType = rateRepository.findAllByRequestIdAndType(idUrl, type);
        if (!allByRequestIdAndType.isEmpty()) {
            Map<Double, Long> collect = allByRequestIdAndType.stream().collect(Collectors.groupingBy(Rate::getRate, Collectors.counting()));
            rateDto.setRates(collect);
            rateDto.setTotal((long) allByRequestIdAndType.size());
            return collect;

        }
        return null;
    }
}

