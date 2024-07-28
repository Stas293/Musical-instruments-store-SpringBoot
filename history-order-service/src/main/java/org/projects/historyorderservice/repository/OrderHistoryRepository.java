package org.projects.historyorderservice.repository;

import org.projects.historyorderservice.model.OrderHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderHistoryRepository extends MongoRepository<OrderHistory, String> {
}
