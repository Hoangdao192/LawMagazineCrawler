package org.hoangdao.taskmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MagazineContent {

    public static final String TEXT_CONTENT = "text";
    public static final String IMAGE_CONTENT = "image";

    private List<AbstractContent> contents = new ArrayList<>();

    @Data
    @AllArgsConstructor
    public static abstract class AbstractContent {
        private String type;
        private int index = 0;

        public AbstractContent() {
        }
    }

    @Data
    public static class TextContent extends AbstractContent {
        private String asHtml;
        private String rawText;

        public TextContent(int index, String asHtml, String rawText) {
            super(TEXT_CONTENT, index);
            this.asHtml = asHtml;
            this.rawText = rawText;
        }

        public TextContent() {
            super(TEXT_CONTENT, 0);
        }
    }

    @Data
    public static class ImageContent extends AbstractContent {
        private String url;
        private String title;

        public ImageContent(int index, String url, String title) {
            super(IMAGE_CONTENT, index);
            this.url = url;
            this.title = title;
        }

        public ImageContent() {
            super(IMAGE_CONTENT, 0);
        }
    }

}
