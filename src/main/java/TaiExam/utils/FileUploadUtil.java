package TaiExam.utils;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUploadUtil {

    // 临时文件存储目录，可以配置在application.properties中
    private static final String TEMP_FILE_DIR = System.getProperty("java.io.tmpdir") + "/exam-temp/";

    static {
        // 确保临时目录存在
        File dir = new File(TEMP_FILE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 保存文件到临时目录
     * @param file 上传的文件
     * @return 保存后的文件路径
     * @throws IOException 处理文件时可能抛出的异常
     */
    public static String saveTempFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传的文件为空");
        }

        // 生成唯一文件名，避免冲突
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
        File tempFile = new File(TEMP_FILE_DIR + uniqueFileName);
        
        // 保存文件
        file.transferTo(tempFile);
        
        // 返回文件的绝对路径
        return tempFile.getAbsolutePath();
    }

    /**
     * 清理临时文件
     * @param filePath 临时文件路径
     * @return 是否清理成功
     */
    public static boolean cleanTempFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.getAbsolutePath().startsWith(TEMP_FILE_DIR)) {
            return file.delete();
        }
        return false;
    }
}
    