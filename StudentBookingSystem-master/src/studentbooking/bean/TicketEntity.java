package studentbooking.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TicketEntity {
    private String ticketId = "";
    private String issueType = "";
    private String description = "";
    private String status = "";
    private String submittedBy = "";
    private String assignedTo = "";
    private String notes = "";
    private String createdTime = "";
    private String lastUpdated = "";

    // 自动生成时间戳
    private static String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    // 修改无参构造方法，初始化字符串为空值
    public TicketEntity() {
        this.ticketId = "";
        this.issueType = "";
        this.description = "";
        this.status = "";
        this.submittedBy = "";
        this.assignedTo = "";
        this.notes = "";
        this.createdTime = getCurrentTime();
        this.lastUpdated = getCurrentTime();
    }

    // Getters and Setters
    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }

    public String getIssueType() { return issueType; }
    public void setIssueType(String issueType) { this.issueType = issueType; }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
        updateLastModified();
    }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
        updateLastModified();
    }

    public String getSubmittedBy() { return submittedBy; }
    public void setSubmittedBy(String submittedBy) { this.submittedBy = submittedBy; }

    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
        updateLastModified();
    }

    public String getNotes() { return notes; }
    public void setNotes(String notes) {
        this.notes = notes;
        updateLastModified();
    }

    public String getCreatedTime() { return createdTime; }

    // 添加 setCreatedTime 方法
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getLastUpdated() { return lastUpdated; }

    // 添加 setLastUpdated 方法
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    private void updateLastModified() {
        this.lastUpdated = getCurrentTime();
    }


}