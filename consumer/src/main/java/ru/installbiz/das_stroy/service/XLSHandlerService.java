package ru.installbiz.das_stroy.service;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.Units;
import ru.installbiz.das_stroy.entity.Config;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class XLSHandlerService {
    public void start() {
        try (FileInputStream inputStream = new FileInputStream(Config.getFullFilePath());
             Workbook wb = new HSSFWorkbook(inputStream);
             OutputStream fileOut = new FileOutputStream(Config.getFullFilePath())) {
            List<HSSFPicture> list = getPicture(Config.getPositionSize(), wb);
            readAndResizeImage(wb, list);
            wb.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readAndResizeImage(Workbook workbook, List<HSSFPicture> list) {
        HSSFSheet sheet = (HSSFSheet) workbook.getSheetAt(0);
        for (HSSFShape shape : sheet.getDrawingPatriarch().getChildren()) {
            HSSFClientAnchor anchor = (HSSFClientAnchor) shape.getAnchor();
            if (shape instanceof HSSFPicture) {
                HSSFPicture pic = (HSSFPicture) shape;
                for (HSSFPicture picture : list)
                    if (pic.equals(picture)) {

                        //get column width of column in px
                        float columnWidthPx = sheet.getColumnWidthInPixels(anchor.getCol1());

                        //get the height of row in px
                        Row row = sheet.getRow(anchor.getRow1());
                        float rowHeightPt = row.getHeightInPoints();
                        float rowHeightPx = rowHeightPt * Units.PIXEL_DPI / Units.POINT_DPI;

                        double scPic = (double) pic.getImageDimension().width / (double) pic.getImageDimension().height;
                        double scCell = (double) columnWidthPx / (double) rowHeightPx;
                        int pictWidthPx;
                        int pictHeightPx;
                        if (scCell == scPic) {
                            pictHeightPx = (int) rowHeightPx;
                            pictWidthPx = (int) columnWidthPx;
                        } else if (scCell > scPic || pic.getImageDimension().height > pic.getImageDimension().width) {
                            pictHeightPx = (int) rowHeightPx;
                            pictWidthPx = (int) (pictHeightPx * scPic);
                            double X = (double) pictHeightPx / (double) pic.getImageDimension().height;
                            double Y = (double) pictWidthPx / (double) pic.getImageDimension().width;
                            pic.resize();
                            pic.getPreferredSize(X, Y);
                        } else {
                            pictWidthPx = (int) (columnWidthPx);
                            double Y = (double) pictWidthPx / (double) pic.getImageDimension().width;
                            pictHeightPx = (int) (Y * pic.getImageDimension().height);
                            double X = (double) pictHeightPx / (double) pic.getImageDimension().height;
                            pic.resize();
                            pic.getPreferredSize(X, Y);
                        }

//                //is horizontal centering possible?
                        if (pictWidthPx <= columnWidthPx) {
                            //calculate the horizontal center position
                            int horCenterPosPx = Math.round(columnWidthPx / 2f - pictWidthPx / 2f);
                            //set the horizontal center position as Dx1 of anchor
                            if (workbook instanceof HSSFWorkbook) {
                                //see https://stackoverflow.com/questions/48567203/apache-poi-xssfclientanchor-not-positioning-picture-with-respect-to-dx1-dy1-dx/48607117#48607117 for HSSF
                                int DEFAULT_COL_WIDTH = 10 * 256;
                                anchor.setDx1(Math.round(horCenterPosPx * Units.DEFAULT_CHARACTER_WIDTH / 256f * 14.75f * DEFAULT_COL_WIDTH / columnWidthPx) + 10);
                                anchor.setDx2(anchor.getDx2() + anchor.getDx1());
                            }

                        }
//                is vertical centering possible?
                        if (pictHeightPx <= rowHeightPx) {
                            //calculate the vertical center position
                            int vertCenterPosPx = Math.round(rowHeightPx / 2f - pictHeightPx / 2f);
                            //set the vertical center position as Dy1 of anchor
                            if (workbook instanceof HSSFWorkbook) {
                                //see https://stackoverflow.com/questions/48567203/apache-poi-xssfclientanchor-not-positioning-picture-with-respect-to-dx1-dy1-dx/48607117#48607117 for HSSF
                                float DEFAULT_ROW_HEIGHT = 12.75f;
                                anchor.setDy1(Math.round(vertCenterPosPx * Units.PIXEL_DPI / Units.POINT_DPI * 14.75f * DEFAULT_ROW_HEIGHT / rowHeightPx) + 10);
                                anchor.setDy2(anchor.getDy2() + anchor.getDy1());
                            }
                        }
                    }
            }
        }
    }

    public static List<HSSFPicture> getPicture(int countRow, Workbook wb) throws IOException {
//        System.out.println("countRow = " + countRow);
        FileInputStream inputStream = new FileInputStream(Config.getFullTemplatePath());
        Workbook workbook = new HSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
        final String beginKey = "<jx:forEach";
        final String endKey = "</jx:forEach";

        int countRowItem = findCountRowItem(firstSheet, beginKey, endKey);

//        System.out.println("count Row Item: " + countRowItem);
        int startRowInd = 0;
        for (Row row : firstSheet) {
            for (Cell cell : row) {
                DataFormatter formatter = new DataFormatter();
                String str = formatter.formatCellValue(cell);
                if (str.contains(beginKey)) {
                    startRowInd = cell.getRowIndex();
                    break;
                }
            }
        }
        int endIndex = startRowInd + countRow * countRowItem + 1;
//        System.out.println("startRowInd = " + startRowInd);
        List<HSSFPicture> list = getUPicture(wb, startRowInd, endIndex);
        return list;
    }

    public static int findCountRowItem(Sheet sheet, String beginKey, String endKey) {
        boolean flagBreak = false;
        int indStart = 0;
        int indEnd = 0;
        for (Row row : sheet) {
            for (Cell cell : row) {
                DataFormatter formatter = new DataFormatter();
                String str = formatter.formatCellValue(cell);
                if (str.contains(beginKey)) {
                    indStart = cell.getRowIndex();
                }

                if (str.contains(endKey)) {
                    indEnd = cell.getRowIndex();
                    flagBreak = true;
                    break;
                }
            }
            if (flagBreak) {
//                System.out.println("Was break");
                break;
            }
        }
        return indEnd - indStart - 1;
    }

    public static List<HSSFPicture> getUPicture(Workbook wb, int startIndex, int endIndex) throws IOException {
        HSSFSheet sheet = (HSSFSheet) wb.getSheetAt(0);
        List<HSSFPicture> list = new ArrayList<>();
        for (HSSFShape shape : sheet.getDrawingPatriarch().getChildren()) {
            if (shape instanceof HSSFPicture) {
                HSSFPicture picture = (HSSFPicture) shape;
                HSSFClientAnchor anchor = (HSSFClientAnchor) picture.getAnchor();
                if (anchor.getRow1() >= startIndex && anchor.getRow1() <= endIndex) {
//                    System.out.println("row: " + anchor.getRow1() + " col: " + anchor.getCol1());
                    list.add(picture);
                }
            }
        }
        return list;
    }
}