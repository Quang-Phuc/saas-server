package com.phuclq.student.service.impl;

import com.phuclq.student.dao.UserHistoryDao;
import com.phuclq.student.domain.User;
import com.phuclq.student.domain.UserHistoryCoin;
import com.phuclq.student.dto.*;
import com.phuclq.student.repository.UserHistoryCoinRepository;
import com.phuclq.student.repository.UserRoleRepository;
import com.phuclq.student.security.AuthoritiesConstants;
import com.phuclq.student.service.UserHistoryCoinService;
import com.phuclq.student.service.UserService;
import com.phuclq.student.types.HistoryCoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserHistoryCoinServiceImpl implements UserHistoryCoinService {
    @Autowired
    private UserHistoryDao userHistoryDao;
    @Autowired
    private UserHistoryCoinRepository userHistoryCoinRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UserService userService;

    @Override
    public UserHistoryCoinResultDto getHistoryTransaction(FileHomePageRequest historyCoinRequest, Pageable pageable) {

        User userLogin = userService.getUserLogin();
        Integer loginId = null;
        if (userRoleRepository.getOne(userLogin.getRoleId()).getRole().equalsIgnoreCase(
                AuthoritiesConstants.USER)) {
            loginId = userLogin.getId();
        }
        historyCoinRequest.setLoginId(loginId);
        Page<UserHistoryResult> fileResultDto = userHistoryDao.listUserHistory(historyCoinRequest,
                pageable);
        UserHistoryCoinResultDto userResultDto = new UserHistoryCoinResultDto();
        userResultDto.setList(fileResultDto.getContent());
        PaginationModel paginationModel = new PaginationModel(
                fileResultDto.getPageable().getPageNumber(), fileResultDto.getPageable().getPageSize(),
                (int) fileResultDto.getTotalElements());
        userResultDto.setPaginationModel(paginationModel);
        return userResultDto;
    }

    @Override
    public List<UserHistoryCoinResultTotalDto> getHistoryTransactionTotal() {

        User userLogin = userService.getUserLogin();
        List<UserHistoryCoin> allByCreatedBy = userHistoryCoinRepository.findAllByCreatedBy(userLogin.getId().toString());
        List<UserHistoryCoinResultTotalDto> userHistoryCoinResultTotalDtoList = new ArrayList<>();
        UserHistoryCoinResultTotalDto userHistoryCoinResultTotalDto = new UserHistoryCoinResultTotalDto();

        List<UserHistoryCoin> userHistoryCoinStream = allByCreatedBy.stream().filter(x -> x.getType().equals("+") && x.getTransaction().equals(HistoryCoinType.PAY_COIN.getCode())&& Objects.nonNull(x.getTxnId())).collect(Collectors.toList());
        List<UserHistoryCoin> userHistoryCoins = allByCreatedBy.stream().filter(x -> x.getType().equals("+") && !x.getTransaction().equals(HistoryCoinType.PAY_COIN.getCode())).collect(Collectors.toList());
        userHistoryCoinStream.addAll(userHistoryCoins);
        double sum = userHistoryCoinStream.stream().map(UserHistoryCoin::getCoin).collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).sum();
        userHistoryCoinResultTotalDto.setTotal(sum);
        userHistoryCoinResultTotalDto.setTransaction(1);
        userHistoryCoinResultTotalDto.setName("Xu Được Cộng");


        UserHistoryCoinResultTotalDto userHistoryCoinResultTotalDto2 = new UserHistoryCoinResultTotalDto();
        List<UserHistoryCoin> userHistoryCoinsMinus = allByCreatedBy.stream().filter(x -> x.getType().equals("-") ).collect(Collectors.toList());
        double sumMinus = userHistoryCoinsMinus.stream().map(UserHistoryCoin::getCoin).collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).sum();

        userHistoryCoinResultTotalDto2.setTotal(sumMinus);
        userHistoryCoinResultTotalDto2.setTransaction(2);
        userHistoryCoinResultTotalDto2.setName("Xu Bị Trừ ");

        userHistoryCoinResultTotalDtoList.add(userHistoryCoinResultTotalDto);
        userHistoryCoinResultTotalDtoList.add(userHistoryCoinResultTotalDto2);
        return userHistoryCoinResultTotalDtoList;

    }

    @Override
    public void deleteHistoryCoin(Integer id) {
        UserHistoryCoin historyCoin = userHistoryCoinRepository.findById(id).get();
        userHistoryCoinRepository.delete(historyCoin);
    }
}
