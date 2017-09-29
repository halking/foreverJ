package org.hal.forj.common.util;

import java.io.Serializable;

public class Pagination<T> implements Serializable {

	private static final long serialVersionUID = -5079179307901712051L;

	/** The data number of per page */
	private int pageSize;

	/** All the data */
	private int total;

	/** the number of total page */
	private int totalPage;

	/** the offset of page #currentPage */
	private int offset;

	private T result;

	public Pagination() {
	}

	public Pagination(int pageSize, int total, int offset, T object) {
		setPageSize(pageSize);
		setTotal(total);
		setOffset(offset);
		setResult(object);
		calculate();
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	private void calculate() {
		this.totalPage = (total % pageSize == 0) ? (total / pageSize) : (total / pageSize + 1);
		if (offset < 1 || totalPage == 0) {
			offset = 1;
		}
		if (totalPage != 0 && offset > totalPage) {
			offset = totalPage;
		}
	}
}
