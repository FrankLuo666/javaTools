import java.io.File;

public class RenameFilesInDirectory {
    public static void main(String[] args) {
        String directoryPath = "/Users/luo/test";  // 修改为你的目标文件夹路径
        String searchString = "co";  // 要替换的字符串
        String replaceString = "cobol_";  // 替换后的字符串

        File directory = new File(directoryPath);
        renameFiles(directory, searchString, replaceString);
    }

    public static void renameFiles(File directory, String searchString, String replaceString) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        renameFiles(file, searchString, replaceString);  // 递归处理子文件夹
                    } else {
                        String oldName = file.getName();
                        if (oldName.contains(searchString)) {
                            String newName = oldName.replace(searchString, replaceString);
                            File newFile = new File(file.getParent(), newName);
                            if (file.renameTo(newFile)) {
                                System.out.println("Renamed: " + oldName + " -> " + newName);
                            } else {
                                System.out.println("Error renaming: " + oldName);
                            }
                        }
                    }
                }
            }
        }
    }
}
