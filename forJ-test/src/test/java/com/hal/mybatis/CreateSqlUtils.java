package com.hal.mybatis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateSqlUtils {

	/**
	 * 列定义文件 seller_user_credit_log 为table名
	 */
	private static final String INPUT_FILE = "/Users/Sunlight/小蚂蚁项目/02_数据结构/蚂蚁POS表结构/文档/库存.xlsx";
	
	public static void main(String[] args) throws IOException {
		File inputFile = new File(INPUT_FILE);
		if (inputFile == null || !inputFile.exists()) {
			System.out.println(INPUT_FILE + " not exist!!!");
			System.exit(0);
		}
		List<Column> pkList = new ArrayList<Column>();
		List<Column> keyList = new ArrayList<Column>();
		List<Column> columnList = new ArrayList<Column>();
		String inputFileParentPath = inputFile.getParent();
		String inputFileName = inputFile.getName();
		int lastIndex = inputFileName.lastIndexOf(".");
		String tableName = inputFileName.substring(0, lastIndex);
		
		InputStream iStream = new FileInputStream(inputFile);
		BufferedReader reader = new BufferedReader(new InputStreamReader(iStream)) ;
		String row = null;
		while ((row = reader.readLine()) != null) {
			String[] cols = row.split("\\s+");
			Column column = new Column();
			column.setName(cols[0]);
			column.setType(cols[1]);
			boolean isDate = cols[1].startsWith("date");
			if (isDate) {
				column.setComment(cols[2]);
			} else {
				column.setLength(cols[2]);
				if ("pk".equalsIgnoreCase(cols[3])) {
					column.setIndexType((byte) 1);
					column.setComment(cols[4]);
					pkList.add(column);
				} else if (cols[3].startsWith("idx")) {
					column.setIndexType((byte) 2);
					column.setIndexName(cols[3]);
					column.setComment(cols[4]);
					keyList.add(column);
				} else {
					column.setComment(cols[3]);
				}
			}
			
			columnList.add(column);
		}
		reader.close();
		// 解析
		StringBuffer buffer = new StringBuffer();
		buffer.append(ColumnBuilder.getCreateTableBegin(tableName));
		for (Column column : columnList) {
			ColumnBuilder builder = new ColumnBuilder(column);
			buffer.append(builder.buildColumnSql());
			buffer.append(",\n");
		}
		if (pkList.size() > 0) {
			ColumnBuilder pkBuilder = new ColumnBuilder(pkList.get(0));
			buffer.append(pkBuilder.getPk());
			buffer.append(",\n");
		}
		for (Column column : keyList) {
			ColumnBuilder builder = new ColumnBuilder(column);
			buffer.append(builder.getKey());
			buffer.append(",\n");
		}
		lastIndex = buffer.lastIndexOf(",\n");
		// 删除最后一个逗号
		buffer.replace(lastIndex, lastIndex + 1, "");
		buffer.append(ColumnBuilder.getCreateTableEnd("损益明细表"));
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
		String nowDateStr = dateFormat.format(new Date());
		String outputFilePath = (inputFileParentPath == null ? "" : inputFileParentPath) 
				+ File.separator + tableName + "_" + nowDateStr + ".sql";
		File outputFile = new File(outputFilePath);
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outputFile));
		writer.append(buffer.toString());
		writer.flush();
		writer.close();
	}

}
