import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * java生成类
 *
 * @author 科兴第一盖伦
 * @version 2019/4/30
 */
public class ProtoGen
{
    public static void main(String[] args)
    {
        String curPath = System.getProperty("user.dir") + File.separator;
        String protoPath = curPath + "/src/main/resources/proto" + File.separator;
        String destPath = curPath + "/src/main/java" + File.separator;
        int intervalDay = 0;

        List<String> totalFileList = new ArrayList<>();
        getTotalFiles(protoPath, totalFileList, intervalDay);
        Runtime rt = Runtime.getRuntime();
        try
        {
            Process pr = null;
            for (String filePath : totalFileList)
            {
                String createProto = "/src/main/resources/protoc.exe --proto_path="
                        + protoPath + " --java_out=" + destPath + " " + filePath;
                String cmdOper = curPath + createProto;
                pr = rt.exec(cmdOper);
                pr.waitFor();
                if (pr.exitValue() != 0)
                {
                    System.out.println("error，fileName: " + filePath + "，please check");
                    return;
                }
            }

            System.out.println("all success");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /*
     * 得到某一路径下所有的目录及其文件
     * 有修改时间限制
     */
    private static void getTotalFiles(String filePath, List<String> totalFileList, int intervalDay)
    {
        Date nowDate = new Date();
        long nowTime = nowDate.getTime();
        long intervalTime = 24 * 3600000L * intervalDay;
        File root = new File(filePath);
        File[] files = root.listFiles();
        if (files == null)
            return;

        for (File file : files)
        {
            if (file.getName().equals(".") || file.getName().equals(".."))
                continue;

            if (file.isDirectory())
                getTotalFiles(file.getAbsolutePath(), totalFileList, intervalDay);
            else if (file.getName().endsWith(".proto")
                    && (intervalTime == 0 || (nowTime - file.lastModified() <= intervalTime)))
                totalFileList.add(file.getAbsolutePath());
        }
    }
}
