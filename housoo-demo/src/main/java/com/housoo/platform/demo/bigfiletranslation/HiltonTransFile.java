package com.housoo.platform.demo.bigfiletranslation;

import com.google.common.util.concurrent.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @PackageName:com.housoo.platform.demo.bigfiletranslation
 * @ClassName:HiltonTransFile
 * @Description:
 * @author:涂德东
 * @date2020/5/910:22
 */
@Service
public class HiltonTransFile extends AbstractService {

    private static volatile int fileType = 0;
    private static AtomicInteger count = new AtomicInteger(0);
    private static StringBuilder stringBuilder = null;


    @Override
    protected void doStart() {

    }

    @Override
    protected void doStop() {

    }
}
