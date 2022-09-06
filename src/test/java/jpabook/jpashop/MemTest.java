package jpabook.jpashop;

import jpabook.jpashop.hello.Mem;
import jpabook.jpashop.hello.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
class MemTest {
    @Autowired
    MemberRepository memberRepository;


    @Test
    @Transactional
    @Rollback(false)
    public void testMember(){
        Mem mem = new Mem();
        mem.setUsername("memberA");
        Long savedId = memberRepository.save(mem);
        Mem findMem = memberRepository.find(savedId);
        Assertions.assertThat(findMem.getId()).isEqualTo(mem.getId());

        Assertions.assertThat(findMem.getUsername()).isEqualTo(mem.getUsername());
        Assertions.assertThat(findMem).isEqualTo(mem);
    }
}