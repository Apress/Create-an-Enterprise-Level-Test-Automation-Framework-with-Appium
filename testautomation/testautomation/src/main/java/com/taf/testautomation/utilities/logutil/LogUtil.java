package com.taf.testautomation.utilities.logutil;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

import static com.taf.testautomation.utilities.excelutil.ExcelUtil.getCustomProperties;

@Slf4j
public class LogUtil {

    private static PrintStream ps;
    private static FileOutputStream fos = null;

    static {
        try {
            ps = new PrintStream(new File("./output.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public LogUtil() {
    }

    public static void logOutput(String content) {
        PrintStream console = System.out;
        try {
            System.setOut(ps);
            System.out.append(content);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        System.setOut(console);
    }

    public static void takeDeviceLog() {
        try {
            Runtime.getRuntime().exec("./adb_log_command.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearTestOutPut() {
        try {
            Runtime.getRuntime().exec("./clear_testresult_command.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void executeCommandFile() {
        try {
            Runtime.getRuntime().exec("./make_executable_command.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createScriptFile(String command) {
        try {
            String commandFilePath = getCustomProperties().get("reportPrefix") + "xxxx.txt";
            File commandFile = new File(commandFilePath);
            BufferedWriter writer = new BufferedWriter(new FileWriter(commandFile));
            String temp = "";
            writer.write("#! /bin/bash");
            writer.newLine();
            switch (command) {
                case "command1":
                    temp = "xxxx";
                    log.info("the command is" + temp);
                    break;
                case "command2":
                    temp = "yyyy";
                    log.info("the command is" + temp);
                    break;
                default:
                    temp = "zzzz";
                    log.info("the command is" + temp);
                    break;
            }
            writer.write(temp);
            writer.newLine();
            String lastLine = "xxxx";
            writer.write(lastLine);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean deviceLogCheck(String content) {
        if (content.contains("xxxx")) {
            content = "xxxx";
        } else {
            content = "xxxx";
        }
        try {
            String filePath = getCustomProperties().get("reportPrefix") + "devicelog_android.txt";
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String strLine = null;
            while ((strLine = br.readLine()) != null) {// read every line in the file
                if (strLine.contains(content)) {
                    return true;
                }
            }
            br.close();
        } catch (Exception e1) {
            log.info(e1.getMessage());
        }
        return false;
    }
}
