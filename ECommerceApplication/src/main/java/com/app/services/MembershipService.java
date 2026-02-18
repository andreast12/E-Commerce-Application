package com.app.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.entites.Membership;
import com.app.repositories.MembershipRepo;
import com.app.exceptions.ResourceNotFoundException;
import java.util.List;

@Service
public class MembershipService {

    private final MembershipRepo membershipRepo;

    public MembershipService(MembershipRepo membershipRepo) {
        this.membershipRepo = membershipRepo;
    }

    public Optional<Membership> findByCode(String code) {
        if (code == null) return Optional.empty();
        return membershipRepo.findByCode(code.trim());
    }

    public Membership create(Membership membership) {
        return membershipRepo.save(membership);
    }

    public List<Membership> getAll() {
        return membershipRepo.findAll();
    }

    public Membership getById(Long id) {
        return membershipRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership", "id", id));
    }

    public Membership update(Long id, Membership membership) {
        Membership existing = getById(id);
        existing.setCode(membership.getCode());
        existing.setDiscountPercent(membership.getDiscountPercent());
        existing.setActive(membership.getActive());
        return membershipRepo.save(existing);
    }

    public void delete(Long id) {
        Membership existing = getById(id);
        membershipRepo.delete(existing);
    }
}
