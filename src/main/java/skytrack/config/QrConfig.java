package skytrack.config;

import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QrConfig {

    @Bean
    public QRCodeWriter qrCodeWriter() {
        return new QRCodeWriter();
    }
}