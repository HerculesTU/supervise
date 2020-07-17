package com.housoo.platform.core.service;

import java.io.File;
import java.util.List;

/**
 * @author housoo
 */
public interface AppEmailService extends BaseService {
    /**
     * 发送简单邮件
     *
     * @param subject
     * @param content
     * @param toMails
     */
    public void sendSimpleMail(String subject, String content, String[] toMails);

    /**
     * 发送带附件的邮件
     *
     * @param subject
     * @param content
     * @param fileList
     * @param toMails
     */
    public void sendAttachMail(String subject, String content,
                               List<File> fileList, String[] toMails);
}
 