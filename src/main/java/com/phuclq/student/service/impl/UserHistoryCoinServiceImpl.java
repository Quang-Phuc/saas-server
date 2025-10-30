package com.phuclq.student.service.impl;

import com.phuclq.student.dao.UserHistoryDao;
import com.phuclq.student.domain.User;
import com.phuclq.student.dto.*;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserHistoryCoinServiceImpl implements UserHistoryCoinService {
    @Autowired
    private UserHistoryDao userHistoryDao;
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

        return  null;

    }

    @Override
    public void deleteHistoryCoin(Integer id) {

    }
}
