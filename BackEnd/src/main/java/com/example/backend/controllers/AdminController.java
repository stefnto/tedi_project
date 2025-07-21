package com.example.backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.models.Member;
import com.example.backend.models.MemberInfo;
import com.example.backend.services.MemberServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

  private final MemberServiceImpl memberService;


   /**
    * Get a list of all members with their information, only accessible by admin.
    * @return A ResponseEntity containing a list of MemberInfo objects.
    */
    @GetMapping("/members-info")
    public ResponseEntity<List<MemberInfo>> getMembers() {
      // ok() gives a 200 status message
      return ResponseEntity.ok().body(memberService.getMembers());
    }

    /**
     * @param emails list of emails to filter members
     * @return A ResponseEntity containing a list of Member objects.
     */
    @GetMapping("/members-by-emails")
    public ResponseEntity<List<Member>> getSpecMembers(@RequestHeader List<String> emails){
        return ResponseEntity.ok().body(memberService.getSpecifiedMembers(emails));
    }
  
}
