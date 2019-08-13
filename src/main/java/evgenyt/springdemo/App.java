package evgenyt.springdemo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hibernate batch processing
 * We must clear cache periodically to prevent OutOfMemory error.
 * Aug 2019 EvgenyT
 */

public class App {

    private static final int FLUSH_PERIOD = 20; // in practice around 50.000 is ok

    public static void main( String[] args ) {
        // Get application context from file
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "applicationContext.xml");
        // Get factory bean and create session
        SessionFactory sessionFactory = context.getBean("sessionFactory",
                SessionFactory.class);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        for(int count = 1; count <= 100; count++) {
            PersonEntity personEntity = new PersonEntity("Person N" + count);
            session.save(personEntity);
            // It's time to free memory from cached data
            if(count % FLUSH_PERIOD == 0) {
                session.flush();
                session.clear();
            }
        }
        session.getTransaction().commit();
        session.close();
    }
}
