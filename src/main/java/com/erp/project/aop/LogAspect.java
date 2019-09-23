/**
 * LogAspect  2019/3/27
 * <p>
 * Copyright (c) 2018, DEEPEXI Inc. All rights reserved.
 * DEEPEXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.erp.project.aop;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @description: 日志统一打印切面
 * @program: LogAspect
 **/
@Component
@Aspect
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution (* com.erp.project.controller..*.*(..))")
    public void apiLogAop() {
    }

    @Around("apiLogAop()")
    public Object aroundApi(ProceedingJoinPoint point) throws Throwable {
        String traceId = DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + (int) ((Math.random() * (9999 - 1000 + 1)) + 1000);
        MDC.put("TRACE_ID", traceId);
        logger.info("日志统一打印 ===== {}.{}() start =====,参数:\n{}", point.getSignature().getDeclaringTypeName(), point.getSignature().getName(), argsToString(point.getArgs()));
        DateTime startTime = new DateTime();
        DateTime endTime;
        Interval interval;
        Object response;

        try {
            //执行该方法
            response = point.proceed();
        } catch (Exception e) {
            endTime = new DateTime();
            interval = new Interval(startTime.getTime(), endTime.getTime());
            logger.info("日志统一打印 ===== {}.{}() end =====,响应时间:{}毫秒,响应内容:\n{}", point.getSignature().getDeclaringTypeName(), point.getSignature().getName(), interval.toDurationMillis(), null);
            throw e;
        }
        endTime = new DateTime();
        interval = new Interval(startTime.getTime(), endTime.getTime());
        logger.info("日志统一打印 ===== {}.{}() end =====,响应时间:{}毫秒,响应内容:\n{}", point.getSignature().getDeclaringTypeName(), point.getSignature().getName(), interval.toDurationMillis(), argsToString(response));
        return response;
    }

    private String argsToString(Object object) {
        try {
            return JSON.toJSONString(object);
        } catch (Exception e) {
            logger.error("发生错误", e);
        }
        return String.valueOf(object);
    }

    /**
     * 获取业务参数
     *
     * @param request
     * @param param
     */
    private String getParam(HttpServletRequest request, String param) {
        String[] reqParam = request.getParameterValues(param);
        return (reqParam == null || reqParam.length < 1 ? null : reqParam[0]);
    }

}
