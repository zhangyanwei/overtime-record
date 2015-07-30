package tools.ctd.logic.report;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import tools.ctd.dao.DAOFactoryManager;
import tools.ctd.dao.IDAOFactoryProvider;
import tools.ctd.dao.IProjectDAO;
import tools.ctd.dao.IRecordDAO;
import tools.ctd.env.CTDProperties;
import tools.ctd.env.CoreProperties;
import tools.ctd.exception.CTDException;
import tools.ctd.ldap.LDAPAccessor;
import tools.ctd.util.DateFormat;
import tools.ctd.util.FileFinder;
import tools.ctd.util.MultiSort;
import tools.ctd.vo.ActualRecord;
import tools.ctd.vo.PlanRecord;
import tools.ctd.vo.Project;
import tools.ctd.vo.WorkRecord;

public class RecordReport {

	private static final Log LOG = LogFactory.getLog(RecordReport.class);
	
	public void report(String projectid, OutputStream os)
			throws CTDException {
		IDAOFactoryProvider provider = DAOFactoryManager.getProvider();
		IRecordDAO recordDAO = provider.getRecordDAO();
		List<WorkRecord> records = recordDAO.getRecords(projectid);
		
		reportRecords(projectid, records, os);
	}

	public void report(String projectid, String usrid, OutputStream os)
			throws CTDException {
		IDAOFactoryProvider provider = DAOFactoryManager.getProvider();
		IRecordDAO recordDAO = provider.getRecordDAO();
		List<WorkRecord> records = recordDAO.getRecords(projectid, usrid);

		reportRecords(projectid, records, os);
	}
	
	private void reportRecords(String projectid, List<WorkRecord> records, OutputStream os)
			throws CTDException {
		OPCPackage pkg = null;
		try {
			CTDProperties props = CoreProperties
					.getProperties(CTDProperties.class);
			String tmplDir = props.getTmplRecordDir();
			InputStream is = new FileInputStream(FileFinder.findFile(
					"ctd_record_normal.xlsx", tmplDir));
			pkg = OPCPackage.open(is);
			Workbook wb = new XSSFWorkbook(pkg); // or new HSSFWorkbook();

			IDAOFactoryProvider df = DAOFactoryManager.getProvider();
			IProjectDAO projectDAO = df.getProjectDAO();
			Project project = projectDAO.getProject(projectid);

			CellStyle cellStyle = wb.createCellStyle();
			Font font = wb.createFont();
			font.setFontHeightInPoints((short) 9); // 字体大小
			cellStyle.setFont(font);
			cellStyle.setBorderTop(CellStyle.BORDER_THIN);
			cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
			cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
			cellStyle.setBorderRight(CellStyle.BORDER_THIN);

			Calendar calendar = Calendar.getInstance();
			// 星期一是一周的第一天(因为周末的加班要上一周申请)。
			calendar.setFirstDayOfWeek(Calendar.MONDAY);
			// 如果1日那天是周日，这一周就算是该月的第一周，同时也算是上个月的最后一周。
			calendar.setMinimalDaysInFirstWeek(1);

			MultiSort.sort(records, new String[] { "workDate", "usrId" },
					new boolean[] { true, true });

			String currentMonth = null;
			Sheet sheet = null;
			int index = 8, currentWeek = 0, number = 1;
			int wkStartRow = -1;
			for (WorkRecord record : records) {

				Date workDate = record.getWorkDate();
				String month = DateFormat.formatMonth(workDate);
				if (!month.equals(currentMonth)) {
					if (wkStartRow > -1) {
						// 为前一个选项卡中的最后一周的记录进行分组。
						sheet.groupRow(wkStartRow, index - 1);
					}
					
					currentMonth = month;
					sheet = createSheet(wb, currentMonth);
					
					// project name
					Cell cell = getCell(sheet, 3, 2);
					cell.setCellValue(project.getName());

					// project id
					cell = getCell(sheet, 4, 2);
					cell.setCellValue(projectid);

					// 为新的选项卡初始化数据。
					index = 8;
					currentWeek = 0;
					number = 1;
					
					// 冻结选项卡中的Title部分。
					sheet.createFreezePane(0, index); // index 始终是 8
				}

				// I'm sure sheet is not null.

				calendar.setTime(workDate);
				int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
				if (currentWeek != weekOfMonth) {
					currentWeek = weekOfMonth;
					int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2;
					calendar.add(Calendar.DATE, dayOfWeek > 0 ? 0 - dayOfWeek : 0 - dayOfWeek - 7);
					Date begin = calendar.getTime();
					calendar.add(Calendar.DATE, 6);
					Date end = calendar.getTime();
					String weekPeroid = DateFormat.formatDatePeroid(begin, end);

					// 合并单元格
					sheet.addMergedRegion(new CellRangeAddress(index, index, 1, 13));
					CellStyle style = wb.createCellStyle();
					style.cloneStyleFrom(cellStyle);
					style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
					style.setFillPattern(CellStyle.SOLID_FOREGROUND);
					Cell cell = getCell(sheet, index, 1);
					cell.setCellValue(weekPeroid);
					
					// 设置合并单元格的边框
					for (int i = 1; i <= 13; i++) {
						cell = getCell(sheet, index, i);
						cell.setCellStyle(style);
					}
					
					if (wkStartRow > -1) {
						// 为该月前一周的记录进行分组。
						sheet.groupRow(wkStartRow, index - 1);
					}

					index++;
					wkStartRow = index;
				}

				// No.
				Cell cell = getCell(sheet, index, 1);
				cell.setCellValue(number++);

				// 加班员工姓名
				cell = getCell(sheet, index, 2);
				cell.setCellValue(record.getUserName());

				// 加班日期
				cell = getCell(sheet, index, 3);
				cell.setCellValue(DateFormat.formatDate(record.getWorkDate()));

				PlanRecord plan = record.getPlan();
				// 计划
				if (plan != null) {
					// 加班时间段
					cell = getCell(sheet, index, 4);
					cell.setCellValue(DateFormat.formatTimePeroid(
							plan.getBeginTime(), plan.getEndTime()));

					// 计时
					cell = getCell(sheet, index, 5);
					cell.setCellValue(DateFormat.formatTimeInterval(
							plan.getBeginTime(), plan.getEndTime()));

					// 加班理由
					// TODO
					cell = getCell(sheet, index, 6);
					cell.setCellValue("");
				}

				ActualRecord actual = record.getActual();
				// 实际
				if (actual != null) {
					// 加班时间段
					cell = getCell(sheet, index, 7);
					cell.setCellValue(DateFormat.formatTimePeroid(
							actual.getBeginTime(), actual.getEndTime()));

					// 计时
					cell = getCell(sheet, index, 8);
					cell.setCellValue(DateFormat.formatTimeInterval(
							actual.getBeginTime(), actual.getEndTime()));

					// 上车时间
					cell = getCell(sheet, index, 9);
					cell.setCellValue(DateFormat.formatTime(actual
							.getTaxiTimeBegin()));

					// 下车时间
					cell = getCell(sheet, index, 10);
					cell.setCellValue(DateFormat.formatTime(actual
							.getTaxiTimeEnd()));

					// 起点
					cell = getCell(sheet, index, 11);
					cell.setCellValue(actual.getTaxiStartLocation());

					// 终点
					cell = getCell(sheet, index, 12);
					cell.setCellValue(actual.getTaxiEndLocation());

					// 车票金额
					cell = getCell(sheet, index, 13);
					cell.setCellValue(actual.getTaxiTicket());
				}
				
				// 无论是否有数据都需要设置单元格的样式，否则看起来就不那么好看了。
				for (int i = 1; i <= 13; i++) {
					cell = getCell(sheet, index, i);
					cell.setCellStyle(cellStyle);
				}

				index++;
			}
			
			if (wkStartRow > -1) {
				// 最后一周的记录进行分组。
				sheet.groupRow(wkStartRow, index - 1);
			}
			
			// 删除template选项卡。
			wb.removeSheetAt(wb.getSheetIndex("template"));
			
			//设置当前Sheet
			wb.setActiveSheet(wb.getNumberOfSheets() - 1);
			wb.setSelectedTab(wb.getNumberOfSheets() - 1);

			wb.write(os);

			pkg.close();
		} catch (Exception e) {
			LOG.error(e);
			throw new CTDException(e);
		} finally {
			if (pkg != null) {
				try {
					pkg.close();
				} catch (IOException e) {
					LOG.warn(e);
				}
			}
		}
	}

	private static Sheet createSheet(Workbook wb, String sheetName) {
		int sheetIndex = wb.getSheetIndex("template");
		Sheet cloneSheet = wb.cloneSheet(sheetIndex);
		wb.setSheetName(wb.getSheetIndex(cloneSheet), sheetName);
		return cloneSheet;
	}

//	private static Cell getCellWithStyle(Sheet sheet, int rownum, int cellnum,
//			CellStyle style) {
//		Cell cell = getCell(sheet, rownum, cellnum);
//		cell.setCellStyle(style);
//		return cell;
//	}

	private static Cell getCell(Sheet sheet, int rownum, int cellnum) {
		Row row = sheet.getRow(rownum);
		if (row == null) {
			row = sheet.createRow(rownum);
		}

		Cell cell = row.getCell(cellnum);
		if (cell == null) {
			cell = row.createCell(cellnum);
		}

		return cell;
	}

}
