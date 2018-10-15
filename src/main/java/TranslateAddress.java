import com.google.gson.Gson;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TranslateAddress {

    private static final String INPUT_FILE_NAME = "G:\\input.xlsx";
    private static final String OUTPUT_FILE_NAME = "G:\\output.xlsx";
    private final String USER_AGENT = "Mozilla/5.0";
    private WebDriver driver = null;


    public TranslateAddress() {
        System.setProperty("webdriver.chrome.driver", "G:\\Webdriver\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    public void parse(int startIndex) throws IOException, InterruptedException {
        // Write the output to a file
        FileInputStream input = new FileInputStream(new File(INPUT_FILE_NAME));

        Workbook inputWorkbook = new XSSFWorkbook(input);
        Workbook outputWorkbook = new XSSFWorkbook();
        Sheet sheetNew = outputWorkbook.createSheet();

        Sheet sheet1 = inputWorkbook.getSheetAt(0);

        try {
            for (int i = startIndex; i < sheet1.getLastRowNum(); i++) {
                if(i==startIndex+2) break;
                System.out.println("處理第" + i + "+1行中");
                Row currentRow = sheet1.getRow(i);
                Thread.sleep(100);
                String address = "";
                if (currentRow.getCell(2).toString().equals("0.0")) {
                    if (!currentRow.getCell(4).getStringCellValue().trim().equals("")) {
                        address = currentRow.getCell(4).getStringCellValue().trim();
                    }
                } else {
                    address = currentRow.getCell(2).toString() + currentRow.getCell(4).getStringCellValue();
                }

                if (!address.trim().equals("")) {
                    String result = getAddressFromGov(address);

                    while (result.equals("處理中...")) {
                        result = getAddressFromGov(address);
                    }
//
//                    if (sheet1.getRow(i).getLastCellNum() > 6) {
//                        if (!sheet1.getRow(i).getCell(6).equals("無法辨識") || !sheet1.getRow(i).getCell(6).equals("處理中")) {
//                            Cell cell = sheet1.getRow(i).getCell(6);
//                            cell.setCellValue(result);
//                            outputWorkbook.write(output);
//                        }
//                    } else {
//                        Cell cell = sheet1.getRow(i).createCell(6);
//                        cell.setCellValue(result);
//                        outputWorkbook.write(output);
//                    }

                    Cell cell = sheetNew.createRow(i).createCell(0);
                    cell.setCellValue(result);
                    System.out.println("updated" + "," + i + "," + result);
                }
                FileOutputStream output = new FileOutputStream(OUTPUT_FILE_NAME);
                outputWorkbook.write(output);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            inputWorkbook.close();
            outputWorkbook.close();
            input.close();
        }
    }

    public String getAddressFromGov(String address) throws IOException, InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        String urlBase = "https://map.tgos.tw/TGOSCloud/Web/Map/TGOSViewer_Map.aspx?addr=%s";
        String url = String.format(urlBase, address);
        driver.get(url);
        String result = "無法辨識";
        By resultBy = By.xpath("(//div[@id='LocateBox_FunctionWork_Addr_Reslut']/div)[1]");
        try {
            WebElement resultElement = wait.until(ExpectedConditions.presenceOfElementLocated(resultBy));
            result = resultElement.getText();
        } catch (Exception e) {
            driver.close();
            driver = new ChromeDriver();
        }

        return result;
    }

    public static void main(String args[]) throws IOException, InterruptedException {
        TranslateAddress translateAddress = new TranslateAddress();
        translateAddress.parse(513);
    }
}
