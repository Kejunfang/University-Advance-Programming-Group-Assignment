package studentbooking.db;

import studentbooking.bean.TicketEntity;
import studentbooking.bean.StudentEntity;
import studentbooking.bean.OperatorEntity;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBHelper {
    public static final String DATA_DIR = "data";
    public static final String STUDENT_FILE = DATA_DIR + "/student.txt";
    public static final String OPERATOR_FILE = DATA_DIR + "/operator.txt";
    public static final String TICKETS_FILE = DATA_DIR + "/tickets.txt";
    private static final int MAX_ACTIVE_TICKETS = 50;

    // Initialize data directories and files
    public static void initializeDataFiles() {
        createDirectory(DATA_DIR);
        createFileIfNotExists(STUDENT_FILE);
        createFileIfNotExists(OPERATOR_FILE);
        createFileIfNotExists(TICKETS_FILE);

        // 只初始化管理员数据
        if (isFileEmpty(OPERATOR_FILE)) {
            System.out.println("Initializing operator data...");
            List<String> operatorData = Arrays.asList(
                    "1001,admin123,Admin User,Male,555-0001"
            );
            writeAllRecords(OPERATOR_FILE, operatorData);
        }

        // 不再初始化学生和工单数据
    }

    private static void createDirectory(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("Created directory: " + dirPath);
            } else {
                System.err.println("Unable to create data catalog: " + dirPath);
            }
        }
    }

    private static void createFileIfNotExists(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("Created file: " + fileName);
                } else {
                    System.err.println("Unable to create file: " + fileName);
                }
            } catch (IOException e) {
                System.err.println("File creation error: " + fileName + " - " + e.getMessage());
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
        File file = new File(fileName);
        if (!file.exists()) {
            System.err.println("File does not exist: " + fileName);
            return records;
        }

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
            System.err.println("Error reading file: " + fileName + " - " + e.getMessage());
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
            System.out.println("Successfully wrote " + data.size() + " records to " + fileName);
        } catch (IOException e) {
            System.err.println("Write file error: " + fileName + " - " + e.getMessage());
        }
    }

    public static void appendRecord(String fileName, String data) {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName, true), StandardCharsets.UTF_8))) {
            bw.write(data);
            bw.newLine();
            System.out.println("Successfully appended a record to " + fileName + ": " + data);
        } catch (IOException e) {
            System.err.println("Append file error: " + fileName + " - " + e.getMessage());
        }
    }

    // Student Related Operations
    public static StudentEntity findStudent(String identifier, String password) {
        List<String[]> students = readAllRecords(STUDENT_FILE);
        System.out.println("Searching student. Total records: " + students.size());

        for (String[] fields : students) {
            System.out.println("Checking record: " + Arrays.toString(fields));

            // 确保有足够字段（至少5个：姓名,性别,密码,年龄,学号）
            if (fields.length < 5) {
                System.out.println("Skipping invalid student record (insufficient fields)");
                continue;
            }

            boolean match = false;
            String recordName = fields[0].trim();
            String recordId = fields[4].trim();
            String recordPwd = fields[2].trim();

            // 匹配学号或姓名
            if (recordId.equals(identifier.trim()) ){
                match = true;
                System.out.println("Matched by student ID: " + identifier);
            } else if (recordName.equals(identifier.trim())) {
                match = true;
                System.out.println("Matched by name: " + identifier);
            }

            // 验证密码
            if (match && recordPwd.equals(password.trim())) {
                StudentEntity student = new StudentEntity();
                student.setName(recordName);
                student.setSex(fields[1]);
                student.setPassword(recordPwd);

                try {
                    student.setAge(Integer.parseInt(fields[3]));
                } catch (NumberFormatException e) {
                    student.setAge(0);
                }

                student.setStudentId(recordId);
                // 防止数组越界
                student.setUniversity(fields.length > 5 ? fields[5] : "");
                student.setFaculty(fields.length > 6 ? fields[6] : "");
                student.setProfession(fields.length > 7 ? fields[7] : "");
                student.setPhoneNum(fields.length > 8 ? fields[8] : "");
                student.setAddress(fields.length > 9 ? fields[9] : "");

                System.out.println("Found matching student: " + student.getName());
                return student;
            }
        }
        System.out.println("No matching student found for identifier: " + identifier);
        return null;
    }

    // 获取所有学生
    public static List<StudentEntity> getAllStudents() {
        List<StudentEntity> students = new ArrayList<>();
        List<String[]> records = readAllRecords(STUDENT_FILE);
        for (String[] fields : records) {
            if (fields.length < 3) continue;
            StudentEntity student = new StudentEntity();
            student.setName(fields[0]);
            student.setSex(fields[1]);
            student.setPassword(fields[2]);

            try {
                student.setAge(fields.length > 3 ? Integer.parseInt(fields[3]) : 0);
            } catch (NumberFormatException e) {
                student.setAge(0);
            }

            student.setStudentId(fields.length > 4 ? fields[4] : "");
            student.setUniversity(fields.length > 5 ? fields[5] : "");
            student.setFaculty(fields.length > 6 ? fields[6] : "");
            student.setProfession(fields.length > 7 ? fields[7] : "");
            student.setPhoneNum(fields.length > 8 ? fields[8] : "");
            student.setAddress(fields.length > 9 ? fields[9] : "");
            students.add(student);
        }
        return students;
    }

    // 添加学生
    public static void addStudent(StudentEntity student) {
        String data = String.join(",",
                student.getName(),
                student.getSex(),
                student.getPassword(),
                String.valueOf(student.getAge()),
                student.getStudentId(),
                student.getUniversity(),
                student.getFaculty(),
                student.getProfession(),
                student.getPhoneNum(),
                student.getAddress()
        );
        appendRecord(STUDENT_FILE, data);
    }

    // 删除学生
    public static void deleteStudent(String name) {
        List<StudentEntity> students = getAllStudents();
        List<String> updatedData = new ArrayList<>();

        for (StudentEntity student : students) {
            if (!student.getName().equals(name)) {
                updatedData.add(String.join(",",
                        student.getName(),
                        student.getSex(),
                        student.getPassword(),
                        String.valueOf(student.getAge()),
                        student.getStudentId(),
                        student.getUniversity(),
                        student.getFaculty(),
                        student.getProfession(),
                        student.getPhoneNum(),
                        student.getAddress()
                ));
            }
        }
        writeAllRecords(STUDENT_FILE, updatedData);
    }

    // 获取所有操作员
    public static List<OperatorEntity> getAllOperators() {
        List<OperatorEntity> operators = new ArrayList<>();
        List<String[]> records = readAllRecords(OPERATOR_FILE);
        for (String[] fields : records) {
            if (fields.length < 2) continue;
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
            operators.add(operator);
        }
        return operators;
    }

    // 获取所有操作员名字
    public static List<String> getAllOperatorNames() {
        List<String> names = new ArrayList<>();
        for (OperatorEntity operator : getAllOperators()) {
            names.add(operator.getName());
        }
        return names;
    }

    public static List<TicketEntity> getAllTickets() {
        List<TicketEntity> tickets = new ArrayList<>();
        List<String[]> records = readAllRecords(TICKETS_FILE);
        for (String[] fields : records) {
            if (fields.length < 9) continue;

            // 检查工单状态，如果是"Closed"则跳过
            String status = fields[3].trim();
            if ("Closed".equals(status)) {
                continue;
            }

            TicketEntity ticket = new TicketEntity();
            ticket.setTicketId(fields[0]);
            ticket.setIssueType(fields[1]);
            ticket.setDescription(fields[2]);
            ticket.setStatus(status);
            ticket.setSubmittedBy(fields[4]);
            ticket.setAssignedTo(fields[5]);
            ticket.setNotes(fields[6]);
            ticket.setCreatedTime(fields[7]);
            ticket.setLastUpdated(fields[8]);
            tickets.add(ticket);
        }
        return tickets;
    }

    // 修改addTicket方法
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
                // 如果工单状态为"Closed"，不添加到更新列表（即删除）
                if (!"Closed".equals(updatedTicket.getStatus())) {
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
                }
            } else {
                // 对于其他工单，只保留非"Closed"状态的
                if (!"Closed".equals(ticket.getStatus())) {
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
        }
        writeAllRecords(TICKETS_FILE, updatedData);
    }


    private static void writeAllTickets(List<TicketEntity> tickets) {
        List<String> data = new ArrayList<>();
        for (TicketEntity ticket : tickets) {
            data.add(String.join(",",
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
        writeAllRecords(TICKETS_FILE, data);
    }

    // 修复 findOperator 方法
    public static OperatorEntity findOperator(String accountInput, String password) {
        List<String[]> operators = readAllRecords(OPERATOR_FILE);
        for (String[] fields : operators) {
            if (fields.length < 2) continue;

            String recordAccount = fields[0].trim();
            String recordPwd = fields[1].trim();

            if (recordAccount.equals(accountInput.trim())) {
                if (recordPwd.equals(password.trim())) {
                    OperatorEntity operator = new OperatorEntity();
                    try {
                        operator.setAccount(Integer.parseInt(recordAccount));
                    } catch (NumberFormatException e) {
                        continue; // 跳过无效账号
                    }
                    operator.setPassword(recordPwd);
                    operator.setName(fields.length > 2 ? fields[2] : "");
                    operator.setSex(fields.length > 3 ? fields[3] : "");
                    operator.setPhoneNum(fields.length > 4 ? fields[4] : "");
                    return operator;
                }
            }
        }
        return null;
    }

    public static String generateTicketId() {
        return "TKT" + System.currentTimeMillis();
    }
}