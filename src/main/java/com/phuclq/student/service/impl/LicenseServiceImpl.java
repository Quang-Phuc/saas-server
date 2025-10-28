package com.phuclq.student.service.impl;

import com.phuclq.student.domain.*;
import com.phuclq.student.dto.LicenseStatusDto;
import com.phuclq.student.dto.StoreInfoDto;
import com.phuclq.student.dto.UserInfoDto;
import com.phuclq.student.dto.UserDTO;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.repository.*;
import com.phuclq.student.service.LicenseService;
import com.phuclq.student.service.UserService;
import com.phuclq.student.types.RoleConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LicenseServiceImpl implements LicenseService {

    private final UserService userService;
    private final UserLicenseRepository userLicenseRepository;
    private final LicensePackageRepository licensePackageRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final UsersStoresRepository usersStoresRepository;

    @Override
    public LicenseStatusDto checkLicense() {
        // ✅ Lấy thông tin user đăng nhập
        UserDTO userResultLogin = userService.getUserResultLogin();
        Integer userId = userResultLogin.getId();

        // ✅ Lấy user, nếu không có thì ném lỗi
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessHandleException("SS007"));

        // ✅ Lấy license mới nhất của user
        UserLicense userLicense = userLicenseRepository.findTopByUserIdOrderByExpiryDateDesc(userId)
                .orElse(null);

        if (userLicense == null) {
            // Không có license nào
            return new LicenseStatusDto("not_found");
        }

        // ✅ Kiểm tra license hết hạn
        if (userLicense.isExpired()) {
            // Nếu là chủ (owner) — trả về thông tin store và users
            if (Objects.equals(user.getRoleId(), RoleConstant.OWNER)) {
                List<UsersStores> usersStoresList = usersStoresRepository.findAllByUserId(userId);

                List<Long> storeIds = usersStoresList.stream()
                        .map(UsersStores::getStoreId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                List<Integer> userIds = usersStoresList.stream()
                        .map(UsersStores::getUserId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                List<Store> stores = storeIds.isEmpty() ? Collections.emptyList()
                        : storeRepository.findAllByIdIn(storeIds);

                List<UserInfoDto> users = userIds.isEmpty() ? Collections.emptyList()
                        : userRepository.findAllByIdInAndRoleId(userIds, RoleConstant.STAFF)
                        .stream()
                        .map(u -> new UserInfoDto(u.getId(), u.getUserName()))
                        .collect(Collectors.toList());

                return new LicenseStatusDto(
                        "expired",
                        stores.size(),
                        users.size(),
                        stores,
                        users
                );
            }

            // Nếu là staff => ném lỗi business
            if (Objects.equals(user.getRoleId(), RoleConstant.STAFF)) {
                throw new BusinessHandleException("SS004");
            }

            // Các role khác => trả về expired chung (hoặc bạn có thể thay bằng hành vi khác)
            return new LicenseStatusDto("expired");
        } else {
            // License còn hạn — trả về trạng thái hợp lệ (tùy bạn muốn thêm thông tin license vào DTO)
            return new LicenseStatusDto("valid");
        }
    }
}




