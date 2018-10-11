package com.dttmap.thank.bean;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;
import top.eg100.code.excel.jxlhelper.annotations.ExcelContent;
import top.eg100.code.excel.jxlhelper.annotations.ExcelContentCellFormat;
import top.eg100.code.excel.jxlhelper.annotations.ExcelSheet;
import top.eg100.code.excel.jxlhelper.annotations.ExcelTitleCellFormat;

/**
 * 用户表，作为用户的导出Excel的中间格式化实体，所有字段都为 String
 */
@ExcelSheet(sheetName = "用户表")
public class UserExcelBean {



    @ExcelContent(titleName = "姓名", index = 1)
    private String personName;

    @ExcelContent(titleName = "区域", index = 2)
    private String area;

    @ExcelContent(titleName = "地址", index = 3)
    private String address;

    @ExcelContent(titleName = "客户", index = 4)
    private String custmer;

    @ExcelContent(titleName = "经度", index = 5)
    private String lng;

    @ExcelContent(titleName = "纬度", index = 6)
    private String lat;

    @ExcelTitleCellFormat(titleName = "姓名")
    private static WritableCellFormat getTitleFormat() {
        WritableCellFormat format = new WritableCellFormat();
        try {
            // 单元格格式
            // 背景颜色
            // format.setBackground(Colour.PINK);
            // 边框线
            format.setBorder(Border.BOTTOM, BorderLineStyle.THIN, Colour.RED);
            // 设置文字居中对齐方式;
            format.setAlignment(Alignment.CENTRE);
            // 设置垂直居中;
            format.setVerticalAlignment(VerticalAlignment.CENTRE);
            // 设置自动换行
            format.setWrap(false);

            // 字体格式
            WritableFont font = new WritableFont(WritableFont.ARIAL);
            // 字体颜色
            font.setColour(Colour.BLUE2);
            // 字体加粗
            font.setBoldStyle(WritableFont.BOLD);
            // 字体加下划线
            font.setUnderlineStyle(UnderlineStyle.SINGLE);
            // 字体大小
            font.setPointSize(20);
            format.setFont(font);

        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

    private static int f1flag = 0;
    private static int f2flag = 0;
    private static int f3flag = 0;
    private static int f4flag = 0;
    private static int f5flag = 0;
    private static int f6flag = 0;

//    @ExcelContent(titleName = "姓名", index = 1)
//    private String personName;
//
//    @ExcelContent(titleName = "区域", index = 2)
//    private String area;
//
//    @ExcelContent(titleName = "地址", index = 3)
//    private String address;
//
//    @ExcelContent(titleName = "客户", index = 4)
//    private String custmer;
//
//    @ExcelContent(titleName = "经度", index = 5)
//    private String lng;
//
//    @ExcelContent(titleName = "纬度", index = 6)
//    private String lat;


    @ExcelContentCellFormat(titleName = "姓名")
    private WritableCellFormat f1() {
        WritableCellFormat format = null;
        try {
            format = new WritableCellFormat();
            if ((f1flag & 1) != 0) {
                format.setBackground(Colour.GRAY_25);
            }

//            if (personName.contains("4")) {
//                format.setBackground(Colour.RED);
//            }

            f1flag++;
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

    @ExcelContentCellFormat(titleName = "区域")
    private WritableCellFormat f2() {
        WritableCellFormat format = null;
        try {
            format = new WritableCellFormat();
            if ((f2flag & 1) != 0) {
                format.setBackground(Colour.GRAY_25);
            }
            f2flag++;
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

    @ExcelContentCellFormat(titleName = "地址")
    private WritableCellFormat f3() {
        WritableCellFormat format = null;
        try {
            format = new WritableCellFormat();
            if ((f3flag & 1) != 0) {
                format.setBackground(Colour.GRAY_25);
            }
            f3flag++;
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

    @ExcelContentCellFormat(titleName = "客户")
    private WritableCellFormat f4() {
        WritableCellFormat format = null;
        try {
            format = new WritableCellFormat();
            if ((f4flag & 1) != 0) {
                format.setBackground(Colour.GRAY_25);
            }
            f4flag++;
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

    @ExcelContentCellFormat(titleName = "经度")
    private WritableCellFormat f5() {
        WritableCellFormat format = null;
        try {
            format = new WritableCellFormat();
            if ((f5flag & 1) != 0) {
                format.setBackground(Colour.GRAY_25);
            }
            f5flag++;
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

    @ExcelContentCellFormat(titleName = "纬度")
    private WritableCellFormat f6() {
        WritableCellFormat format = null;
        try {
            format = new WritableCellFormat();
            if ((f6flag & 1) != 0) {
                format.setBackground(Colour.GRAY_25);
            }
            f6flag++;
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

    public UserExcelBean() {

    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustmer() {
        return custmer;
    }

    public void setCustmer(String custmer) {
        this.custmer = custmer;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}