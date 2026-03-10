package com.fruitshop.fruit_shop.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

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
}