package kr.book.search;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private String title;
    private String authors;
    private String publisher;
    private String thumbnail;
}
