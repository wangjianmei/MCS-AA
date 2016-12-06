package org.tju.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class Excel {
	
	//Create data to excel file
	public void createExcel(String filePath, String[] lables) throws WriteException, IOException{
		
		WritableWorkbook book = null;
		
		try { 
			//Open File
		    book= Workbook.createWorkbook(new File(filePath)); 
		    
		    //Generate the first sheet
		    WritableSheet sheet=book.createSheet("First Sheet",0); 
		    
		    
		    //First Row
		    for(int i=0; i<lables.length; i++){
		    	Label label = new Label(i, 0, lables[i]);
		    	sheet.addCell(label);	    	
		    }
		    
		    //Write the data to files 
		    book.write(); 
	    } catch(Exception e) { 
	        System.out.println(e); 
	    } finally{
		    book.close();
	    }	
		
	}
	
	
	//Read excel file
	public void readExcel(String filePath){
		jxl.Workbook rwb = null;
	    try {
	      //����Workbook����, ֻ��Workbook����
	      //ֱ�Ӵӱ����ļ�����Workbook
	      //������������Workbook
	      InputStream is = new FileInputStream(filePath);
	      rwb = Workbook.getWorkbook(is);

	      //Sheet(���������)����Excel������½ǵ�Sheet1,Sheet2,Sheet3���ڳ�����
	      //Sheet���±��Ǵ�0��ʼ
	      //��ȡ��һ��Sheet��
	       Sheet rs = rwb.getSheet(0);
	       //��ȡSheet������������������
	       int rsColumns = rs.getColumns();
	       //��ȡSheet������������������
	       int rsRows = rs.getRows();
	       //��ȡָ����Ԫ��Ķ�������
	       for(int i=0;i<rsRows;i++){
	         for(int j=0;j<rsColumns;j++){
	           Cell cell = rs.getCell(j,i);
	           System.out.print(cell.getContents()+" ");
	         }
	         System.out.println();
	       }
	    } catch(Exception e) {
	      e.printStackTrace();
	    } finally {
	      //�������ʱ���رն����ͷ�ռ�õ��ڴ�ռ�
	      rwb.close();
	    }
	}
	
	
	//Update Excel file
	public void updateExcel(String filePString){
		jxl.Workbook rwb = null;
	    try{	    	
	       //����Workbook����, ֻ��Workbook����
	       //ֱ�Ӵӱ����ļ�����Workbook
	       //������������Workbook
	       InputStream is = new FileInputStream(filePString);
	       rwb = Workbook.getWorkbook(is);
	       //�����Ѿ�������Excel�����������µĿ�д���Excel������
	       jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(new File(filePString),rwb);
	       //��ȡ��һ�Ź�����
	       jxl.write.WritableSheet ws = wwb.getSheet(0);

	       //��õ�һ����Ԫ�����
	       jxl.write.WritableCell wc = ws.getWritableCell(0, 0);
	       //�жϵ�Ԫ�������, ������Ӧ��ת��
	       if (wc.getType() == CellType.LABEL) {
	         Label l = (Label) wc;
	         l.setString("The value has been modified.");
	       }
	       //д��Excel����
	       wwb.write();
	       wwb.close();

	    }catch(Exception e){
	      e.printStackTrace();
	    }
	    finally{
	      rwb.close();
	    }
		
	}

}
