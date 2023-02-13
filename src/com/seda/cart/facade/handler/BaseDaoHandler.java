package com.seda.cart.facade.handler;

import java.sql.Connection;
import com.seda.data.spi.DaoHandler;
import com.seda.data.spi.PageInfo;
/**
 * 
 * @author mmontisano
 *
 */
public abstract class BaseDaoHandler extends DaoHandler {

	public static final String IDX_DOLIST_LISTA= "0";
	public static final String IDX_DOLIST_RIEPILOGO= "1";
	public static final String IDX_DODETAIL_DATI= "0";

	private PageInfo pageInfo = null;

	public BaseDaoHandler(Connection connection, String schema) {
		super(connection, schema);
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}
	
	protected void registerPageInfo(int rowsPerPage, int pageNumber, int firstRow, int lastRow, int numRows, int numPages) {
		this.pageInfo = new PageInfo();
		pageInfo.setFirstRow(firstRow);
		pageInfo.setLastRow(lastRow);
		pageInfo.setNumRows(numRows);
		pageInfo.setNumPages(numPages);
		pageInfo.setPageNumber(pageNumber);
		pageInfo.setRowsPerPage(rowsPerPage);
//		if (numPages > pageInfo.getNumPages())
//			pageInfo.setPageNumber(pageInfo.getNumPages());
//		else pageInfo.setPageNumber(numPages);
	}
}