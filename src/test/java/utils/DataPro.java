package utils;
import org.testng.annotations.DataProvider;

import java.util.List;
import java.util.Map;
import utils.ReadExcel;

public class DataPro {

    String filePath = "C:\\Users\\marvi\\data.xlsx";
    @DataProvider(name = "logindata")
    public Object[][] dataMethod(){
        List<Map<String, String>> result = ReadExcel.getExcuteList(filePath);
        Object[][] files = new Object[result.size()][];
        for(int i=0; i<result.size(); i++){
            files[i] = new Object[]{result.get(i)};
        }
        return files;
    }

}