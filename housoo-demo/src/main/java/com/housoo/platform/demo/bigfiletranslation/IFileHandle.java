package com.housoo.platform.demo.bigfiletranslation;


import org.springframework.stereotype.Service;

@Service
public interface IFileHandle {

    void handle(String line);
}
