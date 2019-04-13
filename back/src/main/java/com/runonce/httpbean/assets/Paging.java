package com.runonce.httpbean.assets;


import com.runonce.util.Tool;

/**
 * @author 
 */
public class Paging {
	private Integer total; // 总记录数
	private Integer pageIndex; // 当前页
	private Integer pageSize; // size
	private Integer startRow; // 起始行
	private Integer endRow; // 结束行
	private Integer totalPage;// 总页数

	public Paging() {
		super();
	}

	public Paging(Integer total, Integer pageIndex, Integer pageSize, Integer startRow, Integer endRow,
                  Integer totalPage) {
		super();
		this.total = total;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.startRow = (this.pageIndex - 1) * this.pageSize;
		this.endRow = endRow;
		this.totalPage = totalPage;
	}

	public Integer getTotal() {
		if (this.total == null) {
			this.total = 0;
		}
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getPageIndex() {
		if (this.pageIndex == null) {
			this.pageIndex = 0;
		}
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		if (this.pageSize == null) {
			this.pageSize = 0;
		}
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getStartRow() {
		this.startRow = (this.pageIndex - 1) * this.pageSize;
		return startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}

	public Integer getEndRow() {
		this.endRow = (this.pageIndex - 1) * this.pageSize + this.pageSize;
		return endRow;
	}

	public void setEndRow(Integer endRow) {
		this.endRow = endRow;
	}

	public Integer getTotalPage() {
		if (this.total != null && this.pageSize != null && this.total != 0 && this.pageSize != 0) {
			this.totalPage = Tool.getInt(this.total, this.pageSize);
		}
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
}
