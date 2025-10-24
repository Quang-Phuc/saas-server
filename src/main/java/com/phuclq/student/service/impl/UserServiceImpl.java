package com.phuclq.student.service.impl;

import com.phuclq.student.config.JwtTokenUtil;
import com.phuclq.student.dao.UsersDao;
import com.phuclq.student.domain.*;
import com.phuclq.student.dto.*;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.repository.*;
import com.phuclq.student.service.*;
import com.phuclq.student.types.FileType;
import com.phuclq.student.utils.DateTimeUtils;
import com.phuclq.student.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UsersDao usersDao;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private TokenFireBaseRepository tokenFireBaseRepository;
    private final StoreRepository storeRepository;
    private final LicensePackageRepository licensePackageRepository;
    private final UserLicenseRepository userLicenseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public RegisterResponse registryUser(RegisterRequest request) {

        // 1️⃣ Kiểm tra dữ liệu đầu vào
        if (request.getPhone() == null || request.getPassword() == null || request.getStoreName() == null) {
            throw new BusinessHandleException("SS017");
        }

        // 2️⃣ Kiểm tra trùng số điện thoại
        userRepository.findByPhone(request.getPhone())
                .ifPresent(u -> { throw new BusinessHandleException("SS017"); });

        // 3️⃣ Tạo cửa hàng
        Store store = new Store();
        store.setName(request.getStoreName());
        store.setAddress("Chưa cập nhật");
        Store savedStore = storeRepository.saveAndFlush(store);

        // 4️⃣ Mã hóa mật khẩu
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // 5️⃣ Tạo chủ tiệm (User)
        User owner = new User();
        owner.setFullName(request.getStoreName());
        owner.setPhone(request.getPhone());
        owner.setPassword(hashedPassword);
        owner.setStoreId(savedStore.getId());
        owner.setRoleId(1); // 1 = Chủ tiệm
        userRepository.save(owner);

        // 6️⃣ Cấp License mặc định (Trial 30 ngày)
        LocalDate start = LocalDate.now();
        LocalDate expire = start.plusDays(30);

        // Nếu có sẵn gói trial trong DB thì gán, nếu chưa thì bỏ qua
        LicensePackage trialPackage = licensePackageRepository.findByName("TRIAL")
                .orElseGet(() -> {
                    LicensePackage pkg = new LicensePackage();
                    pkg.setName("TRIAL");
                    pkg.setPrice(0D);
                    pkg.setDiscount(0D);
                    pkg.setMaxStore(3);
                    pkg.setMaxUserPerStore(10);
                    pkg.setDurationDays(30);
                    return licensePackageRepository.save(pkg);
                });

        UserLicense userLicense = new UserLicense();
        userLicense.setUserId(owner.getId());
        userLicense.setLicensePackageId(trialPackage.getId());
        userLicense.setPurchaseDate(start);
        userLicense.setExpiryDate(expire);
        userLicense.setFinalPrice(0D);
        userLicense.setStatus("ACTIVE");
        userLicenseRepository.save(userLicense);

        // 7️⃣ Trả response
        return RegisterResponse.builder()
                .storeId(savedStore.getId())
                .storeName(savedStore.getName())
                .ownerPhone(owner.getPhone())
                .plan(trialPackage.getName())
                .expiredAt(expire.toString())
                .build();
    }


    @Transactional
    public User saveAndFlushUser(User user) {
        try {
            User managedUser = entityManager.merge(user);
            return userRepository.saveAndFlush(managedUser);


        } catch (Exception e) {
            // Log the exception
            System.err.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
            throw e;  // or handle it appropriately
        }
    }

    public  String generateReferralCode() {
        User topByOrderByIdDesc = userRepository.findTopByOrderByIdDesc();
        String idString = String.valueOf(Objects.nonNull(topByOrderByIdDesc)? topByOrderByIdDesc.getId():0);

        StringBuilder stringBuffer = new StringBuilder();
        for(int i = 0;i<= 4- idString.length();i++){
            stringBuffer.append("0");
        }
        stringBuffer.append(idString);

        return stringBuffer.toString();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmailIgnoreCaseAndIsDeletedFalseAndUserFaceIdIsNull(email);
    }

    @Override
    public Page<UserDTO> getUser(Pageable pageable) {
        Page<UserResult> page = userRepository.findUserResult(pageable);
        List<UserDTO> list = new ArrayList<UserDTO>();
        page.getContent().forEach(user -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUserName(user.getUser_name());
            //userDTO.setPassword(user.getPassword());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhone(user.getPhone());
            userDTO.setRoleId(user.getRole_id());
            userDTO.setTotalCoin(user.getTotal_coin());
            list.add(userDTO);
        });
        return new PageImpl<UserDTO>(list, pageable, page.getTotalElements());
    }

    @Override
    public UserResultDto getUser2(FileHomePageRequest request, Pageable pageable) {
        Page<UserAdminResult> fileResultDto = usersDao.listUserAdmin(request, pageable);
        UserResultDto userResultDto = new UserResultDto();
        userResultDto.setList(fileResultDto.getContent());
        PaginationModel paginationModel = new PaginationModel(
                fileResultDto.getPageable().getPageNumber(), fileResultDto.getPageable().getPageSize(),
                (int) fileResultDto.getTotalElements());
        userResultDto.setPaginationModel(paginationModel);
        return userResultDto;
    }

    @Override
    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public User deleteUser(Integer Id) {
        User userDelete = userRepository.findById(Id).get();
        userDelete.setIsDeleted(true);
        return userRepository.save(userDelete);
    }

    @Override
    public User getUserLogin() {
        User user = new User();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                if (Objects.nonNull(authentication) && Objects.nonNull(authentication.getName())) {
                    String email = authentication.getName();
                    user = userRepository.findUserByUserFaceIdAndIsDeletedFalse(email);
                    if (Objects.isNull(user)) {
                        user = userRepository.findUserByEmailIgnoreCaseAndIsDeletedFalseAndUserFaceIdIsNull(email);
                    }
                } else
                    return user;
            }
            return user;
        } catch (Exception e) {
            log.info("Error login {}", e);
            return user;
        }
    }

    @Override
    public Page<File> getListFileByUser(Integer userId, Pageable pageable) {
        return userRepository.findFilesByUser(userId, pageable);
    }

    @Override
    public Page<User> findUserByUserNameAndEmailAndPhone(UsersSearchRequest usersSearchRequest,
                                                         Pageable pageable) {
        Date dateFrom = null;
        Date dateTo = null;
        String dateFromStr = usersSearchRequest.getStartDate();
        String dateToStr = usersSearchRequest.getEndDate();
        Page<User> userPage = null;

        boolean hasDateFrom = StringUtils.isStringNotNullAndHasValue(dateFromStr);
        boolean hasDateTo = StringUtils.isStringNotNullAndHasValue(dateToStr);

        if (hasDateFrom && hasDateTo) {
            dateFrom = DateTimeUtils.convertStringDateYYmmdd(dateFromStr);
            dateTo = DateTimeUtils.convertStringDateYYmmdd(dateToStr);
            userPage = userRepository.findUserByUserNameAndEmailAndTime

                    (usersSearchRequest.getUserName(), usersSearchRequest.getEmail(),
                            usersSearchRequest.getPhone(), dateFrom, dateTo, pageable);
        } else {
            userPage = userRepository.findUserByUserNameAndEmail(usersSearchRequest.getUserName(),
                    usersSearchRequest.getEmail(), usersSearchRequest.getPhone(), pageable);
        }

        return userPage;
    }

    @Override
    public void forgotPassword(String email) {
        String pass = RandomStringUtils.randomAlphanumeric(8);
        User existingUser = userRepository.findUserByEmailIgnoreCaseAndIsDeletedFalseAndUserFaceIdIsNull(email);
        if (existingUser == null) {
            throw new BusinessHandleException("SS004");
        }
        existingUser.setPassword(passwordEncoder.encode(pass));
        userRepository.save(existingUser);
        String sub = "THÔNG TIN MẬT KHẨU ";
        SendMailDto sendMailDto = new SendMailDto();
        sendMailDto.setName(existingUser.getFullName());
        sendMailDto.setPassword(pass);
        sendMailDto.setSub(sub);
        sendMailDto.setEmail(existingUser.getEmail());
       sendMail(sendMailDto);
    }

    void sendMail(SendMailDto sendMailDto) {
        Context context = new Context();
        context.setVariable("name", sendMailDto.getName());
        context.setVariable("pass", sendMailDto.getPassword());
        emailSenderService.sendHtmlMessage(sendMailDto.getEmail(), sendMailDto.getSub(), context,"forgotPassword.html");
    }

    @Override
    public boolean changePassword(String password, String passwordNew, String passwordConfirm) {
        User user = getUserLogin();
        boolean passwordDefine = passwordEncoder.matches(password, user.getPassword());
        if (passwordDefine && passwordNew.equals(passwordConfirm)) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(passwordNew);
            user.setPassword(hashedPassword);
            userRepository.save(user);
            return true;
        }
        throw new BusinessHandleException("SS008");

    }

    @Override
    public User createAdmin(UserAccountDTO accountDTO) {
        User user = new User();
        BeanUtils.copyProperties(accountDTO, user);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(accountDTO.getPassword());
        user.setPassword(hashedPassword);
        user.setIsEnable(false);
        user.setIsDeleted(false);
        user.setRoleId(1);
        userRepository.save(user);
        return user;
    }

    @Override
    public void updateAdmin(UserAccountDTO accountDTO) {
        User user = userRepository.findUserByEmailIgnoreCaseAndIsDeletedFalseAndUserFaceIdIsNull(accountDTO.getEmail());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(accountDTO.getPassword());
        user.setPassword(hashedPassword);
        if (StringUtils.isStringNotNullAndHasValue(accountDTO.getUserName())) {
            user.setUserName(accountDTO.getUserName());
        }
        if (StringUtils.isStringNotNullAndHasValue(accountDTO.getPhone())) {
            user.setPhone(accountDTO.getPhone());
        }
        userRepository.save(user);

    }

    @Override
    public UserDTO getUserResultLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserResult userResult = null;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            userResult = userRepository.findUserResultByEmail(email, FileType.USER_AVATAR.getName());

        }
        UserDTO user = new UserDTO();
        if (Objects.nonNull(userResult)) {
            user.setId(userResult.getId());
            user.setUserName(userResult.getUser_name());
            user.setEmail(userResult.getEmail());
            user.setPhone(userResult.getPhone());
            user.setRoleId(userResult.getRole_id());
            user.setTotalCoin(userResult.getTotal_coin());
            user.setBirthDay(userResult.getBirthDay());
            user.setFullName(userResult.getFullName());
            user.setGender(userResult.getGender());
            user.setIntroduction(userResult.getIntroduction());
            user.setIndustryId(userResult.getIndustryId());
            user.setAddress(userResult.getAddress());
            user.setFullName(userResult.getFullName());
            user.setImage(userResult.getImage());
            user.setRole(authentication.getAuthorities());
            user.setReferralCode(userResult.getReferralCode());
        }
        return user;
    }

    @Override
    public List<UserInfoDTO> getUserInfos() {
        List<UserInfoResult> userInfoResults = userRepository.findUserInfoResult();
        List<UserInfoDTO> userInfoDTOs = new ArrayList<UserInfoDTO>();
        userInfoResults.forEach(userInfoResult -> {
            UserInfoDTO userDto = new UserInfoDTO();
            BeanUtils.copyProperties(userInfoResult, userDto);
            userInfoDTOs.add(userDto);
        });
        return userInfoDTOs;
    }

    @Override
    public List<UserDTO> getUsersByRole(Integer roleId) {
        List<User> userList = userRepository.findUserByRoleIdAndIsDeleted(roleId, false);
        return userList.stream().map(UserDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserInfoResult> findTop10OrderByIdDesc() {
        return userRepository.findTop10OrderByIdDesc(FileType.USER_AVATAR.getName());

    }

    @Override
    public UserDTO changeRole(AdminRoleDTO adminRoleDTO) {
        User user = userRepository.getOne(adminRoleDTO.getUserId());
        user.setRoleId(adminRoleDTO.getRoleId());
        user = userRepository.save(user);
        return new UserDTO(user);
    }

    @Override
    public User save(UserSaveDTO accountDTO) throws IOException {
        User userLogin = userRepository.findById(getUserLogin().getId()).get();
        setUser(accountDTO, userLogin);
        return userRepository.save(userLogin);
    }

    @Override
    public User saveAdmin(UserSaveDTO accountDTO) throws IOException {
        User userLogin = userRepository.findById(accountDTO.getId()).get();
        setUser(accountDTO, userLogin);
        return userRepository.save(userLogin);
    }

    private void setUser(UserSaveDTO accountDTO, User userLogin) throws IOException {
        if (Objects.nonNull(accountDTO.getBirthDay())) {
            userLogin.setBirthDay(accountDTO.getBirthDay());
        }
        if (Objects.nonNull(accountDTO.getFullName())) {
            userLogin.setFullName(accountDTO.getFullName().trim());
        }
        if (Objects.nonNull(accountDTO.getGender())) {
            userLogin.setGender(accountDTO.getGender());
        }
        if (Objects.nonNull(accountDTO.getAddress())) {
            userLogin.setAddress(accountDTO.getAddress().trim());
        }
        if (Objects.nonNull(accountDTO.getIndustryId())) {
            userLogin.setIndustryId(accountDTO.getIndustryId());
        }
        if (Objects.nonNull(accountDTO.getIntroduction())) {
            userLogin.setIntroduction(accountDTO.getIntroduction().trim());
        }
        if (Objects.nonNull(accountDTO.getUserName())) {
            userLogin.setUserName(accountDTO.getUserName().trim());
        }
        if (Objects.nonNull(accountDTO.getPhone())) {
            userLogin.setPhone(accountDTO.getPhone().trim());
        }
        if (Objects.nonNull(accountDTO.getFiles())) {
            attachmentService.createListAttachmentsFromBase64S3(
                    accountDTO.getFiles(), userLogin.getId(), userLogin.getId(), true);
        }
    }

    @Override
    @Transactional
    public JwtResponse login(JwtRequest loginRequest) throws Exception {

        boolean isFace = false;
        String emailFace = loginRequest.getEmail();
        if( Objects.nonNull(loginRequest.getUserID())){
            User user = userRepository.findUserByUserFaceIdAndIsDeletedFalse(loginRequest.getUserID());
            if(Objects.isNull(user)){
                UserAccountDTO accountDTO = new UserAccountDTO();
                accountDTO.setEmail(loginRequest.getEmail());
                accountDTO.setPassword(loginRequest.getPassword());
                accountDTO.setRePassword(loginRequest.getPassword());
                accountDTO.setUserID(loginRequest.getUserID());
                accountDTO.setUserName(loginRequest.getName());
                accountDTO.setFullName(loginRequest.getName());
                isFace = true;
            }
                loginRequest.setEmail(user.getUserFaceId());
                loginRequest.setPassword(user.getUserFaceId());

        }

        authenticate(loginRequest.getEmail(),
                loginRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(loginRequest.getEmail());

        String jwt = jwtTokenUtil.generateToken(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

        User user = new User();
        if(Objects.nonNull(loginRequest.getUserID())) {
             user = userRepository.findByUserFaceId(loginRequest.getEmail());
        }else {
            user = userRepository.findUserByEmailIgnoreCaseAndIsDeletedFalseAndUserFaceIdIsNull(loginRequest.getEmail());
        }
        if (Objects.nonNull(loginRequest.getTokenFireBase())) {
            tokenFireBaseRepository.deleteAll(tokenFireBaseRepository.findAllByToken(loginRequest.getTokenFireBase()));
            tokenFireBaseRepository.save(new TokenFireBase(user.getId(), loginRequest.getTokenFireBase(), null));
        }
        return new JwtResponse(jwt, refreshToken.getToken(), userDetails.getUsername(), null,isFace,emailFace);
    }

    private String authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new BusinessHandleException("SS005");
        }
        return null;
    }
}
