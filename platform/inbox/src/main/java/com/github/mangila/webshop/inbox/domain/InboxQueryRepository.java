package com.github.mangila.webshop.inbox.domain;


import com.github.mangila.webshop.inbox.domain.cqrs.InboxReplayQuery;

import java.util.List;

public interface InboxQueryRepository {
    List<Inbox> replay(InboxReplayQuery query);
}
