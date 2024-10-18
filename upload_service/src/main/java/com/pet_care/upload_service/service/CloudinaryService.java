package com.pet_care.upload_service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.coobird.thumbnailator.Thumbnails;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryService {

    @NotNull Cloudinary cloudinary;

    /**
     * @param file
     * @return
     */
    @NotNull Mono<String> uploadImage(@NotNull MultipartFile file) {
        return Mono.fromCallable(() -> {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            return (String) uploadResult.get("url");
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * @param fileParts
     * @return
     */
    @NotNull
    public Mono<List<String>> uploadImages(@NotNull Flux<FilePart> fileParts) {
        return fileParts
                .flatMap(filePart -> filePart.content()
                        .map(dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            DataBufferUtils.release(dataBuffer);  // Giải phóng tài nguyên
                            return bytes;
                        })
                        .next()  // Lấy buffer đầu tiên của FilePart
                        .flatMap(bytes -> Mono.fromCallable(() -> {
                            // Upload ảnh lên Cloudinary

                            Map<?, ?> uploadResult = cloudinary.uploader().upload(bytes, ObjectUtils.emptyMap());
                            return (String) uploadResult.get("url");
                        }))
                )
                .collectList();  // Thu thập các URL thành List
    }


    private ByteArrayInputStream compressImage(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Chọn Writer cho định dạng PNG
        ImageWriter writer = ImageIO.getImageWritersByFormatName("webp").next();
        ImageWriteParam param = writer.getDefaultWriteParam();

        // Thiết lập chế độ nén không mất dữ liệu
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(1.0f); // 1.0 cho chất lượng tốt nhất (lossless)

        try (ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(image, null, null), param);
        }

        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
