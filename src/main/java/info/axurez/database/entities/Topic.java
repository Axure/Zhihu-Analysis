package info.axurez.database.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by axurez on 2016/2/25.
 */
@Entity
@Table(name = "topics")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @ManyToMany(
        targetEntity = Question.class
    )
    private Set<Question> questions = new HashSet<>();
    @Column(name = "name")
    private String name;
    @OneToMany(
        targetEntity = Topic.class,
        mappedBy = "parentTopic"
    )
    private Set<Topic> childrenTopics = new HashSet<>();
    @ManyToOne(targetEntity = Topic.class)
    private Topic parentTopic;
    @Column(name = "zhihu_id", unique = true)
    private String zhihuId;

    public Topic() {
    }

    public long getId() {
        return id;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Topic> getChildrenTopics() {
        return childrenTopics;
    }

    public void setChildrenTopics(Set<Topic> childrenTopics) {
        this.childrenTopics = childrenTopics;
    }

    public Topic getParentTopic() {
        return parentTopic;
    }

    public void setParentTopic(Topic parentTopic) {
        this.parentTopic = parentTopic;
    }

    public String getZhihuId() {
        return zhihuId;
    }

    public void setZhihuId(String zhihuId) {
        this.zhihuId = zhihuId;
    }
}
