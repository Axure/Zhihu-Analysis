package info.axurez;

import javax.persistence.*;

import info.axurez.database.entities.User;

public class main {
    public static void main(String args[]) {
        System.out.println("321");
        User user = new User("haha");
        EntityManagerFactory entityManagerFactory = Persistence
            .createEntityManagerFactory("info.axurez.zhihu");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(new User("Our very first event!"));
        entityManager.persist(new User("A follow up event"));
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}

