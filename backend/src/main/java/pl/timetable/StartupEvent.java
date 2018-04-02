package pl.timetable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupEvent {

    private static Log Logger = LogFactory.getLog(StartupEvent.class);

    @Value("${timetable.app.run}")
    private String runLevel;

    @Autowired
    private DebugService debugService;

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {
        if ("DEBUG".equals(runLevel)) {
            Logger.info("Initialize debug mode");
            debugService.init();
        }
    }
}
