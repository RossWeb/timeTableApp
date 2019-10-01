package pl.timetable;

import org.apache.log4j.Logger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private static final Logger LOGGER = Logger.getLogger(AsyncExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {

        LOGGER.error("Exception Cause - " + throwable.getMessage());
        LOGGER.error("Method name - " + method.getName());
        for (Object param : obj) {
            LOGGER.error("Parameter value - " + param);
        }
    }
}