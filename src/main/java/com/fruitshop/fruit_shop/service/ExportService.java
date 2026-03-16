package com.fruitshop.fruit_shop.service;

import com.fruitshop.fruit_shop.entity.Order;
import com.fruitshop.fruit_shop.repository.OrderRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import com.itextpdf.text.Font;

@Service
public class ExportService {

	private final OrderRepository orderRepository;

	public ExportService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	// ================= EXPORT EXCEL =================

	public byte[] exportExcel() throws Exception {

		List<Order> orders = orderRepository.findAll();

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Orders");

		Row header = sheet.createRow(0);

		header.createCell(0).setCellValue("ID");
		header.createCell(1).setCellValue("Khách hàng");
		header.createCell(2).setCellValue("Người nhận");
		header.createCell(3).setCellValue("SĐT");
		header.createCell(4).setCellValue("Tổng tiền");
		header.createCell(5).setCellValue("Trạng thái");
		header.createCell(6).setCellValue("Ngày tạo");

		int rowNum = 1;

		for (Order o : orders) {

			Row row = sheet.createRow(rowNum++);

			row.createCell(0).setCellValue(o.getId());

			row.createCell(1).setCellValue(o.getUser() != null ? o.getUser().getLastName() : "");

			row.createCell(2).setCellValue(o.getReceiverName());

			row.createCell(3).setCellValue(o.getReceiverPhone());

			row.createCell(4).setCellValue(o.getTotal());

			row.createCell(5).setCellValue(o.getStatus().name());

			row.createCell(6).setCellValue(o.getCreatedAt() != null ? o.getCreatedAt().toString() : "");
		}

		for (int i = 0; i < 7; i++) {
			sheet.autoSizeColumn(i);
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workbook.write(out);
		workbook.close();

		return out.toByteArray();
	}

	// ================= EXPORT PDF =================

	public byte[] exportPdf() throws Exception {

		List<Order> orders = orderRepository.findAll();

		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		PdfWriter.getInstance(document, out);

		document.open();

		// load font từ resources
		InputStream fontStream = getClass().getResourceAsStream("/fonts/arial.ttf");

		byte[] fontBytes = fontStream.readAllBytes();

		BaseFont baseFont = BaseFont.createFont("arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, fontBytes,
				null);

		Font titleFont = new Font(baseFont, 18, Font.BOLD);
		Font normalFont = new Font(baseFont, 12);

		Paragraph title = new Paragraph("Danh sách đơn hàng", titleFont);
		title.setAlignment(Element.ALIGN_CENTER);

		document.add(title);
		document.add(new Paragraph(" "));

		PdfPTable table = new PdfPTable(7);
		table.setWidthPercentage(100);

		table.addCell(new Phrase("ID", normalFont));
		table.addCell(new Phrase("Khách hàng", normalFont));
		table.addCell(new Phrase("Người nhận", normalFont));
		table.addCell(new Phrase("SĐT", normalFont));
		table.addCell(new Phrase("Tổng tiền", normalFont));
		table.addCell(new Phrase("Trạng thái", normalFont));
		table.addCell(new Phrase("Ngày tạo", normalFont));

		for (Order o : orders) {

			table.addCell(new Phrase(o.getId().toString(), normalFont));

			table.addCell(new Phrase(o.getUser() != null ? o.getUser().getLastName() : "", normalFont));

			table.addCell(new Phrase(o.getReceiverName(), normalFont));

			table.addCell(new Phrase(o.getReceiverPhone(), normalFont));

			table.addCell(new Phrase(o.getTotal().toString(), normalFont));

			table.addCell(new Phrase(o.getStatus().name(), normalFont));

			table.addCell(new Phrase(o.getCreatedAt() != null ? o.getCreatedAt().toString() : "", normalFont));
		}

		document.add(table);

		document.close();

		return out.toByteArray();
	}
}