package me.totoku103.tutorial.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.WebHandler;
import org.springframework.web.server.adapter.HttpWebHandlerAdapter;

@Slf4j
public class CustomHttpWebHandlerAdapter extends HttpWebHandlerAdapter {
    public CustomHttpWebHandlerAdapter(WebHandler delegate) {
        super(delegate);
        log.info(">>> {}", delegate);
    }
}
