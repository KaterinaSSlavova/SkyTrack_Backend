package skytrack.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import skytrack.business.exception.booking.QrCodeGenerationException;
import skytrack.business.service.QrGenerator;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QrGeneratorTest {
    private final QRCodeWriter realQrCodeWriter = new QRCodeWriter();

    @Mock
    private QRCodeWriter mockQrCodeWriter;

    @InjectMocks
    private QrGenerator qrGenerator;

    @BeforeEach
    void setUp() {
        qrGenerator = new QrGenerator(realQrCodeWriter);
        ReflectionTestUtils.setField(qrGenerator, "baseUrl", "http://localhost:8080");
    }

    @Test
    void generate_shouldReturnByteArray_whenReferenceIsValid() throws Exception {
        byte[] result = qrGenerator.generate("SKY-ABC123");

        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    void generate_shouldReturnPngImage_whenReferenceIsValid() throws Exception {
        byte[] result = qrGenerator.generate("SKY-ABC123");

        assertEquals((byte) 137, result[0]);
        assertEquals((byte) 80, result[1]);
        assertEquals((byte) 78, result[2]);
        assertEquals((byte) 71, result[3]);
    }

    @Test
    void generate_shouldReturnDifferentQrCodes_whenReferencesAreDifferent() throws Exception {
        byte[] result1 = qrGenerator.generate("SKY-ABC123");
        byte[] result2 = qrGenerator.generate("SKY-XYZ999");

        assertFalse(Arrays.equals(result1, result2));
    }

    @Test
    void generate_shouldThrowQrCodeGenerationException_whenWriterFails() throws Exception {
        QrGenerator qrGeneratorWithMock = new QrGenerator(mockQrCodeWriter);
        ReflectionTestUtils.setField(qrGeneratorWithMock, "baseUrl", "http://localhost:8080");

        when(mockQrCodeWriter.encode(anyString(), any(BarcodeFormat.class), anyInt(), anyInt()))
                .thenThrow(new WriterException("error"));

        assertThrows(QrCodeGenerationException.class, () -> qrGeneratorWithMock.generate("SKY-ABC123"));
    }

    @Test
    void generate_shouldThrowQrCodeGenerationException_whenReferenceIsNull() throws Exception {
        QrGenerator qrGeneratorWithMock = new QrGenerator(mockQrCodeWriter);
        ReflectionTestUtils.setField(qrGeneratorWithMock, "baseUrl", "http://localhost:8080");

        when(mockQrCodeWriter.encode(anyString(), any(BarcodeFormat.class), anyInt(), anyInt()))
                .thenThrow(new WriterException("null input"));

        assertThrows(QrCodeGenerationException.class, () -> qrGeneratorWithMock.generate(null));
    }
}
