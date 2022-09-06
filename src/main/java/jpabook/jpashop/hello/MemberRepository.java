package jpabook.jpashop.hello;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Mem mem) {
        em.persist(mem);
        return mem.getId();
    }

    public Mem find(Long id) {
        return em.find(Mem.class, id);
    }
}
