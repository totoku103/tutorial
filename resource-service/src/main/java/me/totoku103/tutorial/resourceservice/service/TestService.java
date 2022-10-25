package me.totoku103.tutorial.resourceservice.service;

import me.totoku103.tutorial.resourceservice.component.CustomThreadLocal;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    public void increment(){
        CustomThreadLocal.increment(CustomThreadLocal.get() + 1);
        CustomThreadLocal.print();
    };
}
