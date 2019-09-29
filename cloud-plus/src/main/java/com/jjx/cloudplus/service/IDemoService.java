package com.jjx.cloudplus.service;

import java.util.concurrent.Future;

public interface IDemoService {

    String test1();

    Future<String> test2();

}
