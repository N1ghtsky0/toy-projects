package xyz.jiwook.chatting.domain.member.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.jiwook.chatting.domain.member.entity.MemberEntity;

import java.util.Optional;

public interface MemberCrudRepo extends CrudRepository<MemberEntity, String> {
    boolean existsByUsername(String username);
    Optional<MemberEntity> findByUsername(String username);
}
