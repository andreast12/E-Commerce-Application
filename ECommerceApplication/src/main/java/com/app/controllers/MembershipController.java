package com.app.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entites.Membership;
import com.app.services.MembershipService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/admin/memberships")
@SecurityRequirement(name = "E-Commerce Application")
public class MembershipController {

    private final MembershipService membershipService;

    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @PostMapping
    public ResponseEntity<Membership> create(@RequestBody Membership membership) {
        Membership created = membershipService.create(membership);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Membership>> list() {
        return ResponseEntity.ok(membershipService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Membership> get(@PathVariable Long id) {
        return ResponseEntity.ok(membershipService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Membership> update(@PathVariable Long id, @RequestBody Membership membership) {
        return ResponseEntity.ok(membershipService.update(id, membership));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        membershipService.delete(id);
        return ResponseEntity.ok("Membership deleted");
    }

}
