package com.hal.mybatis;

/**
 * SQL列
 * @author xuchangwen
 */
public class Column {
	
	/**
	 * 列名
	 */
	private String name;
	/**
	 * 列类型
	 */
	private String type;
	/**
	 * 列长度
	 */
	private String length;
	/**
	 * 列索引类型（0：无索引；1：主键；2：索引）
	 */
	private byte indexType;
	/**
	 * 列索引名称
	 */
	private String indexName;
	/**
	 * 列备注
	 */
	private String comment;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public byte getIndexType() {
		return indexType;
	}
	public void setIndexType(byte indexType) {
		this.indexType = indexType;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

}
