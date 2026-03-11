package com.fruitshop.fruit_shop.service;

import java.util.List;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.fruitshop.fruit_shop.entity.OrderItem;

import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

	private final JavaMailSender mailSender;

	public MailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendResetPasswordMail(String to, String code) {

		try {

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(to);
			helper.setSubject("Khôi phục mật khẩu");

			String html = """
					<div style="font-family:Arial;padding:20px;background:#f4f6f9">
					    <div style="max-width:500px;margin:auto;background:white;padding:30px;border-radius:8px">

					        <h2 style="color:#28a745;text-align:center">
					            Fruit Shop
					        </h2>

					        <p>Xin chào,</p>

					        <p>Bạn vừa yêu cầu đặt lại mật khẩu.</p>

					        <p>Mã xác nhận của bạn là:</p>

					        <div style="
					            font-size:28px;
					            font-weight:bold;
					            text-align:center;
					            background:#f1f1f1;
					            padding:15px;
					            border-radius:6px;
					            letter-spacing:4px;">
					            """ + code + """
					        </div>

					        <p style="margin-top:20px">
					            Mã này có hiệu lực trong <b>10 phút</b>.
					        </p>

					        <hr>

					        <p style="font-size:12px;color:#888">
					            Nếu bạn không yêu cầu đặt lại mật khẩu, hãy bỏ qua email này.
					        </p>

					    </div>
					</div>
					""";

			helper.setText(html, true);

			mailSender.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendOrderSuccess(String toEmail, String toName, Integer orderId, List<OrderItem> items, Double total) {

		try {

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(toEmail);
			helper.setSubject("Đặt hàng thành công!" + orderId);

			StringBuilder rows = new StringBuilder();

			for (OrderItem item : items) {

				String image = item.getProduct().getImage() != null ? item.getProduct().getImage()
						: "https://via.placeholder.com/60";

				rows.append("""
						    <tr style="border-bottom:1px solid #eee">
						        <td>
						            <img src="%s" width="60"
						                 style="vertical-align:middle;border-radius:4px;margin-right:10px">
						            <span>%s</span>
						        </td>
						        <td align="center">%d</td>
						        <td align="right">%,.0f đ</td>
						    </tr>
						""".formatted(image, item.getProduct().getName(), item.getQuantity(), item.getPrice()));
			}

			String body = """
					<div style="font-family:Arial,Helvetica,sans-serif;background:#f4f4f4;padding:20px">
					<table width="600" align="center" style="background:#ffffff;border-radius:6px;overflow:hidden">

					<tr>
					<td style="background:#28a745;color:#fff;padding:16px;text-align:center">
					<h2 style="margin:0">Fruit Shop</h2>
					<p style="margin:5px 0 0">Đặt hàng thành công</p>
					</td>
					</tr>

					<tr>
					<td style="padding:20px">

					<h3>Xin chào %s, cảm ơn bạn đã đặt hàng!</h3>
					<p>Mã đơn hàng: <b>#%d</b></p>

					<table width="100%%" cellpadding="8" cellspacing="0"
					style="border-collapse:collapse;margin-top:15px">

					<tr style="background:#f8f8f8">
					<th align="left">Sản phẩm</th>
					<th align="center">SL</th>
					<th align="right">Giá</th>
					</tr>

					%s

					</table>

					<table width="100%%" style="margin-top:20px">

					<tr>
					<td align="right">Tạm tính:</td>
					<td align="right" width="140">%,.0f đ</td>
					</tr>

					<tr>
					<td align="right"><b>Tổng thanh toán:</b></td>
					<td align="right">
					<b style="color:#28a745;font-size:16px">%,.0f đ</b>
					</td>
					</tr>

					</table>

					<div style="text-align:center;margin-top:25px">
					<a href="http://localhost:8080/orders/%d"
					style="display:inline-block;background:#28a745;color:#fff;
					padding:12px 20px;text-decoration:none;border-radius:4px;font-weight:bold">
					Xem chi tiết đơn hàng
					</a>
					</div>

					<p style="margin-top:20px">
					Chúng tôi sẽ liên hệ với bạn khi đơn hàng được giao.
					</p>

					<p style="font-size:12px;color:#888">
					Nếu bạn không thực hiện đơn hàng này, vui lòng bỏ qua email.
					</p>

					</td>
					</tr>

					<tr>
					<td style="background:#f1f1f1;text-align:center;padding:10px;font-size:12px;color:#666">
					© 2026 Fruitables. All rights reserved.
					</td>
					</tr>

					</table>
					</div>
					""".formatted(toName, orderId, rows, total, total, orderId);

			helper.setText(body, true);

			mailSender.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}