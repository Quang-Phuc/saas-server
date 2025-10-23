package com.phuclq.student.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.phuclq.student.utils.ValueUtils;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class OtpGenerator {
    private LoadingCache<String, Integer> otpCache;

    /**
     * Constructor configuration.
     */
    public OtpGenerator() {
        super();

        otpCache = CacheBuilder.newBuilder().expireAfterWrite(ValueUtils.getOtpMinMinutesExpire(), TimeUnit.MINUTES)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String s) throws Exception {
                        return 0;
                    }
                });
    }

    /**
     * Method for generating OTP and put it in cache.
     *
     * @param key - cache key
     * @return cache value (generated OTP number)
     * @throws Exception
     */
    public Integer generateOTP(String key) {
        Integer OTP = generateOTPNumber();

        otpCache.asMap().put(key, OTP);

        return OTP;
    }

    /**
     * Method for generating OTP number.
     *
     * @return cache value (generated OTP number)
     * @throws Exception
     */
    public Integer generateOTPNumber() {
        Random random = new Random();
        int OTP = 100000 + random.nextInt(900000);

        return OTP;
    }

    /**
     * Method for generating OTP string.
     *
     * @return cache value (generated OTP string)
     * @throws Exception
     */
    public String generateOTPString() {
        return ValueUtils.toString(generateOTPNumber());
    }


    /**
     * Method for getting OTP value by key.
     *
     * @param key - target key
     * @return OTP value
     */
    public Integer getOTPByKey(String key) {
        try {
            return otpCache.get(key);
        } catch (ExecutionException e) {
            return -1;
        }
    }

    /**
     * Method for removing key from cache.
     *
     * @param key - target key
     */
    public void clearOTPFromCache(String key) {
        otpCache.invalidate(key);
    }

}
