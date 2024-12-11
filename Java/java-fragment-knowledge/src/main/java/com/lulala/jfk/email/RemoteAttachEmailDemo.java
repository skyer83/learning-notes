package com.lulala.jfk.email;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.URLDataSource;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 将远程文件作为附件，发送邮件 <br/>
 * 参考 https://blog.csdn.net/lovewebeye/article/details/107294042 <br/>
 * @author shenjh
 * @version 1.0
 * @since 2024/12/10 14:56
 */
@Slf4j
public class RemoteAttachEmailDemo {
    public static void main(String[] args) {

        Thread thread = new Thread(() -> {
            try {
                String username = "xxx@126.com";
                String password = "xxx";
                sendMessage(getSession(), username, password, getDataSourceList());
            } catch (Exception e) {
                log.error("", e);
            }
        });
        thread.start();
    }

    private static List<DataSource> getDataSourceList() throws IOException {
        List<String> urls = new ArrayList<>();
        urls.add("https://c-ssl.duitang.com/uploads/item/202003/18/20200318125814_shmfo.jpg");
        // 异常的 url
        urls.add("https://c-ssl.duitang11.com/uploads/item/202003/18/20200318125814_shmfo.jpg");
        urls.add("https://c-ssl.duitang.com/uploads/item/202003/18/20200318125814_shmfo.jpg");
        List<DataSource> dataSourceList = new ArrayList<>();
        dataSourceList.add(null);
        for (String urlStr : urls) {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    dataSourceList.add(new URLDataSource(url));
                }
            } catch (Exception e) {
                log.error("链接【{}】不可用", urlStr, e);
            }
        }
        dataSourceList.add(null);
        return dataSourceList;
    }

    private static void sendMessage(Session session, String username, String password, List<DataSource> dataSourceList) throws MessagingException, UnsupportedEncodingException {
        Transport transport = session.getTransport();
        // 连接邮箱
        transport.connect(username, password);
        // 邮件内容
        MimeMessage message = getMimeMessage(session, dataSourceList);
        // 接收者的邮件地址
        Address[] allRecipients = message.getAllRecipients();
        // 开始发送邮件
        transport.sendMessage(message, allRecipients);
    }

    private static MimeMessage getMimeMessage(Session session, List<DataSource> dataSourceList) throws UnsupportedEncodingException, MessagingException {
        //设置端口信息
        MimeMessage message = new MimeMessage(session);
        // 2. From: 发件人
        message.setFrom(new InternetAddress("shenjunhua-2008@126.com", "发件人姓名", "UTF-8"));
        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("shenjunhua-2008@126.com"));
        // 4,抄送人
        message.addRecipient(MimeMessage.RecipientType.CC, new InternetAddress("shenjunhua-2008@126.com"));
        // 5. Subject: 邮件主题
        message.setSubject("xxx标题", "UTF-8");
        // 6，设置邮件内容，混合模式
        MimeMultipart msgMultipart = new MimeMultipart("mixed");
        message.setContent(msgMultipart);
        // 7，设置消息正文
        MimeBodyPart content = new MimeBodyPart();
        msgMultipart.addBodyPart(content);
        // 8，设置正文格式
        MimeMultipart bodyMultipart = new MimeMultipart("related");
        content.setContent(bodyMultipart);
        // 9，设置正文内容
        MimeBodyPart htmlPart = new MimeBodyPart();
        bodyMultipart.addBodyPart(htmlPart);
        htmlPart.setContent("邮件内容，html格式", "text/html;charset=UTF-8");
        // 10，设置附件
        if (CollectionUtil.isNotEmpty(dataSourceList)) {
            dataSourceList.forEach(dataSource -> {
                if (dataSource == null) {
                    return;
                }
                try {
                    //设置相关文件
                    MimeBodyPart filePart = new MimeBodyPart();
                    DataHandler dataHandler = new DataHandler(dataSource);
                    // 文件处理
                    filePart.setDataHandler(dataHandler);
                    // 附件名称
                    filePart.setFileName(dataSource.getName());
                    // 放入正文（有先后顺序，所以在正文后面）
                    msgMultipart.addBodyPart(filePart);
                } catch (Exception e) {
                    log.error("send mail file error fileName={}", dataSource.getName(), e);
                }
            });
        }

        // 11. 设置发件时间
        message.setSentDate(new Date());

        // 12. 保存文件准备发送
        message.saveChanges();

        return message;
    }

    private static Session getSession() {
        return Session.getInstance(getProperties());
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        // 使用的协议（JavaMail规范要求）
        properties.setProperty("mail.transport.protocol", "smtp");
        // 发件服务器，如：smtp.qq.com、smtp.163.com、smtp.126.com
        properties.setProperty("mail.smtp.host", "smtp.126.com");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.ssl.enable", "true");
        return properties;
    }


}
