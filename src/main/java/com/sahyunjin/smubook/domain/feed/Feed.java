package com.sahyunjin.smubook.domain.feed;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Feed {

    private Long id;
    private String content;
    private String modifiedDate;  // 글 생성시각 및 수정시각

    private Long writerUserId;
    private List<Long> likeUserIds;
    private Integer likeCount;
    private List<Long> commentIds;
}
