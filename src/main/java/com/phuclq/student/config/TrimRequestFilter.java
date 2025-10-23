package com.phuclq.student.config;

import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class TrimRequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Read and trim the request body
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestBody = StreamUtils.copyToString(httpServletRequest.getInputStream(), StandardCharsets.UTF_8);
        requestBody = requestBody.trim();

        // Create a new request with the trimmed body
        TrimmedServletRequestWrapper trimmedRequest = new TrimmedServletRequestWrapper(httpServletRequest, requestBody);

        chain.doFilter(trimmedRequest, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code
    }

    @Override
    public void destroy() {
        // Cleanup code
    }

    private static class TrimmedServletRequestWrapper extends HttpServletRequestWrapper {

        private final byte[] trimmedBody;

        public TrimmedServletRequestWrapper(HttpServletRequest request, String trimmedBody) {
            super(request);
            this.trimmedBody = trimmedBody.getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }

                private int lastIndexRetrieved = -1;

                @Override
                public int read() throws IOException {
                    if (lastIndexRetrieved == trimmedBody.length - 1) {
                        return -1;
                    }
                    lastIndexRetrieved++;
                    return trimmedBody[lastIndexRetrieved];
                }
            };
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(this.getInputStream()));
        }
    }
}

