package com.yusiwen.tool.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author yusiwen
 * date 2021/6/11
 * description
 */
public class SessionInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(SessionInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        log.debug("get session");
        if (session == null) {
            log.debug("session creating");
            session = request.getSession(true);
            // 当天0点失效
            session.setMaxInactiveInterval(todayRemainSeconds());
            log.info("session created");
        }
        return true;
    }

    /**
     * 计算离凌晨还有多少秒
     *
     * @return
     */
    private int todayRemainSeconds() {
        int timeSeconds = (int) (System.currentTimeMillis() / 1000);
        int oneDayMillis = 24 * 60 * 60;
        int nextDayZeroSeconds = ((timeSeconds / oneDayMillis) + 1) * oneDayMillis - 8 * 60 * 60;
        return nextDayZeroSeconds - timeSeconds;
    }
}
