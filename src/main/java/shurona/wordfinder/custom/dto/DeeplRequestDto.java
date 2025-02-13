package shurona.wordfinder.custom.dto;

import java.util.Arrays;

public class DeeplRequestDto {

    private String[] text;
    private String target_lang;
    private String source_lang;

    public DeeplRequestDto(String[] text) {
        this.text = text;
        this.target_lang = "KO";
        this.source_lang = "EN";
    }

    public String[] getText() {
        return text;
    }

    public String getTarget_lang() {
        return target_lang;
    }

    public String getSource_lang() {
        return source_lang;
    }

    @Override
    public String toString() {
        return "DeeplRequestDto{" +
            "text=" + Arrays.toString(text) +
            ", target_lang='" + target_lang + '\'' +
            ", source_lang='" + source_lang + '\'' +
            '}';
    }
}
