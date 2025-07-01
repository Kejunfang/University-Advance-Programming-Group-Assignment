package studentbooking.db;

import studentbooking.bean.TicketEntity; // 添加这行
import studentbooking.bean.StudentEntity;
import studentbooking.bean.OperatorEntity;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DBHelper {
    private static final String DATA_DIR = "data";
    private static final String STUDENT_FILE = DATA_DIR + "/student.txt";
    private static final String OPERATOR_FILE = DATA_DIR + "/operator.txt";
    private static final String TICKETS_FILE = DATA_DIR + "/tickets.txt";

    // Initialize data directories and files
    public static void initializeDataFiles() {
        createDirectory(DATA_DIR);
        createFileIfNotExists(STUDENT_FILE);
        createFileIfNotExists(OPERATOR_FILE);
        createFileIfNotExists(TICKETS_FILE);

        // If the file is empty, add the example data
        if (isFileEmpty(STUDENT_FILE)) {
            appendRecord(STUDENT_FILE, "John Doe,Male,password123,21,S12345,University of Tech,Engineering,Computer Science,555-1234,123 Main St,New York");
            appendRecord(STUDENT_FILE, "Jane Smith,Female,abc456,22,S67890,State University,Science,Biology,555-5678,456 Oak St,Boston");
        }

        if (isFileEmpty(OPERATOR_FILE)) {
            // Writing a new file using overwrite mode
            List<String> operatorData = new ArrayList<>();
            operatorData.add("1001,admin123,Admin User,Male,555-0001");
            operatorData.add("1002,op456,Operator Two,Female,555-0002");
            operatorData.add("1003,abc,Operator Three,Male,555-0003");
            writeAllRecords(OPERATOR_FILE, operatorData);
            System.out.println("Operator files have been reinitialized");
        }

    }

    private static void createDirectory(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists() && !dir.mkdirs()) {
            System.err.println("Unable to create data catalog: " + dirPath);
        }
    }

    private static void createFileIfNotExists(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    System.err.println("Unable to create file: " + fileName);
                }
            } catch (IOException e) {
                System.err.println("File creation error: " + fileName);
                e.printStackTrace();
            }
        }
    }

    private static boolean isFileEmpty(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            return br.readLine() == null;
        } catch (IOException e) {
            return true;
        }
    }

    public static List<String[]> readAllRecords(String fileName) {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // Skip empty lines

                String[] fields = line.split(",");
                if (fields.length < 2) { // Ensure that there are enough fields
                    System.out.println("Skip invalid records: " + line);
                    continue;
                }
                records.add(fields);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + fileName);
            e.printStackTrace();
        }
        return records;
    }

    public static void writeAllRecords(String fileName, List<String> data) {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            for (String line : data) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Write file error: " + fileName);
            e.printStackTrace();
        }
    }

    public static void appendRecord(String fileName, String data) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), StandardCharsets.UTF_8))) {
            bw.write(data);
            bw.newLine();
            System.out.println("Successfully wrote a record to " + fileName + ": " + data); // 调试输出
        } catch (IOException e) {
            System.err.println("Append file error: " + fileName);
            e.printStackTrace();
        }
    }

    // Student Related Operations
    public static StudentEntity findStudent(String name, String password) {
        List<String[]> students = readAllRecords(STUDENT_FILE);
        for (String[] fields : students) {
            if (fields.length >= 3 && fields[0].equals(name) && fields[2].equals(password)) {
                StudentEntity student = new StudentEntity();
                student.setName(fields[0]);
                student.setSex(fields[1]);
                student.setPassword(fields[2]);
                student.setAge(fields.length > 3 ? Integer.parseInt(fields[3]) : 0);
                student.setStudentId(fields.length > 4 ? fields[4] : "");
                student.setUniversity(fields.length > 5 ? fields[5] : "");
                student.setFaculty(fields.length > 6 ? fields[6] : "");
                student.setProfession(fields.length > 7 ? fields[7] : "");
                student.setPhoneNum(fields.length > 8 ? fields[8] : "");
                student.setAddress(fields.length > 9 ? fields[9] : "");
                return student;
            }
        }
        return null;
    }

    // Operator-related operations
    public static void resetOperatorFile() {
        try {
            // Delete old files
            new File(OPERATOR_FILE).delete();

            // Creating a new file and writing data
            List<String> operatorData = new ArrayList<>();
            operatorData.add("1001,admin123,Admin User,Male,555-0001");
            operatorData.add("1002,op456,Operator Two,Female,555-0002");
            operatorData.add("1003,abc,Operator Three,Male,555-0003");
            writeAllRecords(OPERATOR_FILE, operatorData);

            System.out.println("Operator file reset");
        } catch (Exception e) {
            System.err.println("Failed to reset operator file: " + e.getMessage());
        }
    }

    public static List<TicketEntity> getAllTickets() {
        List<TicketEntity> tickets = new ArrayList<>();
        List<String[]> records = readAllRecords(TICKETS_FILE);
        for (String[] fields : records) {
            if (fields.length < 9) continue;
            TicketEntity ticket = new TicketEntity();
            ticket.setTicketId(fields[0]);
            ticket.setIssueType(fields[1]);
            ticket.setDescription(fields[2]);
            ticket.setStatus(fields[3]);
            ticket.setSubmittedBy(fields[4]);
            ticket.setAssignedTo(fields[5]);
            ticket.setNotes(fields[6]);
            ticket.setCreatedTime(fields[7]);
            ticket.setLastUpdated(fields[8]);
            tickets.add(ticket);
        }
        return tickets;
    }

    public static void addTicket(TicketEntity ticket) {
        String data = String.join(",",
                ticket.getTicketId(),
                ticket.getIssueType(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getSubmittedBy(),
                ticket.getAssignedTo(),
                ticket.getNotes(),
                ticket.getCreatedTime(),
                ticket.getLastUpdated()
        );
        appendRecord(TICKETS_FILE, data);
    }

    public static void updateTicket(TicketEntity updatedTicket) {
        List<TicketEntity> tickets = getAllTickets();
        List<String> updatedData = new ArrayList<>();

        for (TicketEntity ticket : tickets) {
            if (ticket.getTicketId().equals(updatedTicket.getTicketId())) {
                updatedData.add(String.join(",",
                        updatedTicket.getTicketId(),
                        updatedTicket.getIssueType(),
                        updatedTicket.getDescription(),
                        updatedTicket.getStatus(),
                        updatedTicket.getSubmittedBy(),
                        updatedTicket.getAssignedTo(),
                        updatedTicket.getNotes(),
                        updatedTicket.getCreatedTime(),
                        updatedTicket.getLastUpdated()
                ));
            } else {
                updatedData.add(String.join(",",
                        ticket.getTicketId(),
                        ticket.getIssueType(),
                        ticket.getDescription(),
                        ticket.getStatus(),
                        ticket.getSubmittedBy(),
                        ticket.getAssignedTo(),
                        ticket.getNotes(),
                        ticket.getCreatedTime(),
                        ticket.getLastUpdated()
                ));
            }
        }
        writeAllRecords(TICKETS_FILE, updatedData);
    }

    public static OperatorEntity findOperator(String account, String password) {
        List<String[]> operators = readAllRecords(OPERATOR_FILE);
        for (String[] fields : operators) {
            // 字段顺序: 账号,密码,姓名,性别,电话
            if (fields.length >= 2 && fields[0].equals(account) && fields[1].equals(password)) {
                OperatorEntity operator = new OperatorEntity();
                try {
                    operator.setAccount(Integer.parseInt(fields[0]));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid account format: " + fields[0]);
                    continue;
                }
                operator.setPassword(fields[1]);
                operator.setName(fields.length > 2 ? fields[2] : "");
                operator.setSex(fields.length > 3 ? fields[3] : "");
                operator.setPhoneNum(fields.length > 4 ? fields[4] : "");
                return operator;
            }
        }
        return null;
    }


    public static String generateTicketId() {
        return "TKT" + System.currentTimeMillis();
    }
}