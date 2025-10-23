package com.phuclq.student.config;

import com.phuclq.student.domain.User;
import com.phuclq.student.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component("currentAuditor")
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        UserService bean = ApplicationContextHolder.getBean(UserService.class);
        User currentUser = bean.getUserLogin();
        return Objects.nonNull(currentUser) && Objects.nonNull(currentUser.getId())
                ? Optional.of(currentUser.getId().toString())
                : Optional.of("system");
    }
}
