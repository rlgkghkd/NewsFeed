package com.example.newsFeed.global.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

public class LoginFilter implements Filter {

    private static final String[] WHITE_LIST = {
            "/users",
            "/users/login"
            };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        //WHITE_LIST에 포함되지 않는 경우
        if(!isWhitleList(requestURI)) {

            HttpSession session = httpServletRequest.getSession(false);

            if (session == null || session.getAttribute(Const.LOGIN_USER) == null) {
                throw new RuntimeException("로그인 해주세요.");
            }
            System.out.println("로그인에 성공했습니다.");

        }
        //1. 처음 WHITE_LIST에 등록되 요청이라면 filterChain.doFilter()가 호출
        //2. 다음 WHITE_LIST에 등록된 요청이아니라면 위 필터로직을 수행 후에 filterChain.doFilter() 다음 필터나 Servlet을 호출한다.
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isWhitleList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}