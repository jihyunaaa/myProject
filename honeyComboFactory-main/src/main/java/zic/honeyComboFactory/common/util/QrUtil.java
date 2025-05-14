package zic.honeyComboFactory.common.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QrUtil { // QR 코드
	// 전달받은 텍스트를 QR 코드로 생성하고, 이를 Base64로 인코딩하여 반환 기능
	public static String generateQRCodeBase64(String text, int width, int height, Color foregroundColor,
			Color backgroundColor) {
		try {
			// QR 코드 생성 객체화
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			// 텍스트를 바탕으로 QR 코드 비트 행렬 생성
			BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

			// BitMatrix를 BufferedImage(이미지 객체)로 변환
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			// BufferedImage의 각 픽셀을 색상에 맞게 설정
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					// QR 코드의 비트값에 따라 색상 설정
					bufferedImage.setRGB(x, y,
							bitMatrix.get(x, y) ? foregroundColor.getRGB() : backgroundColor.getRGB());
				}
			}

			// 이미지 데이터를 바이트 배열로 저장하기 위한 스트림 생성
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			// BufferedImage를 PNG 형식으로 outputStream에 저장
			ImageIO.write(bufferedImage, "png", outputStream);

			// 바이트 배열로 변환된 이미지 데이터를 가져옴
			byte[] imageBytes = outputStream.toByteArray();
			// 바이트 배열을 Base64 문자열로 인코딩
			String base64Image = Base64.getEncoder().encodeToString(imageBytes);

			return base64Image;
		} catch (Exception e) { // 에러 발생 시
			e.printStackTrace();
			return null;
		}
	}
}
