package info.axurez.database.dao;

import info.axurez.database.entities.Question;
import info.axurez.database.entities.Topic;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;

/**
 * Created by axurez on 2016/2/26.
 */
@Repository
public class TopicDao {
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public TopicDao() {
    }

    public Topic findById(long id) {
        Topic topic = entityManager.find(Topic.class, id);
        return topic;
    }

    @Transactional
    public void addTopicToQuestion(Question question, Topic topic) {
        Set<Topic> topics = question.getTopics();
        topics.add(topic);
        question.setTopics(topics);
    }
}
