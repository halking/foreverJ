package com.hal.mybatis;

public class ColumnBuilder {
	//`cart_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '购物车ID（自增）'
	
	private Column column;
	
	public ColumnBuilder(Column column) {
		this.column = column;
	}
	
	public static String getCreateTableBegin(String tableName) {
		return "CREATE TABLE `" + tableName + "` (\n";
	}
	
	public static String getCreateTableEnd(String comment) {
		return ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='" + (comment == null ? "" : comment) + "'";
	}
	
	public String getName() {
		return "`" + column.getName() + "` ";
	}
	
	public String getPk() {
		return " PRIMARY KEY (`" + column.getName() + "`)";
	}
	
	public String getKey() {
		return " KEY `" + column.getIndexName() + "` (`" + column.getName() + "`)";
	}
	
	public String getType() {
		return column.getType() + (column.getType().startsWith("date") ? "" : "(" + column.getLength() + ") ");
	}
	
	public String getNotNull() {
		return " NOT NULL ";
	}
	
	public String getDefault() {
		String defValue = null;
		String type = column.getType();
		String name = column.getName();
		if (defValue == null || defValue.length() == 0) {
			if ("tinyint".equalsIgnoreCase(type) || "byte".equalsIgnoreCase(type)
					|| "int".equalsIgnoreCase(type) || "bigint".equalsIgnoreCase(type)) {
				return " DEFAULT '0' ";
			} else if ("integer".equalsIgnoreCase(type) || "varchar".equalsIgnoreCase(type)) {
				return " DEFAULT NULL ";
			} else if ("datetime".equalsIgnoreCase(type)) {
				if ("create_datetime".equalsIgnoreCase(name)) {
					return " DEFAULT CURRENT_TIMESTAMP ";
				} else if ("update_datetime".equalsIgnoreCase(name)) {
					return " DEFAULT CURRENT_TIMESTAMP";
//						return " DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ";
				}
				return " DEFAULT NULL ";
			} else if ("decimal".equalsIgnoreCase(type)) {
				return " DEFAULT '0.000000' ";
			}
		}
		return "NULL".equalsIgnoreCase(defValue) ? " DEFAULT NULL " : " DEFAULT '" + defValue + "'";
	}
	
	public String getAutoIncrement() {
		return " AUTO_INCREMENT ";
	}
	
	public String getComment() {
		return column.getComment() == null ? "" : " COMMENT '" + column.getComment() + "'";
	}
	
	public boolean isPk() {
		return 1 == column.getIndexType();
	}
	
	public boolean isNumberType() {
		if ("int".equalsIgnoreCase(column.getType())
				|| "long".equalsIgnoreCase(column.getType())) {
			return true;
		}
		return false;
	}
	
	public String buildColumnSql() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getName());
		buffer.append(getType());
		if (isPk() && isNumberType()) {
			buffer.append(getNotNull());
			buffer.append(getAutoIncrement());
		} else {
			buffer.append(getDefault());
		}
		buffer.append(getComment());
		return buffer.toString();
	}
	

}
