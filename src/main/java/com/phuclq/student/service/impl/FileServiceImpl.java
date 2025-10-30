package com.phuclq.student.service.impl;

//import com.aspose.words.Document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.phuclq.student.common.Constants;
import com.phuclq.student.domain.*;
import com.phuclq.student.dto.*;
import com.phuclq.student.exception.BusinessException;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.exception.ExceptionUtils;
import com.phuclq.student.exception.NotFoundException;
import com.phuclq.student.repository.*;
import com.phuclq.student.service.*;
import com.phuclq.student.types.*;
import com.phuclq.student.utils.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.phuclq.student.types.ActivityConstants.CARD;
import static com.phuclq.student.types.ActivityConstants.LIKE;
import static com.phuclq.student.types.RoleType.ADMIN;
import static com.phuclq.student.utils.StringUtils.getSearchableStringUrl;
import static com.phuclq.student.utils.StringUtils.getSearchableStringUrlExit;

@Service
@Transactional
public class FileServiceImpl implements FileService {

    private static List<RequestFileDTO> dtos;
    private final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);
    @Autowired
    RateRepository rateRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private FilePriceRepository filePriceRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private UserCoinRepository userCoinRepository;
    @Autowired
    private UserCoinBackupRepository userCoinBackupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserHistoryRepository userHistoryRepository;
    @Autowired
    private UserHistoryFileRepository userHistoryFileRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private IndustryRepository industryRepository;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserHistoryService userHistoryService;
    @Autowired
    private FCMService fcmService;
    @Autowired
    private RequestHistorySubService requestHistorySubService;

    @Value("${coin.vip}")
    private int coinVip;

    @Value("${coin.upload}")
    private Double coinUpload;

    @Value("${coin.percent.file}")
    private int percentFile;

    @Value("${coin.percent.file.admin}")
    private int percentFileAdmin;

    @Value("${student.account.admin}")
    private String adminId;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailSenderService emailSenderService;

    private static String zipB64(List<RequestFileDTO> dto) throws IOException {
        List<java.io.File> files = convertBase64toFile(dto);
        return filesZip(files);
    }

    static List<java.io.File> convertBase64toFile(List<RequestFileDTO> dtos) {
        List<java.io.File> files = new ArrayList<>();
        dtos.forEach(x -> {
            try {
                java.io.File file = convertMultiPartToFile(uploadFile(x.getContent(), x.getName()));
                files.add(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return files;

    }

    private static java.io.File convertMultiPartToFile(MultipartFile file) throws IOException {
        java.io.File convFile = new java.io.File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public static MultipartFile uploadFile(String base64, String fileName) {
        final String[] base64Array = base64.split(",");
        String dataUir, data;
        if (base64Array.length > 1) {
            dataUir = base64Array[0];
            data = base64Array[1];
        } else {
            //Build according to the specific file you represent
            dataUir = "data:image/jpg;base64";
            data = base64Array[0];
        }

        return new Base64ToMultipartFile(dataUir + "," + data, dataUir, fileName);
    }

    static String filesZip(List<java.io.File> files) throws IOException {
        String newZipFileName = files.get(0).getName().replace(files.get(0).getName().substring(files.get(0).getName().lastIndexOf(".")), ".zip");
        String replaceName = StringUtils.getSearchableString(generateFileName(newZipFileName)).replace(" ", "");
        FileOutputStream fos = new FileOutputStream(replaceName);
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        bo.writeTo(fos);


        ZipOutputStream zipOut = new ZipOutputStream(bo);

        for (java.io.File xlsFile : files) {
            if (!xlsFile.isFile()) continue;
            ZipEntry zipEntry = new ZipEntry(xlsFile.getName());
            zipOut.putNextEntry(zipEntry);
            zipOut.write(IOUtils.toByteArray(new FileInputStream(xlsFile)));
            zipOut.closeEntry();
        }
        zipOut.setComment(replaceName);
        zipOut.close();
        return "data:application/x-zip-compressed;base64," + new String(Base64.encodeBase64(bo.toByteArray()));
    }

    @Override
    public Page<File> findFilesByCategory(Integer categoryId, Pageable pageable) {
        Page<File> filePage = fileRepository.findFilesByCategory(categoryId, pageable);
        return filePage;
    }

    @Override
    public FileDTO getFile(Integer id) {
        Optional<File> optionalFile = fileRepository.findById(id);
        File file = new File();
        if (optionalFile.isPresent()) {
            file = optionalFile.get();
        }
        byte[] array = null;
        try {
            array = FileUtils.readFileToByteArray(new java.io.File(file.getFileCut())); // Doc file theo đường dẫn và tên
        } catch (Exception e) {
            log.error("Err file > " + e.getMessage(), e);
        }
        User user = new User();
        Optional<User> userOptional = userService.findUserById(file.getAuthorId());
        if (optionalFile.isPresent()) {
            user = userOptional.get();
        }

        Optional<Category> category = categoryRepository.findById(file.getCategoryId());
        Optional<Industry> industry = industryRepository.findById(file.getIndustryId());
        FilePrice filePrice = filePriceRepository.findByFileId(file.getId());
        FileDTO fileDTO = new FileDTO(file);
        fileDTO.setAuthorName(user.getUserName());
        fileDTO.setFileId(file.getId());
        fileDTO.setCountView(file.getView() + 1);
        fileDTO.setCountDownload(file.getDowloading());
        fileDTO.setFileTitle(file.getTitle());
        fileDTO.setCategoryId(category.get().getId());
        fileDTO.setCategoryName(category.get().getCategory());
        fileDTO.setIndustryName(industry.get().getValue());
        fileDTO.setFileBase64(array);
        fileDTO.setFilePrice(filePrice.getPrice());
        int countView = file.getView() + 1;
        optionalFile.get().setView(countView);
        fileRepository.save(optionalFile.get());
        return fileDTO;
    }

    @Override
    public Page<File> searchFiles(Integer category, Integer specialization, Integer school, String title, Boolean isVip, Float price, Pageable pageable) {
        return fileRepository.searchFiles(category, specialization, school, title, isVip, price, pageable);
    }

    @Override
    @Async
    @Transactional
    public void uploadFile(FileUploadRequest dto) throws Exception {
        long startTimeCallWs = System.currentTimeMillis();
        Integer login = userService.getUserLogin().getId();
        if (dto.getIsVip() && Objects.isNull(dto.getId())) {
            boolean status = registryFileVip(login);
            if (!status) {
                throw new BusinessHandleException("SS006");
            }
        }

        List<RequestFileDTO> files = dto.getFiles();
        try {
            if (Objects.nonNull(dto.getFiles())) {
                String zipB64 = zipB64(files.stream().filter(x -> !x.getType().equals(FileType.FILE_AVATAR.getName())).collect(Collectors.toList()));
                RequestFileDTO requestFileDTOZip = new RequestFileDTO();
                requestFileDTOZip.setType(FileType.FILE_ZIP.getName());
                requestFileDTOZip.setType(FileType.FILE_ZIP.getName());
                requestFileDTOZip.setName(com.phuclq.student.utils.StringUtils.getSearchableString(generateFileName(dto.getTitle() + FileType.FILE_ZIP.getName())).replace(" ", ""));
                requestFileDTOZip.setExtension(".zip");
                requestFileDTOZip.setContent(zipB64);
                files.add(requestFileDTOZip);

                RequestFileDTO requestFileDTO = files.stream().filter(x -> x.getType().equals(FileType.FILE_UPLOAD.getName())).findFirst().get();
                if (requestFileDTO.getExtension().equalsIgnoreCase(".PDF")) {
                    RequestFileDTO cutFileShow = cutFileShow(dto.getStartPageNumber(), dto.getEndPageNumber(), requestFileDTO);
                    files.add(cutFileShow);
                }
                List<String> docs = Arrays.asList(".DOC", ".DOCX");
                if (docs.contains(requestFileDTO.getExtension().toUpperCase())) {
                    String fileName = com.phuclq.student.utils.StringUtils.getSearchableString(generateFileName(FileType.FILE_CONVERT_DOC_PDF.getName() + requestFileDTO.getName()).replace(" ", ""));
                    RequestFileDTO convertDoctoPdf = SplitDocs.convertDoctoPdf(requestFileDTO.getContent(), fileName);

                    if (convertDoctoPdf.getExtension().equalsIgnoreCase(".PDF")) {
                        RequestFileDTO cutFileShow = cutFileShow(dto.getStartPageNumber(), dto.getEndPageNumber(), convertDoctoPdf);
                        files.add(cutFileShow);
                    }

                }
            }
            if (Objects.isNull(dto.getId())) {
                File file = new File(login);
                BeanUtils.copyProperties(dto, file);
                file.setIdUrl(getSearchableStringUrl(dto.getTitle(), fileRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getTitle())).size()));
                File saveFile = fileRepository.save(file);
                Double price = dto.getFilePrice() != null ? dto.getFilePrice() : 0;
                FilePrice filePrice = new FilePrice(saveFile.getId(), price, login);
                filePriceRepository.save(filePrice);
                attachmentService.createListAttachmentsFromBase64S3(files, saveFile.getId(), login, true);
                userHistoryService.activateFileHistory(login, file.getId(), ActivityConstants.UPLOAD);
            } else {
                File byId = fileRepository.findById(dto.getId()).get();
                if (dto.getIsVip() && !byId.getIsVip()) {
                    boolean status = registryFileVip(login);
                    if (!status) {
                        throw new BusinessHandleException("SS006");
                    }
                }
                int exit = 0;
                if (Objects.nonNull(dto.getTitle())) {
                    exit = fileRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getTitle())).size();
                }
                File file = updateFile(byId, dto, login, exit);
                fileRepository.save(file);
                if (Objects.nonNull(dto.getFiles())) {
                    attachmentService.createListAttachmentsFromBase64S3(files, file.getId(), login, true);
                }
            }
        } catch (Exception e) {
            ObjectMapper mapper = new ObjectMapper();
            // Convert object to JSON string
            String requestContent = StringUtils.convertObjectToJson(dto.getFiles().stream().filter(x -> x.getType().equals(FileType.FILE_UPLOAD.getName())).collect(Collectors.toList()));
            requestHistorySubService.saveLog("/file/upload", requestContent, e.toString(),
                    400, RequestType.ERROR_UPLOAD_FILE, startTimeCallWs, login);
        }

    }

    @Override
    public File uploadFileAdmin(FileUploadRequest dto) throws IOException, DocumentException {
        File byId = fileRepository.findById(dto.getId()).get();
        if (Objects.nonNull(dto.getFiles())) {
            Integer login = userService.getUserLogin().getId();
            attachmentService.createListAttachmentsFromBase64S3(dto.getFiles(), byId.getId(), login, true);
        }
        return null;
    }

    public File updateFile(File file, FileUploadRequest dto, Integer loginId, int exit) {
        if (Objects.nonNull(dto.getTitle())) {
            file.setTitle(dto.getTitle());
            file.setIdUrl(getSearchableStringUrl(dto.getTitle(), exit));
        }
        if (Objects.nonNull(dto.getIsVip())) {
            file.setIsVip(dto.getIsVip());
        }
        if (Objects.nonNull(dto.getCategoryId())) {
            file.setCategoryId(dto.getCategoryId());
        }
        if (Objects.nonNull(dto.getIndustryId())) {
            file.setIndustryId(dto.getIndustryId());
        }
        if (Objects.nonNull(dto.getSpecializationId())) {
            file.setSpecializationId(dto.getSpecializationId());
        }
        if (Objects.nonNull(dto.getLanguageId())) {
            file.setLanguageId(dto.getLanguageId());
        }
        if (Objects.nonNull(dto.getSchoolId())) {
            file.setSchoolId(dto.getSchoolId());
        }
        if (Objects.nonNull(dto.getDescription())) {
            file.setDescription(dto.getDescription());
        }
        if (Objects.nonNull(dto.getFilePrice())) {
            FilePrice filePrice = filePriceRepository.findByFileId(dto.getId());
            filePrice.setPrice(dto.getFilePrice());
            filePrice.setLastUpdatedBy(loginId.toString());
            filePrice.setLastUpdatedDate(LocalDateTime.now());
            filePriceRepository.save(filePrice);
        }
        if (Objects.nonNull(dto.getLanguageId())) {
            file.setLanguageId(dto.getLanguageId());
        }
        file.setApprovedDate(null);
        file.setApproverId(null);
        return file;
    }

    public UserCoin findByUserId(Integer userId) {
        UserCoin byUserId = userCoinRepository.findByUserId(userId);
        return Objects.nonNull(byUserId) ? byUserId : new UserCoin(userId, 0D);
    }

    public UserCoinBackup findByUserIdBackUp(Integer userId) {
        UserCoinBackup byUserId = userCoinBackupRepository.findByUserId(userId);
        return Objects.nonNull(byUserId) ? byUserId : new UserCoinBackup(userId, 0D);
    }

    @Override
    public boolean registryFileVip(Integer userId) {
        UserCoin userCoin = findByUserId(userId);
        UserCoinBackup userCoinBackUp = findByUserIdBackUp(userId);
        if (userCoin != null) {
            double totalCoin = userCoin.getTotalCoin() != null ? userCoin.getTotalCoin() : 0;
            if (totalCoin > coinVip) {
                totalCoin -= coinVip;
                userCoin.setTotalCoin(totalCoin);
                userCoinRepository.save(userCoin);

                userCoinBackUp.setTotalCoin(totalCoin);
                userCoinBackupRepository.save(userCoinBackUp);
                historyCoinVip(userId, (double) coinVip, totalCoin);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Transactional
    @Override
    public AttachmentDTO downloadDocument(DownloadFileDTO downloadFileDTO) {
        User user = userService.getUserLogin();
        boolean passwordDefine = passwordEncoder.matches(downloadFileDTO.getPassword(), user.getPassword());

        if (passwordDefine) {

            File file = fileRepository.findByIdUrl(downloadFileDTO.getIdUrl()).orElseThrow(() -> new BusinessException(ExceptionUtils.REQUEST_NOT_EXIST));
            userHistoryService.activateFileHistory(user.getId(), file.getId(), ActivityConstants.DOWNLOAD);
            file.setDowloading(Objects.isNull(file.getDowloading()) ? 1 : file.getDowloading() + 1);
            FilePrice filePrice = filePriceRepository.findByFileId(file.getId());
            UserCoin userCoinDownload = findByUserId(user.getId());
            UserCoinBackup userCoinDownloadBackup = findByUserIdBackUp(user.getId());
            if (userCoinDownload != null) {
                Double fileCost = filePrice.getPrice() != null ? filePrice.getPrice() : 0;
                Double userTotalCoinDownload = userCoinDownload.getTotalCoin() != null ? userCoinDownload.getTotalCoin() : 0;
                if (userTotalCoinDownload >= fileCost) {
                    userCoinDownload.setTotalCoin(userTotalCoinDownload - fileCost);
                    UserCoin userUpload = findByUserId(file.getAuthorId());
                    UserCoinBackup userUploadBackup = findByUserIdBackUp(file.getAuthorId());

                    Double costMoney = (fileCost * percentFile) / 100;
                    Double totalCoinUpload = Objects.isNull(userUpload.getTotalCoin()) ? 0 : userUpload.getTotalCoin();
                    userUpload.setTotalCoin(totalCoinUpload + costMoney);

                    userCoinRepository.save(userCoinDownload);
                    userCoinBackupRepository.save(userCoinDownloadBackup);

                    userCoinRepository.save(userUpload);
                    userCoinBackupRepository.save(userUploadBackup);

                    historyCoin(user, file, fileCost, costMoney, userTotalCoinDownload - fileCost, totalCoinUpload + costMoney);


                    User userUploadInfo = userRepository.findById(file.getAuthorId()).get();
                    SendMailDto sendMailDto = new SendMailDto();
                    sendMailDto.setMoney(costMoney);
                    sendMailDto.setEmail(userUploadInfo.getEmail());
                    sendMailDto.setName(userUploadInfo.getFullName());
                    sendMailDto.setSub("TÀI LIỆU CỦA BẠN ĐƯỢC TẢI BẠN NHẬN ĐƯỢC " + sendMailDto.getMoney() + " XU");
                    sendMailDownload(sendMailDto);
                    sumAdmin(userRepository.findUserByEmailIgnoreCaseAndIsDeletedFalseAndUserFaceIdIsNull(adminId).getId(), (fileCost * percentFileAdmin) / 100, true);
                    //notification
                    fcmService.tokenFireBase(NotificationType.DOWNLOADED.getTitle(), NotificationType.DOWNLOADED.getMessage(), new Notification(NotificationType.DOWNLOADED.getCode(), file.getCreatedBy(), NotificationType.DOWNLOADED.getType(), StatusType.DONE.getName(), NotificationType.DOWNLOADED.getMessage(), NotificationType.DOWNLOADED.getImageIcon(), NotificationType.DOWNLOADED.getUrlDetail()));
                    fileRepository.save(file);
                    return attachmentService.getAttachmentByRequestIdFromS3AndTypes(file.getId(), Collections.singletonList(FileType.FILE_ZIP.getName())).get(0);
                } else {
                    throw new BusinessHandleException("SS009");
                }
            } else {
                throw new BusinessHandleException("SS009");
            }
        } else {
            throw new BusinessHandleException("SS008");
        }
    }

    public void sumAdmin(Integer userId, Double amount, Boolean admin) {
        UserCoin userCoinDownload = findByUserId(userId);
        userCoinDownload.setTotalCoin(Objects.nonNull(userCoinDownload.getTotalCoin()) ? userCoinDownload.getTotalCoin() + amount : amount);
        userCoinRepository.save(userCoinDownload);

        UserCoinBackup userCoinDownloadBackup = findByUserIdBackUp(userId);
        userCoinDownloadBackup.setTotalCoin(Objects.nonNull(userCoinDownloadBackup.getTotalCoin()) ? userCoinDownloadBackup.getTotalCoin() + amount : amount);
        userCoinBackupRepository.save(userCoinDownloadBackup);
        if (admin) {
            historyCoin(userId, amount, Objects.nonNull(userCoinDownloadBackup.getTotalCoin()) ? userCoinDownloadBackup.getTotalCoin() + amount : amount);
        } else {
            historyCoinUpload(userId, amount, Objects.nonNull(userCoinDownloadBackup.getTotalCoin()) ? userCoinDownloadBackup.getTotalCoin() + amount : amount);
        }
    }

    private void historyCoin(Integer userId, Double coin, Double totalCoin) {


    }

    private void historyCoinUpload(Integer userId, Double coin, Double totalCoin) {


    }

    private void historyCoinVip(Integer userId, Double coin, Double totalCoin) {


    }

    private void historyCoin(User user, File file, Double fileCost, Double costMoney, Double totalCoinDownload, Double totalCoinUpload) {

    }

    void sendMailDownload(SendMailDto sendMailDto) {
        Context context = new Context();
        context.setVariable("name", sendMailDto.getName());
        context.setVariable("money", sendMailDto.getMoney());
        emailSenderService.sendHtmlMessage(sendMailDto.getEmail(), sendMailDto.getSub(), context, "downloadFile.html");
    }

    private static String generateFileName(String fileName) {
        String time = Instant.now().toString();
        time = time.replace(":", "");
        time = time.replace(".", "");
        time = time.replace("T", "");
        time = time.replace("Z", "");


        return time + fileName;
    }

    @Override
    public void approveFile(Integer id) {
        File file = fileRepository.findById(id).orElseThrow(NotFoundException::new);
        file.setApproverId(userService.getUserLogin().getId());
        file.setApprovedDate(DateTimeUtils.timeNow());
        fileRepository.save(file);
        fcmService.tokenFireBase(NotificationType.FILE_ACCEPTED.getTitle(), NotificationType.FILE_ACCEPTED.getMessage(), new Notification(NotificationType.FILE_ACCEPTED.getCode(), file.getCreatedBy(), NotificationType.FILE_ACCEPTED.getType(), StatusType.DONE.getName(), NotificationType.FILE_ACCEPTED.getMessage(), NotificationType.FILE_ACCEPTED.getImageIcon(), NotificationType.FILE_ACCEPTED.getUrlDetail()));
    }

    @Override
    public List<CategoryHomeDTO> getCategoriesHome() {
        List<CategoryHomeDTO> categoryHomeDTOList = new ArrayList<CategoryHomeDTO>();
        List<Category> categoryList = categoryRepository.findAll();
        categoryList.forEach(category -> {
            CategoryHomeDTO categoryHomeDTO = new CategoryHomeDTO();
            categoryHomeDTO.setId(category.getId());
            categoryHomeDTO.setNameCategory(category.getCategory());
            categoryHomeDTO.setCountCategory(fileRepository.countCategoriesHome(category.getId()));
            categoryHomeDTOList.add(categoryHomeDTO);
        });

        return categoryHomeDTOList;
    }

    @Override
    public CategoryHomeFileResult filesPage(FileHomePageRequest request, Pageable pageable) {
        setRandomHome(request);
        CategoryHomeFileResult categoryHomeFileResult = new CategoryHomeFileResult();
        Page<CategoryFilePageDTO> listCategory = Objects.nonNull(request.getCategoryIds()) && !request.getCategoryIds().isEmpty() ? categoryRepository.findAllByIdInFile(request.getCategoryIds(), pageable,request.getSearch()) : categoryRepository.findAllByIdInFile(pageable,request.getSearch());
        List<FileHomeDoFilterDTO> listFile = new ArrayList<FileHomeDoFilterDTO>();
        User userLogin = userService.getUserLogin();
        List<UserHistoryDTO> fileHistoryHome = new ArrayList<>();
        if (Objects.nonNull(userLogin.getId())) {
            fileHistoryHome = userHistoryFileRepository.findFileHistoryHome(userLogin.getId());

        }

        List<UserHistoryDTO> finalFileHistoryHome = fileHistoryHome;
        listCategory.stream().parallel().forEach(category -> {
            FileHomeDoFilterDTO file = new FileHomeDoFilterDTO();
            file.setCategory(category.getName());
            file.setId(category.getId());
            List<FileResult> fileByCategory = searchFileInCategory(request, category.getId());
            fileByCategory.parallelStream().forEach(x -> {

                setLikeAndCard(finalFileHistoryHome, x, userLogin);
            });
            if(!fileByCategory.isEmpty()) {
                file.setListFile(fileByCategory);
                listFile.add(file);
            }
        });
        categoryHomeFileResult.setFileHomeDoFilterDTOS(listFile);
        PaginationModel paginationModel = new PaginationModel(listCategory.getPageable().getPageNumber(),Objects.isNull(request.getCategoryIds())|| request.getCategoryIds().isEmpty() ? 4: listFile.size(),(int) listCategory.getTotalElements());
        categoryHomeFileResult.setPaginationModel(paginationModel);
        return categoryHomeFileResult;
    }

    private void setRandomHome(FileHomePageRequest request) {
        if(Objects.nonNull(request.getHome())&& request.getHome()){
            List<Long> category = categoryRepository.getCategoriesHome().stream().map(CategoryHomeResult::getId).collect(Collectors.toList());
            Random random = new Random();
            int randomIndex = !category.isEmpty() ?random.nextInt(category.size()):1;
            request.setCategoryId(!category.isEmpty() ?category.get(randomIndex).intValue():1);
        }
    }

    public List<FileResult> searchFileInCategory(FileHomePageRequest request, BigInteger categoryIds) {
        List<Object> objList = null;

        StringBuilder sqlStatement = new StringBuilder();
        List<Object> listParam = new ArrayList<Object>();
        sqlStatement.append("from file f  inner join category c on f.category_id = c.id inner join file_price fp on f.id = fp.file_id " + " left join industry i on f.industry_id = i.id left join attachment a on f.id = a.request_id and a.file_type = " + "'" + FileType.FILE_AVATAR.getName() + "'" + " inner join user u on f.author_id = u.id left join attachment ab on u.id = ab.request_id and ab.file_type = " + "'" + FileType.USER_AVATAR.getName() + "'" + "where f.approver_id is not null and f.is_deleted =0  ");
        sqlStatement.append(" and f.category_id = ? ");
        listParam.add(categoryIds);
        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            sqlStatement.append(" and (LOWER(f.title) like LOWER(?) ");
            sqlStatement.append(" or LOWER(i.value) like LOWER(?) ");
            sqlStatement.append(" or LOWER(c.category) like LOWER(?) ");
            sqlStatement.append(" or LOWER(u.user_name) like LOWER(?)) ");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
        }
        if (Objects.nonNull(request.getPriceStart())) {
            sqlStatement.append(" and fp.price >= ? ");
            listParam.add(request.getPriceStart());
        }

        if (Objects.nonNull(request.getPriceEnd())) {
            sqlStatement.append(" and fp.price <= ? ");
            listParam.add(request.getPriceEnd());
        }
        if (Objects.nonNull(request.getIndustry())) {
            sqlStatement.append(" and f.industry_id = ? ");
            listParam.add(request.getIndustry());
        }
        if (Objects.nonNull(request.getSchool())) {

            sqlStatement.append(" and f.school_id = ? ");
            listParam.add(request.getSchool());
        }
        if (Objects.nonNull(request.getIsVip())&& request.getIsVip()) {

            sqlStatement.append(" and f.is_vip = ? ");
            listParam.add(request.getIsVip());
        }

        if (Objects.nonNull(request.getOrderType())) {
            if (request.getOrderType().equals(OrderFileType.DOWNLOADS.getCode())) {
                sqlStatement.append(" order by f.dowloading ").append(request.getOrder());
            }
            if (request.getOrderType().equals(OrderFileType.FAVORITES.getCode())) {
                sqlStatement.append(" order by f.total_like ").append(request.getOrder());
            }
            if (request.getOrderType().equals(OrderFileType.PRICE.getCode())) {
                sqlStatement.append(" order by  fp.price ").append(request.getOrder());
            }
            if (request.getOrderType().equals(OrderFileType.VIEW.getCode())) {
                sqlStatement.append(" order by f.view ").append(request.getOrder());
            }
            sqlStatement.append(" ,case when f.start_money_top is null then 1 else 0 end,f.start_money_top,f.id desc ");
        } else {
            sqlStatement.append(" order BY case when f.start_money_top is null then 1 else 0 end,f.start_money_top,f.id desc ");
        }

        sqlStatement.append(" LIMIT ? OFFSET ?");
        listParam.add(request.getSizeFile());
        Integer page = request.getSizeFile() == 12 ? 0 : request.getPage();
        listParam.add(request.getSizeFile() * page);
        Query query = entityManager.createNativeQuery(Constants.SQL_FILE + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i + 1, listParam.get(i));
        }
        objList = query.getResultList();
        List<FileResult> list = new ArrayList<>();
        for (Object obj : objList) {
            FileResult result = new FileResult((Object[]) obj);
            list.add(result);
        }

        return list;

    }

    @Override
    public FileResultDto searchFileCategory(FileHomePageRequest request, Integer categoryId, Pageable pageable) {
        setRandomHome(request);
        List objList = null;
        User userLogin = userService.getUserLogin();
        Integer loginId = userLogin.getId();
        StringBuilder sqlStatement = new StringBuilder();
        List<Object> listParam = new ArrayList<Object>();
        sqlStatement.append("from file f inner join category c on f.category_id = c.id inner join file_price fp on f.id = fp.file_id " + "left join industry i on f.industry_id = i.id left join attachment a on f.id = a.request_id and a.file_type =" + "'").append(FileType.FILE_AVATAR.getName()).append("'").append(" inner join user u on f.author_id = u.id left join attachment ab on u.id = ab.request_id and ab.file_type = ").append("'").append(FileType.USER_AVATAR.getName()).append("'");
        if (Objects.nonNull(userLogin.getRoleId()) && userLogin.getRoleId().equals(ADMIN)) {
            sqlStatement.append("where f.is_deleted =0  ");
        } else {
            sqlStatement.append("where f.approver_id is not null and f.is_deleted =0  ");
        }

        if (Objects.nonNull(categoryId)) {
            sqlStatement.append(" and f.category_id = ? ");
            listParam.add(categoryId);
        }
        if (Objects.nonNull(request.getIsApprove()) && request.getIsApprove()) {
            sqlStatement.append(" and f.approver_id is not null ");
        }
        if (Objects.nonNull(request.getIsApprove()) && !request.getIsApprove()) {
            sqlStatement.append(" and f.approver_id is  null ");
        }
        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            sqlStatement.append(" and (LOWER(f.title) like LOWER(?) ");
            sqlStatement.append(" or LOWER(i.value) like LOWER(?) ");
            sqlStatement.append(" or LOWER(u.user_name) like LOWER(?)) ");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
        }
        if (Objects.nonNull(request.getPriceStart())) {
            sqlStatement.append(" and fp.price >= ? ");
            listParam.add(request.getPriceStart());
        }

        if (Objects.nonNull(request.getPriceEnd())) {
            sqlStatement.append(" and fp.price <= ? ");
            listParam.add(request.getPriceEnd());
        }
        if (Objects.nonNull(request.getIsVip()) && request.getIsVip()) {

            sqlStatement.append(" and f.is_vip = ? ");
            listParam.add(request.getIsVip());
        }
        if (Objects.nonNull(request.getIsDuplicate()) && request.getIsDuplicate()) {

            sqlStatement.append(" and f.is_duplicate = ? ");
            listParam.add(request.getIsDuplicate());
        }
        if (Objects.nonNull(request.getIndustry())) {
            sqlStatement.append(" and f.industry_id = ? ");
            listParam.add(request.getIndustry());
        }
        if (Objects.nonNull(request.getSchool())) {

            sqlStatement.append(" and f.school_id = ? ");
            listParam.add(request.getSchool());
        }
        if (Objects.nonNull(request.getFileId())) {
            sqlStatement.append(" and f.id = ? ");
            listParam.add(request.getFileId());
            if (Objects.nonNull(request.getIsBase64()) && request.getIsBase64()) {
                sqlStatement.append(" and f.author_id = ? ");
                listParam.add(loginId);
            }
        }
        if (Objects.nonNull(request.getIdUrl())) {
            sqlStatement.append(" and f.ID_URL = ? ");
            listParam.add(request.getIdUrl());
            if (Objects.nonNull(request.getIsBase64()) && request.getIsBase64()) {
                sqlStatement.append(" and f.author_id = ? ");
                listParam.add(loginId);
            }
        }

        if (Objects.nonNull(request.getIsDuplicate()) && request.getIsDuplicate()) {
            sqlStatement.append(" order by f.CODE_FILE desc ");
        }else {
            if (Objects.nonNull(request.getOrderType())) {
                if (request.getOrderType().equals(OrderFileType.DOWNLOADS.getCode())) {
                    sqlStatement.append(" order by f.dowloading " + request.getOrder());
                }
                if (request.getOrderType().equals(OrderFileType.FAVORITES.getCode())) {
                    sqlStatement.append(" order by f.total_like " + request.getOrder());
                }
                if (request.getOrderType().equals(OrderFileType.PRICE.getCode())) {
                    sqlStatement.append(" order by  fp.price " + request.getOrder());
                }
                if (request.getOrderType().equals(OrderFileType.VIEW.getCode())) {
                    sqlStatement.append(" order by f.view " + request.getOrder());
                }
                sqlStatement.append(" ,case when f.start_money_top is null then 1 else 0 end,f.start_money_top,f.id desc ");
            } else {
                sqlStatement.append(" order BY case when f.start_money_top is null then 1 else 0 end,f.start_money_top,f.id desc ");
            }
        }

        Query queryCount = entityManager.createNativeQuery(" select count(f.id) " + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            queryCount.setParameter(i + 1, listParam.get(i));
        }
        Integer count = ((Number) queryCount.getSingleResult()).intValue();

        sqlStatement.append(" LIMIT ? OFFSET ?");
        listParam.add(request.getSize());
        listParam.add(request.getSize() * request.getPage());
        Query query = entityManager.createNativeQuery(Constants.SQL_FILE + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i + 1, listParam.get(i));
        }
        objList = query.getResultList();
        List<FileResult> list = new ArrayList<>();
        for (Object obj : objList) {
            FileResult result = new FileResult((Object[]) obj);
            list.add(result);
        }

        Page<FileResult> file = new PageImpl<FileResult>(list, pageable, count);
        //add code
        List<Attachment> allByRequestIdInAndFileTypeIn = attachmentRepository.findAllByRequestIdInAndFileTypeIn(file.stream().map(FileResult::getId).collect(Collectors.toList()), Collections.singletonList(FileType.FILE_CUT.getName()));

        FileResultDto fileResultDto = new FileResultDto();
        List<UserHistoryDTO> fileHistoryHome = new ArrayList<>();
        if (Objects.nonNull(loginId)) {
            fileHistoryHome = userHistoryFileRepository.findFileHistoryHome(loginId);

        }
        List<UserHistoryDTO> finalFileHistoryHome = fileHistoryHome;
        file.stream().parallel().forEach(x -> {
            x.setIsFileCut(allByRequestIdInAndFileTypeIn.stream().anyMatch(y -> y.getRequestId().equals(x.getId())));

            setLikeAndCard(finalFileHistoryHome, x, userLogin);
            if ((Objects.nonNull(request.getFileId()) && Objects.nonNull(request.getIsBase64()) && (request.getIsBase64())) || (Objects.nonNull(request.getIdUrl()) && Objects.nonNull(request.getIsBase64()) && (request.getIsBase64()))) {
                List<Attachment> attachmentOptional = attachmentRepository.findAllByRequestIdAndFileTypeIn(x.getId(), Arrays.asList(FileType.FILE_AVATAR.getName(), FileType.FILE_UPLOAD.getName(), FileType.FILE_DEMO.getName()));
                x.setAttachmentOptional(attachmentOptional);
            }

            if ((Objects.nonNull(request.getFileId()) && Objects.nonNull(request.getIsBase64()) && (!request.getIsBase64())) || (Objects.nonNull(request.getIdUrl()) && Objects.nonNull(request.getIsBase64()) && (!request.getIsBase64()))) {
                try {
                    AttachmentDTO fileCut = attachmentService.getAttachmentByRequestIdFromS3(x.getId(), FileType.FILE_CUT.getName());
                    AttachmentDTO attachmentByRequestIdFromS3 = Objects.nonNull(fileCut) ? fileCut : attachmentService.getAttachmentByRequestIdFromS3(x.getId(), FileType.FILE_UPLOAD.getName());
                    x.setAttachmentDTO(attachmentByRequestIdFromS3);

                    // get comment
                    List<Comment> listComment = commentRepository.findAllByRequestIdAndTypeOrderByIdDesc(x.getIdUrl(), CommentType.COMMENT_FILE.getName());
                    listComment.forEach(y -> {
                        y.setIsDelete(Objects.nonNull(loginId) && y.getCreatedBy().equals(Objects.requireNonNull(loginId).toString()));
                    });
                    x.setComments(listComment);

                    // get rate

                    x.setTotalRate(Arrays.stream(ArrayUtils.toPrimitive(rateRepository.findAllByRequestIdAndType(x.getIdUrl(), RateType.RATE_FILE.getName()).stream().map(Rate::getRate).toArray(Double[]::new))).average().orElse(0));
                    if (Objects.nonNull(loginId)) {

                        x.setTotalRateUser(Arrays.stream(ArrayUtils.toPrimitive(rateRepository.findAllByRequestIdAndType(x.getIdUrl(), RateType.RATE_FILE.getName()).stream().filter(y -> y.getCreatedBy().equals(loginId.toString())).map(Rate::getRate).toArray(Double[]::new))).average().orElse(0));

                    }
                } catch (IOException e) {
                }
                File byId = fileRepository.findById(x.getId()).get();
                byId.setView(Objects.isNull(byId.getView()) ? 1 : byId.getView() + 1);
                fileRepository.save(byId);
            }
        });
        fileResultDto.setList(file.getContent());
        PaginationModel paginationModel = new PaginationModel(file.getPageable().getPageNumber(), file.getPageable().getPageSize(), (int) file.getTotalElements());
        fileResultDto.setPaginationModel(paginationModel);
        return fileResultDto;
    }

    private void setLikeAndCard(List<UserHistoryDTO> finalFileHistoryHome, FileResult x, User userLogin) {
        if (finalFileHistoryHome.size() > 0) {
            List<UserHistoryDTO> collect = finalFileHistoryHome.stream().filter(f -> f.getFileId().equals(x.getId())).collect(Collectors.toList());
            if (collect.size() > 0) {
                x.setIsLike(collect.stream().anyMatch(f -> f.getActivityId().equals(LIKE)));
                x.setIsCard(collect.stream().anyMatch(f -> f.getActivityId().equals(CARD)));
            }
            x.setIsMy(Objects.nonNull(userLogin) && Objects.nonNull(userLogin.getId()) && userLogin.getId().toString().equals(x.getCreatedBy()));
        }
    }

    @Override
    public Page<FileApprove> getFileUnApprove(Pageable pageable) {
        Page<File> page = fileRepository.findByApproverId(null, pageable);
        List<FileApprove> list = new ArrayList<>();
        page.getContent().forEach(file -> {
            FileApprove fileApprove = new FileApprove(file);
            Optional<User> userOptional = userRepository.findById(file.getAuthorId());
            fileApprove.setAuthor(userOptional.get().getUserName());
            list.add(fileApprove);
        });
        Page<FileApprove> result = new PageImpl<FileApprove>(list, pageable, page.getTotalElements());
        return result;
    }

    public List<File> findTop8FileOrderByIdDesc() {
        return fileRepository.findTop8FileOrderByIdDesc();
    }

    @Override
    public Page<FileResult> searchfileDownloaded(Integer userId, Pageable pageable) {

        List<UserHistory> list = userHistoryRepository.findDownloadUserHistory(userId, pageable);
        List<UserHistoryFile> uFList = new ArrayList<UserHistoryFile>();
        for (UserHistory userHistory : list) {

            List<UserHistoryFile> historyFileList = userHistoryFileRepository.findByUserHisotyId(userHistory.getId());
            if (!historyFileList.isEmpty()) {
                UserHistoryFile historyFile = historyFileList.get(0);
                uFList.add(historyFile);
            }
        }

        List<File> fList = new ArrayList<File>();
        for (UserHistoryFile userHistoryFile : uFList) {
            Optional<File> optional = fileRepository.findById(userHistoryFile.getFileId());
            if (optional.isPresent()) {
                File file = optional.get();
                fList.add(file);
            }
        }

        List<FileResult> lst = new ArrayList<>();
        for (File obj : fList) {
            FileResult result = new FileResult();
            result.setTitle(obj.getTitle());
            result.setId(obj.getId());
            result.setFileHashCode(obj.getFileHashcode());
            lst.add(result);
        }

        Page<FileResult> pageTotal = new PageImpl<FileResult>(lst, pageable, lst.size());
        return pageTotal;
    }

    @Override
    public FileTotalDTO totalFile() {
        List objList = null;
        StringBuilder sqlStatement = new StringBuilder();
        List<Object> listParam = new ArrayList<Object>();
        sqlStatement.append("select f.id as id, approver_id as approver from file f    inner join category c on f.category_id = c.id inner join file_price fp on f.id = fp.file_id " + "left join industry i on f.industry_id = i.id left join attachment a on f.id = a.request_id and a.file_type =" + "'" + FileType.FILE_UPLOAD.getName() + "'" + " inner join user u on f.author_id = u.id left join attachment ab on u.id = ab.request_id and ab.file_type = " + "'" + FileType.USER_AVATAR.getName() + "'");
        sqlStatement.append("where f.is_deleted =0  ");

        Query query = entityManager.createNativeQuery(sqlStatement.toString());
        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i + 1, listParam.get(i));
        }
        objList = query.getResultList();
        List<FileResult> list = new ArrayList<>();
        for (Object obj : objList) {
            FileResult result = new FileResult((Object[]) obj, true);
            list.add(result);
        }
        FileTotalDTO fileTotalDTO = new FileTotalDTO();
        fileTotalDTO.setTotal(list.size());
        fileTotalDTO.setPending(list.stream().filter(x -> Objects.isNull(x.getApprover())).count());
        fileTotalDTO.setApproved(list.stream().filter(x -> Objects.nonNull(x.getApprover())).count());


        return fileTotalDTO;
    }

    // Phương thức này nhận một chuỗi và trả về phần mở rộng của tệp (ví dụ: ".doc")
    private  String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }

    private  List<String> convertListToLowerCase(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(i).toLowerCase());
        }
        return list;
    }

    @Override
    public void uploadFile2(FileUploadRequest dto) throws IOException {

        long startTimeCallWs = System.currentTimeMillis();
        Integer login = userService.getUserLogin().getId();
        if (Objects.nonNull(dto.getId()) && dto.getIsVip()) {
            boolean status = registryFileVip(login);
            if (!status) {
                throw new BusinessHandleException("SS006");
            }
        }
        List<FileData> fileData = new ArrayList<>();
        List<MultipartFile> multipartFiles = new ArrayList<>();


        if (Objects.nonNull(dto.getFileDemo())){
            FileData fileDemo = new FileData();
            fileDemo.setFile(dto.getFileDemo());
            fileDemo.setType(FileType.FILE_DEMO.getName());
            fileData.add(fileDemo);
            multipartFiles.add(dto.getFileDemo());

        }

        if (Objects.nonNull(dto.getFileImage())){
            FileData fileImage = new FileData();
            fileImage.setFile(dto.getFileImage());
            fileImage.setType(FileType.FILE_AVATAR.getName());
            fileData.add(fileImage);

        }


        if (Objects.nonNull(dto.getFileUpload())){
            FileData fileUpload = new FileData();
            fileUpload.setFile(dto.getFileDemo());
            fileUpload.setType(FileType.FILE_UPLOAD.getName());
            fileData.add(fileUpload);
            multipartFiles.add(dto.getFileUpload());

        }


        MultipartFile zipFile = ZipUtils.createZipFile(multipartFiles, StringUtils.getSearchableString(generateFileName(dto.getTitle())).replace(" ", ""));
        FileData fileZip = new FileData();
        fileZip.setFile(zipFile);
        fileZip.setType(FileType.FILE_ZIP.getName());
        fileData.add(fileZip);

        MultipartFile cutFileShow = null;
        try {

            if (dto.getFileUpload().getOriginalFilename().toUpperCase().endsWith(".PDF")) {
                cutFileShow = FileProcessor.cutAndSelectPages(dto.getStartPageNumber(), dto.getEndPageNumber(), dto.getFileUpload());
            }

            List<String> docs = Arrays.asList(".DOC", ".DOCX");
            String lowerCaseFileName = dto.getFileUpload().getOriginalFilename().toLowerCase();
            List<String> lowerCaseExtensions = convertListToLowerCase(docs);

            if (lowerCaseExtensions.contains(getFileExtension(lowerCaseFileName))) {
                String fileName = StringUtils.getSearchableString(generateFileName(FileType.FILE_CONVERT_DOC_PDF.getName() + "_"+lowerCaseFileName).replace(" ", "").replace(".docx",".pdf").replace(".doc",".pdf"));
                MultipartFile convertDoctoPdf = FileProcessor.convertDocToPdf(dto.getFileUpload(), fileName);

                if (convertDoctoPdf.getOriginalFilename().toUpperCase().endsWith(".PDF")) {
                    cutFileShow = FileProcessor.cutAndSelectPages(dto.getStartPageNumber(), dto.getEndPageNumber(), convertDoctoPdf);
                }

            }

            FileData fileCut = new FileData();
            fileCut.setFile(cutFileShow);
            fileCut.setType(FileType.FILE_CUT.getName());
            fileData.add(fileCut);

            if (Objects.isNull(dto.getId())) {
                File file = new File(login);
                BeanUtils.copyProperties(dto, file);
                file.setIdUrl(getSearchableStringUrl(dto.getTitle(), fileRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getTitle())).size()));
                File saveFile = fileRepository.save(file);
                Double price = dto.getFilePrice() != null ? dto.getFilePrice() : 0;
                FilePrice filePrice = new FilePrice(saveFile.getId(), price, login);
                filePriceRepository.save(filePrice);
                attachmentService.createListAttachmentsFromBase64S3MultipartFile(fileData, saveFile.getId(), login, true);
                userHistoryService.activateFileHistory(login, file.getId(), ActivityConstants.UPLOAD);
            } else {
                File byId = fileRepository.findById(dto.getId()).get();
                if (dto.getIsVip() && !byId.getIsVip()) {
                    boolean status = registryFileVip(login);
                    if (!status) {
                        throw new BusinessHandleException("SS006");
                    }
                }
                int exit = 0;
                if (Objects.nonNull(dto.getTitle())) {
                    exit = fileRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getTitle())).size();
                }
                File file = updateFile(byId, dto, login, exit);
                fileRepository.save(file);
                if (Objects.nonNull(dto.getFiles())) {
                    attachmentService.createListAttachmentsFromBase64S3MultipartFile(fileData, file.getId(), login, true);
                }
            }
        } catch (Exception e) {
            ObjectMapper mapper = new ObjectMapper();
            // Convert object to JSON string
            String requestContent = StringUtils.convertObjectToJson(dto);
            requestHistorySubService.saveLog("/file/upload", requestContent, e.toString(),
                    400, RequestType.ERROR_UPLOAD_FILE, startTimeCallWs, login);
        }
    }

    public RequestFileDTO cutFileShow(Integer startPageNumber, Integer endPageNumber, RequestFileDTO dto) throws IOException, com.itextpdf.text.DocumentException {

        PdfReader reader = new PdfReader(Base64.decodeBase64(dto.getContent().split(Constants.DOT_COMMA_2)[1]));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, baos);
        int n = reader.getNumberOfPages();
        int endPage = endPageNumber > n ? n : endPageNumber;
        String viewPage = startPageNumber + "-" + endPage;
        reader.selectPages(viewPage);
        stamper.close();
        String base64cutFile = com.itextpdf.text.pdf.codec.Base64.encodeBytes(baos.toByteArray());
        RequestFileDTO requestFileDTO = new RequestFileDTO();
        requestFileDTO.setContent(dto.getContent().split(Constants.DOT_COMMA_2)[0] + Constants.DOT_COMMA_2 + base64cutFile.replace("\n", ""));
        requestFileDTO.setType(FileType.FILE_CUT.getName());
        requestFileDTO.setName(FileType.FILE_CUT.getName() + dto.getName());
        requestFileDTO.setExtension(dto.getExtension());

        return requestFileDTO;
    }

    @Override
    public List<FileMyMapResult> filesPageMyUser(FileHomePageRequest request) {

        String userId = userService.getUserLogin().getId().toString();
        List<CategoryFilePageDTO> listCategory = categoryRepository.filesPageMyUser(userId);
        List<FileMyResult> list = new ArrayList<>();
        listCategory.forEach(x -> {
            List<FileMyResult> fileByCategory = filesPageMyUser(request, x.getId());
            list.addAll(fileByCategory);
        });
        Map<BigInteger, List<FileMyResult>> collect = list.stream().collect(Collectors.groupingBy(FileMyResult::getCategoryId));
        List<FileMyMapResult> results = new ArrayList<>();
        for (Map.Entry<BigInteger , List<FileMyResult>> entry : collect.entrySet()) {

            FileMyMapResult fileMyMapResult = new FileMyMapResult();
            String nameCategory = listCategory.stream().filter(k -> k.getId().equals(entry.getKey())).findFirst().orElseThrow(NotFoundException::new).getName();
            fileMyMapResult.setCategoryId(entry.getKey());
            fileMyMapResult.setCategory(nameCategory);
            fileMyMapResult.setRowNums(collect.get(entry.getKey()).stream().map(FileMyResult::getRowNum).map(Double::intValue).collect(Collectors.toSet()));
            results.add(fileMyMapResult);
        }
        return results;

    }

    public List<FileMyResult> filesPageMyUser(FileHomePageRequest request, BigInteger categoryIds) {
        List<Object> objList = null;

        StringBuilder sqlStatement = new StringBuilder();
        List<Object> listParam = new ArrayList<Object>();
        sqlStatement.append("from file f  inner join category c on f.category_id = c.id inner join file_price fp on f.id = fp.file_id " + " left join industry i on f.industry_id = i.id left join attachment a on f.id = a.request_id and a.file_type = " + "'" + FileType.FILE_AVATAR.getName() + "'" + " inner join user u on f.author_id = u.id left join attachment ab on u.id = ab.request_id and ab.file_type = " + "'" + FileType.USER_AVATAR.getName() + "'");
        sqlStatement.append(", (SELECT @rownum \\:= 0) r  ");
        sqlStatement.append("where f.approver_id is not null and f.is_deleted =0  ");
        sqlStatement.append(" and f.category_id =  ? ");
        listParam.add(categoryIds);

        sqlStatement.append(" and f.created_by =  ? ");
        listParam.add(userService.getUserLogin().getId().toString());
        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            sqlStatement.append(" and (LOWER(f.title) like LOWER(?) ");
            sqlStatement.append(" or LOWER(i.value) like LOWER(?) ");
            sqlStatement.append(" or LOWER(c.category) like LOWER(?) ");
            sqlStatement.append(" or LOWER(u.user_name) like LOWER(?)) ");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
        }

        if (Objects.nonNull(request.getPriceStart())) {
            sqlStatement.append(" and fp.price >= ? ");
            listParam.add(request.getPriceStart());
        }

        if (Objects.nonNull(request.getPriceEnd())) {
            sqlStatement.append(" and fp.price <= ? ");
            listParam.add(request.getPriceEnd());
        }
        if (Objects.nonNull(request.getIsVip())&& request.getIsVip()) {

            sqlStatement.append(" and f.is_vip = ? ");
            listParam.add(request.getIsVip());
        }
        if (Objects.nonNull(request.getIndustry())) {
            sqlStatement.append(" and f.industry_id = ? ");
            listParam.add(request.getIndustry());
        }
        if (Objects.nonNull(request.getSchool())) {

            sqlStatement.append(" and f.school_id = ? ");
            listParam.add(request.getSchool());
        }

        if (Objects.nonNull(request.getOrderType())) {
            if (request.getOrderType().equals(OrderFileType.DOWNLOADS.getCode())) {
                sqlStatement.append(" order by f.dowloading ").append(request.getOrder());
            }
            if (request.getOrderType().equals(OrderFileType.FAVORITES.getCode())) {
                sqlStatement.append(" order by f.total_like ").append(request.getOrder());
            }
            if (request.getOrderType().equals(OrderFileType.PRICE.getCode())) {
                sqlStatement.append(" order by  fp.price ").append(request.getOrder());
            }
            if (request.getOrderType().equals(OrderFileType.VIEW.getCode())) {
                sqlStatement.append(" order by f.view ").append(request.getOrder());
            }
            sqlStatement.append(" ,case when f.start_money_top is null then 1 else 0 end,f.start_money_top,f.id desc ");
        } else {
            sqlStatement.append(" order BY case when f.start_money_top is null then 1 else 0 end,f.start_money_top,f.id desc ");
        }

        Query query = entityManager.createNativeQuery(Constants.SQL_FILE_LOCAL + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i + 1, listParam.get(i));
        }
        objList = query.getResultList();
        List<FileMyResult> list = new ArrayList<>();
        for (Object obj : objList) {
            FileMyResult result = new FileMyResult((Object[]) obj);
            list.add(result);
        }

        return list;

    }


}
