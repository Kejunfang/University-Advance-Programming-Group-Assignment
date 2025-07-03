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

    // Automatic generation of timestamps
    private static String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    // Modify the parameterless constructor to initialize the string to null
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
        this.notes = notes; // 直接替换而不是追加
        updateLastModified();
    }

    public String getCreatedTime() { return createdTime; }

    // Add setCreatedTime method
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getLastUpdated() { return lastUpdated; }

    // Add setLastUpdated method
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    private void updateLastModified() {
        this.lastUpdated = getCurrentTime();
    }


}