package zic.honeyComboFactory.view.boardCombo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import zic.honeyComboFactory.common.config.CKEditor5ImgUploadConfig;

@Controller
public class ImgUploadController {
	// application.properties에서 설정된 upload.path 값을 가져오는 설정 클래스 주입
	@Autowired
	private CKEditor5ImgUploadConfig uploadConfig;

	// CKEditor5에서 이미지 업로드 시 호출
	@PostMapping("/boardCombo/CKEditor5/uploadImage")
	@ResponseBody
	public ResponseEntity<?> uploadImage(@RequestParam("upload") MultipartFile file) {
		try {
			// 업로드된 원본 파일의 확장자 추출
			String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			// UUID 기반으로 고유 파일명 생성
			String newFileName = UUID.randomUUID().toString() + ext;
			System.out.println("파일명 [" + newFileName + "]");
			// 서버 저장 경로 (application.properties에서 설정한 경로)
			File target = new File(uploadConfig.getPath(), newFileName);
			System.out.println("실제 저장 경로 [" + target + "]");
			// 실제 파일 저장
			file.transferTo(target);

			// 저장된 이미지에 접근 가능한 URL 구성
			String imageUrl = "/boardCombo/CKEditor5/loadImage?name=" + newFileName;
			System.out.println("<img src=> 안에 들어간 URL [" + imageUrl + "]");
			// CKEditor에서 사용할 수 있도록 JSON 응답
			return ResponseEntity.ok(Map.of("url", imageUrl));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "이미지 업로드 실패"));
		}
	}

	// 저장된 이미지를 웹에서 보여줄 수 있도록 스트리밍
	// 이미지에 저장된 /editor/image가 @GetMapping 호출해서 실제 저장된 위치에서 이미지 불러옴
	@GetMapping("/boardCombo/CKEditor5/loadImage")
	public ResponseEntity<byte[]> getImage(@RequestParam String name) throws IOException {
		// 업로드된 이미지 경로에서 파일 찾기
		File image = new File(uploadConfig.getPath(), name);
		System.out.println("찾은 경로 [" + image + "]");
		// 파일 존재하지 않으면 에러 반환
		if (!image.exists()) {
			return ResponseEntity.notFound().build();
		}
		// MIME 타입 자동 추론
		String mime = Files.probeContentType(image.toPath());
		System.out.println("타입 [" + mime + "]");
		// 파일 내용을 byte[]로 읽기
		byte[] data = Files.readAllBytes(image.toPath());

		// 이미지 데이터와 MIME 타입 포함하여 응답
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(mime)).body(data);
	}
}
