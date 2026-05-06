package skytrack.business.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.WriterException;
import skytrack.business.exception.booking.QrCodeGenerationException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


@Service
@RequiredArgsConstructor
public class QrGenerator {
    @Value("${app.base-url}")
    private String baseUrl;

    private final QRCodeWriter qrCodeWriter;

    public byte[] generate(String bookingReference){
        try{
            String qrContent = baseUrl + "/bookings/" + bookingReference;
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 250, 250);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", output);
            return output.toByteArray();
        }
        catch(WriterException | IOException e){
            throw new QrCodeGenerationException(e);
        }
    }
}
