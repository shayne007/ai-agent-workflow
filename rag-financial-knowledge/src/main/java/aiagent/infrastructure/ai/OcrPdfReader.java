package aiagent.infrastructure.ai;

import java.io.IOException;
import java.util.List;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.core.io.Resource;

/**
 * OcrPdfReader
 *
 * @since 2025/4/16
 */
public class OcrPdfReader implements DocumentReader {
    private final Tesseract tesseract = new Tesseract();
    private final Resource resource;

    public OcrPdfReader(Resource resource) {
        this.resource = resource;
        tesseract.setLanguage("chi_sim");
    }

    @Override
    public List<Document> get() {
        try {
            // 转换
            String ocr = tesseract.doOCR(resource.getFile());
            Document document = new Document(ocr);
            return List.of(document);
        } catch (TesseractException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
