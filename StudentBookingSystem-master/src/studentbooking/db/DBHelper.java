package studentbooking.db;

import studentbooking.bean.OrdersEntity;
import studentbooking.bean.TrainInfoEntity;
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
    private static final String ORDERS_FILE = DATA_DIR + "/orders.txt";
    private static final String TRAIN_INFO_FILE = DATA_DIR + "/train_info.txt";

    // 初始化数据目录和文件
    public static void initializeDataFiles() {
        createDirectory(DATA_DIR);
        createFileIfNotExists(STUDENT_FILE);
        createFileIfNotExists(OPERATOR_FILE);
        createFileIfNotExists(ORDERS_FILE);
        createFileIfNotExists(TRAIN_INFO_FILE);

        // 如果文件为空，添加示例数据
        if (isFileEmpty(STUDENT_FILE)) {
            appendRecord(STUDENT_FILE, "John Doe,Male,password123,21,S12345,University of Tech,Engineering,Computer Science,555-1234,123 Main St,New York");
            appendRecord(STUDENT_FILE, "Jane Smith,Female,abc456,22,S67890,State University,Science,Biology,555-5678,456 Oak St,Boston");
        }

        if (isFileEmpty(OPERATOR_FILE)) {
            // 使用覆盖模式写入新文件
            List<String> operatorData = new ArrayList<>();
            operatorData.add("1001,admin123,Admin User,Male,555-0001");
            operatorData.add("1002,op456,Operator Two,Female,555-0002");
            operatorData.add("1003,abc,Operator Three,Male,555-0003");
            writeAllRecords(OPERATOR_FILE, operatorData);
            System.out.println("操作员文件已重新初始化");
        }

        if (isFileEmpty(TRAIN_INFO_FILE)) {
            appendRecord(TRAIN_INFO_FILE, "Express100,New York,08:00,08:05,5,1,10.0");
            appendRecord(TRAIN_INFO_FILE, "Express100,Boston,12:00,12:05,5,2,99.99");
            appendRecord(TRAIN_INFO_FILE, "Express200,Boston,09:00,09:05,5,1,15.0");
            appendRecord(TRAIN_INFO_FILE, "Express200,New York,13:00,13:05,5,2,90.50");
        }
        if (isFileEmpty(ORDERS_FILE)) {
            appendRecord(ORDERS_FILE, "ORD123,John Doe,2023-01-01 10:00,No,Express100,New York,Boston,08:00,12:00,Student Ticket,50,86.49");
            appendRecord(ORDERS_FILE, "ORD456,Jane Smith,2023-01-02 11:00,Yes,Express200,Boston,New York,09:00,13:00,Adult Ticket,30,75.50");
        }
    }

    private static void createDirectory(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists() && !dir.mkdirs()) {
            System.err.println("无法创建数据目录: " + dirPath);
        }
    }

    private static void createFileIfNotExists(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    System.err.println("无法创建文件: " + fileName);
                }
            } catch (IOException e) {
                System.err.println("创建文件错误: " + fileName);
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
                if (line.isEmpty()) continue; // 跳过空行

                String[] fields = line.split(",");
                if (fields.length < 2) { // 确保有足够字段
                    System.out.println("跳过无效记录: " + line);
                    continue;
                }
                records.add(fields);
            }
        } catch (IOException e) {
            System.err.println("读取文件错误: " + fileName);
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
            System.err.println("写入文件错误: " + fileName);
            e.printStackTrace();
        }
    }

    public static void appendRecord(String fileName, String data) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), StandardCharsets.UTF_8))) {
            bw.write(data);
            bw.newLine();
            System.out.println("成功写入记录到 " + fileName + ": " + data); // 调试输出
        } catch (IOException e) {
            System.err.println("追加文件错误: " + fileName);
            e.printStackTrace();
        }
    }

    // 学生相关操作
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

    // 操作员相关操作
    public static OperatorEntity findOperator(String account, String password) {
        System.out.println("=== 调试：开始查找操作员 ===");
        System.out.println("输入的账号: '" + account + "'");
        System.out.println("输入的密码: '" + password + "'");

        List<String[]> operators = readAllRecords(OPERATOR_FILE);
        System.out.println("操作员记录总数: " + operators.size());

        for (String[] fields : operators) {
            System.out.println("当前记录: " + String.join("|", fields)); // 用|分隔字段更清晰

            // 确保字段足够长
            if (fields.length < 2) {
                System.out.println("记录字段不足，跳过");
                continue;
            }

            // 检查账号匹配
            if (fields[0].equals(account)) {
                System.out.println("找到匹配账号，密码比对: " +
                        "输入密码='" + password + "', 存储密码='" + fields[1] + "'");

                // 检查密码匹配
                if (fields[1].equals(password)) {
                    System.out.println("密码匹配成功！");

                    // 直接创建并返回 OperatorEntity 对象
                    OperatorEntity operator = new OperatorEntity();
                    operator.setAccount(Integer.parseInt(fields[0]));
                    operator.setPassword(fields[1]);
                    operator.setName(fields.length > 2 ? fields[2] : "");
                    operator.setSex(fields.length > 3 ? fields[3] : "");
                    operator.setPhoneNum(fields.length > 4 ? fields[4] : "");

                    return operator;
                } else {
                    System.out.println("密码不匹配");
                }
            }
        }
        System.out.println("=== 调试：未找到匹配操作员 ===");
        return null;
    }

    // 订单相关操作
    public static List<OrdersEntity> getAllOrders() {
        System.out.println("正在从文件加载订单..."); // 调试输出
        List<OrdersEntity> orders = new ArrayList<>();
        List<String[]> records = readAllRecords(ORDERS_FILE);
        System.out.println("读取到 " + records.size() + " 条订单记录"); // 调试输出
        for (String[] fields : records) {
            if (fields.length < 12) continue;
            OrdersEntity order = new OrdersEntity();
            order.setOrderNum(fields[0]);
            order.setName(fields[1]);
            order.setTime(fields[2]);
            order.setIspaid(fields[3]);
            order.setTrainName(fields[4]);
            order.setStartPlace(fields[5]);
            order.setEndPlace(fields[6]);
            order.setStartTime(fields[7]);
            order.setEndTime(fields[8]);
            order.setTicketType(fields[9]);
            order.setRemainTickets(Integer.parseInt(fields[10]));
            order.setFare(Float.parseFloat(fields[11]));
            orders.add(order);
        }
        return orders;
    }

    public static List<OrdersEntity> getOrdersByName(String name) {
        List<OrdersEntity> result = new ArrayList<>();
        for (OrdersEntity order : getAllOrders()) {
            if (order.getName().equals(name)) {
                result.add(order);
            }
        }
        return result;
    }

    public static void addOrder(OrdersEntity order) {
        String data = String.join(",",
                order.getOrderNum(),
                order.getName(),
                order.getTime(),
                order.getIspaid(),
                order.getTrainName(),
                order.getStartPlace(),
                order.getEndPlace(),
                order.getStartTime(),
                order.getEndTime(),
                order.getTicketType(),
                String.valueOf(order.getRemainTickets()),
                String.valueOf(order.getFare())
        );
        appendRecord(ORDERS_FILE, data);
    }

    public static void removeOrder(String orderNum) {
        List<OrdersEntity> orders = getAllOrders();
        List<String> updatedData = new ArrayList<>();
        for (OrdersEntity order : orders) {
            if (!order.getOrderNum().equals(orderNum)) {
                updatedData.add(String.join(",",
                        order.getOrderNum(),
                        order.getName(),
                        order.getTime(),
                        order.getIspaid(),
                        order.getTrainName(),
                        order.getStartPlace(),
                        order.getEndPlace(),
                        order.getStartTime(),
                        order.getEndTime(),
                        order.getTicketType(),
                        String.valueOf(order.getRemainTickets()),
                        String.valueOf(order.getFare())
                ));
            }
        }
        writeAllRecords(ORDERS_FILE, updatedData);
    }

    // 车次信息相关操作
    public static List<TrainInfoEntity> getAllTrainInfos() {
        List<TrainInfoEntity> trainInfos = new ArrayList<>();
        List<String[]> records = readAllRecords(TRAIN_INFO_FILE);
        for (String[] fields : records) {
            if (fields.length < 7) continue;
            TrainInfoEntity info = new TrainInfoEntity();
            info.setTrainName(fields[0]);
            info.setStationName(fields[1]);
            info.setArriveTime(fields[2]);
            info.setStartTime(fields[3]);
            info.setStayTime(fields[4]);
            info.setNum(Integer.parseInt(fields[5]));
            info.setFare(Float.parseFloat(fields[6]));
            trainInfos.add(info);
        }
        return trainInfos;
    }
    public static void resetOperatorFile() {
        try {
            // 删除旧文件
            new File(OPERATOR_FILE).delete();

            // 创建新文件并写入数据
            List<String> operatorData = new ArrayList<>();
            operatorData.add("1001,admin123,Admin User,Male,555-0001");
            operatorData.add("1002,op456,Operator Two,Female,555-0002");
            operatorData.add("1003,abc,Operator Three,Male,555-0003");
            writeAllRecords(OPERATOR_FILE, operatorData);

            System.out.println("操作员文件已重置");
        } catch (Exception e) {
            System.err.println("重置操作员文件失败: " + e.getMessage());
        }
    }
}