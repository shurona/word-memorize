package shurona.wordfinder.custom.dto;

import java.util.List;

public class DeeplResponseDto {

    private List<Translation> translations;

    //============================================================================================================
    public List<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = translations;
    }

    @Override
    public String toString() {
        return "DeeplResponseDto{" +
            "translations=" + translations +
            '}';
    }

    public static class Translation {

        private String detected_source_language;

        private String text;

        public String getDetected_source_language() {
            return detected_source_language;
        }

        public void setDetected_source_language(String detected_source_language) {
            this.detected_source_language = detected_source_language;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return "Translation{" +
                "detected_source_language='" + detected_source_language + '\'' +
                ", text='" + text + '\'' +
                '}';
        }
    }
}
