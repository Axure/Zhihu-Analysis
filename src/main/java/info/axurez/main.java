package info.axurez;

import javax.persistence.*;

import info.axurez.database.entities.User;

public class main {
    public static void main(String args[]) {
        System.out.println("321");
        User user = new User("haha");
        /**
         * Get the entity manager.
         */

        EntityManagerFactory entityManagerFactory = Persistence
            .createEntityManagerFactory("info.axurez.zhihu");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        /**
         * Do the transaction.
         */
        entityManager.getTransaction().begin();
        entityManager.persist(new User("Our very first event!"));
        entityManager.persist(new User("A follow up event"));
        entityManager.getTransaction().commit();
        entityManager.close();
        /**
         * Done.
         */
        entityManagerFactory.close();
        System.out.println("Done!");
        return;
    }
}

