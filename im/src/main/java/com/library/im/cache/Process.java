package com.library.im.cache;

import com.library.im.domain.EaseUser;

/**
 * Created by longbh on 16/5/26.
 */
public interface Process {
    EaseUser process(String key);
}
