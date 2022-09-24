package com.example.backend.services;

import com.example.backend.models.Ad;
import com.example.backend.models.MemberInfo;

import java.math.BigInteger;
import java.util.List;

public interface AdService {

    Ad saveAd(Ad ad);

    void addAdv(String email, Ad ad);

    List<Ad> getFriendsAds(String email);


    List<Ad> getNonFriendsAds(String email);

    List<Ad> getOwnAds(String email);

    void applyForAd(String email, Long ad_id);

    void unApplyForAd(String email, Long ad_id);

    BigInteger isAppliedAd(String email, Long id);

    List<BigInteger> appliedAds(List<Long> ad_ids, String email);

    List<MemberInfo> getAppliedMembers(Long id);

    List<List<MemberInfo>> getAllAppliedMembers(List<Long> ad_ids);
}
