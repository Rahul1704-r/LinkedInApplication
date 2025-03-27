package com.Project.LinkedIn.Service.Event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostLikedEvent {
    Long creatorId;
    Long postId;
    Long likedByUserId;
}
