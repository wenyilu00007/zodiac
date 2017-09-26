package com.hoau.zodiac.core.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
* @Title: MailEntity 
* @Package com.hoau.zodiac.core.entity 
* @Description: 邮件接口入参
* @author 陈宇霖  
* @date 2017/9/25 14:12
* @version V1.0   
*/
@ApiModel(value = "邮件发送参数")
public class MailEntity implements Serializable {

    private static final long serialVersionUID = -4010462356012428207L;

    @NotNull(message = "{validator.mail.sendType.NotNull}")
    @ApiModelProperty(value = "邮件发送类型:0异步发送、1实时发送、2定时发送", required = true, dataType = "int")
    private int sendType;

    @Length(max = 200, message = "{validator.mail.from.MaxLength}")
    @ApiModelProperty(value = "发件人", dataType = "String")
    private String from;

    @Length(max = 200, message = "{validator.mail.subject.MaxLength}")
    @ApiModelProperty(value = "主题", required = true, dataType = "String")
    private String subject;

    @ApiModelProperty(value = "收件人", required = true, dataType = "String[]")
    private String[] to;

    @ApiModelProperty(value = "抄送", dataType = "String[]")
    private String[] cc;

    @ApiModelProperty(value = "秘密抄送", dataType = "String[]")
    private String[] bcc;

    @Length(max = 5000, message = "{validator.mail.text.MaxLength}")
    @ApiModelProperty(value = "邮件文本内容", dataType = "String")
    private String text;

    @Length(max = 100, message = "{validator.mail.templateName.MaxLength}")
    @ApiModelProperty(value = "模板名称", dataType = "String")
    private String templateName;

    @ApiModelProperty(value = "模板数据", dataType = "Map")
    private Map data;

    @ApiModelProperty(value = "附件", dataType = "LinkedHashMap")
    private LinkedHashMap<String, byte[]> attachment;

    @ApiModelProperty(value = "发送优先级，1-5", dataType = "int")
    private Integer priority;

    @ApiModelProperty(value = "要求发送时间", dataType = "Date")
    private Date requiredSendTime;

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String[] getBcc() {
        return bcc;
    }

    public void setBcc(String[] bcc) {
        this.bcc = bcc;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }

    public LinkedHashMap<String, byte[]> getAttachment() {
        return attachment;
    }

    public void setAttachment(LinkedHashMap<String, byte[]> attachment) {
        this.attachment = attachment;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getRequiredSendTime() {
        return requiredSendTime;
    }

    public void setRequiredSendTime(Date requiredSendTime) {
        this.requiredSendTime = requiredSendTime;
    }
}